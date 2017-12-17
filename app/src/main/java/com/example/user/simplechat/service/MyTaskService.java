package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


public class MyTaskService extends Service {

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
        Log.d(Const.MY_LOG, "onStartCommand : flags = " + flags + " startId = " + startId);
        String action = intent.getAction();
        if (action.equals(Const.UPDATE_ONLINE_STATUS)){
            Log.d(Const.MY_LOG, "onStartCommand : UPDATE_ONLINE_STATUS");
            ChangeOnlineStatusUserTask task = new ChangeOnlineStatusUserTask(startId, intent);
            task.run();
        }
        if (action.equals(Const.UPDATE_PROFILE)){
            Log.d(Const.MY_LOG, "onStartCommand : UPDATE_PROFILE");
            updateUserProfileTask task = new updateUserProfileTask(startId, intent);
            task.run();
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

    private class ChangeOnlineStatusUserTask implements Runnable{
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

        private ChangeOnlineStatusUserTask(int startId, Intent intent){
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

    private class updateUserProfileTask implements Runnable {
        private FirebaseDatabase database;
        private DatabaseReference ref;
        private Intent intent;
        private int startId;
        private String currentID;

        public updateUserProfileTask(int startId, Intent intent) {
            this.startId = startId;
            this.intent = intent;
        }

        @Override
        public void run() {
            currentID = intent.getStringExtra(Const.CURRENT_ID_KEY);
            database = FirebaseDatabase.getInstance();
            ref = database.getReference(Const.CHAT_USER_INFO).child(currentID);
            ref.addListenerForSingleValueEvent(updateChildrenListener);
        }

        private ValueListener updateChildrenListener = new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    try {
                        String link = dataSnapshot.getValue(User.class).getImageUrl();
                        new DecodeStream(new URL(link)).execute();
                    } catch (MalformedURLException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        private String bitMapToString(Bitmap bitmap){
            if (bitmap == null){
                return null;
            }
            ByteArrayOutputStream baos = new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }

        private void saveImageToSharedPref(String image) {
            SharedPreferences sPref = getSharedPreferences(Const.USER_DATA, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(Const.USER_IMAGE_KEY, image);
            editor.apply();
        }

        private void stop(){
            stopSelf(startId);
            Log.d(Const.MY_LOG, "stop run with task = " + startId);
        }

        private class DecodeStream extends AsyncTask<Void, Void, Bitmap> {
            URL link = null;
            Bitmap myPhoto = null;

            DecodeStream(URL link) {
                this.link = link;
            }

            @Override
            protected Bitmap doInBackground(Void... voids){
                if (link != null){
                    try {
                        myPhoto = BitmapFactory.decodeStream((InputStream) link.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return myPhoto;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                saveImageToSharedPref(bitMapToString(bitmap));
                stop();
            }
        }
    }
}
