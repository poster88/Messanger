package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        //check internetconnection
        //check if destroy app
        Log.d(Const.MY_LOG, "MySystemService : onStartCommand");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.MY_LOG, "MySystemService : onDestroy " + FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}
