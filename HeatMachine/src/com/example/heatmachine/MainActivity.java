package com.example.heatmachine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import java.math.BigDecimal;

import com.android.aplexcheck.MX6Check;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	/**
	 * 创建四个线程用于占用cpu资源，也就是说该程序适合用于1-4核的CPU测试
	 */
	Thread thread1 = null;
	Thread thread2 = null;
	Thread thread3 = null;
	Thread thread4 = null;

	/**
	 * 这个线程用于获取CPU的利用率
	 */
	Thread getCPURateThread = null;

	/**
	 * 用于保存当前的按键状态
	 */
	boolean status = true;

	/**
	 * 用于保存合成的CPU利用率的字符串，主要是用于更新显示界面的UI
	 */
	String CPURateString = null;
	
	/**
	 * 用于保存合成的CPU温度的字符串，主要是用于更新显示界面的UI
	 */
	String CPUTEMPString = null;
	/**
	 * 主要是因为线程不能直接进行UI界面的更新，这里使用了handler来更新界面
	 */
	Handler handler = null;

	/**
	 * CPU占用率的文本显示框
	 */
	TextView cpuRate = null;
	
	/**
	 * CPU温度文本显示框
	 */
	TextView cpuTemp = null;
	
	/**
	 * 开始和停止测试按钮
	 */
	Button button = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cpuRate = (TextView) findViewById(R.id.rate);
		cpuTemp = (TextView) findViewById(R.id.temp);
		button = (Button) findViewById(R.id.start);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/**
				 * 判断当前的测试状态，并根据当前的测试状态进行对应的操作
				 */
				if (button.getText().equals("start")) {
					button.setText("stop");
					status = true;

					/**
					 * 接下来创建4个对应的线程，并启动线程
					 */
					thread1 = new Thread(new Runnable() {

						@Override
						public void run() {
							int i = 0;
							while (status) {
								i++;
							}
							thread1 = null;
						}
					});
					thread2 = new Thread(new Runnable() {

						@Override
						public void run() {
							int j = 0;
							while (status) {
								j++;
							}
							thread2 = null;
						}
					});
					thread3 = new Thread(new Runnable() {

						@Override
						public void run() {
							int k = 0;
							while (status) {
								k++;
							}
							thread3 = null;
						}
					});
					thread4 = new Thread(new Runnable() {

						@Override
						public void run() {
							int l = 0;
							while (status) {
								l++;
							}
							thread4 = null;
						}
					});

					thread1.start();
					thread2.start();
					thread3.start();
					thread4.start();
				} else {
					button.setText("start");

					if (thread1 != null && thread2 != null && thread3 != null && thread4 != null) {
						status = false;
					}
				}

			}
		});

		/**
		 * 因为android的UI界面不能用用线程直接进行更新，当前程序使用Handler进行界面的更新
		 */
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0)// 成功
				{
					cpuRate.setText(CPURateString);
					cpuTemp.setText(CPUTEMPString);
				}
			}

		};

		/**
		 * 获取CPU利用率线程，里面合成了需要显示的字符串
		 */
		getCPURateThread = new Thread(new Runnable() {

			@Override
			public void run() {
				int cpuCurrentUsage = 0;
				String cpuCurrentTemp = "0";
				while (true) {
					cpuCurrentUsage = (int) getProcessCpuRate();
					cpuCurrentTemp = getAppCpuTemp();
					CPURateString = String.format("CPU Rate: %3d%%",
							cpuCurrentUsage > 100 ? 100 : cpuCurrentUsage < 0 ? 0 : cpuCurrentUsage);
					CPUTEMPString = (cpuCurrentTemp == null && "".equals(cpuCurrentTemp)) ? "CPU Rate:  0℃":"CPU temp:  "+cpuCurrentTemp+"℃"; 
					try {
						handler.sendEmptyMessage(0);// UI线程外想更新UI线程
						Thread.sleep(300);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		getCPURateThread.start();

		// MX6Check.checkingNumber(this, 7112);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static float getProcessCpuRate() {

		float totalCpuTime1 = getTotalCpuTime();
		float processCpuTime1 = getAppCpuTime();
		try {
			Thread.sleep(360);
		} catch (Exception e) {
		}

		float totalCpuTime2 = getTotalCpuTime();
		float processCpuTime2 = getAppCpuTime();

		float cpuRate = 100 * (processCpuTime2 - processCpuTime1) / (totalCpuTime2 - totalCpuTime1);

		return cpuRate;
	}

	public static long getTotalCpuTime() { // 获取系统总CPU使用时间
		String[] cpuInfos = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/proc/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		long totalCpu = Long.parseLong(cpuInfos[2]) + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
				+ Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5]) + Long.parseLong(cpuInfos[7])
				+ Long.parseLong(cpuInfos[8]);
		return totalCpu;
	}

	public static long getAppCpuTime() { // 获取应用占用的CPU时间
		String[] cpuInfos = null;
		try {
			int pid = android.os.Process.myPid();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("/proc/" + pid + "/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		long appCpuTime = Long.parseLong(cpuInfos[13]) + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
				+ Long.parseLong(cpuInfos[16]);
		return appCpuTime;
	}

	public static String getAppCpuTemp() { // 获取应用占用的CPU时间
		FileReader fr1;
		String text1 = "";
		try {
			fr1 = new FileReader("/sys/class/thermal/thermal_zone0/temp");
			BufferedReader br1 = new BufferedReader(fr1);
			text1 = br1.readLine();
			text1 = text1.trim();
			text1 = (new BigDecimal(Float.parseFloat(text1)/1000).setScale(1, BigDecimal.ROUND_HALF_UP)) + "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text1;
	}
}
