package com.example.orangeline;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoomActivity2_RecyclerAdapter extends RecyclerView.Adapter<RoomActivity2_RecyclerAdapter.ViewHolder> {

    public String UserId;
    public String Location;
    public String RoomName;
    public int RoomId;

    int numOfPeople;
    public LocalDateTime []arrTimes;
    public LocalDateTime []depTimes;
    public String [] names;
    public int[] states;

    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void callThisRoomInfo(int roomId, String thisUserId){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        //임시로 대충 지정. 원래였으면 해당 roomId로 DB 읽어와서 해당 방 정보 띄움.
        this.UserId = thisUserId;
        this.RoomId = roomId;
        Location = "청년다방 고대점";
        RoomName = "떡볶이가 좋아";
        numOfPeople = 4;

        arrTimes = new LocalDateTime[numOfPeople];
        depTimes = new LocalDateTime[numOfPeople];
        names = new String[numOfPeople];
        states = new int[numOfPeople]; // 귀가 전 = 1, 귀가 중 = 2, 귀가 완료 = 3 세 가지 상태

        depTimes[0] = LocalDateTime.parse("2020-09-01T21:00:00");
        depTimes[1] = LocalDateTime.parse("2020-09-01T22:00:00");
        depTimes[2] = LocalDateTime.parse("2020-09-01T22:00:00");
        depTimes[3] = LocalDateTime.parse("2020-09-01T22:00:00");

        arrTimes[0] = LocalDateTime.parse("2020-09-01T22:30:00");
        arrTimes[1] = LocalDateTime.parse("2020-09-01T22:30:00");
        arrTimes[2] = LocalDateTime.parse("2020-09-01T23:00:00");
        arrTimes[3] = LocalDateTime.parse("2020-09-01T23:30:00");

        names[0] = "박노경";
        names[1] = "이유빈";
        names[2] = "임채원";
        names[3] = "소병희";

        states[0] = 1;
        states[1] = 1;
        states[2] = 2;
        states[3] = 3;

    }

    public LocalDateTime[] appTimes;
    public class ViewHolder  extends RecyclerView.ViewHolder{
        public ImageView profileImage;
        public TextView name;
        public TextView detail;
        public Button state;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView2);
            name = (TextView)itemView.findViewById(R.id.name);
            detail = (TextView)itemView.findViewById(R.id.detail);
            state = (Button)itemView.findViewById(R.id.State);
        }
    }
    @NonNull
    @Override
    public RoomActivity2_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RoomActivity2_RecyclerAdapter.ViewHolder holder, int position) {
        holder.name.setText(names[position]);
       /* appTimes[i].getMonthValue() + "월 "+
                appTimes[i].getDayOfMonth() + "일 " + appTimes[i].getHour() + "시 " + appTimes[i].getMinute() + "분 ";*/
       holder.detail.setText(depTimes[position].getHour() + "시 " +
               depTimes[position].getMinute() +"분 >> " + arrTimes[position].getHour()
        + "시 " + arrTimes[position].getMinute() + "분");
        //holder.detail.setText(depTimes[position].toString() + " >> " + arrTimes[position].toString());
        holder.profileImage.setImageResource(images[position]);
        switch(states[position]){
            case 1: holder.state.setText("귀가 전"); break;
            case 2: holder.state.setText("귀가 중"); break;
            case 3: holder.state.setText("귀가 완료"); break;
        }
        //holder.state.setText("귀가 중");
    }

    @Override
    public int getItemCount() {
        return numOfPeople;
    }


}
