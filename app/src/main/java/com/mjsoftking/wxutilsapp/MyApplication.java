package com.mjsoftking.wxutilsapp;


import com.mjsoftking.wxlibrary.WxApplication;

//todo 第一种方案,AppKey和AppSecret需要在manifest的mete-data赋值
public class MyApplication extends WxApplication {

    @Override
    protected boolean isCheckSignature() {
        return super.isCheckSignature(); //todo 默认返回true
    }

    @Override
    protected boolean isNowRegister() {
        return super.isNowRegister();//todo 默认返回true，设置为true时，直接注册到微信，如无特殊需要，无需重写
    }
}
