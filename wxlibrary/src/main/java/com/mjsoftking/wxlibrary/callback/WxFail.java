package com.mjsoftking.wxlibrary.callback;

/**
 * 微信登录、分享、支付 失败时的回调接口
 */
public interface WxFail {

    void fail(int wxErrCode, String wxErrStr);
}
