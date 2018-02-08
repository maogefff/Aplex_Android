package com.aplex.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aplex.JniBase.AplexGPIO;
import com.aplex.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class GPIOFragment extends Fragment {
    private final String TAG = "GPIOFragment";
    Context context;
    /**
     * command type
     */
    final int GPIO_IN0_CMD_APLEX  = 66;
    final int GPIO_IN1_CMD_APLEX  = 67;
    final int GPIO_IN2_CMD_APLEX  = 68;
    final int GPIO_IN3_CMD_APLEX  = 69;

    final int GPIO_OUT0_CMD_APLEX = 70;
    final int GPIO_OUT1_CMD_APLEX = 71;
    final int GPIO_OUT2_CMD_APLEX = 72;
    final int GPIO_OUT3_CMD_APLEX = 73;

    Button in0 = null;
    Button in1 = null;
    Button in2 = null;
    Button in3 = null;

    Button out0 = null;
    Button out1 = null;
    Button out2 = null;
    Button out3 = null;

    Button atuo = null;

    AplexGPIO aplexGPIO = new AplexGPIO();

    long in0Data = 0;
    long in1Data = 0;
    long in2Data = 0;
    long in3Data = 0;

    Timer timer = new Timer();

    Handler handler = null;
    List<Integer> list;

    public GPIOFragment() {
        Log.d(TAG, "GPIOFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.activity_gpio, container, false);
        context = getActivity();
        in0 = (Button) view.findViewById(R.id.in1);
        in1 = (Button) view.findViewById(R.id.in2);
        in2 = (Button) view.findViewById(R.id.in3);
        in3 = (Button) view.findViewById(R.id.in4);

        out0 = (Button) view.findViewById(R.id.out1);
        out1 = (Button) view.findViewById(R.id.out2);
        out2 = (Button) view.findViewById(R.id.out3);
        out3 = (Button) view.findViewById(R.id.out4);

        atuo = (Button)view.findViewById(R.id.autoGpio) ;
        list = new ArrayList<Integer>();

        File file = new File("dev/gpio_mio");
        //
        if(aplexGPIO==null){
            aplexGPIO = new AplexGPIO();
        }

        if (file.exists()) {
            aplexGPIO.fd = aplexGPIO.open("/dev/gpio_mio", 0);

            if ( aplexGPIO.fd > 2) {
                GPIOOnClick gpioOnClick = new GPIOOnClick();
                out0.setOnClickListener(gpioOnClick);
                out1.setOnClickListener(gpioOnClick);
                out2.setOnClickListener(gpioOnClick);
                out3.setOnClickListener(gpioOnClick);
                atuo.setOnClickListener(gpioOnClick);

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                }, 500, 200);

            } else {
                new AlertDialog.Builder(context).setTitle("Error").setMessage("Missing read/write permission, trying to chmod the file.").setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //finish();    //hj  这里。。。
                            }
                        } ).show();
            }
        } else {
            new AlertDialog.Builder(context).setTitle("Error").setMessage("Can't find driver node file.").setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
//                            finish();         //hj  这里。。。
                        }
                    } ).show();
        }

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0 && aplexGPIO != null)
                {
                    in0Data = aplexGPIO.ioctl(aplexGPIO.fd, GPIO_IN0_CMD_APLEX, 0);
                    in1Data = aplexGPIO.ioctl(aplexGPIO.fd, GPIO_IN1_CMD_APLEX, 0);
                    in2Data = aplexGPIO.ioctl(aplexGPIO.fd, GPIO_IN2_CMD_APLEX, 0);
                    in3Data = aplexGPIO.ioctl(aplexGPIO.fd, GPIO_IN3_CMD_APLEX, 0);

                    String text = in0.getText().toString();
                    in0.setText(text.substring(0, text.length()-1) + (in0Data != 0 ? "H" : "L"));

                    text = in1.getText().toString();
                    in1.setText(text.substring(0, text.length()-1) + (in1Data != 0 ? "H" : "L"));

                    text = in2.getText().toString();
                    in2.setText(text.substring(0, text.length()-1) + (in2Data != 0 ? "H" : "L"));

                    text = in3.getText().toString();
                    in3.setText(text.substring(0, text.length()-1) + (in3Data != 0 ? "H" : "L"));
                }else if(msg.what == 1 && aplexGPIO != null){
                    switch (msg.arg1){
                        case 0:
                            setButton(out0);
                            break;
                        case 1:
                            setButton(out1);
                            break;
                        case 2:
                            setButton(out2);
                            break;
                        case 3:
                            setButton(out3);
                            break;
                    }
                }else if((msg.what == 2 || msg.what == 3) && aplexGPIO != null){
                    StringBuilder str = new StringBuilder("异常引脚有：");
                    char i0 = in0.getText().toString().charAt(7);
                    char i1 = in1.getText().toString().charAt(7);
                    char i2 = in2.getText().toString().charAt(7);
                    char i3 = in3.getText().toString().charAt(7);
                    char o0 = out0.getText().toString().charAt(8);
                    char o1 = out1.getText().toString().charAt(8);
                    char o2 = out2.getText().toString().charAt(8);
                    char o3 = out3.getText().toString().charAt(8);
                    if(i0!=o0 || i1!=o1 || i2!=o2 || i3 !=o3){
                        if(i0!=o0){
                            list.add(0);
                        }
                        if(i1!=o1){
                            list.add(1);
                        }
                        if(i2!=o2){
                            list.add(2);
                        }
                        if(i3!=o3){
                            list.add(3);
                        }
                    }

                    if(msg.what==3){
                        Iterator<Integer> it = list.iterator();

                        while (it.hasNext()){
                            str.append(it.next()+",");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("GPIO检测错误");
                        builder.setMessage(str.substring(0,str.length()-1));
                        builder.setPositiveButton("确认键", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                    }
                }
            }

        };

        //MX6Check.checkingNumber(this, 7112);
        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (aplexGPIO == null) {
            aplexGPIO = new AplexGPIO();
            aplexGPIO.fd = aplexGPIO.open("/dev/gpio_mio", 0);
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (aplexGPIO != null)
            aplexGPIO.close(aplexGPIO.fd);
        aplexGPIO = null;
    }

    private void setHigh(View view){
        Button currentButton = (Button)view;
        String text = currentButton.getText().toString();
        int buttonNumber = Integer.valueOf(""+text.charAt(6));
        if(text.endsWith("_L")){
            currentButton.setText(text.replace('L', 'H'));
            setGPIOStatus(buttonNumber, 0);
        }
    }

    private void setGPIOStatus(int buttonNumber, int status) {
        switch (buttonNumber) {
            case 0:
                aplexGPIO.ioctl(aplexGPIO.fd, GPIO_OUT0_CMD_APLEX, status);
                break;
            case 1:
                aplexGPIO.ioctl(aplexGPIO.fd, GPIO_OUT1_CMD_APLEX, status);
                break;
            case 2:
                aplexGPIO.ioctl(aplexGPIO.fd, GPIO_OUT2_CMD_APLEX, status);
                break;
            case 3:
                aplexGPIO.ioctl(aplexGPIO.fd, GPIO_OUT3_CMD_APLEX, status);
                break;
        }
    }

    private void setButton(View view){
        Button currentButton = (Button)view;
        String text = currentButton.getText().toString();
        int buttonNumber = Integer.valueOf(""+text.charAt(6));
        if (text.endsWith("_H")) {
            currentButton.setText(text.replace('H', 'L'));
            setGPIOStatus(buttonNumber, 1);
        } else {
            currentButton.setText(text.replace('L', 'H'));
            setGPIOStatus(buttonNumber, 0);
        }
    }

    class GPIOOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if(view.getId()==R.id.autoGpio){
                autoFunction();
            }else{
                setButton(view);
            }
        }

        private void autoFunction(){
            setHigh(out0);
            setHigh(out1);
            setHigh(out2);
            setHigh(out3);

            new Thread(new Runnable(){
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        list.clear();
                        for(int i=0; i<4; i++){
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.arg1 = i;      //几号gpio
                            handler.sendMessage(msg);
                            Thread.sleep(200);
                        }
                        Thread.sleep(500);
                        handler.sendEmptyMessage(2);   //对比是否正确

                        for(int i=0; i<4; i++){
                            Message msg = Message.obtain();
                            msg.what = 1;
                            msg.arg1 = i;      //几号gpio
                            handler.sendMessage(msg);
                            Thread.sleep(200);
                        }
                        Thread.sleep(500);
                        handler.sendEmptyMessage(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Looper.loop();
                }
            }).start();
        }
    }
}
