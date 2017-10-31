package com.example.user.simplechat.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.google.firebase.auth.FirebaseAuth;


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


}