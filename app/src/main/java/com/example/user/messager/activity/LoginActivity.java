package com.example.user.messager.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.messager.fragment.LoginFragment;
import com.example.user.messager.R;
import com.example.user.messager.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseAuth.AuthStateListener stateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            Log.d(Utils.TAG, "onAuthStateChanged : user = " + user);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null){
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(stateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(stateListener);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
    }
}
