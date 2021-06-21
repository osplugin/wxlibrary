package com.mjsoftking.wxutilsapp;

import android.app.Application;

import com.mjsoftking.wxlibrary.WxApiGlobal;


//todo 第二种方案，此方案非必须在Application下进行，但是需要在使用工具前进行初始化
public class MyApplication2 extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //todo 参数appKey和appSecret,需要在manifest的mete-data赋值

        //todo 参数一：上下文
        //todo 参数二：签名检查，根据需要，不需要则传递false即可
        WxApiGlobal.getInstance().init(this, false);
        //todo 参数三（可选）：是否立即注册到微信，传递false，则需要手动调用WxApiGlobal.getInstance().registerApp();注册到微信
        WxApiGlobal.getInstance().init(this, false, true);
        //todo checkSignature为true，立即注册到微信，赋值方式参阅README.MD文档、manifest、gradle(app)文件下的说明
        WxApiGlobal.getInstance().init(this);
        //todo 此处仅为了说明，请不要多次初始化。
    }
}
