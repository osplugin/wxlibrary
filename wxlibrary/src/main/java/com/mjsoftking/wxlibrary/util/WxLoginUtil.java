package com.mjsoftking.wxlibrary.util;

import com.mjsoftking.wxlibrary.WxApiGlobal;
import com.mjsoftking.wxlibrary.callback.WxFail;
import com.mjsoftking.wxlibrary.callback.WxLoginSucceed;
import com.mjsoftking.wxlibrary.callback.WxNoInstalled;
import com.mjsoftking.wxlibrary.callback.WxUserCancel;
import com.mjsoftking.wxlibrary.util.event.WXLibraryBaseEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryFailEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryNoInstalledEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibrarySucceedEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryUserCancelEvent;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

public class WxLoginUtil {

    private WxLoginUtil() {
        EventBus.getDefault().register(this);
    }

    private void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public static WxLoginUtil newInstance() {
        return new WxLoginUtil();
    }

    private WxLoginSucceed succeed;
    private WxNoInstalled noInstalled;
    private WxFail fail;
    private WxUserCancel userCancel;

    /**
     * 注册微信登录成功监听
     */
    public WxLoginUtil setSucceed(WxLoginSucceed succeed) {
        this.succeed = succeed;
        return this;
    }

    /**
     * 注册微信未安装监听
     */
    public WxLoginUtil setNoInstalled(WxNoInstalled noInstalled) {
        this.noInstalled = noInstalled;
        return this;
    }

    /**
     * 注册微信登录失败监听
     */
    public WxLoginUtil setFail(WxFail fail) {
        this.fail = fail;
        return this;
    }

    /**
     * 注册微信登录时用户取消监听
     */
    public WxLoginUtil setUserCancel(WxUserCancel userCancel) {
        this.userCancel = userCancel;
        return this;
    }

    private WxLoginSucceed getSucceed() {
        if (null == succeed) {
            succeed = (code) -> {
            };
        }
        return succeed;
    }

    private WxNoInstalled getNoInstalled() {
        if (null == noInstalled) {
            noInstalled = () -> {
            };
        }
        return noInstalled;
    }

    private WxFail getFail() {
        if (null == fail) {
            fail = (errCode, errStr) -> {
            };
        }
        return fail;
    }

    private WxUserCancel getUserCancel() {
        if (null == userCancel) {
            userCancel = () -> {
            };
        }
        return userCancel;
    }


    /**
     * 微信登录
     */
    public void logIn() {
        logIn("snsapi_userinfo", "none");
    }

    /**
     * 微信登录
     */
    public void logIn(String scope, String state) {
        if (wxNoInstalled()) {
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = scope;
        req.state = state;
        req.transaction = "login:" + UUID.randomUUID();
        WxApiGlobal.getInstance().getWXApi().sendReq(req);
    }

    /**
     * 注册回调接口，并检查是否安装微信
     */
    private boolean wxNoInstalled() {
        //检查是否安装微信客户端
        if (!WxApiGlobal.getInstance().getWXApi().isWXAppInstalled()) {
            EventBus.getDefault().post(new WXLibraryNoInstalledEvent());
            return true;
        }
        return false;
    }

    /**
     * 不要主动调用此方法
     */
    @Deprecated
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(WXLibraryBaseEvent event) {
        destroy();
        if (event instanceof WXLibrarySucceedEvent) {
            WXLibrarySucceedEvent event1 = (WXLibrarySucceedEvent) event;
            getSucceed().succeed(event1.getCode());
        }
        //
        else if (event instanceof WXLibraryUserCancelEvent) {
            getUserCancel().userCancel();
        }
        //
        else if (event instanceof WXLibraryFailEvent) {
            WXLibraryFailEvent event1 = (WXLibraryFailEvent) event;
            getFail().fail(event1.getWxErrCode(), event1.getWxErrStr());
        }
        //
        else if (event instanceof WXLibraryNoInstalledEvent) {
            getNoInstalled().noInstalled();
        }
        //
//        else if (event instanceof WXLibraryInvalidOperateEvent) {
//           //无需处理，只要保证观察者被注销即可
//        }
    }
}
