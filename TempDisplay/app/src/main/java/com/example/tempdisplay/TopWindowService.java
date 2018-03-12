package com.example.tempdisplay;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TopWindowService extends Service {

    private String TAG = "TopWindowService";

    private boolean isAdded = false; // 是否已增加悬浮窗
    private static WindowManager wm;
    private static WindowManager.LayoutParams params;
    private Button btn_floatView;
    private String CPUTEMPString;
    private Thread getfolatViewThread = null;
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        createFloatView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        wm.removeView(btn_floatView);
    }

    public static String getAppCpuTemp() { // 拿到温度
        FileReader fr1;
        String text1 = "";
        float ftmp;
        try {
            fr1 = new FileReader("/sys/class/thermal/thermal_zone0/temp");
            BufferedReader br1 = new BufferedReader(fr1);

            text1 = br1.readLine();
            text1 = text1.trim();
            ftmp = Float.parseFloat(text1);

            if(ftmp>1000){
                ftmp = ftmp/1000;
                text1 = String.valueOf(ftmp);
            }
            //text1 = (new BigDecimal(Float.parseFloat(text1)/1000).setScale(1, BigDecimal.ROUND_HALF_UP)) + "";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return text1;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:
                    btn_floatView.setText(CPUTEMPString);
                    break;
            }
        }
    };

    /**
     * 创建悬浮窗
     */

    private void createFloatView()
    {
        btn_floatView = new Button(getApplicationContext());
        btn_floatView.setBackgroundColor(Color.TRANSPARENT);   //将背景设置为透明
        btn_floatView.setText("CPU temp:  "+0+"℃");

        //拿到窗口管理系统服务
        wm = (WindowManager) getApplicationContext().getSystemService(
                Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        // 设置window type
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;

        // 设置Window flag
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        // 设置悬浮窗的长得宽
        params.width = 180;
        params.height = 80;

        // 设置悬浮窗的Touch监听
        btn_floatView.setOnTouchListener(new View.OnTouchListener()
        {
            int lastX, lastY;
            int paramX, paramY;

            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params.x;
                        paramY = params.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params.x = paramX + dx;
                        params.y = paramY + dy;
                        // 更新悬浮窗位置
                        wm.updateViewLayout(btn_floatView, params);
                        break;
                }
                return true;
            }
        });

        wm.addView(btn_floatView, params);
        isAdded = true;

        //测试，每500ms更新一次
        getfolatViewThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String cpuCurrentTemp = "0";

                while (true){
                    try {
                        Thread.sleep(500);
                        cpuCurrentTemp = getAppCpuTemp();
                        CPUTEMPString = (cpuCurrentTemp == null && "".equals(cpuCurrentTemp)) ? "CPU Rate:  0℃":"CPU temp:  "+cpuCurrentTemp+"℃";
                        mHandler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        getfolatViewThread.start();
    }

}