package com.example.user.simplechat.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.example.user.simplechat.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class RegistrationFragment extends BaseFragment {
    @BindView(R.id.regUserImage) ImageView userImage;
    @BindView(R.id.regUserNameEdit) EditText userName;
    @BindView(R.id.regUserEmailEdit) EditText userEmail;
    @BindView(R.id.regPassEdit) EditText userPass;

    private Uri photoUri;
    private boolean isTaskRunning;

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
    }

    private OnSuccessListener regSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            updateStoragePhoto(photoUri);
        }
    };

    private OnSuccessListener<UploadTask.TaskSnapshot> addPhotoSuccessListener = new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            @SuppressWarnings("VisibleForTests") String getDownloadUrl = taskSnapshot.getDownloadUrl().toString();
            saveUserInRealTimeDB(new User(), getDownloadUrl);
        }
    };

    private OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            onTaskFinished();
            isTaskRunning = false;
        }
    };

    private void saveUserInRealTimeDB(User user, String downloadUrl){
        user.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.setUserName(userName.getText().toString());
        user.setUserEmail(userEmail.getText().toString());
        user.setImageUrl(downloadUrl);
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put(user.getUserID(), user);
        FirebaseDatabase.getInstance().getReference(USER_INFO).updateChildren(user.toMap());

        onTaskFinished();
        isTaskRunning = false;
        super.replaceFragments(ChatListFragment.newInstance(), CHAT_LIST_FRAG);
    }

    private void updateStoragePhoto(Uri photoUri) {
        if (photoUri != null){
            FirebaseStorage fs = FirebaseStorage.getInstance();
            StorageReference sr = fs.getReference(USERS_IMAGES).child(photoUri.getLastPathSegment());

            userImage.setDrawingCacheEnabled(true);
            userImage.buildDrawingCache();
            Bitmap bitmap = userImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uTask = sr.putBytes(data);
            uTask.addOnFailureListener(failureListener);
            uTask.addOnSuccessListener(addPhotoSuccessListener);
            return;
        }
        saveUserInRealTimeDB(new User(), PHOTO_DEFAULT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null && photoUri != null){
            super.setRoundImageToView(photoUri, userImage);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isTaskRunning){
            setRegistrationTask();
        }
    }

    @OnClick(R.id.setImageBtn)
    public void setImageAction(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_PERMISSION);
                return;
            }
        }
        openFilePicker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST){
            if (resultCode == RESULT_OK){
                photoUri = data.getData();
                super.setRoundImageToView(photoUri, userImage);
            }
        }
    }

    @OnClick({R.id.regOkBtn, R.id.regCancelBtn})
    public void buttonAction(Button button){
        if (button.getId() == R.id.regCancelBtn){
            getActivity().getSupportFragmentManager().popBackStack();
            return;
        }
        if (fieldValidation(userName) && fieldValidation(userEmail) && fieldValidation(userPass)){
            isTaskRunning = true;
            setRegistrationTask();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail.getText().toString(), userPass.getText().toString())
                    .addOnSuccessListener(regSuccessListener)
                    .addOnFailureListener(failureListener);
        }
    }

    private void setRegistrationTask(){
        super.onTaskStarted(getActivity(), "Registration", "Please wait a moment!",
                true, false, null, null, null, null);
    }

    @Override
    public void onDetach() {
        onTaskFinished();
        super.onDetach();
    }
}