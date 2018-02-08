package com.aplex.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aplex.JniBase.SerialMode;
import com.aplex.R;
import com.aplex.utils.SPUtils;

import java.io.File;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class ComSelectFragment extends Fragment {

    private final String TAG = "ComSelectFragment";
    Context context;

    final int TEST_RS232_CMD_APLEX = 66;
    final int TEST_RS485_CMD_APLEX = 67;
    final int TEST_RS422_CMD_APLEX = 68;
    final int TEST_LOOPBACK_CMD_APLEX  = 69;
    final int TEST_RS485_TERM_CMD_APLEX = 70;
    final int TEST_RS422_TERM_CMD_APLEX = 71;

    SerialMode serialMode = new SerialMode();    //本地函数对象
    Button BtRS232;
    Button BtRS485;
    Button BtRS422;
    Button BtRS485Trml;
    Button BtRS422Trml;
    Button BtLoopBack;
    TextView TV;

    int BtId = -1;
    String Prompt;
    int cmd;

    public ComSelectFragment() {
        Log.d(TAG, "ComSelectFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.activity_comselect,container,false);
        context = getActivity();

        BtRS232 = (Button)view.findViewById(R.id.ButtonRS232);
        BtRS485 = (Button)view.findViewById(R.id.ButtonRS485);
        BtRS422 = (Button)view.findViewById(R.id.ButtonRS422);
        BtRS485Trml = (Button)view.findViewById(R.id.ButtonRS485Trml);
        BtRS422Trml = (Button)view.findViewById(R.id.ButtonRS422Trml);
        BtLoopBack = (Button)view.findViewById(R.id.ButtonLoopBack);
        TV = (TextView)view.findViewById(R.id.serialtype);

        if (serialMode == null) {
            serialMode = new SerialMode();
        }

        File file = new File("dev/com3mode");
        if (file.exists()) {
            serialMode.fd = serialMode.open("/dev/com3mode", 0);
            if ( serialMode.fd > 0) {
                BtId = (Integer)SPUtils.getValue("serialmode", Integer.valueOf(0));
                Prompt = (String)SPUtils.getValue("serialstring", new String());

                if(BtId==0){
                    Log.v(TAG, "no serialmode");
                }
                else{
                    Log.v(TAG, "serialmode="+BtId);
                    TV.setText(Prompt);
                }

                ButtonOnClick buttonOnClick = new ButtonOnClick();
                BtRS232.setOnClickListener(buttonOnClick);
                BtRS485.setOnClickListener(buttonOnClick);
                BtRS422.setOnClickListener(buttonOnClick);
                BtRS485Trml.setOnClickListener(buttonOnClick);
                BtRS422Trml.setOnClickListener(buttonOnClick);
                BtLoopBack.setOnClickListener(buttonOnClick);

            } else {
                /**
                 * 可能驱动节点文件不存在，或者权限不够
                 */
                new AlertDialog.Builder(context).setTitle("Error").setMessage("Missing read/write permission, trying to chmod the file.").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finish();
                            }
                        } ).show();

            }
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        if (serialMode == null) {
            serialMode = new SerialMode();
            serialMode.fd = serialMode.open("/dev/com3mode", 0);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (serialMode != null)
            serialMode.close(serialMode.fd);
        serialMode = null;
    }

    class ButtonOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ButtonRS232:
                    BtId = 1;
                    Prompt = "RS232";
                    cmd = TEST_RS232_CMD_APLEX;
                    serialMode.ioctl(serialMode.fd, TEST_RS232_CMD_APLEX, 0);
                    break;
                case R.id.ButtonRS485:
                    BtId = 2;
                    Prompt = "RS485";
                    cmd = TEST_RS485_CMD_APLEX;
                    serialMode.ioctl(serialMode.fd, TEST_RS485_CMD_APLEX , 0);
                    break;
                case R.id.ButtonRS422:
                    BtId = 3;
                    Prompt = "RS422";
                    cmd = TEST_RS485_CMD_APLEX;
                    serialMode.ioctl(serialMode.fd, TEST_RS422_CMD_APLEX , 0);
                    break;
                case R.id.ButtonRS485Trml:
                    BtId = 4;
                    Prompt = "RS485 Terminal";
                    cmd = TEST_RS485_TERM_CMD_APLEX;
                    serialMode.ioctl(serialMode.fd, TEST_RS485_TERM_CMD_APLEX, 0);
                    break;
                case R.id.ButtonRS422Trml:
                    BtId = 5;
                    Prompt = "RS422 Terminal";
                    cmd = TEST_RS422_TERM_CMD_APLEX;
                    serialMode.ioctl(serialMode.fd, TEST_RS422_TERM_CMD_APLEX, 0);
                    break;
                case R.id.ButtonLoopBack:
                    BtId = 6;
                    Prompt = "LoopBack";
                    serialMode.ioctl(serialMode.fd, TEST_LOOPBACK_CMD_APLEX, 0);
                    cmd = TEST_LOOPBACK_CMD_APLEX;
                    break;
                default:BtId = 0;
                    Prompt = null;
                    cmd = 0;
                    break;
            }
            SPUtils.pushInt("serialmode", BtId);
            SPUtils.pushString("serialstring", Prompt);
            TV.setText(Prompt);
            Log.v(TAG, "ioctl("+serialMode.fd+" , "+cmd+")");
            Log.v(TAG, Prompt+"="+BtId);
        }

    }
}
