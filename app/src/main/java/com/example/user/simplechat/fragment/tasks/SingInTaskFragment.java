package com.example.user.simplechat.fragment.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.user.simplechat.activity.BaseActivity;
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
        Bundle args = new Bundle();
        args.putString(Const.EMAIL_KEY, email);
        args.putString(Const.PASSWORD_KEY, password);
        SingInTaskFragment fragment = new SingInTaskFragment();
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
            signInTask = ((BaseActivity)getActivity()).getAuth()
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
        protected void onPostExecute(Boolean isSuccessful) {
            if (callbacks != null){
                if (isSuccessful){
                    callbacks.onPostExecute(Const.SIGN_IN_OK, null);
                }else {
                    try {
                        callbacks.onPostExecute(Const.TASK_FAIL, signInTask.getException().getMessage());
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                SingInTaskFragment.super.remove(((BaseActivity)getActivity()).getFm(), SingInTaskFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            if (callbacks != null) callbacks.onCancelled();
        }
    }
}
