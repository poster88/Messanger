package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


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
        Log.d(Const.MY_LOG, "onStartCommand : flags = " + flags + " startId = " + startId);
        String action = intent.getAction();
        if (action.equals(Const.UPDATE_ONLINE_STATUS)){
            Log.d(Const.MY_LOG, "onStartCommand : UPDATE_ONLINE_STATUS");
            ChangeOnlineStatusUser task = new ChangeOnlineStatusUser(startId, intent);
            task.run();
        }
        if (action.equals(Const.UPLOAD_IMAGE_TASK)){
            Log.d(Const.MY_LOG, "onStartCommand : uploadTask");
            UploadImageTask task = new UploadImageTask(startId, intent);
            task.run();
        }
        return START_REDELIVER_INTENT;
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

    private class UploadImageTask implements Runnable{
        private int startId;
        private byte[] userImage;
        private Uri photoUri;
        private Intent intent;

        private UploadImageTask(int startId, Intent intent) {
            this.startId = startId;
            this.intent = intent;
        }

        private OnSuccessListener addPhotoSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String photoUrl = taskSnapshot.getDownloadUrl().toString();
                stop(Const.UPLOAD_STATUS_OK, photoUrl, null);
            }
        };

        private OnFailureListener failureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                stop(Const.UPLOAD_STATUS_FAIL,null, e.getMessage());
            }
        };

        @Override
        public void run() {
            innitDataFromIntent();
            if (photoUri != null){
                FirebaseStorage fs = FirebaseStorage.getInstance();
                StorageReference sr = fs.getReference(Const.USERS_IMAGES).child(photoUri.getLastPathSegment());
                UploadTask uTask = sr.putBytes(userImage);
                uTask.addOnSuccessListener(addPhotoSuccessListener);
                uTask.addOnFailureListener(failureListener);
            }else {
                stop(Const.UPLOAD_STATUS_OK, null, null);
            }
        }

        private void innitDataFromIntent() {
            photoUri = intent.getData();
            userImage = intent.getByteArrayExtra(Const.BYTE_IMAGE_KEY);
        }

        private void stop(String uploadStatus, String imageUrl, String exceptionMessage){
            try {
                Intent intent = new Intent(Const.UPLOAD_IMAGE_ACTION);
                intent.putExtra(Const.UPLOAD_STATUS_KEY, uploadStatus);
                intent.putExtra(Const.UPLOAD_IMAGE_URL, imageUrl);
                intent.putExtra(Const.UPLOAD_MESSAGE_KEY, exceptionMessage);
                sendBroadcast(intent);
            } finally {
                stopSelf(startId);
                Log.d(Const.MY_LOG, "stop run UploadImageTask / uploadStatus = " + uploadStatus +
                        " imageUrl = " + imageUrl + " exceptionMessage = " + exceptionMessage);
            }
        }
    }
}
