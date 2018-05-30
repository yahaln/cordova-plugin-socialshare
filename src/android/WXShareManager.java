package com.yahaln.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author yahaln
 * @mail yahaln@163.com
 * @date 2018/5/29.
 */
public final class WXShareManager {
    private IWXAPI api;
    private ShareResultListener listener;

    private final String TRANSACTION_TEXT = "text";
    private final String TRANSACTION_IMAGE = "image";
    private final String TRANSACTION_WEBPAGE = "webpage";
    private final String TRANSACTION_MUSIC = "music";
    private final String TRANSACTION_VIDEO = "video";
    private final String TRANSACTION_FILE = "file";

    private final int THUMBNAIL_SIZE = 150;

    //分享类型
    public enum ShareType {
        FRIENDS, FRIENDSCIRCLE, FAVOURITE
    }

    public WXShareManager(Context appContext,String AppId){
        api = WXAPIFactory.createWXAPI(appContext, AppId, true);
        api.registerApp(AppId);
    }

    public static class InstanceHolder {
//        private static WXShareManager INSTANCE = new WXShareManager();
    }

//    public static WXShareManager get() {
//        return InstanceHolder.INSTANCE;
//    }

//    /**
//     * 初始化微信API 推荐在自定义的Application的onCreate方法中调用
//     * @param appContext
//     */
//    public void init(Context appContext) {
//        api = WXAPIFactory.createWXAPI(appContext, APP_ID, true);
//        api.registerApp(APP_ID);
//    }

