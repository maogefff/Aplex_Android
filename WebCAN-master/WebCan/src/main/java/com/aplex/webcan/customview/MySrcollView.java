package com.aplex.webcan.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MySrcollView extends ScrollView{
	public MySrcollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MySrcollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MySrcollView(Context context) {
		super(context);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
	    getParent().requestDisallowInterceptTouchEvent(true);
	    return super.dispatchTouchEvent(ev);
	}
}
