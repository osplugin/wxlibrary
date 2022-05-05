# wxlibrary aar文件使用说明
[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![](https://jitpack.io/v/com.gitee.osard/wxlibrary.svg)](https://jitpack.io/#com.gitee.osard/wxlibrary)
### 一、项目介绍
1. APP 使用示例项目，libs下含有以编译最新的aar资源。
2. wxlibrary arr资源项目，需要引入的资源包项目。
3. jitpack 仓库在线引入即可

### 二、工程引入工具包
**工程的build.gradle文件添加** 

```
allprojects {
    repositories {
        google()
        mavenCentral()

        //jitpack 仓库
        maven { url 'https://jitpack.io' }
    }
}
```

**APP的build.gradle文件添加** 
```
dependencies {
    ...
    implementation 'com.gitee.osard:wxlibrary:1.1.1'
    //引入wxlibrary.aar的依赖资源，以下2个
    implementation 'com.tencent.mm.opensdk:wechat-sdk-android-without-mta:+'
    implementation 'org.greenrobot:eventbus:3.2.0'
}
```

### 三、工具包初始准备工作
* 工程继承WxApplication 或者 application 的 onCreate 下使用，获取 **APPkey** 和**AppSecret**需要使用mete-data方式获取。
    **isCheckSignature()** 与 **isNowRegister()** 默认即可
```
    WxApiGlobal.getInstance().init(getApplicationContext(), true, true);
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

License
-------

    Copyright 2021 mjsoftking

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