    /**
     * 分享文字
     * @param shareText
     * @param title
     * @param description
     * @param type
     * @param listener
     * @return
     */
    public boolean shareText(@NonNull String shareText, @NonNull String title, @NonNull String description,
                             @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;
        WXTextObject obj = new WXTextObject(shareText);
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_TEXT), getWxShareType(type));
        return api.sendReq(req);
    }

    public boolean shareImage(@NonNull Bitmap bitmap, @NonNull String title, @NonNull String description,
                              @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXMediaMessage.IMediaObject obj = new WXImageObject(bitmap);
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        msg.setThumbImage(bitmap);
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_IMAGE), getWxShareType(type));
        return api.sendReq(req);
    }

    public boolean shareImage(@NonNull final String pathOrUrl, @NonNull Bitmap thumbnail, @NonNull String title, @NonNull String description,
                              @NonNull ShareType type, ShareResultListener listener) {
        this.listener = listener;

        //分享url图片
        if(pathOrUrl.contains("://")) {
            try{
                Bitmap bitmap= getImage(pathOrUrl);
                byte[] b= bitmap2Bytes(bitmap,32);
                shareImage(b, title, description, type, listener);
            }catch (Exception ex){
                return  false;
            }
            return true;
        }
        //分享文件系统中的图片
        WXImageObject obj = new WXImageObject();
        obj.imagePath = pathOrUrl;
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        if(thumbnail != null) {
            msg.setThumbImage(thumbnail);
        } else {
            Bitmap thumbNail = getThumbnail(pathOrUrl, THUMBNAIL_SIZE);
            msg.setThumbImage(thumbNail);
            thumbNail.recycle();
        }
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_IMAGE), getWxShareType(type));
        return api.sendReq(req);
    }

    /**
     * 分享图片
     * @param imageData
     * @param title
     * @param description
     * @param type
     * @param listener
     * @return
     */
    public boolean shareImage(@NonNull byte[] imageData, @NonNull String title, @NonNull String description,
                              @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXMediaMessage.IMediaObject obj = new WXImageObject(imageData);
        WXMediaMessage msg = buildMediaMesage(obj, title, description);

        Bitmap thumbnail = getThumbnail(imageData, THUMBNAIL_SIZE);
        msg.setThumbImage(thumbnail);
        thumbnail.recycle();

        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_IMAGE), getWxShareType(type));
        return api.sendReq(req);
    }

    public boolean shareMusic(@NonNull String url, @NonNull String title, @NonNull String description,
                              @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXMusicObject obj = new WXMusicObject();
        obj.musicUrl = url;
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_MUSIC), getWxShareType(type));
        return api.sendReq(req);
    }

    public boolean shareVideo(@NonNull String url, @NonNull String title, @NonNull String description,
                              @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXVideoObject obj = new WXVideoObject();
        obj.videoUrl = url;
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_VIDEO), getWxShareType(type));
        return api.sendReq(req);
    }

    /**
     * 分享网页链接
     * @param weburl
     * @param imageUrl
     * @param title
     * @param description
     * @param type
     * @param listener
     * @return
     */
    public boolean shareWebPage(@NonNull String weburl,@NonNull String imageUrl, @NonNull String title, @NonNull String description,
                                @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXWebpageObject obj = new WXWebpageObject(weburl);
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        try{
            Bitmap bitmap= getImage(imageUrl);
            byte[] b= bitmap2Bytes(bitmap,32);
            Bitmap thumbnail = getThumbnail(b, THUMBNAIL_SIZE);
            msg.setThumbImage(thumbnail);
            thumbnail.recycle();
        }catch (Exception ex){
            return  false;
        }
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_WEBPAGE), getWxShareType(type));
        return api.sendReq(req);
    }

    public boolean shareFile(@NonNull String filepath, @NonNull String title, @NonNull String description,
                             @NonNull ShareType type, @Nullable ShareResultListener listener) {
        this.listener = listener;

        WXFileObject obj = new WXFileObject(filepath);
        WXMediaMessage msg = buildMediaMesage(obj, title, description);
        BaseReq req = buildSendReq(msg, buildTransaction(TRANSACTION_FILE), getWxShareType(type));
        return api.sendReq(req);
    }

    public void handleIntent(Intent intent, IWXAPIEventHandler handler) {
        api.handleIntent(intent, handler);
    }

    public boolean performShareResult(boolean result) {
        if(listener != null) {
            Log.e("_share_", "performShareResult: " + result);
            listener.onShareResult(result);
            listener = null;
            return true;
        }

        return false;
    }

    private int getWxShareType(ShareType type) {
        if(type == ShareType.FRIENDS) {
            return SendMessageToWX.Req.WXSceneSession;
        } else if(type == ShareType.FRIENDSCIRCLE) {
            return SendMessageToWX.Req.WXSceneTimeline;
        } else if(type == ShareType.FAVOURITE) {
            return SendMessageToWX.Req.WXSceneFavorite;
        }

        throw new IllegalArgumentException("非法参数: 不识别的ShareType -> " + type.name());
    }

    private Bitmap getThumbnail(@NonNull String path, int thumbnailSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        Log.e("_share_", "outWidth=" + options.outWidth + ", outHeight=" + options.outHeight);
        int sourceSize = Math.min(options.outWidth, options.outHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sourceSize / thumbnailSize;
        return BitmapFactory.decodeFile(path, options);
    }

    private Bitmap getThumbnail(@NonNull byte[] imageData, int thumbnailSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
        options.inJustDecodeBounds = false;
        Log.e("_share_", "outWidth=" + options.outWidth + ", outHeight=" + options.outHeight);
        int sourceSize = Math.min(options.outWidth, options.outHeight);
        options.inSampleSize = sourceSize / thumbnailSize;
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
    }

    private BaseReq buildSendReq(WXMediaMessage msg, String transaction, int scene) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = msg;
        req.transaction = transaction;
        req.scene = scene;
        return req;
    }

    private WXMediaMessage buildMediaMesage(WXMediaMessage.IMediaObject obj, String title, String description) {
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = obj;
        msg.title = title;
        msg.description = description;
        return msg;
    }

    /**
     * @param type text/image/webpage/music/video
     * @return
     */
    private String buildTransaction(String type) {
        return TextUtils.isEmpty(type) ? String.valueOf(System.currentTimeMillis()) : (type + System.currentTimeMillis());
    }

    /**
     * 分享结果回调
     */
    public interface ShareResultListener {
        void onShareResult(boolean result);
    }

    /**
     * uri获取bitmap
     * @param path
     * @return
     * @throws Exception
     */
    public static Bitmap getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream is = conn.getInputStream();
        Bitmap bitmap=BitmapFactory.decodeStream(is);
        return bitmap;
    }
    /**
     * 计算像素压缩的缩放比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
    /**
     * 二次压缩，先按照像素压缩再按照质量压缩
     * @param imgUrl 图片路径
     * @param reqWidth 期望宽度 可以根据市面上的常用分辨率来设置
     * @param size 期望图片的大小，单位为kb
     * @param quality 图片压缩的质量，取值1-100，越小表示压缩的越厉害，如输入30，表示压缩70%
     * @return Bitmap 压缩后得到的图片
     */
    public static Bitmap compressBitmap(String imgUrl,int reqWidth,int size,int quality){
        // 创建bitMap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgUrl, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight;
        reqHeight = (reqWidth * height) / width;
        // 在内存中创建bitmap对象，这个对象按照缩放比例创建的
        options.inSampleSize = calculateInSampleSize(
                options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(
                imgUrl, options);
        Bitmap mBitmap = compressImage(Bitmap.createScaledBitmap(
                bm, 480, reqHeight, false),size,quality);
        return  mBitmap;
    }
    /*
     * 质量压缩图片，图片占用内存减小，像素数不变，常用于上传
     * @param image
     * @param size 期望图片的大小，单位为kb
     * @param options 图片压缩的质量，取值1-100，越小表示压缩的越厉害,如输入30，表示压缩70%
      * @return
     */
    private static Bitmap compressImage(Bitmap image,int size,int options) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > size) {
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 输入流转byte数组
     * @param inStream
     * @return
     * @throws IOException
     */
    public static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }
    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb&& options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }
}