package com.aplex.JniBase;

public class I2CController {
    public int fd = -1;
    native public int open(String path, int flags);
    native public String readStr(int fd, int addr, int offset, int count);
    native public int writeStr(int fd, int addr, int offset, String buf, int count);
    native public void close(int fd);
    native public String readSoftID(int fd, int addr, int offset, int count);

    static {
        System.loadLibrary("aplexTest");
    }
}
