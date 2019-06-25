## 项目概述
Android独代，该项目主要包含一个jydudailib子工程，存放所有独代相关请求操作。该独代层目前依赖济游sdk，调用济游sdk的功能。
## 运行环境
* 语言 java
* JDK 1.8
* 工具 Android Studio 3.3.2
* 主工程 compileSdkVersion 26、 targetSdkVersion 22、minSdkVersion 15

## aar依赖
目前该项目需要依赖一下5个aar包，在app工程的build.gradle中进行如下依赖配置
```
    implementation(name: 'jydudailib-release', ext: 'aar')
    implementation(name: 'jygeneralimplib-release', ext: 'aar')
    implementation(name: 'jysdklib-release', ext: 'aar')
    implementation(name: 'toutiaopluginlib-release', ext: 'aar')
    implementation(name: 'AkSDK_PayWebNative-release', ext: 'aar')
```

