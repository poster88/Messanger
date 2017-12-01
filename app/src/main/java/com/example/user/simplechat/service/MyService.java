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
    public static String UPDATE_ONLINE_STATUS = "UPDATE_ONLINE_STATUS";
    public static String CURRENT_ID_KEY = "CURRENT_ID_KEY";
    public static String ONLINE_STATUS_KEY = "ONLINE_STATUS_KEY";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MY_TAG", "onStartCommand");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MY_TAG", "onStartCommand");

        if (intent.getAction().equals(UPDATE_ONLINE_STATUS)){
            String currentID = intent.getStringExtra(CURRENT_ID_KEY);
            boolean isOnline = intent.getBooleanExtra(ONLINE_STATUS_KEY, false);
            ChangeOnlineStatusUser task = new ChangeOnlineStatusUser(startId, currentID, isOnline);
            task.run();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("MY_TAG", "onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MY_TAG", "onDestroy service");
    }

    private class ChangeOnlineStatusUser implements Runnable{
        private FirebaseDatabase database;
        private DatabaseReference ref;
        private User currentUser;
        private String currentID;
        private int startId;
        private boolean isOnline;

        private ChangeOnlineStatusUser(int startId, String currentID, boolean isOnline){
            this.startId = startId;
            this.currentID = currentID;
            this.isOnline = isOnline;
        }

        private void innitDB(){
            database = FirebaseDatabase.getInstance();
            ref = database.getReference(Const.CHAT_USER_INFO).child(currentID);
        }

        @Override
        public void run() {
            innitDB();
            ref.addListenerForSingleValueEvent(new ValueListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    currentUser = dataSnapshot.getValue(User.class);
                    currentUser.setOnline(isOnline);
                    database.getReference(Const.CHAT_USER_INFO).updateChildren(currentUser.toMap());
                    stop();
                }
            });
        }

        private void stop(){
            stopSelf(startId);
        }
    }
}
