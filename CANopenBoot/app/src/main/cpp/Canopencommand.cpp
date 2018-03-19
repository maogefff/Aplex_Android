#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/un.h>
#include <sys/socket.h>
#include <string.h>

extern "C" {

#include "android/log.h"
static const char *TAG="Canopencommand";
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO,  TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, TAG, fmt, ##args)
#define BUF_SIZE            100000
static const int ERRNO_BUFFER_LEN = 1024;
/* Helper functions */
void errExit(char *msg) {
    perror(msg);
    exit(EXIT_FAILURE);
}

/**
 * 抛出异常方法
 */
static void throwException(JNIEnv *env, const std::string& exception_name,
                           const std::string& msg)
{
    const jclass exception = env->FindClass(exception_name.c_str());
    if (exception == NULL) {
        return;
    }
    env->ThrowNew(exception, msg.c_str());
}

/**
 * 抛出IO异常
 */
static void throwIOExceptionMsg(JNIEnv *env, const std::string& msg)
{
    throwException(env, "java/io/IOException", msg);
}

/**
 * 抛出errno对应的异常信息
 */
static void throwIOExceptionErrno(JNIEnv *env, const int exc_errno)
{
    char message[ERRNO_BUFFER_LEN];
    // The strerror() function returns a pointer to a string that describes the error code
    const char *const msg = (char *) strerror_r(exc_errno, message, ERRNO_BUFFER_LEN);
    if (((long)msg) == 0) {
        // POSIX strerror_r, success
        throwIOExceptionMsg(env, std::string(message));
    } else if (((long)msg) == -1) {
        // POSIX strerror_r, failure
        // (Strictly, POSIX only guarantees a value other than 0. The safest
        // way to implement this function is to use C++ and overload on the
        // type of strerror_r to accurately distinguish GNU from POSIX. But
        // realistic implementations will always return -1.)
        snprintf(message, ERRNO_BUFFER_LEN, "errno %d", exc_errno);
        throwIOExceptionMsg(env, std::string(message));
    } else {
        // glibc strerror_r returning a string
        throwIOExceptionMsg(env, std::string(msg));
    }
}

/**
 * 抛出参数传递异常
 */
static void throwIllegalArgumentException(JNIEnv *env, const std::string& message)
{
    throwException(env, "java/lang/IllegalArgumentException", message);
}

/**
 * 内存越界异常
 */
static void throwOutOfMemoryError(JNIEnv *env, const std::string& message)
{
    throwException(env, "java/lang/OutOfMemoryError", message);
}

static int printErrorDescription = 0;


/* Extract error description */
typedef struct {
    int code;
    char* desc;
} errorDescs_t;

static const errorDescs_t errorDescs[] = {
        {100, "Request not supported."},
        {101, "Syntax error."},
        {102, "Request not processed due to internal state."},
        {103, "Time-out (where applicable)."},
        {104, "No default net set."},
        {105, "No default node set."},
        {106, "Unsupported net."},
        {107, "Unsupported node."},
        {200, "Lost guarding message."},
        {201, "Lost connection."},
        {202, "Heartbeat started."},
        {203, "Heartbeat lost."},
        {204, "Wrong NMT state."},
        {205, "Boot-up."},
        {300, "Error passive."},
        {301, "Bus off."},
        {303, "CAN buffer overflow."},
        {304, "CAN init."},
        {305, "CAN active (at init or start-up)."},
        {400, "PDO already used."},
        {401, "PDO length exceeded."},
        {501, "LSS implementation- / manufacturer-specific error."},
        {502, "LSS node-ID not supported."},
        {503, "LSS bit-rate not supported."},
        {504, "LSS parameter storing failed."},
        {505, "LSS command failed because of media error."},
        {600, "Running out of memory."},
        {0x00000000, "No abort."},
        {0x05030000, "Toggle bit not altered."},
        {0x05040000, "SDO protocol timed out."},
        {0x05040001, "Command specifier not valid or unknown."},
        {0x05040002, "Invalid block size in block mode."},
        {0x05040003, "Invalid sequence number in block mode."},
        {0x05040004, "CRC error (block mode only)."},
        {0x05040005, "Out of memory."},
        {0x06010000, "Unsupported access to an object."},
        {0x06010001, "Attempt to read a write only object."},
        {0x06010002, "Attempt to write a read only object."},
        {0x06020000, "Object does not exist."},
        {0x06040041, "Object cannot be mapped to the PDO."},
        {0x06040042, "Number and length of object to be mapped exceeds PDO length."},
        {0x06040043, "General parameter incompatibility reasons."},
        {0x06040047, "General internal incompatibility in device."},
        {0x06060000, "Access failed due to hardware error."},
        {0x06070010, "Data type does not match, length of service parameter does not match."},
        {0x06070012, "Data type does not match, length of service parameter too high."},
        {0x06070013, "Data type does not match, length of service parameter too short."},
        {0x06090011, "Sub index does not exist."},
        {0x06090030, "Invalid value for parameter (download only)."},
        {0x06090031, "Value range of parameter written too high."},
        {0x06090032, "Value range of parameter written too low."},
        {0x06090036, "Maximum value is less than minimum value."},
        {0x060A0023, "Resource not available: SDO connection."},
        {0x08000000, "General error."},
        {0x08000020, "Data cannot be transferred or stored to application."},
        {0x08000021, "Data cannot be transferred or stored to application because of local control."},
        {0x08000022, "Data cannot be transferred or stored to application because of present device state."},
        {0x08000023, "Object dictionary not present or dynamic generation fails."},
        {0x08000024, "No data available."}
};


static char* sendCommand(int fd, char *command, size_t commandLength) {
    size_t n;
    char buf[BUF_SIZE];

    if (write(fd, command, commandLength) != commandLength) {
        LOGD("Socket write failed");
    }

    n = read(fd, buf, sizeof(buf));

    if (printErrorDescription == 1) {
        char *errLoc = strstr(buf, "ERROR:");
        char *endLoc = strstr(buf, "\r\n");

        if (errLoc != NULL && endLoc != NULL) {
            int num;
            char *sRet = NULL;

            errLoc += 6;

            num = strtol(errLoc, &sRet, 0);
            if (strlen(errLoc) != 0 && sRet == strchr(errLoc, '\r')) {
                int i, len;

                len = sizeof(errorDescs) / sizeof(errorDescs_t);

                for (i = 0; i < len; i++) {
                    const errorDescs_t *ed = &errorDescs[i];
                    if(ed->code == num) {
                        sprintf(endLoc, " - %s\r\n", ed->desc);
                        break;
                    }
                }
            }
        }
    }
    LOGD("返回值为：%s",buf);
    return buf;
}


JNIEXPORT jstring JNICALL Java_com_aplex_canopenboot_CANopen_canopenUtils
        (JNIEnv *env, jobject obj, jobjectArray argv){
    char *socketPath = "/tmp/CO_command_socket";  /* Name of the local domain socket, configurable by arguments. */
    char *inputFilePath = NULL;
    char buf[BUF_SIZE];
    int fd;
    struct sockaddr_un addr;
    int opt;
    int i;
    int client_len;
    int cnt = 0;
    jboolean isCopy;

    int len = env->GetArrayLength(argv);
    for (int i = 0; i < len; ++i) {
        jstring str = (jstring)env->GetObjectArrayElement(argv, i);
        const char* cstr= env->GetStringUTFChars(str, &isCopy);
    }
    /* 创建socket */
    fd = socket(AF_UNIX, SOCK_STREAM, 0);
    if (fd == -1) {
        LOGD("Socket creation failed");
    }

    memset(&addr, 0, sizeof(struct sockaddr_un));
    addr.sun_family = AF_UNIX;

    addr.sun_path[0]='\0';
    strcpy (addr.sun_path+1, socketPath);
    client_len = offsetof(struct sockaddr_un, sun_path) + strlen(addr.sun_path+1);

    if(connect(fd, (struct sockaddr *)&addr, client_len) == -1) {
        LOGD("Socket connection failed");
    }

    buf[0] = 0;
    size_t buflen = 0;

    //当第1个元素不为[x]的时候补上
    jstring jstr = (jstring)env->GetObjectArrayElement(argv, 0);
    const char* cstr = env->GetStringUTFChars(jstr, &isCopy);
    if(isCopy!=JNI_TRUE){
        LOGD("GetStringUTFChars error");
    }

    if(cstr[1]!='['){
        strcat(buf, "[1] ");
    } else{
        cnt++;
    };
    env->ReleaseStringUTFChars(jstr, cstr);   //释放
    int argc = env->GetArrayLength(argv);    //拿到字符串数组长度

    for (i = cnt; i < argc; i++) {
        jstring jstr = (jstring)env->GetObjectArrayElement(argv, i);
        const char* cstr = env->GetStringUTFChars(jstr, &isCopy);
        strncat(buf, cstr, (BUF_SIZE - 2) - buflen);

        strcat(buf, " ");
        buflen = strlen(buf);
        if (buflen >= (BUF_SIZE - 1)) {
            fprintf(stderr, "String too long!\n");
        }
    }

    buf[buflen - 1] = '\n';
    buf[buflen] = 0;

    printErrorDescription = 1;
    const char* ret = sendCommand(fd, buf, buflen);
    if(ret[0]=='['){
        ret=ret+3;
    }
    close(fd);

    return env->NewStringUTF(ret);

}

}
