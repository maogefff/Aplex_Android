package com.aplex.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aplex.JniBase.I2CController;
import com.aplex.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class AboutFragment extends Fragment {

    String i2c_node = "/dev/i2c-2"; 		//保存i2c节点路径
    I2CController i2cController = null;
    Context context;
    View view;
    public AboutFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView about = (TextView)view.findViewById(R.id.about);
        context = getActivity();
        i2cController = new I2CController();	//i2c通信需要调用本地接口
        int writeAddr = 0x50;
        int writeOffset = 0x0;
        Log.e("open file ", i2c_node);
        i2cController.fd = i2cController.open(i2c_node, 0);

        String readData=null;
        if (i2cController.fd < 3) {
            Toast.makeText(context, "Missing read/write permission, \ntrying to chmod the file.", Toast.LENGTH_LONG).show();

        } else {
            readData = i2cController.readSoftID(i2cController.fd, writeAddr, writeOffset, 0x40);
            /**
             * 这里和-1进行比较是在jni里设定好的，如果出错，那么返回字符串-1
             */
            if("-1".equals(readData)) {
                Toast.makeText(context, "Read failed.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Read successed.", Toast.LENGTH_SHORT).show();
            }
            i2cController.close(i2cController.fd);
            i2cController.fd=-1;
        }
        String message = "File System Version: " + SystemProperties.get("FileSystemVersion", "") + "\n";
        if(readData==null || "-1".equals(readData) || readData.length() != 12)
        {
            message += "Software Part Number: " + SystemProperties.get("SoftwarePartNumber", "") + "\n";
        }
        else
        {
            message += "Software Part Number: " + readData + "\n";
        }
        message += "CPU: " + SystemProperties.get("CPU", "") + "\n";
        message += "RAM: " + SystemProperties.get("RAM", "") + "\n";

        //about.setText("File System Version: 001 \nSoftware Part Number: 071661000600 \nCPU: i.MX6 Cortex A9 1.0GHz \nRAM: DDR3 1GB.");
        about.setText(message);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_about,container,false);
        return view;
    }
}
