build.gradle
apply plugin: 'com.android.application'

android {
    compileSdkVersion 18
    buildToolsVersion "27.0.2"

    defaultConfig {
        applicationId "com.example.heatmachine"
        minSdkVersion 8
        targetSdkVersion 8
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.txt'
        }
    }
}

dependencies {
    compile 'com.android.support:support-v4:18.0.0'
    compile files('libs/AplexChecking.jar')
}


gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-3.5-all.zip

