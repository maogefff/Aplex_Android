package com.aplex.canopenboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.aplex.canopenboot.utils.CanUtils;
import com.aplex.canopenboot.utils.SPUtils;


public class BootReceiver extends BroadcastReceiver {
    private final String TAG = "BootReceiver11";
    public final static String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    Boolean isOpen;
    Integer baudRateIndex;
    Integer nodeID;
    Integer baudRate;

    // 重写onReceive方法
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(ACTION_BOOT_COMPLETED)){
            isOpen = (Boolean) SPUtils.getValue("isOpen", false);
            //如果是关闭的
            if(isOpen==true){
                Log.d(TAG, "isOpen=1");
                baudRateIndex = (Integer) SPUtils.getValue("baudRateIndex", Integer.valueOf(0));
                baudRate = (Integer) SPUtils.getValue("baudRate", Integer.valueOf(0));
                nodeID = (Integer) SPUtils.getValue("nodeID", Integer.valueOf(0));
                Log.d(TAG, "baudRateIndex="+baudRateIndex);
                Log.d(TAG, "baudRate="+baudRate);
                Log.d(TAG, "nodeID="+nodeID);
                //设置数据
                CanUtils.setCmdList(String.valueOf(baudRate), String.valueOf(nodeID));
                //启动
                CanUtils.startCanopen();
            }else{
                Log.d(TAG, "isOpen=0");
            }
        }
    }
}

