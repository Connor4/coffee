# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


#1.基本指令区###########
# 设置混淆的压缩比率 0 ~ 7
-optimizationpasses 5
# 混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 指定不去忽略非公共库的成员
-dontskipnonpubliclibraryclassmembers
# 混淆时不做预校验
-dontpreverify
# 混淆时不记录日志
-verbose
# 忽略警告
#-ignorewarning
# 代码优化
-dontshrink
# 不优化输入的类文件
-dontoptimize
# 保留注解不混淆
#-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
# 保留代码行号，方便异常信息的追踪
-keepattributes SourceFile,LineNumberTable
# 混淆采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*

## dump.txt文件列出apk包内所有class的内部结构
#-dump class_files.txt
## seeds.txt文件列出未混淆的类和成员
#-printseeds seeds.txt
## usage.txt文件列出从apk中删除的代码
#-printusage unused.txt
# mapping.txt文件列出混淆前后的映射
#-printmapping mapping.txt

#不需混淆的Android类########
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

#5.关闭 Log日志
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** e(...);
    public static *** w(...);
}
### 打开日志
#-keepclassmembers class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(java.lang.String, java.lang.String);
#    public static int v(java.lang.String, java.lang.String, java.lang.Throwable);
#    public static int d(java.lang.String, java.lang.String);
#    public static int d(java.lang.String, java.lang.String, java.lang.Throwable);
#    public static int i(java.lang.String, java.lang.String);
#    public static int i(java.lang.String, java.lang.String, java.lang.Throwable);
#    public static int w(java.lang.String, java.lang.String);
#    public static int w(java.lang.String, java.lang.String, java.lang.Throwable);
#    public static int w(java.lang.String, java.lang.Throwable);
#    public static int e(java.lang.String, java.lang.String);
#    public static int e(java.lang.String, java.lang.String, java.lang.Throwable);
#}

#6.避免资源混淆
-keep class **.R$* {*;}

#9.避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#10.Natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#11.避免Parcelable/Serializable 混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontwarn com.inno.serialport.core.SerialPortFinder
-dontwarn com.inno.serialport.function.SerialPortDataManager$Companion
-dontwarn com.inno.serialport.function.SerialPortDataManager
-dontwarn com.inno.serialport.function.CommunicationController
-dontwarn com.inno.serialport.function.data.DataCenter
-dontwarn com.inno.serialport.function.data.Subscriber
-dontwarn com.inno.serialport.function.driver.UnitTestKt
-dontwarn com.inno.serialport.utilities.HeartBeatReply$BoilerTemperature
-dontwarn com.inno.serialport.utilities.HeartBeatReply$Error
-dontwarn com.inno.serialport.utilities.HeartBeatReply$MakeDrink
-dontwarn com.inno.serialport.utilities.ReceivedData$HeartBeat
-dontwarn com.inno.serialport.utilities.ReceivedData$HeatBeatList
-dontwarn com.inno.serialport.utilities.ReceivedData$SerialErrorData
-dontwarn com.inno.serialport.utilities.ReceivedData
-dontwarn com.inno.serialport.utilities.ReceivedDataType
-dontwarn com.inno.serialport.utilities.profile.ComponentProfile
-dontwarn com.inno.serialport.utilities.profile.ComponentProfileList
-dontwarn com.inno.serialport.utilities.profile.ProductProfile
-dontwarn com.inno.serialport.utilities.statusenum.BoilerStatusEnum
-dontwarn com.inno.serialport.utilities.statusenum.ErrorStatusEnum
-dontwarn com.inno.serialport.utilities.statusenum.MakeDrinkStatusEnum

-dontwarn com.inno.common.db.**
-dontwarn com.inno.common.utils.**
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-dontwarn com.inno.common.di.CommonModule
-dontwarn com.inno.common.di.CommonModule_ProvideCoffeeDataStoreFactory
-dontwarn com.inno.common.di.DatabaseModule
-dontwarn com.inno.common.di.DatabaseModule_ProvideCleanHistoryDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideDatabaseFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideDrinksHistoryDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideErrorHistoryDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideFormulaDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideMaintenanceHistoryDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideProductCountDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideRinseHistoryDaoFactory
-dontwarn com.inno.common.di.DatabaseModule_ProvideUserDaoFactory
-dontwarn com.inno.common.enums.ProductType
-dontwarn hilt_aggregated_deps._com_inno_common_di_CommonModule
-dontwarn hilt_aggregated_deps._com_inno_common_di_DatabaseModule

