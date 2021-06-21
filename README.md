# wxlibrary aar文件使用说明
### 一、项目介绍
1. APP 使用示例项目，libs下含有以编译最新的aar资源。
2. wxlibrary arr资源项目，需要引入的资源包项目。
3. aar文件生成，在工具栏直接Gradle - (项目名) - wxlibrary - Tasks - build - assemble，直到编译完成
4. aar文件位置，打开项目所在文件夹，找到 wxlibrary\build\outputs\aar 下。

### 二、工程引入工具包
下载项目，可以在APP项目的libs文件下找到*.aar文件（已编译为最新版），选择其中一个引入自己的工程

引入微信工具包及微信SDK
```
dependencies {
   //引入wxlibrary.aar资源
   implementation files('libs/wxlibrary-release.aar')
   //引入wxlibrary.aar的依赖资源，以下2个
   implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:6.6.5'
   //eventbus，引入后你的项目将支持EventBus，EventBus是一种用于Android的事件发布-订阅总线，替代广播的传值方式，使用方法可以度娘查询。
   implementation 'org.greenrobot:eventbus:3.1.1'
   ...
}
```
### 三、工具包初始准备工作
* 工程继承WxApplication 或者 application 的 onCreate 下使用，获取 **APPkey** 和**AppSecret**需要使用mete-data方式获取。
    **isCheckSignature()** 与 **isNowRegister()** 默认即可
```
    WxApiUtil.getInstance().init(getApplicationContext(), true, true);
```
* **APPkey**和**AppSecret**，需要使用mete-data方式进行赋值 

**方式一，manifest下覆盖mete-data资源** 

```
 <application
       ...>

        <!--todo 微信appKey和appSecret赋值的方法一，tools:replace="android:value"为必要参数，否则编译无法通过-->
        <meta-data
            android:name="WX_LIBRARY_WX_APP_KEY"
            android:value="123456s"
            tools:replace="android:value"/>
        <!--secret参数不需要时覆盖为空字符串即可-->
        <meta-data
            android:name="WX_LIBRARY_WX_APP_SECRET"
            android:value="567890a"
            tools:replace="android:value"/>
        <!--todo 微信appKey和appSecret赋值的方法结束-->

        <activity .../>
 </applicaton>
```
**方式二，manifest下不覆盖mete-data资源，在gradle(app)下赋值** 

```
android {
    ...
    defaultConfig {
        ...

        //todo 微信appKey和appSecret赋值的方法二，2个参数都需要赋值，secret不需要时赋值为空字符串即可
        manifestPlaceholders = [
                WX_LIBRARY_WX_APP_KEY: '',
                WX_LIBRARY_WX_APP_SECRET: ''
        ]
    }
    ...
}
```

### 四、登录、分享和支付的使用，链式写法一句搞定
```
1. 登录使用

 // 注意以下注册回调事件不注册则不会触发
        WxLoginUtil.newInstance()
                .setSucceed((code) -> {
                    // 登录过程回调成功   code为微信返回的code
                    // 如果需要在app获取openID，则在此处使用code向微信服务器请求获取openID。
                    // 使用WxApiGlobal.getInstance().getAppKey()和WxApiGlobal.getInstance().getAppSecret()获取微信的必要参数，使用前请确保已填写正确参数
                    return;
                })
                .setNoInstalled((() -> {
                    // 微信客户端未安装
                    return;
                }))
                .setUserCancel(() -> {
                    // 用户取消
                    return;
                })
                .setFail((errorCode, errStr) -> {
                    // 其他类型错误， errorCode为微信返回的错误码
                    return;
                })
                //发起登录请求
                .logIn();
```

```
2. 分享使用，注意由于微信分享变更，分享时只要唤起微信客户端，无论是否真正分享，都会返回成功

  // 注意以下注册回调事件不注册则不会触发
        WxShareUtil.newInstance()
                .setSucceed(() -> {
                    // 分享过程回调成功
                })
                .setNoInstalled((() -> {
                    // 微信客户端未安装
                }))
                .setUserCancel(() -> {
                    // 用户取消，由于微信调整，用户取消状态不会触发
                })
                .setFail((errorCode, errStr) -> {
                    // 其他类型错误， errorCode为微信返回的错误码
                })
                //发起分享请求
                .shareTextMessage("内容", "标题", "描述", ShareType.WXSceneTimeline);
```

```
3. 支付使用

        // req.appId = json.getString("appid");
        // req.partnerId = json.getString("partnerid");
        // req.prepayId = json.getString("prepayid");
        // req.nonceStr = json.getString("noncestr");
        // req.timeStamp = json.getString("timestamp");
        // req.packageValue = json.getString("package");
        // req.sign = json.getString("sign");
        // 此json文本需要包含以上所需字段，或者使用实体方式，不列举
        // 注意以下注册回调事件不注册则不会触发
        WxPayUtil.newInstance()
                .setSucceed(() -> {
                    // sdk支付成功，向微信服务器查询下具体结果吧
                })
                .setNoInstalled((() -> {
                    // 微信客户端未安装
                }))
                .setUserCancel(() -> {
                    // 用户取消
                })
                .setFail((errorCode, errStr) -> {
                    // 其他类型错误， errorCode为微信返回的错误码
                })
                //发起分享请求
                .payWeChat("json文本");
```
### 五、测试说明
由于微信需要在后台配置签名信息，而测试时不能修改一次打包一次进行测试，所以配置项目的签名信息即可在debug模式下使用正式版签名信息。
```
android {
    signingConfigs {
        release {
            storeFile file('key文件位置，可写相对位置。默认是相对于app的文件夹下')
            storePassword 'key文件密码'
            keyAlias = '打包别名'
            keyPassword '别名密码'
        }
    }
    ...
    buildTypes {
        debug {
            signingConfig signingConfigs.release
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.release
        }
    }

}
```



