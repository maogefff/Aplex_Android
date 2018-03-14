package com.aplex.canopen;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    String[][] device = {
            {"4", "r", "0x1000", "0", "u32"},
            {"4", "r", "0x1001", "0", "u8"},
            {"4", "r", "0x1002", "0", "u32"},

            {"4", "r", "0x1003", "0", "u32"},

            {"4", "r", "0x1005", "0", "u32"},
            {"4", "r", "0x1006", "0", "u32"},
            {"4", "r", "0x1007", "0", "u32"},
            {"4", "r", "0x1008", "0", "vs"},
            {"4", "r", "0x1009", "0", "vs"},
            {"4", "r", "0x100A", "0", "vs"},

    };
    private static final String TAG = "MainActivity";
    Button bt;
    TextView DeviceType;
    TextView ErrorRegister;
    TextView ManufacturerStatusRegister;
    TextView PredefinedrerrorField;
    TextView COB_IDSyncMessage;
    TextView CommunicationCyclePeriod;
    TextView SynchronousWindowsLlength;
    TextView ManufacturerDeviceName;
    TextView ManufacturerHardwareVersion;
    TextView ManufacturerSoftwareVersion;

    // Used to load the 'native-lib' library on application startup.
//    static {
//        System.loadLibrary("Canopencommand");
//    }
    CANopen caNopen = new CANopen();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(caNopen.stringFromJNI());

        bt = (Button) findViewById(R.id.bt);

        DeviceType = (TextView)findViewById(R.id.DeviceType);
        ErrorRegister = (TextView)findViewById(R.id.ErrorRegister);
        ManufacturerStatusRegister = (TextView)findViewById(R.id.ManufacturerStatusRegister);
        PredefinedrerrorField = (TextView)findViewById(R.id.PredefinedrerrorField);
        COB_IDSyncMessage = (TextView)findViewById(R.id.COB_IDSyncMessage);
        CommunicationCyclePeriod = (TextView)findViewById(R.id.CommunicationCyclePeriod);
        SynchronousWindowsLlength = (TextView)findViewById(R.id.SynchronousWindowsLlength);
        ManufacturerDeviceName = (TextView)findViewById(R.id.ManufacturerDeviceName);
        ManufacturerHardwareVersion = (TextView)findViewById(R.id.ManufacturerHardwareVersion);
        ManufacturerSoftwareVersion = (TextView)findViewById(R.id.ManufacturerSoftwareVersion);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String[] deviceType = {"4", "r", "0x1000", "0", "u32"};
                String test = caNopen.canopenUtils(device[0]);
                Log.d(TAG,test.trim());
                DeviceType.setText(test.trim());

                test = caNopen.canopenUtils(device[1]);
                Log.d(TAG,test.trim());
                ErrorRegister.setText(test.trim());

                test = caNopen.canopenUtils(device[2]);
                Log.d(TAG,test.trim());
                ManufacturerStatusRegister.setText(test.trim());

                test = caNopen.canopenUtils(device[3]);
                Log.d(TAG,test.trim());
                PredefinedrerrorField.setText(test.trim());

                test = caNopen.canopenUtils(device[4]);
                Log.d(TAG,test.trim());
                COB_IDSyncMessage.setText(test.trim());

                test = caNopen.canopenUtils(device[5]);
                Log.d(TAG,test.trim());
                CommunicationCyclePeriod.setText(test.trim());

                test = caNopen.canopenUtils(device[6]);
                Log.d(TAG,test.trim());
                SynchronousWindowsLlength.setText(test.trim());

                test = caNopen.canopenUtils(device[7]);
                Log.d(TAG,test.trim());
                ManufacturerDeviceName.setText(test.trim());

                test = caNopen.canopenUtils(device[8]);
                Log.d(TAG,test.trim());
                ManufacturerHardwareVersion.setText(test.trim());

                test = caNopen.canopenUtils(device[9]);
                Log.d(TAG,test.trim());
                ManufacturerSoftwareVersion.setText(test.trim());

            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
//    public native String excuteUtils(int argc, String[] argv);
}
