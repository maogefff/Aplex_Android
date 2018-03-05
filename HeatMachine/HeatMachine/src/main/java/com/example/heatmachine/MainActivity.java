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
	 * �����ĸ��߳�����ռ��cpu��Դ��Ҳ����˵�ó����ʺ�����1-4�˵�CPU����
	 */
	Thread thread1 = null;
	Thread thread2 = null;
	Thread thread3 = null;
	Thread thread4 = null;

	/**
	 * ����߳����ڻ�ȡCPU��������
	 */
	Thread getCPURateThread = null;

	/**
	 * ���ڱ��浱ǰ�İ���״̬
	 */
	boolean status = true;

	/**
	 * ���ڱ���ϳɵ�CPU�����ʵ��ַ�������Ҫ�����ڸ�����ʾ�����UI
	 */
	String CPURateString = null;
	
	/**
	 * ���ڱ���ϳɵ�CPU�¶ȵ��ַ�������Ҫ�����ڸ�����ʾ�����UI
	 */
	String CPUTEMPString = null;
	/**
	 * ��Ҫ����Ϊ�̲߳���ֱ�ӽ���UI����ĸ��£�����ʹ����handler�����½���
	 */
	Handler handler = null;

	/**
	 * CPUռ���ʵ��ı���ʾ��
	 */
	TextView cpuRate = null;
	
	/**
	 * CPU�¶��ı���ʾ��
	 */
	TextView cpuTemp = null;
	
	/**
	 * ��ʼ��ֹͣ���԰�ť
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
				 * �жϵ�ǰ�Ĳ���״̬�������ݵ�ǰ�Ĳ���״̬���ж�Ӧ�Ĳ���
				 */
				if (button.getText().equals("start")) {
					button.setText("stop");
					status = true;

					/**
					 * ����������4����Ӧ���̣߳��������߳�
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
		 * ��Ϊandroid��UI���治�������߳�ֱ�ӽ��и��£���ǰ����ʹ��Handler���н���ĸ���
		 */
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0)// �ɹ�
				{
					cpuRate.setText(CPURateString);
					cpuTemp.setText(CPUTEMPString);
				}
			}

		};

		/**
		 * ��ȡCPU�������̣߳�����ϳ�����Ҫ��ʾ���ַ���
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
						handler.sendEmptyMessage(0);// UI�߳��������UI�߳�
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

	public static long getTotalCpuTime() { // ��ȡϵͳ��CPUʹ��ʱ��
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

	public static long getAppCpuTime() { // ��ȡӦ��ռ�õ�CPUʱ��
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

	public static String getAppCpuTemp() { // ��ȡӦ��ռ�õ�CPUʱ��
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
}
