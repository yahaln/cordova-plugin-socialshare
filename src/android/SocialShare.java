package com.yahaln.share;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import com.tencent.tauth.Tencent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import android.os.Bundle;

/**
 * 社会化分享
 * @author yahaln
 * @mail yahaln@163.com
 * @date 2018/5/29.
 */
public class SocialShare extends CordovaPlugin {
    private static Tencent mTencent;
    private  WXShareManager.ShareResultListener listener=null;
    public SocialShare(){
        listener=new ShareResultListenerImpl();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        JSONObject jsonObject=args.getJSONObject(0);
        if (action.equals("wechatShareText")) {
            wechatShareText(jsonObject,callbackContext);
            return true;
        }
        if (action.equals("wechatShareImage")) {
            wechatShareImage(jsonObject,callbackContext);
            return true;
        }
        if (action.equals("wechatShareWebPage")) {
            wechatShareWebPage(jsonObject,callbackContext);
            return true;
        }
        if (action.equals("QQShareImageText")) {
            QQShareImageText(jsonObject,callbackContext);
            return true;
        }
        if (action.equals("QQZoneShareImageText")) {
            QQZoneShareImageText(jsonObject,callbackContext);
            return true;
        }
        return false;
    }

    /**
     * 微信分享文字信息
     * @param jsonObject
     * @param callbackContext
     */
    private void wechatShareText(JSONObject jsonObject, CallbackContext callbackContext){
        String title="";
        String shareText="";
        String description="";
        String type="";
        try{
            title=jsonObject.getString("title");
            shareText=jsonObject.getString("shareText");
            description=jsonObject.getString("description");
            type=jsonObject.getString("type");
        }catch (JSONException ex){
            callbackContext.error(ex.getMessage());
        }
        String wechatAppId=fetchWechatAppId();
        if(wechatAppId==null||wechatAppId==""){
            callbackContext.error("未获取到appid");
            return;
        }
        WXShareManager wxShareManager=new WXShareManager(this.cordova.getContext(),wechatAppId);
        if("friend".equals(type)){
            wxShareManager.shareText(shareText,title,description, WXShareManager.ShareType.FRIENDS,listener);
        }else if("session".equals(type)){
            wxShareManager.shareText(shareText,title,description, WXShareManager.ShareType.FRIENDSCIRCLE,listener);
        }else if("favourite".equals(type)){
            wxShareManager.shareText(shareText,title,description, WXShareManager.ShareType.FAVOURITE,listener);
        }
        callbackContext.success("分享成功！");
    }
    /**
     * 微信分享图片信息
     * @param jsonObject
     * @param callbackContext
     */
    private void wechatShareImage(JSONObject jsonObject, CallbackContext callbackContext){
        String title="";
        String pathOrUrl="";
        String description="";
        String type="";
        try{
            title=jsonObject.getString("title");
            pathOrUrl=jsonObject.getString("pathOrUrl");
            description=jsonObject.getString("description");
            type=jsonObject.getString("type");
        }catch (JSONException ex){
            callbackContext.error(ex.getMessage());
        }
        String wechatAppId=fetchWechatAppId();
        if(wechatAppId==null||wechatAppId==""){
            callbackContext.error("未获取到appid");
            return;
        }
        WXShareManager wxShareManager=new WXShareManager(this.cordova.getContext(),wechatAppId);
        if("friend".equals(type)){
            wxShareManager.shareImage(pathOrUrl,null,title,description,WXShareManager.ShareType.FRIENDS,listener);
        }else if("session".equals(type)){
            wxShareManager.shareImage(pathOrUrl,null,title,description,WXShareManager.ShareType.FRIENDSCIRCLE,listener);
        }else if("favourite".equals(type)){
            wxShareManager.shareImage(pathOrUrl,null,title,description,WXShareManager.ShareType.FAVOURITE,listener);
        }
        callbackContext.success("分享成功！");
    }

