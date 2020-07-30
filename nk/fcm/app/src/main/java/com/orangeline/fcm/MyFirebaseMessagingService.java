package com.orangeline.fcm;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "MyFirebase 클래스";
    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "생성된 토큰: " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "body: " + remoteMessage.getNotification().getBody());

            Log.d(TAG, "전송된 값: " + remoteMessage.getData().get("여기에 전송한 key를 입력"));
        }

    }

}
