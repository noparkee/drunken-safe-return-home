package com.example.orangeline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.security.AccessController.getContext;

public class RoomActivity2_RecyclerAdapter extends RecyclerView.Adapter<RoomActivity2_RecyclerAdapter.ViewHolder> {

    public String UserId;
    public String Location;
    public String RoomName;
    public LocalDateTime AppTime;
    public int RoomId;

    public Context context;
    public Activity activity;
    int numOfPeople;
    public LocalDateTime []arrTimes;
    public LocalDateTime []depTimes;
    public String [] names;
    public int[] states;

    private int[] images = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4};

    public boolean isFirstAccess(){
        //방에 처음 접속하는지를 판단하는 함수. DB 불러와서 거기 정보에 시작/도착시간이 비어 있으면 true 반환, 이미 차 있으면 false 반환

        return true;
    }

    public void checkFirst(){
        if(isFirstAccess() == true){
            //Dialog를 띄우장. 출발 시간/도착 시간 입력하는 다이얼로그! 출발 시간은 약속 시간 이후로 제한. 도착시간 제한은 없음
        }
    }

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
        states = new int[numOfPeople]; // 귀가 전 = 1, 귀가 중 = 2, 귀가 완료 = 3, 미귀가(도착 시간 초과했는데도 귀가 아닐 때) = 4 이렇게 네 가지 상태

        AppTime = LocalDateTime.parse("2020-09-01T20:00:00");

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

        TextView roomNameTV = activity.findViewById(R.id.roomName);
       TextView appTimeTV = activity.findViewById(R.id.appTime);

       roomNameTV.setText(RoomName);
       appTimeTV.setText(AppTime.getYear() + "-" + AppTime.getMonthValue() + "-" + AppTime.getDayOfMonth() + "  " +
               AppTime.getHour()+":" + AppTime.getMinute());
    }

    public LocalDateTime[] appTimes;
    public class ViewHolder  extends RecyclerView.ViewHolder{
        public ImageView profileImage;
        public TextView name;
        public TextView detail;
        public Button state;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            profileImage = (ImageView)itemView.findViewById(R.id.imageView2);
            name = (TextView)itemView.findViewById(R.id.name);
            detail = (TextView)itemView.findViewById(R.id.detail);
            state = (Button)itemView.findViewById(R.id.State);

            state.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){ //귀가 버튼 클릭 시 나오는 귀가 확인 팝업창
                    //Log.d("hoho", "hhaa");
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("귀가 확인");
                    builder.setMessage(name.getText().toString() + "님의 귀가를 확인하셨습니까?");
                    builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(activity, "웨용?" ,Toast.LENGTH_SHORT);
                            //아무 일도 없었다...!
                        }
                    });
                    builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(activity, "귀가 처리 완료" ,Toast.LENGTH_SHORT);
                            // DB에서 귀가 완료라고 수정해줘야 되나? 그래야 다른 사람들 화면에 공유되나?
                            state.setText("귀가 완료");
                            state.setBackgroundColor(Color.rgb(100, 255, 100));
                        }
                    });
                    builder.show();
                }
            });
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
            case 1: holder.state.setText("귀가 전"); holder.state.setBackgroundColor(Color.rgb(100, 100, 255)); break;
            case 2: holder.state.setText("귀가 중"); holder.state.setBackgroundColor(Color.rgb(255, 255, 0)); break;
            case 3: holder.state.setText("귀가 완료"); holder.state.setBackgroundColor(Color.rgb(100, 255, 100)); break;
            case 4: holder.state.setText("미귀가"); holder.state.setBackgroundColor(Color.rgb(255, 0, 0)); break;
        }
    }

    @Override
    public int getItemCount() {
        return numOfPeople;
    }


}
