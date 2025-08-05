package com.mjsoftking.wxutilsapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mjsoftking.wxlibrary.enumeration.ShareType;
import com.mjsoftking.wxlibrary.util.WxLoginUtil;
import com.mjsoftking.wxlibrary.util.WxPayUtil;
import com.mjsoftking.wxlibrary.util.WxShareUtil;
import com.mjsoftking.wxutilsapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setClick(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login) {
            login();
        } else if (v.getId() == R.id.share) {
            share();
        } else if (v.getId() == R.id.pay) {
            pay();
        }
    }

    /**
     * todo 登录示例
     */
    private void login() {
        //todo 注意以下注册回调事件不注册则不会触发
        WxLoginUtil.newInstance()
                .setSucceed((code) -> {
                    //todo 登录过程回调成功   code为微信返回的code
                    //todo 如果需要在app获取openID，则在此处使用code向微信服务器请求获取openID，微信不推荐将此过程放在APP上所以此处仅获取code。
                    //todo 使用WxApiGlobal.getInstance().getAppKey()和WxApiGlobal.getInstance().getAppSecret()获取微信的必要参数，使用前请确保已赋值正确参数
                })
                .setNoInstalled((() -> {
                    //todo 微信客户端未安装
                }))
                .setUserCancel(() -> {
                    //todo 用户取消
                })
                .setFail((errorCode, errStr) -> {
                    //todo 其他类型错误， errorCode为微信返回的错误码
                })
                //发起登录请求
                .logIn();
    }

    /**
     * todo 分享示例
     */
    private void share() {
        //todo 注意以下注册回调事件不注册则不会触发
        WxShareUtil.newInstance()
                .setSucceed(() -> {
                    //todo 分享过程回调成功
                })
                .setNoInstalled((() -> {
                    //todo 微信客户端未安装
                }))
                .setUserCancel(() -> {
                    //todo 用户取消，由于微信调整，用户取消状态不会触发
                })
                .setFail((errorCode, errStr) -> {
                    //todo 其他类型错误， errorCode为微信返回的错误码
                })
                //发起分享请求
                .shareTextMessage("内容", "标题", "描述", ShareType.WXSceneTimeline);
    }

    /**
     * todo 支付示例
     */
    private void pay() {
        // todo                req.appId = json.getString("appid");
        // todo                req.partnerId = json.getString("partnerid");
        // todo                req.prepayId = json.getString("prepayid");
        // todo                req.nonceStr = json.getString("noncestr");
        // todo                req.timeStamp = json.getString("timestamp");
        // todo                req.packageValue = json.getString("package");
        // todo                req.sign = json.getString("sign");
        //todo 此json文本需要包含以上所需字段，或者使用实体方式，不列举
        //todo 注意以下注册回调事件不注册则不会触发
        WxPayUtil.newInstance()
                .setSucceed(() -> {
                    //todo sdk支付成功，向微信服务器查询下具体结果吧
                })
                .setNoInstalled((() -> {
                    //todo 微信客户端未安装
                }))
                .setUserCancel(() -> {
                    //todo 用户取消，由于微信调整，用户取消状态不会触发
                })
                .setFail((errorCode, errStr) -> {
                    //todo 其他类型错误， errorCode为微信返回的错误码
                })
                //发起支付请求
                .payWeChat("json文本");
    }
}
