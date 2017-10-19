package com.example.user.messager.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.user.messager.fragment.LoginFragment;
import com.example.user.messager.R;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        //// TODO: 20.10.2017 work a correct onBackPress
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0){
            onBackPressed();
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            FirebaseAuth.getInstance().signOut();
        }
    }
}
