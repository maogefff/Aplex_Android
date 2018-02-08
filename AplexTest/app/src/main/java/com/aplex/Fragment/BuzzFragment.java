package com.aplex.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.aplex.JniBase.AplexBuzz;
import com.aplex.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class BuzzFragment extends Fragment implements View.OnClickListener {
    final String TAG = "BuzzFragment";

    Thread thread;
    public BuzzFragment() {
        Log.d(TAG, "BuzzFragment");
    }

    private class AtuoThread extends Thread{
        @Override
        public void run() {
            super.run();
            int count = 0;
            for(int i=0 ;i<4; i++){
                if(!isInterrupted()){
                    count+=500;
                    try {
                        AplexBuzz.setFrequency(count);	// set buzzer frequency
                        AplexBuzz.enable();
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        AplexBuzz.disable();			// disable buzzer
                        e.printStackTrace();
                        break;
                    }
                }
            }
            AplexBuzz.disable();			// disable buzzer
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.activity_buzz,container,false);

        Button btn500 = (Button)view.findViewById(R.id.btn500);
        Button btn1000 = (Button)view.findViewById(R.id.btn1000);
        Button btn1500 = (Button)view.findViewById(R.id.btn1500);
        Button btn2000 = (Button)view.findViewById(R.id.btn2000);
        Button btnStop = (Button)view.findViewById(R.id.btnStop);
        Button btAuto = (Button)view.findViewById(R.id.btautoBuzz);

        btn500.setOnClickListener(this);
        btn1000.setOnClickListener(this);
        btn1500.setOnClickListener(this);
        btn2000.setOnClickListener(this);
        btnStop.setOnClickListener(this);
//        thread = new AtuoThread();


        btAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(thread==null){
                    thread = new AtuoThread();
                }else if(thread.getState()== Thread.State.TERMINATED){
                    thread = new AtuoThread();
                }else {
                    thread.interrupt();
                    AplexBuzz.disable();							// disable buzzer
                    while (thread.getState()!= Thread.State.TERMINATED){
                        Log.d(TAG, "setOnClickListener: interrupt getState="+thread.getState());
                    }
                    thread = new AtuoThread();
                }
                thread.start();
            }
        });
        return view;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if(thread!=null){
            thread.interrupt();
        }
        AplexBuzz.disable();								// disable buzzer

        //finish();
    }

    @Override
    public void onClick(View button) {
        String text = ((Button)button).getText().toString().trim();

        if(thread!=null){
            thread.interrupt();
            AplexBuzz.disable();							// disable buzzer
            while (thread.getState()!= Thread.State.TERMINATED){
                Log.d(TAG, "onClick: interrupt getState="+thread.getState());
            }
        }
        if ("stop".compareTo(text) == 0) {
            AplexBuzz.disable();							// disable buzzer
        } else {
            AplexBuzz.setFrequency(Integer.valueOf(text));	// set buzzer frequency
            AplexBuzz.enable();								// enable setted frequency

        }
    }
}
