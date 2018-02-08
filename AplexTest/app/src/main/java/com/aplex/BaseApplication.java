package com.aplex;

import android.app.Application;
import android.util.Log;

import com.aplex.utils.SPUtils;


public class BaseApplication extends Application {

	private static String TAG = "BaseApplication";
	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		//初始化工具
		SPUtils.setApplication(this);
		super.onCreate();
	}

}
