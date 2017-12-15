package com.example.user.simplechat.fragment.tasks;

import android.content.Context;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/**
 * Created by POSTER on 14.12.2017.
 */

public class RegTaskFragment extends BaseFragment {
    private AsyncTaskCallbacks callbacks;

    public RegTaskFragment() {
    }

    public static RegTaskFragment newInstance(String email, String password, String name, byte[] image, Uri imageURI) {
        Bundle args = new Bundle();
        args.putString(Const.EMAIL_KEY, email);
        args.putString(Const.PASSWORD_KEY, password);
        args.putString(Const.NAME_KEY, name);
        args.putString(Const.URI_PHOTO_KEY, imageURI.toString());
        args.putByteArray(Const.IMAGE_ARRAY_KEY, image);

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
                getArguments().getString(Const.NAME_KEY),
                getArguments().getByteArray(Const.IMAGE_ARRAY_KEY),
                Uri.parse(getArguments().getString(Const.URI_PHOTO_KEY))
        ).execute();
    }

    private class RegTask extends AsyncTask<Void, Void, Boolean> {
        private Task<AuthResult> regTask;
        private UploadTask uploadTask;
        private byte[] userImage;
        private Uri photoUri;
        private String email;
        private String password;
        private String name;

        public RegTask(String email, String password, String name, byte[] userImage, Uri photoUri) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.userImage = userImage;
            this.photoUri = photoUri;
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
                setProgress();
            }
            if (regTask.isSuccessful()){
                saveUserDataInDB(new User(), getImageURL());
            }
            return regTask.isSuccessful();
        }

        private void setProgress() {
            publishProgress();
            SystemClock.sleep(100);
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
                        sendFailMsg(regTask.getException().getMessage());
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                if (uploadTask != null && !uploadTask.isSuccessful()){
                    try {
                        sendFailMsg(uploadTask.getException().getMessage());
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                RegTaskFragment.super.remove(((BaseActivity) getActivity()).getFm(), RegTaskFragment.this);
            }
        }

        private void sendFailMsg(String msg){
            callbacks.onPostExecute(Const.TASK_FAIL, msg);
        }

        @Override
        protected void onCancelled() {
            if (callbacks != null) callbacks.onCancelled();
        }

        private String getImageURL() {
            String imageURL = null;
            if (photoUri != null){
                FirebaseStorage fs = FirebaseStorage.getInstance();
                StorageReference sr = fs.getReference(Const.USERS_IMAGES).child(photoUri.getLastPathSegment());
                uploadTask = sr.putBytes(userImage);
                while (uploadTask.isInProgress()){
                   setProgress();
                }
                if (uploadTask.isSuccessful()){
                    try {
                        imageURL = uploadTask.getResult().getDownloadUrl().toString();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }
            return imageURL;
        }

        private void saveUserDataInDB(User user, String imageURL){
            user.setUserID(((BaseActivity) getActivity()).getAuth().getUid());
            user.setUserName(name);
            user.setUserEmail(email);
            user.setImageUrl(imageURL);
            FirebaseDatabase.getInstance().getReference(Const.USER_INFO).updateChildren(user.toMap());
        }
    }
}

