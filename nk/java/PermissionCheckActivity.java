package com.orangeline.foregroundstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import java.security.Permission;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class PermissionCheckActivity extends AppCompatActivity {
    //private final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1001;
    private final int MY_PERMISSIONS_REQUEST = 1001;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_check);
        checkPermission();        // 퍼미션 체크 -> 없으면 허용 하도록 앱 실행할 때! 퍼미션 체크
    }

    private void checkPermission() {
        int smspermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int gpspermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (smspermissionCheck != PackageManager.PERMISSION_GRANTED || gpspermissionCheck != PackageManager.PERMISSION_GRANTED) {      // 퍼미션 둘 중 하나라도 허용 되어있지 않으면,

            /*if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {       // 퍼미션을 하나라도 거부한 적이 있다면
                Toast.makeText(this, "이 앱을 실행하려면 SMS 권한과 위치 권한을 허용해야합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);

            } else {        // 퍼미션 거부한 적이 없다면 퍼미션 요청
                Toast.makeText(this, "문자 서비스를 이용하시려면 sms 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            }*/
            //Toast.makeText(this, "이 앱을 실행하려면 SMS 권한과 위치 권한이 필요합니다.", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST);

        }
    }

    @Override // 퍼미션 허용 여부값을 return 받는 듯
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   // checkPermission 함수에서 퍼미션 요청
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("onRequestPermissionResult", "!!!!!!!!!!!!!!!!!!!");
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){

                }
            }
        }

        if ( requestCode == MY_PERMISSIONS_REQUEST && grantResults.length == REQUIRED_PERMISSIONS.length) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result == false) {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
                builder.setTitle("권한이 꺼져있습니다.");
                builder.setMessage("[권한] 설정에서 SMS 권환과 위치 권한을 허용해야 합니다.");
                builder.setPositiveButton("설정으로 가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).setNegativeButton("앱 종료", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();
            }
        }

        /*if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {        // 퍼미션 ok
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("sms", "onRequestPermissionsResult() 함수 내부");
                Toast.makeText(this, "sms 권한을 허용하셨습니다.", Toast.LENGTH_LONG).show();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {       // 퍼미션을 거부한 적이 있다면
                    Toast.makeText(this, "sms 권한을 거부하셨습니다.", Toast.LENGTH_LONG).show();
                } else {        // 다시 보지 않기로 거부했다면
                    Toast.makeText(this, "sms 권한을 거부하셨습니다. 문자 서비스를 이용하시려면 설정에서 sms 권한을 허용해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        }*/
    }
}