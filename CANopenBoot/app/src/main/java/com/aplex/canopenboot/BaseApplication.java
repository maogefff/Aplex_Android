package com.aplex.canopenboot;

import android.app.Application;
import android.util.Log;

import com.aplex.canopenboot.utils.SPUtils;


public class BaseApplication extends Application {

	private static String TAG = "BaseApplication";
	@Override
	public void onCreate() {
		//初始化工具
		SPUtils.setApplication(this);
		super.onCreate();
	}

}
