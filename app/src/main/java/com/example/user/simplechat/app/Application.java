package com.example.user.simplechat.app;

import android.content.Intent;

import com.example.user.simplechat.service.MySystemService;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by User on 011 11.10.17.
 */

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        startService(new Intent(getBaseContext(), MySystemService.class));
    }
}
