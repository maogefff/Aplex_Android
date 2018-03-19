package com.aplex.canopenboot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.aplex.canopenboot.utils.CanUtils;
import com.aplex.canopenboot.utils.SPUtils;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private EditText mNode;
    private Spinner mBaudRate;
    private Switch mSwitch;
    CANopen caNopen;
    Integer baudRateIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caNopen = new CANopen();
        mNode = (EditText)findViewById(R.id.nodeID);
        mBaudRate = (Spinner)findViewById(R.id.baudrateID);
        mSwitch = (Switch)findViewById(R.id.switchID);

        baudRateIndex = (Integer)SPUtils.getValue("baudRateIndex", Integer.valueOf(0));
        boolean isStart = (Boolean) SPUtils.getValue("isStart", Boolean.valueOf(true));

        mBaudRate.setSelection(baudRateIndex);

        if(isStart)
            mSwitch.setChecked(true);
        else
            mSwitch.setChecked(false);

        //波特率
        mBaudRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                baudRateIndex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //开关
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean isOpen;

                String currentBaudrate = getResources().getStringArray(R.array.baudRates)[baudRateIndex];  //波特率
                String currentNodeID = mNode.getText().toString();   //节点号

                SPUtils.pushInt("baudRateIndex", baudRateIndex);
                SPUtils.pushInt("baudRate", Integer.valueOf(currentBaudrate));
                SPUtils.pushInt("nodeID", Integer.valueOf(currentNodeID));
                isOpen =mSwitch.isChecked();
                SPUtils.pushBoolean("isOpen", isOpen);

                //设置
                CanUtils.setCmdList(currentBaudrate, currentNodeID);
            }
        });

    }

    static class CanCommand extends com.stericson.RootTools.execution.Command{

        public CanCommand(int id, String... command) {
            super(id, command);
        }
        @Override
        public void commandOutput(int i, String s) {
            //命令执行的过程中会执行此方法，line参数可用于调试
        }
        @Override
        public void commandTerminated(int i, String s) {
            //命令被取消后的执行此方法
        }
        @Override
        public void commandCompleted(int i, int i1) {
            //命令执行完成后会调用此方法
        }
    }
}
