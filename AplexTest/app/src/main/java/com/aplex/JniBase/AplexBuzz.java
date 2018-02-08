package com.aplex.JniBase;

import android.util.Log;

public class AplexBuzz {
	private static final String TAG = "BuzzAplex";

	public int fd;
	/**
	 * enable the onboard buzzer.<br>
	 * Every time when you set the buzzer frequency, you need to 
	 * call the enable() function then it will work under setted frequency
	 */
	public static native void enable();
	/**
	 * set the onboard buzzer's frequency 
	 * @param frequency frequency range: 0~20000(Hz)
	 */
	public static native void setFrequency(int frequency);
	/**
	 * disable the onboard buzzer
	 */
	public static native void disable();
	
	static {
		System.loadLibrary("aplexTest");
	}
}
