package com.example.user.simplechat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.net.MalformedURLException;
import java.net.URL;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String link = intent.getStringExtra("link");
        URL imageUrl;
        try {
            imageUrl = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class DownloadImageTask implements Runnable{
        private URL imageUrl;

        public DownloadImageTask(URL imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {

        }
    }
}
