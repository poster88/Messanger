package com.example.user.simplechat.fragment.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.example.user.simplechat.activity.BaseActivity;
import com.example.user.simplechat.fragment.BaseFragment;
import com.example.user.simplechat.fragment.impl.AsyncTaskCallbacks;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by POSTER on 14.12.2017.
 */

public class RegTaskFragment extends BaseFragment {
    private AsyncTaskCallbacks callbacks;

    public RegTaskFragment() {
    }

    public static RegTaskFragment newInstance(String email, String password, String name) {
        Bundle args = new Bundle();
        args.putString(Const.EMAIL_KEY, email);
        args.putString(Const.PASSWORD_KEY, password);
        args.putString(Const.NAME_KEY, name);

        RegTaskFragment fragment = new RegTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (AsyncTaskCallbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new RegTask(
                getArguments().getString(Const.EMAIL_KEY),
                getArguments().getString(Const.PASSWORD_KEY),
                getArguments().getString(Const.NAME_KEY)
        ).execute();
    }

    private class RegTask extends AsyncTask<Void, Void, Boolean> {
        private Task<AuthResult> regTask;
        private String email;
        private String password;
        private String name;

        public RegTask(String email, String password, String name) {
            this.email = email;
            this.password = password;
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            if (callbacks != null){
                callbacks.onPreExecute();
                regTask = ((BaseActivity) getActivity()).getAuth()
                        .createUserWithEmailAndPassword(email, password);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            while (!regTask.isComplete()){
                publishProgress();
                SystemClock.sleep(100);
            }
            if (regTask.isSuccessful()){
                saveUserDataInDB(new User());
                //TODO : Service - uploadImage; updateUserData;
            }
            return regTask.isSuccessful();
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
                    callbacks.onPostExecute(Const.REG_OK, null);
                }else {
                    try {
                        callbacks.onPostExecute(Const.TASK_FAIL, regTask.getException().getMessage());
                    } catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                RegTaskFragment.super.remove(((BaseActivity) getActivity()).getFm(), RegTaskFragment.this);
            }
        }

        @Override
        protected void onCancelled() {
            if (callbacks != null) callbacks.onCancelled();
        }

        private void saveUserDataInDB(User user){
            user.setUserID(((BaseActivity) getActivity()).getAuth().getUid());
            user.setUserName(name);
            user.setUserEmail(email);
            FirebaseDatabase.getInstance().getReference(Const.USER_INFO).updateChildren(user.toMap());
        }
    }
}

