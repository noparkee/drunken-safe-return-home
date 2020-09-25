package com.orangeline.return_confirmation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


///////////////////////////////////////////////////////////////////
//GPS정보(from GPSActivity), 주소정보(from Kakao API)를 가져오고 두 위치 사이의 거리와 사용자 귀가 여부를 계산하는 액티비티
///////////////////////////////////////////////////////////////////
public class MainActivity extends AppCompatActivity {

    private static KakaoRestApiHelper apiHelper = new KakaoRestApiHelper();		//kakao RestAPI 기능 구현
    private static String[] search_result;	//apiHelper가 반환한 서버응답을 저장 ([0]: 도로명주소, [1]: 주소지의 경도, [2]: 주소지의 위도)
    private double distance;	//GPS좌표와 주소지좌표 간의 거리(km)
    private double latitude;	//사용자의 GPS 위도정보
    private double longitude;	//사용자의 GPS 경도정보
    private boolean isHome;	//사용자의 귀가여부
    private String inputHome;	//사용자가 입력한 주소

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();	//GPSActivity로부터 GPS 위도, 경도를 넘겨받음
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

        TextView location = findViewById(R.id.location);
        location.setText("XXX 님의 현재 위치는\n위도: " + latitude + "\n경도: " + longitude + "\n입니다.");

        Button button = (Button) findViewById(R.id.button);	//주소 입력 확인 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputAddress = (EditText) findViewById(R.id.userInputHome);
                inputHome = inputAddress.getText().toString();	//EditText의 입력을 inputHome 변수에 String 타입으로 저장

                try{
                    test();	//line 65

                } catch (IOException e) {
                    e.printStackTrace();
                }

                calcDistance();	//line 89
                checkHomecoming();	//line 102
            }
        });
    }


    public void test() throws IOException {	//Kakao API에 접근하기 위한 acessToken과 로컬RestAPI를 사용하기 위한 adminKey를 저장하고 testLocal() 실행
        apiHelper.setAccessToken("xxxxxxxxxxxxxxxxxxxxxxx");	//KakaoRestApiHelper.java - line 37
        apiHelper.setAdminKey("xxxxxxxxxxxxxxxxxxxxxxx");	////KakaoRestApiHelper.java - line 41
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>test is beginning soon!!!!!!!");
        testLocal();	//line 73
    }


    public void testLocal() throws IOException {	//사용자가 입력한 주소지를 query로 searchAddress() 실행
        Map<String, String> paramMap;	// 로컬 REST API에 전송할 파라미터를 할당
        paramMap = new HashMap<String, String>();

        //주소 검색에 필요한 파라미터들을 paramMap 변수에 할당 (로컬 REST API > 주소 검색 참고)
        paramMap.put("format", "json");
		paramMap.put("query", inputHome);	//사용자의 입력값을 query와 매핑
        //paramMap.put("query", "안암로 120");	테스트용 입력
        //paramMap.put("page", integer);	생략 가능한 파라미터
        //paramMap.put("size", integer);	생략 가능한 파라미터
        search_result = apiHelper.searchAddress(paramMap);	//KakaoRestApiHelper.java - line 49

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>MainActivity가 KakaoRestApiHelper로부터 주소정보를 가져왔습니다!");
    }


    public void calcDistance() {	//사용자와 주소지 간의 거리를 계산
        double user_x = longitude;
        double user_y = latitude;
        double home_x = Double.parseDouble(search_result[1]);
        double home_y = Double.parseDouble(search_result[2]);

        double x_differ = Math.abs(user_x - home_x) * 1.726; //경도차이의 km계산
        double y_differ = Math.abs(user_y - home_y) * 2.100;    //위도차이의 km계산
        distance = Math.sqrt( x_differ * x_differ + y_differ + y_differ );
    }


    @SuppressLint("SetTextI18n")
    public void checkHomecoming() {	//distance 값을 토대로 귀가여부 확인

        TextView homeInfo = findViewById(R.id.homeInfo);
        homeInfo.setText("임채원 님의 귀가지는\n \"  " + search_result[0] +"  \"\n으로 설정되어 있습니다.\n\n" +
                "귀가지의 위치는\n위도 : " + search_result[2] + "\n경도 : " + search_result[1] + "\n입니다.");

        TextView howFar = findViewById(R.id.howFar);
        howFar.setText("임채원 님의 현재위치에서 귀가지까지의 거리는\n " + distance + " km입니다.");

        isHome = distance > 0.3 ? false : true;

        TextView userState = findViewById(R.id.userState);

        if (isHome)
            userState.setText("임채원 님은 현재 귀가 상태입니다.\n안녕히 주무세요!");
        else
            userState.setText("임채원 님은 현재 미귀가 상태입니다.\n얼른 집에 가세요-_-;");
    }
}