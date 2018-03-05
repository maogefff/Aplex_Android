package com.aplex.aplexboottest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatTextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private FlatTextView mTv;
    private FlatButton mClearBtn;
    private FlatButton mMoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlatUI.initDefaultValues(this);
        FlatUI.setDefaultTheme(FlatUI.SKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSync();
        initView();
        initData();
        initListener();
    }

    private void initSync() {
        String a ="";
        Log.e("MainActivity", Build.HARDWARE);
        Log.e("MainActivity", Build.CPU_ABI);
        Log.e("MainActivity",Build.CPU_ABI2);
        Log.e("MainActivity",Build.MODEL);
        Log.e("build","BOARD:" + Build.BOARD);
        Log.e("build","BOOTLOADER:" + Build.BOOTLOADER);
        Log.e("build","BRAND:" + Build.BRAND);
        Log.e("build","CPU_ABI:" + Build.CPU_ABI);
        Log.e("build","CPU_ABI2:" + Build.CPU_ABI2);
        Log.e("build","DEVICE:" + Build.DEVICE);
        Log.e("build","DISPLAY:" + Build.DISPLAY);
        Log.e("build","FINGERPRINT:" + Build.FINGERPRINT);
        Log.e("build","HARDWARE:" + Build.HARDWARE);
        Log.e("build","HOST:" + Build.HOST);
        Log.d("build","ID:" + Build.ID);
        Log.d("build","MANUFACTURER:" + Build.MANUFACTURER);
        Log.d("build","MODEL:" + Build.MODEL);
        Log.d("build","PRODUCT:" + Build.PRODUCT);
        Log.d("build","RADIO:" + Build.RADIO);
        Log.d("build","TAGS:" + Build.TAGS);
        Log.d("build","TIME:" + Build.TIME);
        Log.d("build","TYPE:" + Build.TYPE);
        Log.d("build","UNKNOWN:" + Build.UNKNOWN);
        Log.d("build","USER:" + Build.USER);
        Log.d("build","VERSION.CODENAME:" + Build.VERSION.CODENAME);
        Log.d("build","VERSION.INCREMENTAL:" + Build.VERSION.INCREMENTAL);
        Log.d("build","VERSION.RELEASE:" + Build.VERSION.RELEASE);
        Log.d("build","VERSION.SDK:" + Build.VERSION.SDK);
        Log.d("build","VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
    }

    private void initListener() {
        mClearBtn.setOnClickListener(this);
        // mMoreBtn.setOnClickListener(this);
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        mTv.setText("Count  :  " + count);
    }

    private void initView() {
        mTv = (FlatTextView) findViewById(R.id.count);
        mClearBtn = (FlatButton) findViewById(R.id.clear);
        //mMoreBtn = (FlatButton) findViewById(R.id.more);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                clear();
                break;
//            case R.id.more:
//                loadMore();
//               break;
            default:
                break;
        }
    }

    private void loadMore() {
//        Intent moreDetailtIntent=new Intent(getApplicationContext(), ListMoreDetail.class);
//        moreDetailtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        moreDetailtIntent.putExtra("aplexData", aplexData);
//        activity.getApplicationContext().startActivity(moreDetailtIntent);
    }

    private void clear() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("count", 0);
        editor.commit();
        mTv.setText("Count  :  0");

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
