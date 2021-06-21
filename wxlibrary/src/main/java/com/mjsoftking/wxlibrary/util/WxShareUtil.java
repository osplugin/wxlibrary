package com.mjsoftking.wxlibrary.util;

import com.mjsoftking.wxlibrary.WxApiGlobal;
import com.mjsoftking.wxlibrary.callback.WxFail;
import com.mjsoftking.wxlibrary.callback.WxNoInstalled;
import com.mjsoftking.wxlibrary.callback.WxSucceed;
import com.mjsoftking.wxlibrary.callback.WxUserCancel;
import com.mjsoftking.wxlibrary.enumeration.ShareType;
import com.mjsoftking.wxlibrary.util.event.WXLibraryBaseEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryFailEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryNoInstalledEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibrarySucceedEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryUserCancelEvent;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

public class WxShareUtil {

    private WxShareUtil() {
        EventBus.getDefault().register(this);
    }

    private void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public static WxShareUtil newInstance() {
        return new WxShareUtil();
    }

    private WxSucceed succeed;
    private WxNoInstalled noInstalled;
    private WxFail fail;
    private WxUserCancel userCancel;

    /**
     * 注册微信分享成功监听
     */
    public WxShareUtil setSucceed(WxSucceed succeed) {
        this.succeed = succeed;
        return this;
    }

    /**
     * 注册微信未安装监听
     */
    public WxShareUtil setNoInstalled(WxNoInstalled noInstalled) {
        this.noInstalled = noInstalled;
        return this;
    }

    /**
     * 注册微信登录失败监听
     */
    public WxShareUtil setFail(WxFail fail) {
        this.fail = fail;
        return this;
    }

    /**
     * 注册微信登录时用户取消监听
     */
    public WxShareUtil setUserCancel(WxUserCancel userCancel) {
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
     * 分享文本消息
     *
     * @param content     内容
     * @param title       标题
     * @param description 描述
     * @param scene       ShareType
     */
    public void shareTextMessage(String content, String title, String description, ShareType scene) {
        if (wxNoInstalled()) {
            return;
        }

        WXTextObject textObject = new WXTextObject();
        textObject.text = content;

        WXMediaMessage msg = new WXMediaMessage(textObject);
        msg.description = description;
        msg.title = title;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "share:text_" + UUID.randomUUID();
        req.message = msg;
        req.scene = scene.getType();

        WxApiGlobal.getInstance().getWXApi().sendReq(req);
    }

    /**
     * 分享图片消息
     *
     * @param image       图片
     * @param title       标题
     * @param description 描述
     * @param thumbData   缩略图
     * @param scene       ShareType
     */
    public void shareImageMessage(byte[] image, String title, String description, byte[] thumbData, ShareType scene) {
        if (wxNoInstalled()) {
            return;
        }

        WXImageObject imageObject = new WXImageObject();
        imageObject.imageData = image;

        WXMediaMessage msg = new WXMediaMessage(imageObject);
        msg.title = title;
        msg.description = description;
        msg.thumbData = thumbData;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "share:img_" + UUID.randomUUID();
        req.message = msg;
        req.scene = scene.getType();

        WxApiGlobal.getInstance().getWXApi().sendReq(req);
    }

    /**
     * 分享网页消息
     *
     * @param url         url地址
     * @param title       标题
     * @param description 描述
     * @param thumbData   缩略图图片： Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h5_logo);
     *                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
     *                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
     *                    baos.toByteArray();
     * @param scene       ShareType
     */
    public void shareWebMessage(String url, String title, String description, byte[] thumbData, ShareType scene) {
        if (wxNoInstalled()) {
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        msg.thumbData = thumbData;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "share:webpage_" + UUID.randomUUID();
        req.message = msg;
        req.scene = scene.getType();

        WxApiGlobal.getInstance().getWXApi().sendReq(req);
    }

    /**
     * 分享自定义任意类型消息
     *
     * @param msg   WXMediaMessage 类型消息
     * @param scene ShareType
     */
    public void shareCustom(WXMediaMessage msg, ShareType scene) {
        if (wxNoInstalled()) {
            return;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "share:custom_" + UUID.randomUUID();
        req.message = msg;
        req.scene = scene.getType();

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
