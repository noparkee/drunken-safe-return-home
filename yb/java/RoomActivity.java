package com.example.orangeline;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomActivity extends AppCompatActivity {

    public String userId;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    public int roomNumber;
    public LocalDateTime[] appTimes;
    public String[] appPlaces;
    public String[] roomNames;
    public int[] roomIds;

    String[] appTimeString;

    @RequiresApi(api = Build.VERSION_CODES.O)
    void callRoomInfo(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
      //  DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        roomNumber = 3;
        roomNames = new String[roomNumber];
        appTimes = new LocalDateTime[roomNumber];
        appPlaces = new String[roomNumber];
        appTimeString = new String[roomNumber];
        roomIds = new int[roomNumber];

        roomNames[0] = "떡볶이가 좋아";
        roomNames[1] = "술파티파티";
        roomNames[2] = "치맥사랑";

        appTimes[0] = LocalDateTime.parse("2020-09-01T20:00:00");
        appTimes[1] = LocalDateTime.parse("2020-08-31T21:30:00");
        appTimes[2] = LocalDateTime.parse("2020-09-02T18:00:00");

        appPlaces[0] = "청년다방 고대점";
        appPlaces[1] = "박군포차";
        appPlaces[2] = "크림스치킨 고대점";

        roomIds[0] = 1;
        roomIds[1] = 2;
        roomIds[2] = 3;
        for(int i = 0; i < roomNumber; i++){
            appTimeString[i] = appTimes[i].getYear() + "년 " + appTimes[i].getMonthValue() + "월 "+
                    appTimes[i].getDayOfMonth() + "일 " + appTimes[i].getHour() + "시 " + appTimes[i].getMinute() + "분 ";
        }
        //여기까진 RoomActivity_RecyclerAdapter에 있던 callRoomInfo와 동일.

        ((RoomActivity_RecyclerAdapter) adapter).roomNumber = roomNumber;
        ((RoomActivity_RecyclerAdapter) adapter).appTimes = appTimes;
        ((RoomActivity_RecyclerAdapter) adapter).roomNames = roomNames;
        ((RoomActivity_RecyclerAdapter) adapter).appPlaces = appPlaces;
        ((RoomActivity_RecyclerAdapter) adapter).appTimeString = appTimeString;
        ((RoomActivity_RecyclerAdapter) adapter).roomIds = roomIds;



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent it = getIntent();
        userId = it.getStringExtra("userId");
        Log.d("userId", userId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("개집찾");
        
        layoutManager =  new LinearLayoutManager(this);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RoomActivity_RecyclerAdapter();

        callRoomInfo(); //이 함수에서 정보들 불러오기

        ((RoomActivity_RecyclerAdapter) adapter).UserId = userId;
       // ((RoomActivity_RecyclerAdapter) adapter).callRoomInfo();
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_ra1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Setting:
                Toast.makeText(this, "Setting AnManDeum", Toast.LENGTH_SHORT); break;
            case R.id.makeRoom :
                Intent intent = new Intent(this, MakeRoomActivity.class);
                startActivityForResult(intent, 1);

               /* DatePickerDialog di = new DatePickerDialog(this, callbackMethod, 2021, 1, 30);
                callbackMethod = new DatePickerDialog.OnDateSetListener(){
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayofMonth){
                        // textView_Date.setText(year + "년" + monthOfYear + "월" + dayofMonth + "일");
                    }
                };*/
                break;
        }
        return true;
    }
}