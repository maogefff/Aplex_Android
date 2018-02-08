package com.aplex.JniBase;

/**
 * Created by aplex on 2018/1/8.
 */

public class AplexGPIO {
    public int fd = 0;
    native public int open(String path, int flags);
    native public void close(int fd);
    native public long ioctl(int fd, int cmd, int value);

    static {
        System.loadLibrary("aplexTest");
    }
}
