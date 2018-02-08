package com.aplex.webcan.base;

import com.aplex.webcan.util.TUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//<CanPresenter, CanModel>
public abstract class BaseFragment<P extends BasePresenter,M extends BaseModel>extends Fragment {
	public P mPresenter;
	public M mModel;
	public Context mContext;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mPresenter = TUtil.getT(this, 0);
		mModel = TUtil.getT(this, 1);
		mPresenter.mContext = getActivity();
		//因为单继承多实现
		if (this instanceof BaseView)
			mPresenter.setVM(mModel,this);
		return createView(inflater);
	}

	protected abstract View createView(LayoutInflater inflater);

	@Override
	public void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
	}
	
	
}
