package com.example.orangeline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

public class MakeRoomActivity extends AppCompatActivity {
    Button dateButton;
    Button timeButton;
    Context context = this;

    private DatePickerDialog.OnDateSetListener callbackMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.5f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_make_room);
        Button selectDate = findViewById(R.id.dateButton);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hoho, ", "haha");
                InitializeListener();
            }
        });
    }
    public void InitializeListener(){
        DatePickerDialog di = new DatePickerDialog(this, callbackMethod, 2021, 1, 30);
        callbackMethod = new DatePickerDialog.OnDateSetListener(){
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth){
               // textView_Date.setText(year + "년" + monthOfYear + "월" + dayofMonth + "일");
            }
        };
    }

}