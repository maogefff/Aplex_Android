gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-3.5-all.zip

build.gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.1'
    }
}

build.gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:support-v4:22.2.1'

    compile files('libs/gson_2.2.4.jar')
    compile files('libs/hyphenatechat_3.1.4.jar')
}