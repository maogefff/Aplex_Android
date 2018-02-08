package com.aplex.webcan.fragment.can;

import com.aplex.webcan.R;
import android.content.Context;

public class CanModel implements CanContract.Model{

	@Override
	public String[] getDevices(Context context) {
		return context.getResources().getStringArray(R.array.can_devices);
	}

	@Override
	public String[] getSendModes(Context context) {
		return context.getResources().getStringArray(R.array.send_mode);
	}

	@Override
	public String[] getTypeData(Context context) {
		return context.getResources().getStringArray(R.array.send_type);
	}

	@Override
	public String[] getFormatTypeData(Context context) {
		return context.getResources().getStringArray(R.array.can_format);
	}

}
