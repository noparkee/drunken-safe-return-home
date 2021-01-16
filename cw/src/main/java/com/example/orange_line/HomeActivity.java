package com.example.orange_line;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import static com.kakao.kakaotalk.StringSet.id;

public class HomeActivity extends StoreActivity {
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private TextView mTextView; // nickname 표시
    ImageView jpgProfile; // profile image 표시
    String strNickname, strProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        jpgProfile = findViewById(R.id.profileImage);
        mTextView = findViewById(R.id.text_nickname);

        DatabaseReference nicknameref = mDatabase.getReference("user").child(id).child("nickname");
        DatabaseReference profileimageref = mDatabase.getReference("user").child(id).child("profileImage");

        // db에서 nickname 읽어오기
        nicknameref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strNickname = snapshot.getValue().toString();
                Log.e("nickname: ","nickname"+strNickname);
                mTextView.setText(strNickname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // db에서 프로필 이미지 읽어오기
       profileimageref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                strProfile = snapshot.getValue().toString();
                Log.e("profile:", "profile"+strProfile);
                Glide.with(HomeActivity.this).load(strProfile).into(jpgProfile); // url 에서 image 가져옴
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /*RequestOptions requestOptions = new RequestOptions();
       requestOptions.placeholder(R.drawable.kakao_default_profile_image);
       requestOptions.error(R.drawable.kakao_default_profile_image);
        Glide.with(HomeActivity.this).load(strProfile).apply(requestOptions).into(jpgProfile);*/
       /*Picasso.with(HomeActivity.this)
               .load((String)strProfile)
               .placeholder(R.drawable.kakao_default_profile_image)
               .into(jpgProfile);
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
