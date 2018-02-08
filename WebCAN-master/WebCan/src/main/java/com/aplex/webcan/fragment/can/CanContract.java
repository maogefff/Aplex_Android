package com.aplex.webcan.fragment.can;

import com.android.socketcan.CanSocket.CanFrame;
import com.aplex.webcan.base.BaseModel;
import com.aplex.webcan.base.BasePresenter;
import com.aplex.webcan.base.BaseView;

import android.content.Context;
//MVP模型设计的
public interface CanContract {

	interface Model extends BaseModel {

		String[] getDevices(Context context);

		String[] getSendModes(Context context);

		String[] getTypeData(Context context);

		String[] getFormatTypeData(Context context);

	}

	interface View extends BaseView {

		void showFormatError();

		void showDataError();

		void showList(CanFrame revCan0Data);

	}

	public abstract class Presenter extends BasePresenter<Model, View> {

		public abstract String[] getDevices();

		public abstract String[] getSendModes();

		public abstract String[] getSendTypes();

		public abstract String[] getCanFormats();

		public abstract void sendCanData(String canIdStr, String text, int mCurrentTypePosition,
				int mCurrentFormatTypePosition,String times,String intvl);

		public abstract void performToggleEvent();

		public abstract void turnOffToggle();

		public abstract void turnOnToggle(int Can0Position, int Can1Position);	
		
	}
}
