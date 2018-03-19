package com.aplex.canopenboot;

import android.util.Log;

/**
 * Created by aplex on 2018/1/29.
 */

public class CANopen {
    private static final String TAG = "CANopen";

    static {
        System.loadLibrary("Canopen-lib");
    }
    public native String canopenUtils(String[] cmd);
}
