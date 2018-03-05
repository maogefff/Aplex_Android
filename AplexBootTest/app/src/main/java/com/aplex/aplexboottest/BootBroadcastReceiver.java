package com.aplex.aplexboottest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by APLEX on 2016/7/25.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    /**
     * 背景知识：当Android启动时，会发出一个系统广播，内容为ACTION_BOOT_COMPLETED，
     * 它的字符串常量表示为 android.intent.action.BOOT_COMPLETED。只要在程序中“捕捉”
     * 到这个消息，再启动之即可。记住，Android框架说：Don''t call me, I''ll call you back。
     * 我们要做的是做好接收这个消息的准备，而实现的手段就是实现一个BroadcastReceiver。
     */
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        //if (intent.getAction().equals(action_boot)) {
        Toast.makeText(context, "接受到开机广播", Toast.LENGTH_SHORT).show();
        SharedPreferences sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", sp.getInt("count", 0) + 1);
        editor.commit();

        Intent ootStartIntent = new Intent(context, MainActivity.class);
        ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.e("aplex receive test", "get Theme BroadcastReceiver");
        //跳到MainActivity.class中去运行
        context.startActivity(ootStartIntent);
        //}
    }
}
