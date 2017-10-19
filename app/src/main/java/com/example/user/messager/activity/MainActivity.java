package com.example.user.messager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.user.messager.fragment.ChatListFragment;
import com.example.user.messager.fragment.LoginFragment;
import com.example.user.messager.R;
import com.example.user.messager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (auth.getCurrentUser() == null){
                ft.add(R.id.container, LoginFragment.newInstance());
            }else {
                loadUserData();
                ft.add(R.id.container, ChatListFragment.newInstance());
            }
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    private void loadUserData() {
        //database.getReference(Utils.USER_INFO).child()
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
