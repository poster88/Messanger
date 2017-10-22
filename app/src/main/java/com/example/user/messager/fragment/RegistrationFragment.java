package com.example.user.messager.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.messager.R;
import com.example.user.messager.model.User;
import com.example.user.messager.utils.Utils;
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

    private boolean isTaskRunning = false;
    private Uri photoUri;

    private DialogInterface.OnClickListener cancelDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            RegistrationFragment.super.progressDialog.dismiss();
        }
    };

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
        }
    };

    private void saveUserInRealTimeDB(User user, String downloadUrl){
        user.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.setUserName(userName.getText().toString());
        user.setUserEmail(userEmail.getText().toString());
        user.setImageUrl(downloadUrl);
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put(user.getUserID(), user);
        FirebaseDatabase.getInstance().getReference(Utils.USER_INFO).updateChildren(user.toMap());
        onTaskFinished();
        RegistrationFragment.super.replaceFragments(ChatListFragment.newInstance(), false);
    }

    private void updateStoragePhoto(Uri photoUri) {
        if (photoUri != null){
            FirebaseStorage fs = FirebaseStorage.getInstance();
            StorageReference sr = fs.getReference(Utils.USERS_IMAGES).child(photoUri.getLastPathSegment());
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
        saveUserInRealTimeDB(new User(), Utils.PHOTO_DEFAULT);
    }

    private boolean isDefaultPhoto(ImageView imageView, @DrawableRes int defPhotoID, Context context){
        if (imageView.getDrawable().getConstantState() == ContextCompat.getDrawable(
                context, defPhotoID).getConstantState()){
            return true;
        }
        return false;
    }

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
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
            showProgressDialog(getActivity(), "Registration", "Please wait a moment!",
                    true, false, cancelDialogListener, "Cancel", null, null);
        }
    }

    @OnClick(R.id.setImageBtn)
    public void setImageAction(){
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Utils.REQUEST_READ_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Utils.PHOTO_REQUEST);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Utils.REQUEST_READ_PERMISSION);
                return;
            }
        }
        openFilePicker();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Utils.PHOTO_REQUEST){
            if (resultCode == Utils.RESULT_OK){
                photoUri = data.getData();
                super.setRoundImageToView(photoUri, userImage);
            }
        }
    }

    @OnClick({R.id.regOkBtn, R.id.regCancelBtn})
    public void buttonAction(Button button){
        if (button.getId() == R.id.regCancelBtn){
            getActivity().onBackPressed();
            return;
        }
        if (!TextUtils.isEmpty(userName.getText().toString()) && !TextUtils.isEmpty(userEmail.getText().toString())){
            RegistrationFragment.super.replaceFragments(ChatListFragment.newInstance(), false);
            /*onTaskStarted();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail.getText().toString(), userPass.getText().toString())
                    .addOnSuccessListener(regSuccessListener)
                    .addOnFailureListener(failureListener);*/
        }
    }

    public void onTaskStarted() {
        isTaskRunning = true;
        showProgressDialog(getActivity(), "Registration", "Please wait a moment!",
                true, false, cancelDialogListener, "Cancel", null, null);
    }

    public void onTaskFinished() {
        if (RegistrationFragment.super.progressDialog != null) {
            RegistrationFragment.super.progressDialog.dismiss();
        }
        isTaskRunning = false;
    }

    @Override
    public void onDetach() {
        hideProgressDialog();
        super.onDetach();
    }
}