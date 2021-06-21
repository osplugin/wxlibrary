package com.mjsoftking.wxlibrary.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.mjsoftking.wxlibrary.WxApiGlobal;
import com.mjsoftking.wxlibrary.util.event.WXLibraryFailEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryInvalidOperateEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibrarySucceedEvent;
import com.mjsoftking.wxlibrary.util.event.WXLibraryUserCancelEvent;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;


public class BaseWXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = BaseWXPayEntryActivity.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        WxApiGlobal.getInstance().getWXApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WxApiGlobal.getInstance().getWXApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseResp) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.transaction == null || baseResp.transaction.length() == 0) {
            Log.e(TAG, "transaction 标识无效，请使用系统提供方法调用。");
            EventBus.getDefault().post(new WXLibraryInvalidOperateEvent());
        } else {
            if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
                if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                    if ("WxPay:".equalsIgnoreCase(baseResp.transaction.substring(0, 6))) {
                        // 支付
                        EventBus.getDefault().post(new WXLibrarySucceedEvent(null));
                    } else {
                        // 认为无效
                        Log.e(TAG, "transaction 标识无效，请使用系统提供方法调用。");
                        EventBus.getDefault().post(new WXLibraryInvalidOperateEvent());
                    }
                } else if (baseResp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                    EventBus.getDefault().post(new WXLibraryUserCancelEvent());
                } else {
                    EventBus.getDefault().post(new WXLibraryFailEvent(baseResp.errCode, baseResp.errStr));
                }
            }
        }
        finish();
    }
}