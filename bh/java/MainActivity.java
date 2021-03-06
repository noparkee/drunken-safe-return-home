package com.orangeline.return_confirmation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*  should be Moved to StateService
enum userState {
    NOTDEPART,
    DEPART,
    ARRIVE,
    NOTARRIVE
}*/

public class MainActivity extends AppCompatActivity {

    private static KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();
    private static String[] search_result;
    private double distance;
    //private double latitude;
    //private double longitude;
    //private userState state;
    //private int isHome;
    private String inputHome;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intent intent = getIntent();
        //latitude = intent.getDoubleExtra("latitude", 0);
        //longitude = intent.getDoubleExtra("longitude", 0);

        //TextView location = findViewById(R.id.location);
        //location.setText("사용자 님의 현재 위치는\n위도: " + latitude + "\n경도: " + longitude + "\n입니다.");

        Button AddressInputbutton = (Button) findViewById(R.id.button);
        AddressInputbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputAddress = (EditText) findViewById(R.id.userInputHome);
                inputHome = inputAddress.getText().toString();

                try{
                    test();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                //calcDistance();
                Button ReturnConfirmButton = (Button) findViewById(R.id.button2);
                ReturnConfirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startService();
                    }
                });
                checkHomecoming();
            }
        });
    }


    public void test() throws IOException {
        apiHelper.setAccessToken("********************************");
        apiHelper.setAdminKey("********************************");
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>test is beginning soon!!!!!!!");
        testLocal();
    }


    public void testLocal() throws IOException {
        Map<String, String> paramMap;

        //주소 검색
        paramMap = new HashMap<String, String>();
        paramMap.put("format", "json");

        //paramMap.put("query", "성북구 안암로 145");
        paramMap.put("query", inputHome);

        //paramMap.put("page", integer);
        //paramMap.put("size", integer);
        search_result = apiHelper.searchAddress(paramMap);

        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>MainActivity가 KakaoRestApiHelper로부터 주소정보를 가져왔습니다!");
    }

    /* Move to StateService
    public void calcDistance() {
        double user_x = longitude;
        double user_y = latitude;
        double home_x = Double.parseDouble(search_result[1]);
        double home_y = Double.parseDouble(search_result[2]);

        double x_differ = Math.abs(user_x - home_x) * 1.726; //경도차이의 km계산
        double y_differ = Math.abs(user_y - home_y) * 2.100;    //위도차이의 km계산
        distance = Math.sqrt( x_differ * x_differ + y_differ + y_differ );
    }*/


    @SuppressLint("SetTextI18n")
    public void checkHomecoming() {

        TextView homeInfo = findViewById(R.id.homeInfo);
        homeInfo.setText("사용자 님의 귀가지는\n \"  " + search_result[0] +"  \"\n으로 설정되어 있습니다.\n\n" +
                "귀가지의 위치는\n위도 : " + search_result[2] + "\n경도 : " + search_result[1] + "\n입니다.");

        TextView howFar = findViewById(R.id.howFar);
        howFar.setText("사용자 님의 현재위치에서 귀가지까지의 거리는\n " + distance + " km입니다.");   //distance should be taken from StateService
        /*
        state = distance > 0.3 ? userState.DEPART : userState.ARRIVE;

        TextView stateText = findViewById(R.id.stateText);

        if (state == userState.ARRIVE)
            stateText.setText("사용자 님은 현재 귀가 상태입니다.\n안녕히 주무세요!");
        else
            stateText.setText("사용자 님은 현재 미귀가 상태입니다.\n얼른 집에 가세요-_-;");*/
    }

    //StateService 시작. 인텐트로 사용자 귀가지의 위치정보를 전달
    public ComponentName startService() {
        Intent intent = new Intent( this, StateService.class);
        intent.putExtra("home_x", Double.parseDouble(search_result[1]));
        intent.putExtra("home_y", Double.parseDouble(search_result[2]));
        return startService(intent);
    }

    //StateService 종료
    public boolean stopService() {
        return stopService(new Intent( this, StateService.class));
    }
}