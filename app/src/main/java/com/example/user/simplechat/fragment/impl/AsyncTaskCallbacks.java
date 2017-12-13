package com.example.user.simplechat.fragment.impl;

/**
 * Created by POSTER on 13.12.2017.
 */

public interface AsyncTaskCallbacks {
    void onPreExecute();
    void onProgressUpdate();
    void onCancelled();
    void onPostExecute(int result, String message);
}
