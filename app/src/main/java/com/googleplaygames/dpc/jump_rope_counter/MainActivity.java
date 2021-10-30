package com.googleplaygames.dpc.jump_rope_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnExit(View v){
        finish();
    }

    public void OnCounter(View v){
        Intent activity = new Intent(getApplicationContext(), Counter.class);
        startActivity(activity);
    }
}