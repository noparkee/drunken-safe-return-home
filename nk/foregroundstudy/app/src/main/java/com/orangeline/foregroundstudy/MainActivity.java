package com.orangeline.foregroundstudy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("test","onCreate 실행");

        start = (Button)findViewById(R.id.start);

       /*start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startService();
            }
        });*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService();
    }

    public void startService(){
        Log.d("test", "startService 실행");

        Intent serviceIntent = new Intent(this, MyService.class);
        //serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");      // Myservice에서 name이 inputExtra인 intent를 가져오는 듯.

        ContextCompat.startForegroundService(this, serviceIntent);
    }

}