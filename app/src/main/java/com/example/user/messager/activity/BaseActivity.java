package com.example.user.messager.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.user.messager.R;

/**
 * Created by User on 011 11.10.17.
 */

public class BaseActivity extends AppCompatActivity{

    protected void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
