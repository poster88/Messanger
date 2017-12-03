package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Const.MY_LOG, "onCreate Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null){
            if (action.equals(Const.UPDATE_ONLINE_STATUS)){
                Log.d(Const.MY_LOG, "onStartCommand : UPDATE_ONLINE_STATUS");
                ChangeOnlineStatusUser task = new ChangeOnlineStatusUser(startId, intent);
                task.run();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(Const.MY_LOG, "onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.MY_LOG, "onDestroy service");
    }

    private class ChangeOnlineStatusUser implements Runnable{
        private FirebaseDatabase database;
        private DatabaseReference ref;
        private User currentUser;
        private Intent intent;
        private int startId;
        private String currentID;
        private boolean isOnline;

        private ValueListener updateChildrenListener = new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                currentUser.setOnline(isOnline);
                database.getReference(Const.CHAT_USER_INFO).updateChildren(currentUser.toMap());
                stop();
            }
        };

        private ChangeOnlineStatusUser(int startId, Intent intent){
            this.startId = startId;
            this.intent = intent;
        }

        @Override
        public void run() {
            innitData();
            ref.addListenerForSingleValueEvent(updateChildrenListener);
        }

        private void innitData(){
            currentID = intent.getStringExtra(Const.CURRENT_ID_KEY);
            isOnline = intent.getBooleanExtra(Const.ONLINE_STATUS_KEY, false);
            database = FirebaseDatabase.getInstance();
            ref = database.getReference(Const.CHAT_USER_INFO).child(currentID);
        }

        private void stop(){
            stopSelf(startId);
            Log.d(Const.MY_LOG, "stop run with task = " + startId + " isOnline = " + isOnline);
        }
    }
}
