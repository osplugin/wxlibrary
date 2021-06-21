package com.mjsoftking.wxlibrary;

import android.app.Application;

/**
 * 微信注册 Application
 */
public class WxApplication extends Application {

    protected boolean isCheckSignature() {
        return true;
    }

    protected boolean isNowRegister() {
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        WxApiGlobal.getInstance().init(getApplicationContext(), isCheckSignature(), isNowRegister());
    }
}
