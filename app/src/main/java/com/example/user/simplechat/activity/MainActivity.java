package com.example.user.simplechat.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.example.user.simplechat.service.MyService;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    private FragmentManager fm;
    private FirebaseAuth auth;
    private String currentID;
    private boolean isOnline;

    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            setOnlineStatus(firebaseAuth.getCurrentUser());
        }
    };

    private void setOnlineStatus(FirebaseUser user) {
        if (user != null){
            currentID = user.getUid();
            isOnline = true;
            startMyService();
            return;
        }
        if (currentID != null){
            isOnline = false;
            startMyService();
            currentID = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        innitVariables();
        if (savedInstanceState == null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }
        Log.d(Const.MY_LOG, "onCreate");
    }

    private void innitVariables() {
        fm = getSupportFragmentManager();
        auth = FirebaseAuth.getInstance();
    }

    private void startMyService(){
        Intent intent = new Intent(MainActivity.this, MyService.class);
        intent.setAction(MyService.UPDATE_ONLINE_STATUS);
        intent.putExtra(MyService.CURRENT_ID_KEY, currentID);
        intent.putExtra(MyService.ONLINE_STATUS_KEY, isOnline);
        startService(intent);
        Log.d(Const.MY_LOG, "startMyService : isOnline = " + isOnline);
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0){
            if ((fm.findFragmentById(R.id.container)) instanceof ChatListFragment){
                auth.signOut();
            }
            fm.popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        Log.d(Const.MY_LOG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Const.MY_LOG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (auth.getCurrentUser() != null){
            isOnline = false;
            currentID = auth.getUid();
            startMyService();
            currentID = null;
        }
        auth.removeAuthStateListener(authStateListener);
        Log.d(Const.MY_LOG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Const.MY_LOG, "onStop");
    }

    public FragmentManager getFm() {
        return fm;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Const.MY_LOG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Const.MY_LOG, "onRestart");
    }
}