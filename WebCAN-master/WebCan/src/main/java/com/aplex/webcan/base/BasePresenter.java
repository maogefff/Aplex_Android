package com.aplex.webcan.base;

import android.content.Context;

public abstract class BasePresenter<M,V> {
	public Context mContext;
	public M mModel;
	public V mView;

	public void setVM(M m,V v){
		this.mModel = m ;
		this.mView = v ;
		onStart();
	}
	public abstract void onDestroy();
	public abstract void onStart();

}
