package com.example.orange_line;
/*
    write user information into Firebase Database
 */
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Database{

    public String id;
    public String nickname;
    public String address;
    public String phone;
    public HashMap<String, Object> info = new HashMap<>();

    public Database(Long id, String nickname) {
        this.id = String.valueOf(id);
        this.nickname = nickname;
    }

    public Database(String address, String phone){
        this.address = address;
        this.phone = phone;
    }


    // store user information in HashMap

    @Exclude
    public Map<String, Object> toMap(boolean flag) {
        if (flag == true) {
            Log.i(id, nickname);
            info.put("id", id); // key: id, value: id
            info.put("nickname", nickname);
        }
        else {
            Log.i(address, phone);
            info.put("address", address);
            info.put("phone", phone);
        }
        return info;
    }

}
