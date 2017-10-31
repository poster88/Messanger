package com.example.user.simplechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Const.TAG, "MainActivity : onCreate ");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            Log.d(Const.TAG, "*** MainActivity : savedInstanceState is null***");
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }else {
            Log.d(Const.TAG, "*** MainActivity : savedInstanceState is't null ***");
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(Const.TAG, "MainActivity : onBackPressed ");
        if (fm.getBackStackEntryCount() > 0){
            if ((fm.findFragmentById(R.id.container)) instanceof ChatListFragment){
                FirebaseAuth.getInstance().signOut();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return;
            }
            fm.popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Const.TAG, "MainActivity : onActivityResult ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(Const.TAG, "MainActivity : onRestart ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Const.TAG, "MainActivity : onSaveInstanceState ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(Const.TAG, "MainActivity : onRestoreInstanceState ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Const.TAG, "MainActivity : onCreate ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(Const.TAG, "MainActivity : onStart ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(Const.TAG, "MainActivity : onPause ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Const.TAG, "MainActivity : onStop ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(Const.TAG, "MainActivity : onDestroy ");
    }
}