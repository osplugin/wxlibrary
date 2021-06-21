package com.mjsoftking.wxlibrary.callback;

/**
 * 微信分享和支付返回成功的接口
 */
public interface WxSucceed {

    /**
     * 微信分享和支付成功时，返回传递的transaction
     */
    void succeed();

}
