package com.example.orangeline;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        arrTimes[0] = LocalDateTime.parse("2020-09-01T21:00:00");
        arrTimes[1] = LocalDateTime.parse("2020-09-01T22:00:00");
        arrTimes[2] = LocalDateTime.parse("2020-09-01T22:00:00");
        arrTimes[3] = LocalDateTime.parse("2020-09-01T22:00:00");

        depTimes[0] = LocalDateTime.parse("2020-09-01T22:30:00");
        depTimes[1] = LocalDateTime.parse("2020-09-01T22:30:00");
        depTimes[2] = LocalDateTime.parse("2020-09-01T23:00:00");
        depTimes[3] = LocalDateTime.parse("2020-09-01T23:30:00");

        names[0] = "박노경";
        names[1] = "이유빈";
        names[2] = "임채원";
        names[3] = "소병희";


    }

    public LocalDateTime[] appTimes;
    public class ViewHolder  extends RecyclerView.ViewHolder{
        public ImageView profileImage;
        public TextView name;
        public TextView detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView2);
            name = (TextView)itemView.findViewById(R.id.name);
            detail = (TextView)itemView.findViewById(R.id.detail);
        }
    }
    @NonNull
    @Override
    public RoomActivity2_RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomActivity2_RecyclerAdapter.ViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.detail.setText(depTimes[position].toString() + " >> " + arrTimes[position].toString());
        holder.profileImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return numOfPeople;
    }


}
