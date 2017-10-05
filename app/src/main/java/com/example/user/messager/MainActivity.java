package com.example.user.messager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.messager.model.Message;
import com.example.user.messager.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Message message = new Message();
        message.setMessageTime();
        System.out.println(message.getMessageTime());

        User user1 = new User();

    }
}
