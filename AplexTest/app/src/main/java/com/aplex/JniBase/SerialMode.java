package com.aplex.JniBase;

/**
 * Created by aplex on 2017/12/13.
 */
public class SerialMode {

    static {
        System.loadLibrary("aplexTest");
    }
    public int fd = -1;
    native public int open(String path, int flags);
    native public void close(int fd);
    native public long ioctl(int fd, int cmd, int value);
}
