package com.example.user.simplechat.fragment;

import android.graphics.Bitmap;

/**
 * Created by POSTER on 17.11.2017.
 */

public interface TaskListener {
    void onTaskStarted();
    void onTaskFinished(Bitmap result);
}
