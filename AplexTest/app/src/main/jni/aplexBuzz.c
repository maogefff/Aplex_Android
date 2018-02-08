#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <string.h>
#include <jni.h>

#include "android/log.h"
static const char *TAG="aplexBuzz";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)

#define BUZZER_ENABLE   182
#define BUZZER_FREQENCY 183
#define BUZZER_DISABLE  184

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    enable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_aplex_JniBase_AplexBuzz_enable
  (JNIEnv *env, jobject obj) {
	int fd = open("/dev/pwm-buzzer", O_RDWR);
	if ( fd == -1 ) {
		LOGE("open buzzer device error.");
		return;
	}

    ioctl(fd, BUZZER_ENABLE, 0);

	close(fd);

	//LOGE("enable buzzer device. ");
}

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    setFrequency
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_aplex_JniBase_AplexBuzz_setFrequency
  (JNIEnv *env, jobject obj, jint frequency) {

	int fd = open("/dev/pwm-buzzer", O_RDWR);
	if ( fd == -1 ) {
		LOGE("open buzzer device error.");
		return;
	}

    ioctl(fd, BUZZER_FREQENCY, frequency);

	close(fd);

	//LOGE("set buzzer device frequency. ");
}

/*
 * Class:     com_android_buzz_AplexBuzz
 * Method:    disable
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_aplex_JniBase_AplexBuzz_disable
  (JNIEnv *env, jobject obj) {

	int fd = open("/dev/pwm-buzzer", O_RDWR);
	if ( fd == -1 ) {
		LOGE("open buzzer device error.");
		return;
	}

    ioctl(fd, BUZZER_DISABLE, 0);

	close(fd);

	//LOGE("disable buzzer device. ");
}

