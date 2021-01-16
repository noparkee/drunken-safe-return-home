package com.example.orangeline;

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
import android.view.View;

import java.time.LocalDateTime;

public class RoomActivity2 extends AppCompatActivity {

    public int roomId;
    public String userId;
    public String roomName;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        roomId = it.getIntExtra("roomId", 1);
        userId = it.getStringExtra("userId");
        roomName = it.getStringExtra("roomName");
        Log.d("roomId", String.valueOf(roomId));
        Log.d("userId", userId);
        setContentView(R.layout.activity_room2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(roomName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RoomActivity2_RecyclerAdapter();
        ((RoomActivity2_RecyclerAdapter) adapter).UserId = userId;
        ((RoomActivity2_RecyclerAdapter) adapter).activity = this;
        ((RoomActivity2_RecyclerAdapter) adapter).callThisRoomInfo(roomId, userId);
        recyclerView.setAdapter(adapter);
    }
}