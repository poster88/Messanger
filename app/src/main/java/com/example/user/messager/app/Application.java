package com.example.user.messager.app;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by User on 011 11.10.17.
 */

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
