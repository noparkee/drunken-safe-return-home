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

    public int RoomId;
    public String userId;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = getIntent();
        RoomId = it.getIntExtra("roomId", 1);
        userId = it.getStringExtra("userId");
        Log.d("roomId", String.valueOf(RoomId));
        Log.d("userId", userId);
        setContentView(R.layout.activity_room2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView =(RecyclerView) findViewById(R.id.recycler_view);
        layoutManager =  new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RoomActivity2_RecyclerAdapter();
        ((RoomActivity2_RecyclerAdapter) adapter).UserId = userId;
        ((RoomActivity2_RecyclerAdapter) adapter).callThisRoomInfo(RoomId, userId);
        recyclerView.setAdapter(adapter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}