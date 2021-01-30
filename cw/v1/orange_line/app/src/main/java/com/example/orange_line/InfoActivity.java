package com.example.orange_line;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InfoActivity extends StoreActivity{

    private Button nextbt;
    private EditText address;
    private EditText contact;
    public String msg_addr;
    public String msg_contact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        nextbt = findViewById(R.id.nextbt);
        address = findViewById(R.id.address);
        contact = findViewById(R.id.contact);

        // when the button "nextbt" is clicked store address and phone number
        nextbt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                msg_addr = address.getText().toString();
                msg_contact = contact.getText().toString();
                writeDatabase(msg_addr, msg_contact);
                // HomeActivity로 이동
                Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

