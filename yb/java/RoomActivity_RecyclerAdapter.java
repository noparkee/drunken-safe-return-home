package com.example.orangeline;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomActivity_RecyclerAdapter extends RecyclerView.Adapter<RoomActivity_RecyclerAdapter.ViewHolder> {

    public String UserId;
    public int roomNumber;
    public LocalDateTime[] appTimes;
    public String[] appPlaces;
    public String[] roomNames;
    public int[] roomIds;

    String[] appTimeString;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void callRoomInfo(){
        /*방 정보 불러오는 함수. 사용자가 들어가 있는 방 개수, 방 이름들, 약속 시간, 약속 장소들
        불러오면 돼!! 아마 이 클래스에선 이 함수만 수정하면 될 듯??
        임시로 대충 선언해놓겠다!!! */

        //임시
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
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
        //임시

    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView RoomName;
        public TextView AppTime;
        public TextView AppPlace;
        public int RoomId;
        public String userId;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            RoomName = (TextView)itemView.findViewById(R.id.RoomName);
            AppTime = (TextView)itemView.findViewById(R.id.AppTime);
            AppPlace = (TextView)itemView.findViewById(R.id.AppPlace);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                  //  userId = UserId;
                    Log.d("Cardview clicked", RoomName.getText().toString());
                    Intent intent = new Intent(itemView.getContext(), RoomActivity2.class);
                    intent.putExtra("userId", userId);
                    intent.putExtra("roomId", RoomId);
                    intent.putExtra("roomName", RoomName.getText().toString());
                    //intent.putExtra("roomId", RoomActivity2.class);
                    itemView.getContext().startActivity(intent);
                }
                });

        }
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.userId = UserId;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.RoomName.setText(roomNames[position]);
        holder.AppPlace.setText(appPlaces[position]);
        holder.AppTime.setText(appTimeString[position]);
        holder.RoomId = roomIds[position];

        /*if(position % 3 == 1){

        }
        /*holder.name.setText(tempNames[position]);
        holder.detail.setText(tempDetails[position]);
        holder.profileImage.setImageResource(images[position]);*/
    }

    @Override
    public int getItemCount() {
        return roomNumber;
    }
}
