package com.aplex.webcan;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.aplex.webcan.flatui.FlatUI;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

public class BaseApplication extends Application{
	private final static String TAG = "BaseApplication";
	private static Context	mContext;    //这里上下文就是自己
	private static Handler	mHandler;
	private static Thread	mMainThread;
	private static long	mMainThreadId;
	private static Looper	mMainThreadLooper;

	@Override
	public void onCreate()
	{
		super.onCreate();

		mContext = this;

		// handler
		mHandler = new Handler();

	
		mMainThread = Thread.currentThread();
		// id
		
		mMainThreadId = android.os.Process.myTid();

		// looper
		mMainThreadLooper = getMainLooper();
		FlatUI.initDefaultValues(this);

        // Setting default theme to avoid to add the attribute "theme" to XML
        // and to be able to change the whole theme at once
        FlatUI.setDefaultTheme(FlatUI.SKY);
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果APP启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
		 
		if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
		    Log.e(TAG, "enter the service process!");
		 
		    // 则此application::onCreate 是被service 调用的，直接返回
		    return;
		}
		//init
		EMOptions options = new EMOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		//初始化
		EMClient.getInstance().init(this, options);
		//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
		EMClient.getInstance().setDebugMode(true);
	}

	//系统一开机就会调用onCreate,所以这里已经创建好了
	public static Context getContext()
	{
		return mContext;
	}

	public static Handler getHandler()
	{
		return mHandler;
	}

	public static Thread getMainThread()
	{
		return mMainThread;
	}

	public static long getMainThreadId()
	{
		return mMainThreadId;
	}

	public static Looper getMainThreadLooper()
	{
		return mMainThreadLooper;
	}

	 private String getAppName(int pID) {
	    	String processName = null;
	    	ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	    	List l = am.getRunningAppProcesses();
	    	Iterator i = l.iterator();
	    	PackageManager pm = this.getPackageManager();
	    	while (i.hasNext()) {
	    		ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
	    		try {
	    			if (info.pid == pID) {
	    				processName = info.processName;
	    				return processName;
	    			}
	    		} catch (Exception e) {
	    			// Log.d("Process", "Error>> :"+ e.toString());
	    		}
	    	}
	    	return processName;
	    }
}
