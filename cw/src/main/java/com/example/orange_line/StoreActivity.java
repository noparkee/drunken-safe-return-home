package com.example.orange_line;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StoreActivity extends AppCompatActivity {
    public FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = firebaseDatabase.getReference();
    public FirebaseDatabase mDatabase;
    public DatabaseReference mReference;
    public ChildEventListener mChild;

    Map<String, Object> info = new HashMap<>();
    public static String id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatabase();
    }

    private void initDatabase() {

        mDatabase = FirebaseDatabase.getInstance();

        mReference = mDatabase.getReference("log");
        mReference.child("log").setValue("check");

        mChild = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addChildEventListener(mChild);
    }

    // store id and nickname
    public void writeDatabase(Long id, String nickname, String profileImage){
        this.id = id.toString();
        info.put("nickname", nickname);
        info.put("profileImage", profileImage);
        databaseReference.child("user").child(this.id).updateChildren(info);
    }

    // store address and contact
    public void writeDatabase(String address, String contact){
        info.put("address", address);
        info.put( "contact", contact);
        databaseReference.child("user").child(id).updateChildren(info);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReference.removeEventListener(mChild);
    }

}
