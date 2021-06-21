package com.mjsoftking.wxlibrary;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信注册的基础工具类
 */
public class WxApiGlobal {

    public final static String WX_LIBRARY_WX_APP_KEY = "WX_LIBRARY_WX_APP_KEY";
    public final static String WX_LIBRARY_WX_APP_SECRET = "WX_LIBRARY_WX_APP_SECRET";

    private final static String TAG = WxApiGlobal.class.getName();

    private final static WxApiGlobal instance = new WxApiGlobal();
    private IWXAPI api;
    private String appKey;
    private String appSecret;

    private WxApiGlobal() {
    }

    /**
     * 获取 WxApiUtil 实例
     */
    public static WxApiGlobal getInstance() {
        return instance;
    }

    public void init(Context context) {
        init(context, true);
    }

    public void init(Context context, boolean checkSignature) {
        init(context, checkSignature, true);
    }

    public void init(Context context, boolean checkSignature, boolean isNowRegister) {
        String appId = fromApplication(context, WX_LIBRARY_WX_APP_KEY);
        String secret = fromApplication(context, WX_LIBRARY_WX_APP_SECRET);

        api = WXAPIFactory.createWXAPI(context, appId, checkSignature);
        appKey = appId;
        appSecret = secret;

        // 将该app注册到微信
        if (isNowRegister) {
            registerApp();
        }
    }

    /**
     * 获取IWXAPI
     */
    public IWXAPI getWXApi() {
        check();
        return api;
    }

    /**
     * 注册app到微信
     */
    public void registerApp() {
        check();
        api.registerApp(appKey);
    }

    /**
     * 从微信卸载app
     */
    public void unregisterApp() {
        check();
        api.unregisterApp();
    }

    /**
     * 打开微信app
     */
    public void openWXApp() {
        check();
        api.openWXApp();
    }

    /**
     * 获取AppKey
     */
    public String getAppKey() {
        check();
        return appKey;
    }

    /**
     * 获取AppSecret
     */
    public String getAppSecret() {
        check();
        return appSecret;
    }

    /**
     * 检查 IWXAPI 是否已注册，未注册则抛出空指针异常
     */
    private void check() {
        if (api == null) {
            Log.e(TAG, "请先使用 WxApiGlobal.getInstance().init() 方法初始化或直接继承WxApplication。");
            throw new NullPointerException("请先使用 WxApiGlobal.getInstance().init() 方法初始化或直接继承WxApplication。");
        }
    }

    private String fromApplication(Context context, String key) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String value = String.valueOf(info.metaData.get(key));
            return "null".equalsIgnoreCase(value) ? "" : value;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "读取mete-data异常", e);
            return "";
        }
    }
}
