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

# Hilt 관련 Keep 설정
-keep class dagger.hilt.** { *; }
-keep class com.nexters.misik.** { *; }
-keep class com.nexters.misik.domain.** { *; }
-keep class com.nexters.misik.ocr.** { *; }

# Hilt-generated 코드 유지
-keep class **_HiltModules { *; }
-keep class **_HiltComponents { *; }

# OcrService 관련 최적화 방지
-keep interface com.nexters.misik.domain.ocr.OcrService
-keep class com.nexters.misik.ocr.OcrServiceImpl { *; }

# 추가적으로 Hilt 관련 어노테이션 유지
-keepattributes *Annotation*
-keep class dagger.hilt.internal.** { *; }
-keep class com.nexters.misik.ocr.di.OcrModule { *; }
-keep class com.nexters.misik.ocr.di.OcrServiceProviderModule { *; }