    /**
     * 分享网页信息
     * @param jsonObject
     * @param callbackContext
     */
    private void wechatShareWebPage(JSONObject jsonObject, CallbackContext callbackContext){
        String title="";
        String webPageUrl="";
        String imageUrl="";
        String description="";
        String type="";
        try{
            title=jsonObject.getString("title");
            webPageUrl=jsonObject.getString("webPageUrl");
            imageUrl=jsonObject.getString("imageUrl");
            description=jsonObject.getString("description");
            type=jsonObject.getString("type");
        }catch (JSONException ex){
            callbackContext.error(ex.getMessage());
        }
        String wechatAppId=fetchWechatAppId();
        if(wechatAppId==null||wechatAppId==""){
            callbackContext.error("未获取到appid");
            return;
        }
        WXShareManager wxShareManager=new WXShareManager(this.cordova.getContext(),wechatAppId);
        if("friend".equals(type)){
            wxShareManager.shareWebPage(webPageUrl,imageUrl,title,description,WXShareManager.ShareType.FRIENDS,listener);
        }else if("session".equals(type)){
            wxShareManager.shareWebPage(webPageUrl,imageUrl,title,description,WXShareManager.ShareType.FRIENDSCIRCLE,listener);
        }else if("favourite".equals(type)){
            wxShareManager.shareWebPage(webPageUrl,imageUrl,title,description,WXShareManager.ShareType.FAVOURITE,listener);
        }
        callbackContext.success("分享成功！");
    }

    /**
     * 获取微信appid
     * @return
     */
    private String fetchWechatAppId(){
        try{
            ApplicationInfo appInfo = this.cordova.getContext().getPackageManager()
                    .getApplicationInfo(this.cordova.getContext().getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("WechatAppid");
        }catch (PackageManager.NameNotFoundException ex){
            return "";
        }
    }
    /**
     * 获取QQappid
     * @return
     */
    private String fetchQQAppId(){
        try{
            ApplicationInfo appInfo = this.cordova.getContext().getPackageManager()
                    .getApplicationInfo(this.cordova.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String app_id=appInfo.metaData.getInt("QQAppid")+"";
            return app_id;
        }catch (PackageManager.NameNotFoundException ex){
            return "";
        }
    }
    /**
     * QQ分享图文消息
     * @param jsonObject
     * @param callbackContext
     */
    private void QQShareImageText(JSONObject jsonObject, CallbackContext callbackContext){
        String mAppid=fetchQQAppId();
        mTencent = Tencent.createInstance(mAppid, this.cordova.getContext());
        String title="";//分享的标题
        String targetUrl="";//这条分享消息被好友点击后的跳转URL。
        String imageUrl="";//分享图片的URL或者本地路径
        String summary="";//分享的消息摘要，最长40个字。
        try{
            title=jsonObject.getString("title");
            targetUrl=jsonObject.getString("targetUrl");
            imageUrl=jsonObject.getString("imageUrl");
            summary=jsonObject.getString("summary");
        }catch (JSONException ex){
            callbackContext.error(ex.getMessage());
        }

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE,title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,imageUrl);

        if (null != mTencent) {
            mTencent.shareToQQ(this.cordova.getActivity(), params, new IUiListenerImpl(callbackContext));
        }else{
            callbackContext.error("分享失败");
        }
    }
    /**
     * QQ空间分享图文消息
     * @param jsonObject
     * @param callbackContext
     */
    private void QQZoneShareImageText(JSONObject jsonObject, CallbackContext callbackContext){
        String mAppid=fetchQQAppId();
        mTencent = Tencent.createInstance(mAppid, this.cordova.getContext());
        String title="";//分享的标题
        String targetUrl="";//这条分享消息被好友点击后的跳转URL。
        String imageUrl="";//分享图片的URL或者本地路径
        String summary="";//分享的消息摘要，最长40个字。
        try{
            title=jsonObject.getString("title");
            targetUrl=jsonObject.getString("targetUrl");
            imageUrl=jsonObject.getString("imageUrl");
            summary=jsonObject.getString("summary");
        }catch (JSONException ex){
            callbackContext.error(ex.getMessage());
        }
        final Bundle params = new Bundle();
        //分享类型
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT );
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,summary);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);//必填
        ArrayList<String> imageUrls = new ArrayList<String>();
        imageUrls.add(imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL,imageUrls);

        if (null != mTencent) {
            mTencent.shareToQzone(this.cordova.getActivity(), params,new IUiListenerImpl(callbackContext));
        }else{
            callbackContext.error("分享失败");
        }
    }
}
