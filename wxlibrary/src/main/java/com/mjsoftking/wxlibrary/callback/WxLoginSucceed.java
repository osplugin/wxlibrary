package com.mjsoftking.wxlibrary.callback;


/**
 * 微信登录使用的登录成功返回接口
 */
public interface WxLoginSucceed {

    /**
     * 登录成功时，返回用户身份code
     *
     * @param code 用户身份码，需要调用微信接口请求用户的 openId
     */
    void succeed(String code);

}
