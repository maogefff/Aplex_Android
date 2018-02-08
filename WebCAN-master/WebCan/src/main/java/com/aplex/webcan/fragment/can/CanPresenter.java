package com.aplex.webcan.fragment.can;

import com.android.socketcan.CanSocket.CanFrame;
import com.android.socketcan.CanSocket.CanId;
import com.android.socketcan.CanUtils;
import com.aplex.webcan.R;
import com.aplex.webcan.util.UIUtils;

import android.os.SystemClock;

public class CanPresenter extends CanContract.Presenter {
	private CanFrame revCan0Data;
	private CanFrame sendCan0Frame;
	private Thread mThread;
	private boolean runFlag;
	private byte[] datas;

	@Override
	public void onDestroy() {
	}

	@Override
	public void onStart() {
	}

	@Override
	public String[] getDevices() {
		return mModel.getDevices(mContext);
	}

	@Override
	public String[] getSendModes() {
		return mModel.getSendModes(mContext);
	}

	@Override
	public String[] getSendTypes() {
		return mModel.getTypeData(mContext);
	}

	@Override
	public String[] getCanFormats() {
		return mModel.getFormatTypeData(mContext);
	}

	@Override
	public void sendCanData(final String canIdStr, final String text, final int mCurrentTypePosition,
			final int mCurrentFormatTypePosition, final String times, final String intvl) {
		try {
			//将ID转换为long型
			Long.valueOf(canIdStr, 16);
		} catch (NumberFormatException e) {
			mView.showFormatError();
			return;
		}
		if (text.length() % 2 != 0) {
			mView.showDataError();
			return;
		}
		//将data转为int
		datas = new byte[text.length() / 2];
		for (int i = 0; i < text.length(); i += 2) {
			try {
				int data = Integer.valueOf(text.substring(i, i + 2), 16);
				datas[i / 2] = (byte) data;
			} catch (NumberFormatException e) {
				mView.showDataError();
				return;
			}
		}
		//匿名线程
		new Thread() {
			public void run() {
				int looptimes = Integer.valueOf(times);
				long intvl_ms = Integer.valueOf(intvl);
				for (int l = 0; l < looptimes; l++) {
					if (looptimes != 0) {
						SystemClock.sleep(intvl_ms);
					}
					//发送数据了：(int Type, int Format,int address, byte[] data)
					int success = CanUtils.sendCan0Data(mCurrentTypePosition == 0 ? CanUtils.EFF : CanUtils.SFF,
							mCurrentFormatTypePosition == 0 ? CanUtils.DATA : CanUtils.REMOTE,
							mCurrentTypePosition == 0 ? Long.valueOf(canIdStr, 16) > 0x1fffffff ? 0x1fffffff : Integer.valueOf(canIdStr, 16)
									: Long.valueOf(canIdStr, 16) > 0x7ff ? 0x7ff : Integer.valueOf(canIdStr, 16),
							datas);
					if (success < 0) {
						return;
					}
					// 保存数据发给view
					CanId canId = new CanId(mCurrentTypePosition == 0
							? Long.valueOf(canIdStr, 16) > 0x1fffffff ? 0x1fffffff : Integer.valueOf(canIdStr, 16)
							: Long.valueOf(canIdStr, 16) > 0x7ff ? 0x7ff : Integer.valueOf(canIdStr, 16));
					if (mCurrentTypePosition == 0)
						canId.setEFFSFF();
					else
						canId.clearEFFSFF();

					if (mCurrentFormatTypePosition == 0)
						canId.clearRTR();
					else
						canId.setRTR();
					sendCan0Frame = new CanFrame(null, canId, datas);
					//子线程更新UI的方式
					UIUtils.post(new Runnable() {
						@Override
						public void run() {
							sendCan0Frame.rx_tx = CanFrame.TX;
							mView.showList(sendCan0Frame);
						}
					});
				}
			};
		}.start();
	}

	@Override
	public void performToggleEvent() {
	}

	@Override
	public void turnOnToggle(int Can0Position, int Can1Position) {
		runFlag = true;
		//配置好cmdList，并通过shell执行
		CanUtils.configCan0Device(mContext.getResources().getStringArray(R.array.baudrates)[Can0Position],
				mContext.getResources().getStringArray(R.array.baudrates)[Can1Position]);
		//创建socket
		CanUtils.initCan0();
		mThread = new Thread() {
			public void run() {
				//在这里死循环了
				while (runFlag) {
					//接收数据
					revCan0Data = CanUtils.revCan0Data();
					if (revCan0Data != null) {
						//BaseApplication.mHandler.post(new Runnable());
						UIUtils.post(new Runnable() {
							@Override
							public void run() {
								revCan0Data.rx_tx = CanFrame.RX;
								//showList在其父类CanFragment.java中实现的方法,显示在屏幕上
								mView.showList(revCan0Data);
							}
						});
					}
				}
			};
		};
		mThread.start();

	}

	@Override
	public void turnOffToggle() {
		runFlag = false;
		mThread.interrupt();
		CanUtils.closeCan();
	}

}
