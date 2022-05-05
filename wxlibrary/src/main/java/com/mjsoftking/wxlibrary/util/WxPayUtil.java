package com.mjsoftking.wxlibrary.util;

import com.mjsoftking.wxlibrary.WxApiGlobal;
import com.mjsoftking.wxlibrary.callback.WxFail;
import com.mjsoftking.wxlibrary.callback.WxNoInstalled;
import com.mjsoftking.wxlibrary.callback.WxSucceed;
import com.mjsoftking.wxlibrary.callback.WxUserCancel;
import com.mjsoftking.wxlibrary.entity.WxPay;
import com.mjsoftking.wxlibrary.util.event.WXLibraryBaseEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryFailEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryNoInstalledEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibrarySucceedEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryUserCancelEvent;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class WxPayUtil {

    private WxPayUtil() {
        EventBus.getDefault().register(this);
    }

    private void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public static WxPayUtil newInstance(){
        return new WxPayUtil();
    }

    private WxSucceed succeed;
    private WxNoInstalled noInstalled;
    private WxFail fail;
    private WxUserCancel userCancel;

    /**
     * 注册微信支付成功监听
     */
    public WxPayUtil setSucceed(WxSucceed succeed) {
        this.succeed = succeed;
        return this;
    }

    /**
     * 注册微信未安装监听
     */
    public WxPayUtil setNoInstalled(WxNoInstalled noInstalled) {
        this.noInstalled = noInstalled;
        return this;
    }

    /**
     * 注册微信登录失败监听
     */
    public WxPayUtil setFail(WxFail fail) {
        this.fail = fail;
        return this;
    }

    /**
     * 注册微信登录时用户取消监听
     */
    public WxPayUtil setUserCancel(WxUserCancel userCancel) {
        this.userCancel = userCancel;
        return this;
    }

    private WxSucceed getSucceed() {
        if (null == succeed) {
            succeed = () -> {
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
     * 微信支付业务
     *
     * @param orderBoby json数据，含有微信要求的全部字段
     */
    public void payWeChat(final String orderBoby) {
        if (wxNoInstalled()) {
            return;
        }

        try {
            JSONObject json = new JSONObject(orderBoby);
            PayReq req = new PayReq();
            req.appId = json.getString("appid");
            req.partnerId = json.getString("partnerid");
            req.prepayId = json.getString("prepayid");
            req.nonceStr = json.getString("noncestr");
            req.timeStamp = json.getString("timestamp");
            req.packageValue = json.getString("package");
            req.sign = json.getString("sign");

            req.transaction = "WxPay:" + UUID.randomUUID();
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            WxApiGlobal.getInstance().getWXApi().sendReq(req);
        } catch (JSONException je) { //NOSONAR

        }
    }

    /**
     * 微信支付业务
     *
     * @param wxPay 支付报文包装后的对象
     */
    public void payWeChat(final WxPay wxPay) {
        if (wxNoInstalled()) {
            return;
        }

        PayReq req = new PayReq();
        req.appId = wxPay.getAppId();
        req.partnerId = wxPay.getPartnerId();
        req.prepayId = wxPay.getPrepayId();
        req.nonceStr = wxPay.getNonceStr();
        req.timeStamp = wxPay.getTimeStamp();
        req.packageValue = wxPay.getPackageValue();
        req.sign = wxPay.getSign();

        req.transaction = "WxPay:" + UUID.randomUUID();
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
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
            getSucceed().succeed();
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
//            //无需处理，只要保证观察者被注销即可
//        }
    }
}
