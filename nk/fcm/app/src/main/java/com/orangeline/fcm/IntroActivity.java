package com.orangeline.fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);

                if (getIntent().getExtras() != null)
                    intent.putExtra("url", getIntent().getExtras().getString("url"));

                startActivity(intent);

                finish();
            }
        };

        Timer timer = new Timer();

        timer.schedule(timerTask, 3000);

        if (getIntent().getExtras() != null){
            Log.d("IntroActivity", getIntent().getExtras().getString("전송된 키 값 입력"));
        }
    }


}