#include <stdio.h>
#include <string.h>
#include <linux/types.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ioctl.h>
#include <errno.h>
#include "i2c-dev.h"
#include "i2c.h"
#include "i2c_data.h"
#include <jni.h>

#include "android/log.h"
static const char *TAG="EEPROM_aplex";
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
/*
 * Class:     com_android_eeprom_I2CController
 * Method:    open
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_aplex_JniBase_I2CController_open
(JNIEnv * env, jobject thiz, jstring path, jint flags){
	int fd = -1;
	// Opening device
	{
		jboolean iscopy;
		/**
		 * 将Java的字符串转换成C中的字符串
		 */
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		//LOGE("Open file %s with flags 0x%x", path_utf, O_RDWR | flags);
		fd = open(path_utf, O_RDWR | flags);
		//LOGE("open() fd = %d", fd);
		/**
		 * 和前面的GetStringUTFChars一对用法，相当于malloc和free
		 */
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd == -1)
		{
			// Throw an exception
			LOGE("Cannot open file");
			// TODO: throw an exception
			return -1;
		}
	}
    return (jint)fd;
}

/*
 * Class:     com_android_eeprom_I2CController
 * Method:    readStr
 * Signature: (IIII)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_aplex_JniBase_I2CController_readStr
(JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jint count){
    unsigned char buf[count+1];
    bzero(buf, count+1);
    if(offset<0)
        return (*env)->NewStringUTF(env, "-1");
    if (i2c_data_read_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count) == 0) {
    	return (*env)->NewStringUTF(env, (char *)buf);
    } else {
    	return (*env)->NewStringUTF(env, "-1");
    }
	//LOGE("read i2c data: %s\n", buf);
}

JNIEXPORT jstring JNICALL Java_com_android_about_I2CController_readData
        (JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jint count){
    unsigned char buf[count+1];
    unsigned char javabuf[count*2+1];
    bzero(buf, count+1);
    bzero(javabuf, count*2+1);
    if (i2c_data_read_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count) == 0) {
        int i;
        for(i=0;i<count;i++)
        {
            sprintf((char *)javabuf+i*2,"%02x",buf[i]);
        }
        return (*env)->NewStringUTF(env, (char *)javabuf);
    } else {
        return (*env)->NewStringUTF(env, "-1");
    }
    //LOGE("read i2c data: %s\n", buf);
}

/*
 * Class:     com_android_eeprom_I2CController
 * Method:    writeStr
 * Signature: (IIII)Ljava/lang/String;
 */
JNIEXPORT jint JNICALL Java_com_aplex_JniBase_I2CController_writeStr
(JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jstring buffer, jint count){
    unsigned char* buf = NULL;
    jclass clsstring = (*env)->FindClass(env, "java/lang/String");
    jstring strencode = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr= (jbyteArray)(*env)->CallObjectMethod(env, buffer, mid, strencode);
    jsize alen = (*env)->GetArrayLength(env, barr);
    jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
    if (alen > 0)
    {
        buf = (unsigned char*)malloc(alen + 1);
    
        memcpy(buf, ba, alen);
        buf[alen] = 0;
    }
    (*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	//LOGE("fd: %d, write String: %s, string.length: %d", fd, buf, alen);
    return i2c_data_write_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count > alen ? alen : count);
}

JNIEXPORT jint JNICALL  Java_com_android_about_I2CController_writeCalibrationData
        (JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jint min_x, jint min_y, jint max_x, jint max_y, jint swap, jint invert_x,jint invert_y)
{
    unsigned char buf[10] = {0};
    buf[0]=0x03;
    buf[1]=0x0e;
    buf[2]=(min_x>>8)&0xff;
    buf[3]=min_x&0xff;
    buf[4]=(min_y>>8)&0xff;
    buf[5]=min_y&0xff;
    buf[6]=(max_x>>8)&0xff;
    buf[7]=max_x&0xff;
    i2c_data_write_str (fd, (unsigned char)addr, (unsigned char)offset, buf,  8);
    buf[0]=(max_y>>8)&0xff;
    buf[1]=max_y&0xff;
    buf[2]=swap?0x01:0x00;
    buf[3]=invert_x?0x01:0x00;
    buf[4]=invert_y?0x01:0x00;
    buf[5]=0xff;
    buf[6]=0xff;
    buf[7]=0xff;
    return i2c_data_write_str (fd, (unsigned char)addr, (unsigned char)offset+8, buf, 8);
}
/*
 * Class:     com_android_eeprom_I2CController
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_aplex_JniBase_I2CController_close
(JNIEnv * env, jobject thiz, jint fd){
	close( (int)fd );
	//LOGE("close() fd = %d", fd);
}

JNIEXPORT jstring JNICALL Java_com_aplex_JniBase_I2CController_readSoftID
        (JNIEnv * env, jobject thiz, jint fd, jint addr, jint offset, jint count){
    unsigned char buf[count+1];
    unsigned char javabuf[12+1];
    bzero(buf, count+1);
    bzero(javabuf, 12+1);

    if (i2c_data_read_str (fd, (unsigned char)addr, (unsigned char)offset, buf, count) == 0) {
#if 0
        int i;
        unsigned long highpart=0;
        unsigned long lowpart=0;
        highpart=0+buf[2]*256+buf[3];
        if(highpart==0 || highpart>9999)
            return (*env)->NewStringUTF(env, "-1");
        for(i=4;i<8;i++)
        {
            lowpart=lowpart*256+buf[i];
        }
        sprintf((char *)javabuf,"%04lu%08lu",highpart,lowpart);
        return (*env)->NewStringUTF(env, (char *)javabuf);
#else
	    int i=0,j=0;
	    int datalength=0;
        unsigned long highpart=0;
        unsigned long lowpart=0;

   	    while(i<0x40)
	    {
		    datalength=buf[i+1]+2;
    		switch(buf[i])
	    	{
		    	default:
    			case 0x00:
	    			break;
		    	case 0x01:
				    break;
    			case 0x02:
		    		break;
    			case 0x03:
                    highpart=0+buf[i+2]*256+buf[i+3];
                    if(highpart<=0 || highpart>9999){
                        return (*env)->NewStringUTF(env, "-1");
                    }

                    for(j=4;j<8;j++)
                    {
                        lowpart=lowpart*256+buf[i+j];
                    }
                    sprintf((char *)javabuf,"%04lu%08lu",highpart,lowpart);
                    return (*env)->NewStringUTF(env, (char *)javabuf);
		    		break;
			    case 0x04:
    				break;
	    		case 0x10:
			    	break;
    			case 0x11:
		    		break;
    		}
    		i=i+datalength;
    	}
        return (*env)->NewStringUTF(env, "-1");
#endif
    } else {
        return (*env)->NewStringUTF(env, "-1");
    }
    //LOGE("read i2c data: %s\n", buf);
}

