package com.example.user.simplechat.fragment.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.fragment.BaseFragment;
import com.example.user.simplechat.fragment.impl.AsyncTaskCallbacks;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by User on 013 13.12.17.
 */

public class SingInTaskFragment extends BaseFragment {
    private AsyncTaskCallbacks callbacks;

    public SingInTaskFragment() {
    }

    public static SingInTaskFragment newInstance(String email, String password){
        SingInTaskFragment fragment = new SingInTaskFragment();
        Bundle args = new Bundle();
        args.putString(Const.EMAIL_KEY, email);
        args.putString(Const.PASSWORD_KEY, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (AsyncTaskCallbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new LoginTask(getArguments().getString(Const.EMAIL_KEY), getArguments().getString(Const.PASSWORD_KEY))
                .execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Const.MY_LOG, "SingInTaskFragment: onResume - in background");
    }

    private class LoginTask extends AsyncTask<Void, Void, Boolean> {
        private Task<AuthResult> signInTask;
        private String email;
        private String password;

        public LoginTask(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            if (callbacks != null) callbacks.onPreExecute();
            signInTask = ((MainActivity)getActivity()).getAuth()
                    .signInWithEmailAndPassword(email, password);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            while (!signInTask.isComplete()){
                publishProgress();
                SystemClock.sleep(100);
            }
            return signInTask.isSuccessful();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if (callbacks != null){
                callbacks.onProgressUpdate();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (callbacks != null){
                if (signInTask.isSuccessful()){
                    callbacks.onPostExecute(Const.SIGN_IN_OK, null);
                }else {
                    try {
                        callbacks.onPostExecute(Const.SIGN_IN_FAIL, signInTask.getException().getMessage());
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                SingInTaskFragment.super.remove(((MainActivity)getActivity()).getFm(), SingInTaskFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            if (callbacks != null) callbacks.onCancelled();
        }
    }
}
