package com.googleplaygames.dpc.jump_rope_counter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Counter extends AppCompatActivity {

    private long startTime;

    private boolean isRunning = false;

    private boolean isTimer = false;

    private final MessageHandler handler = new MessageHandler();

    private int leftTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        SetIsRunningEnabled(isRunning);
        SetIsTimerEnabled(isTimer);

        View view = findViewById(R.id.view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    TextView count = findViewById(R.id.count);
                    int countInt = Integer.parseInt(count.getText().toString());
                    count.setText(Integer.toString(++countInt));
                    TextView jps = findViewById(R.id.jps);
                    
                    //소숫점 값 2자리수 까지 표시되게
                    double sec = ((double) System.currentTimeMillis()-startTime)/1000;
                    double jpsLong = countInt/sec;
                    DecimalFormat frmt = new DecimalFormat();
                    frmt.setMaximumFractionDigits(2);
                    jps.setText(frmt.format(jpsLong));
                }
            }
        });

        CheckBox isTimerCheckBox = findViewById(R.id.isTimer);
        isTimerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetIsTimerEnabled(isChecked);
            }
        });
    }

    public void OnExit(View v){
        finish();
    }

    public void OnStart(View v){
        TextView count = findViewById(R.id.count);
        count.setText(Integer.toString(0));
        TextView jps = findViewById(R.id.jps);
        jps.setText(Integer.toString(0));
        startTime = System.currentTimeMillis();
        SetIsRunningEnabled(true);

        if(isTimer){
            EditText time = findViewById(R.id.time);
            leftTime = Integer.parseInt(time.getText().toString());
            (new Thread(new Timer())).start();
        }
    }

    public void OnStop(View v){
        SetIsRunningEnabled(false);
    }

    private void SetIsRunningEnabled(boolean _isEnabled){
        isRunning = _isEnabled;
        Button startButton = findViewById(R.id.start_button);
        startButton.setEnabled(!_isEnabled);
        Button stopButton = findViewById(R.id.stop_button);
        stopButton.setEnabled(_isEnabled);
        Button exitButton = findViewById(R.id.exit_button);
        exitButton.setEnabled(!_isEnabled);
        CheckBox isTimerCheckBox = findViewById(R.id.isTimer);
        isTimerCheckBox.setEnabled(!_isEnabled);
        EditText time = findViewById(R.id.time);
        time.setEnabled(!_isEnabled);
    }

    private void SetIsTimerEnabled(boolean _isEnabled)
    {
        isTimer = _isEnabled;
        CheckBox isTimerCheckBox = findViewById(R.id.isTimer);
        isTimerCheckBox.setChecked(isTimer);
        EditText time = findViewById(R.id.time);
        time.setEnabled(isTimer);
    }

    //수신 쓰레드
    public class Timer implements Runnable
    {
        @Override
        public void run(){
            while(isRunning){
                LeftTimeMessage(Integer.toString(leftTime));
                if(leftTime <= 0){
                    SetIsRunningEnabledMessage(false);
                }
                leftTime--;
                try {
                    Thread.sleep(1000);
                }catch (Exception ignored){

                }
            }
        }

        private void LeftTimeMessage(String _message)
        {
            Message message = handler.obtainMessage();
            Bundle _bundle = new Bundle();
            _bundle.putString("work", "LeftTimeMessage");
            _bundle.putString("value", _message);
            message.setData(_bundle);
            handler.sendMessage(message);
        }

        private void SetIsRunningEnabledMessage(boolean _message)
        {
            Message message = handler.obtainMessage();
            Bundle _bundle = new Bundle();
            _bundle.putString("work", "SetIsRunningEnabledMessage");
            _bundle.putBoolean("value", _message);
            message.setData(_bundle);
            handler.sendMessage(message);
        }

    }

    private class MessageHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            if(bundle.getString("work").equals("LeftTimeMessage")){
                TextView leftTimeTextView = findViewById(R.id.leftTime);
                leftTimeTextView.setText(bundle.getString("value"));
            }
            if(bundle.getString("work").equals("SetIsRunningEnabledMessage")){
                SetIsRunningEnabled(bundle.getBoolean("value"));
            }
        }
    }
}