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

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.app.Service
-keepnames class * extends androidx.fragment.app.Fragment
-keep public class * extends java.lang.Exception

-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep class com.crashlytics.** { *; }
-keep interface com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keep class com.zomato.ui.android.internal.progressViewImplementation.** { *; }
-keep interface com.zomato.ui.android.internal.progressViewImplementation.** { *; }
-keep class com.zomato.ui.android.tour.views.** { *; }

-dontwarn com.makeramen.roundedimageview.*

#For Huawei
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.agconnect.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}


-keep class com.google.android.gms.ads.identifier.** { *; }
-keepclasseswithmembers class com.zomato.ui.lib.data.ads.** { *; }

-keepclassmembers class * extends javax.net.ssl.SSLSocketFactory {
     private javax.net.ssl.SSLSocketFactory delegate;
}

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepattributes Exceptions, InnerClasses

-keep public class com.application.zomato.newRestaurant.editorialReview.model.data.** {
  public protected *;
}


-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile, LineNumberTable, *Annotation*, EnclosingMethod

-keep class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

## Adding R8 Optimizations

# Multiple passes may result in improvements
-optimizationpasses 5
-optimizations !method/removal/parameter,code/simplification/variable,method/inlining/*
# To broaden access modifiers of classes and class members
-allowaccessmodification

# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-dontwarn okhttp3.**
-dontwarn okio.**

# NewRelic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-dontwarn org.slf4j.**

-keep class android.media.ExifInterface

# Keep Marker and LatLng classes intact as they are used
# in ObjectAnimator
-keep class com.google.android.gms.maps.** { *; }

# Appsflyer + Install referrer
-dontwarn com.android.installreferrer
-dontwarn com.appsflyer.**

# Rules for Glide
 -keep public class * implements com.bumptech.glide.module.GlideModule
 -keep public class * extends com.bumptech.glide.module.AppGlideModule
 -keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
   **[] $VALUES;
   public *;
 }

# Need this for now as R8 crashes the app with no-args constructor exception while parsing the json string into model classes
-keepclassmembers,allowobfuscation class * {public <init>(...);}

# Require this rule to keep classes unobfuscated having serializable fields ( @Serializable annotated)
-keepclassmembers,allowobfuscation class * {
 @com.google.gson.annotations.SerializedName <fields>;

}

-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keepclasseswithmembers class * { native <methods>;
}
-keep class com.datavisor.vangogh.** { *; }
-keep class org.xmlpull.v1.** { *;}

# Keep parcelable safe
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# for square's curtains, affecting the latest leak canary on release
-keep class androidx.appcompat.view.WindowCallbackWrapper { *; }

# removing all the Log.x statements in the project
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Keep the names of the native methods unobfuscated.
-keepclasseswithmembernames,includedescriptorclasses class * {
  native <methods>;
}

# Keep GrofersWebInterface and class members
-keepclassmembers class **.*$GrofersWebInterface{
    public *;
}

##---------------End: proguard configuration for Grofers  ----------


-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
#NEEDS TO BE REMOVED ASAP POST RELEASE v5.1.19
-ignorewarnings

-printseeds seeds.txt
-printusage unused.txt

-keepclassmembers class **.R$* {public static <fields>;}
-keep class **.R$*

# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

# Needed for Parcelable/SafeParcelable Creators to not get stripped
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

# For Crashlytics
-keep class com.crashlytics.** { *; }

-dontwarn com.crashlytics.**
# to de-obfuscate crash reports from crashlytics
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception


-keep class com.paymentsdk.android.model.** { *; }
-keep class * extends android.app.Activity { *; }

#-libraryjars libs

-dontwarn android.support.v4.**
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.widget.** { *; }
-keep interface android.support.v4.widget.** { *; }

-dontwarn android.support.v7.**
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v7.appcompat.** { *; }
-keep class android.support.v7.widget.** { *; }
-keep interface android.support.v7.widget.** { *; }

# Proguard for okio
-dontwarn okio.**

-dontwarn rx.**

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keepattributes *Annotation*

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepclassmembers enum * { *; }

# Proguard for payu
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembers class com.payu.custombrowser.** {
    *;
}
-keepattributes InnerClasses
-keep class com.payu.sdk.**{*;}

# Proguard for JusPay
-keep class in.juspay.** {*;}

# Proguard for Gpay
#-keep class com.google.android.apps.nbu.paisa.inapp.**{*;}
-keep class com.google.android.**{*;}

# Proguard for Facebook
-keep class com.facebook.** {
   *;
}

# Proguard for Support design library rules
-keep class android.support.design.widget.** { *; }
-keep interface android.support.design.widget.** { *; }
-dontwarn android.support.design.**

-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Exceptions
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Proguard for attribution
-keep class com.google.android.gms.ads.** { *; }

-keep class com.google.android.gms.gcm.**{ *; }

# Proguard for Nineolddroid related classes to ignore

-keep class com.nineoldandroids.animation.** { *; }
-keep interface com.nineoldandroids.animation.** { *; }
-keep class com.nineoldandroids.view.** { *; }
-keep interface com.nineoldandroids.view.** { *; }


# Proguard for Fresco related classes to ignore
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip


# Proguard for keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**

# Proguard for Dagger
-dontwarn dagger.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }

# Proguard for Mixpanel
-dontwarn com.mixpanel.**
-keep class **.R$* { <fields>; }

# Proguard for mobikwik
-keep class com.mobikwik.sdk.** { *; }

# Proguard for event bus
-keepclassmembers class ** {
    public void onEvent*(***);
}

# Proguard for amazon sdk

-keep class com.amazonaws.javax.xml.transform.sax.*     { public *; }
-keep class com.amazonaws.javax.xml.stream.**           { *; }
-keep class com.amazonaws.services.**.model.*Exception* { *; }
-keep class org.codehaus.**                             { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }
-keep class com.amazonaws.** { *; }
-keep class org.joda.convert.*                          { *; }
-keepnames class com.fasterxml.jackson.**               { *; }


-dontwarn javax.xml.stream.events.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.logging.impl.**
-dontwarn org.apache.http.conn.scheme.**
-dontwarn com.amazonaws.**

#branch.io
-keep class com.google.android.gms.ads.identifier.** { *; }
-dontwarn com.android.installreferrer.api.**

-dontwarn com.google.android.gms.location.**
-dontwarn com.google.android.gms.gcm.**
-dontwarn com.google.android.gms.iid.**

-keep class com.google.android.gms.gcm.** { *; }
-keep class com.google.android.gms.iid.** { *; }
-keep class com.google.android.gms.location.** { *; }

-keep class com.delight.**  { *; }

 -keepattributes SourceFile, LineNumberTable

# Proguard for annotation
 -optimizationpasses 5
 -dontusemixedcaseclassnames
 -dontskipnonpubliclibraryclasses
 -dontpreverify
 -verbose
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.content.BroadcastReceiver
 -keep public class * extends android.content.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper

 -keepclasseswithmembernames class * {
     native <methods>;
 }

 -keepclasseswithmembernames class * {
     public <init>(android.content.Context, android.util.AttributeSet);
 }

 -keepclasseswithmembernames class * {
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class * implements android.os.Parcelable {
   public static final android.os.Parcelable$Creator *;
 }

 -dontwarn sun.misc.Unsafe
 -dontwarn javax.annotation.**

 -dontwarn org.apache.http.**
 -dontwarn android.net.http.AndroidHttpClient
 -dontwarn com.google.android.gms.**
 -dontwarn com.android.volley.toolbox.**

 -dontwarn javax.servlet.**
 -dontwarn org.joda.time.**
 -dontwarn org.w3c.dom.**

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-dontwarn sun.misc.Unsafe

# Proguard for Parcel library
-keep interface android.os.Parcel
-keep @android.os.Parcel class * { *; }
-keep class **$$Parcelable { *; }

# Proguard for snappy
 -dontwarn sun.reflect.**
 -dontwarn java.beans.**
 -dontwarn sun.nio.ch.**
 -dontwarn sun.misc.**

 -keep class com.esotericsoftware.** {*;}

 -keep class java.beans.** { *; }
 -keep class sun.reflect.** { *; }
 -keep class sun.nio.ch.** { *; }

 -keep class com.snappydb.** { *; }
 -dontwarn com.snappydb.**

 -dontwarn com.facebook.drawee.**

# Proguard for Retrofit 2.X
 ## https://square.github.io/retrofit/ ##

 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 -keepattributes Signature
 -keepattributes Exceptions

 -keepclasseswithmembers class * {
     @retrofit2.http.* <methods>;
 }

# Proguard for Firebase
 -keep class com.firebase.** { *; }
 -keepnames class com.fasterxml.jackson.** { *; }
 -dontwarn com.fasterxml.jackson.**
 -keepnames class javax.servlet.** { *; }
 -keepnames class org.ietf.jgss.** { *; }
 -dontwarn org.w3c.dom.**
 -dontwarn org.joda.time.**
 -dontwarn org.shaded.apache.**
 -dontwarn org.ietf.jgss.**
 -dontwarn com.firebase.**
 -dontnote com.firebase.client.core.GaePlatform
 -keepattributes Signature
 -dontwarn com.firebase.ui.auth.**

-keep class com.freshdesk.hotline.** { *; }
-keep class com.demach.konotor.** { *; }
-keep class com.google.firebase.iid.** { *; }
-keep class com.google.android.gms.internal.firebase_messaging.** { *; }

-keep class com.google.firebase.database.** { *; }
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes *Annotation*

-keep class com.naman.questionbank.** { *; }

# Rules to keep the FCM dependency optional
-dontwarn com.google.firebase.messaging.RemoteMessage
-keep class com.google.firebase.messaging.RemoteMessage

# Proguard config for GSON
# Ref : https://google-gson.googlecode.com/svn/trunk/examples/android-proguard-example/proguard.cfg
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Proguard for Calligraphy
# Keep calligraphy classes when calligraphy is included as a dependency
-keep class uk.co.chrishenx.calligraphy.** { *; }
# Ignore warning from proguard for calligraphy classes, when calligraphy is not included
-dontwarn uk.co.chrishenx.calligraphy.**

# Proguard config for AppCompat
# Ref : https://code.google.com/p/android/issues/detail?id=78293
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.view.ActionProvider {
    public <init>(android.content.Context);
}


# Proguard for React Native

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-dontwarn com.facebook.react.**

-dontwarn android.text.StaticLayout
-dontwarn javax.naming.**

# For CleverTap SDK
-dontwarn com.clevertap.android.sdk.**

# Simpl
-keep public class com.simpl.android.zeroClickSdk.* { *; }
-dontwarn com.simpl.android.fingerprint.a.**

# For MapMyIndia
-keepattributes EnclosingMethod
-keepclasseswithmembers class * {
 @retrofit.http.* <methods>;
}

-keep class com.mmi.services.** { *; }
-keep class com.mapbox.geojson.** { *; }
-dontwarn com.mmi.services.**

-keep class com.mmi.apis.distance.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.geocoder.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.reversegeocode.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.place.** {
    <fields>;
    <methods>;
}

-keep class com.mmi.apis.routing.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.autosuggest.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.details.** {
    <fields>;
    <methods>;
}
-keep class com.mmi.apis.place.nearby.** {
    <fields>;
    <methods>;
}

# For Jiny.io
-dontwarn com.jiny.android.**
-keep class com.jiny.android.** {*;}

# ------------------- TEST DEPENDENCIES -------------------
-dontwarn org.hamcrest.**
-dontwarn android.test.**
-dontwarn android.support.test.**

-keep class org.hamcrest.** {
   *;
}

-keep class org.junit.** { *; }
-dontwarn org.junit.**

-keep class junit.** { *; }
-dontwarn junit.**

-keep class sun.misc.** { *; }
-dontwarn sun.misc.**

-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

-keep class com.xiaomi.** { *; }

-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

#------ WIBMO -----
-keep class com.enstage.wibmo.sdk.inapp.pojo.** { *; }
-keepclassmembers class com.enstage.wibmo.sdk.inapp.pojo.** { *; }
-keep class com.enstage.wibmo.sdk.inapp.InAppBrowserActivity$* { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class com.wibmo.analytics.**{ *;}
#------ WIBMO -----

#---- HERMES ----
-keep class com.facebook.hermes.unicode.** { *; }
-keep class com.facebook.jni.** { *; }
#---- HERMES ------

#---- REACT NATIVE SVG ----
-keep public class com.horcrux.svg.** {*;}
#---- REACT NATIVE SVG ----
-keep class java.io.ObjectInputStream { *; }
# Members of this class may be accessed from native methods. Keep them
# unobfuscated.
-keep class org.aomedia.avif.android.AvifDecoder$Info {
  *;
}
