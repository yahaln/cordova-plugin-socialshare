package com.yahaln.share;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.apache.cordova.CallbackContext;

/**
 * @author yahaln
 * @mail yahaln@163.com
 * @date 2018/5/29.
 */
public class IUiListenerImpl implements IUiListener {

    private  CallbackContext mCallbackContext;

    IUiListenerImpl( CallbackContext callbackContext){

        this.mCallbackContext=callbackContext;
    }
    @Override
    public void onComplete(Object o) {
        this.mCallbackContext.success("分享成功");
    }

    @Override
    public void onError(UiError uiError) {
        this.mCallbackContext.success("分享失败");
    }

    @Override
    public void onCancel() {
        this.mCallbackContext.success("分享取消");
    }
}
