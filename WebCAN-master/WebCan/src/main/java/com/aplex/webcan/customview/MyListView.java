package com.aplex.webcan.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView{

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    getParent().requestDisallowInterceptTouchEvent(true);
	    return super.dispatchTouchEvent(ev);
	}
}
