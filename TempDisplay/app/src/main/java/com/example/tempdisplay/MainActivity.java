package com.example.tempdisplay;

import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "MainActivity";
    private Button btn_show;
    private Button btn_hide;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_show = (Button) findViewById(R.id.bt_start);
        btn_hide = (Button) findViewById(R.id.bt_stop);
        btn_show.setOnClickListener(this);
        btn_hide.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_start:
                //Android6.0之后的要单独单开权限
                if (Build.VERSION.SDK_INT >= 23) {

                    if(!Settings.canDrawOverlays(this)) {
                        Intent it = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        startActivity(it);
                        return;
                    } else {
                        Intent start = new Intent(this, TopWindowService.class);
                        startService(start);
                    }
                } else {
                    Intent start = new Intent(this, TopWindowService.class);
                    startService(start);
                }

                break;
            case R.id.bt_stop:
                Intent stop = new Intent(this, TopWindowService.class);
                stopService(stop);
                break;
        }
    }
}
