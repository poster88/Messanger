package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

/**
 * Created by User on 004 04.12.17.
 */

public class MySystemService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Const.MY_LOG, "MySystemService : onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        //check internetconnection
        //check if destroy app
        Log.d(Const.MY_LOG, "MySystemService : onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.MY_LOG, "MySystemService : onDestroy " + "FirebaseAuth.getInstance().getCurrentUser().getUid()");
        startMyService(false, FirebaseAuth.getInstance().getUid());
    }

    private void startMyService(boolean isOnline, String currentID){
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        intent.setAction(Const.UPDATE_ONLINE_STATUS);
        intent.putExtra(Const.CURRENT_ID_KEY, currentID);
        intent.putExtra(Const.ONLINE_STATUS_KEY, isOnline);
        startService(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(Const.MY_LOG, "MySystemService : onTaskRemoved " + "FirebaseAuth.getInstance().getCurrentUser().getUid()");
    }

    private class Test implements Runnable{
        private FirebaseDatabase database;
        private DatabaseReference ref;
        private User currentUser;
        private String currentID;
        private boolean isOnline;

        private ValueListener updateChildrenListener = new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                currentUser.setOnline(isOnline);
                database.getReference("UserInfo").updateChildren(currentUser.toMap());
                stop();
            }
        };

        @Override
        public void run() {
            innitData();
            ref.addListenerForSingleValueEvent(updateChildrenListener);
        }

        private void innitData(){
            currentID = FirebaseAuth.getInstance().getUid();
            isOnline = false;
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("UserInfo").child(currentID);
        }

        private void stop(){
            stopSelf();
            Log.d(Const.MY_LOG, "stop run with task = " + " isOnline = " + isOnline);
        }
    }
}
