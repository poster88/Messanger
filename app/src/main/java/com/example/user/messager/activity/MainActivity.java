package com.example.user.messager.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.messager.fragment.ChatListFragment;
import com.example.user.messager.fragment.LoginFragment;
import com.example.user.messager.R;
import com.example.user.messager.utils.Utils;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = fm.findFragmentById(R.id.container);
        if (fragment instanceof ChatListFragment){
            Log.d(Utils.TAG, "fragment instanceof ChatListFragment");
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, LoginFragment.newInstance());
            ft.commit();
            return;
        }
        // TODO: 20.10.2017 work a correct onBackPress / add
        super.onBackPressed();
    }
}