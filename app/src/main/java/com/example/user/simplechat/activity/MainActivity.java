package com.example.user.simplechat.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    private FragmentManager fm = getSupportFragmentManager();
    private String currentID;
    private User currentUser;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            if (firebaseAuth.getCurrentUser() != null) {
                setIsOnline();
            } else {
                setIsOffline();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        innitData();
        setIsOnline();
        if (savedInstanceState == null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.commit();
        }
    }

    private void innitData() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentID = auth.getUid();
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

    public void setIsOnline(){
        if (currentID != null){
            ref = database.getReference(Const.CHAT_USER_INFO).child(currentID);
            ref.addListenerForSingleValueEvent(new ValueListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    currentUser.setOnline(true);
                    updateChildren();
                }
            });
        }
    }

    private void updateChildren(){
        database.getReference(Const.CHAT_USER_INFO).updateChildren(currentUser.toMap());
    }

    public void setIsOffline(){
        if (currentID != null && currentUser != null){
            currentUser.setOnline(false);
            updateChildren();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsOffline();
        auth.removeAuthStateListener(authStateListener);
    }

    public FragmentManager getFm() {
        return fm;
    }
}