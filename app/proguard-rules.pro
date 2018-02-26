# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/sangroklee/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.google.errorprone.annotations.*

-keep class com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass$**{
    *;
}

-keepclassmembernames class com.timeofpoetry.timeofpoetry.timeofpoetry.data.PoetryClass$**{
    *;
}

-keepattributes *Annotation*,SourceFile,LineNumberTable

-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }

# support-v7
-dontwarn android.support.v7.**
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.support.v7.** { *; }

-keep class com.facebook.** {
   *;
}

#-keep class com.timeofpoetry.timeofpoetry.timeofpoetry.adapter.Adapter$**{
#    *;
#}
#
-keep class com.github.ybq.android.spinkit.**{
    *;
}

-keep class com.bumptech.glide.**{
    *;
}

-keep class com.google.android.gms.** { *; }