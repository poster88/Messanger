package com.example.user.simplechat.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by User on 013 13.12.17.
 */

public class MyDialogFragment extends BaseFragment {
    private boolean isDialogRunning;
    private DialogCallbacks callbacks;

    public MyDialogFragment() {
    }

    public interface DialogCallbacks {
        void onPreExecute();
        void onProgressUpdate();
        void onCancelled();
        void onPostExecute();
    }

    public static MyDialogFragment newInstance(){
        MyDialogFragment fragment = new MyDialogFragment();
        Bundle b = new Bundle();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (DialogCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        startTask();
    }

    private OnCompleteListener loginTaskListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            Log.d(Const.MY_LOG, "MyDialogFragment : loginTaskListener ");
            isDialogRunning = false;
            if (task.isSuccessful()){
                Log.d(Const.MY_LOG, "MyDialogFragment : task isSuccessful");
                remove(((MainActivity)getActivity()).getFm());
                return;
            }
            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void startTask() {
        isDialogRunning = true;
        LoginTask loginTask = new LoginTask();
        loginTask.execute();
        ((MainActivity)getActivity()).getAuth()
                .signInWithEmailAndPassword("finaluser@gmail.com", "123123")
                .addOnCompleteListener(loginTaskListener);

    }

    public void remove(FragmentManager fm) {
        if(!fm.isDestroyed()){
            fm.beginTransaction()
                    .remove(this)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Const.MY_LOG, "MyDialogFragment: onResume");
    }

    public class LoginTask extends AsyncTask<Void, Boolean, Void> {

        @Override
        protected void onPreExecute() {
            if (callbacks != null){
                callbacks.onPreExecute();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
           while (isDialogRunning){
               SystemClock.sleep(100);
               publishProgress(true);
               Log.d(Const.MY_LOG, "MyDialogFragment : doInBackground flag = " + true);
           }
           return null;
        }

        @Override
        protected void onProgressUpdate(Boolean... values) {
            if (callbacks != null){
                callbacks.onProgressUpdate();
            }
        }

        @Override
        protected void onCancelled() {
            if (callbacks != null){
                callbacks.onCancelled();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (callbacks != null){
                callbacks.onPostExecute();
            }

        }
    }
}
