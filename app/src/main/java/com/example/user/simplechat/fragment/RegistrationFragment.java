package com.example.user.simplechat.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.BaseActivity;
import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.service.MyTaskService;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 011 11.10.17.
 */

public class RegistrationFragment extends BaseFragment {
    @BindView(R.id.regUserImage) CircleImageView userImage;
    @BindView(R.id.regUserNameEdit) EditText userName;
    @BindView(R.id.regUserEmailEdit) EditText userEmail;
    @BindView(R.id.regPassEdit) EditText userPass;

    private Uri photoUri;
    private boolean isDialogRunning;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra(Const.UPLOAD_STATUS_KEY);
            if (result.equals(Const.UPLOAD_STATUS_OK)){
                saveUserDataInDB(new User(), intent.getStringExtra(Const.UPLOAD_IMAGE_URL));
                showChatList();
            }
            if (result.equals(Const.UPLOAD_STATUS_FAIL)){
                showErrorMessage(intent.getStringExtra(Const.UPLOAD_MESSAGE_KEY));
            }
        }
    };
    private OnSuccessListener regSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            sendServiceIntent();
        }
    };
    private OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            showErrorMessage("Exception : " + e.getMessage());
        }
    };

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
    }

    private void sendServiceIntent(){
        Intent serviceIntent = new Intent(getContext(), MyTaskService.class);
        serviceIntent.setAction(Const.UPLOAD_IMAGE_TASK);
        serviceIntent.setData(photoUri);
        serviceIntent.putExtra(Const.BYTE_IMAGE_KEY, super.setByteArrayFromImage(userImage));
        getActivity().startService(serviceIntent);
    }

    private void showErrorMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.UPLOAD_IMAGE_ACTION);
        getActivity().registerReceiver(br, filter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            checkPhotoUri((Uri) savedInstanceState.getParcelable(Const.USER_IMAGE_KEY));
        }
    }

    private void checkPhotoUri(Uri uri){
        photoUri = uri;
        if (photoUri != null) super.setImageToView(photoUri, userImage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.IS_DIALOG_RUNNING_KEY, isDialogRunning);
        outState.putParcelable(Const.USER_IMAGE_KEY, photoUri);
    }

    @OnClick(R.id.setImageBtn)
    public void setImageAction(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Const.REQUEST_READ_PERMISSION);
                return;
            }
        }
        openFilePicker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Const.REQUEST_READ_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openFilePicker();
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Const.PHOTO_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.PHOTO_REQUEST){
            if (resultCode == Const.RESULT_OK){
                checkPhotoUri(data.getData());
            }
        }
    }

    @OnClick(R.id.regCancelBtn)
    public void cancelBtnClick(){
        super.removeFragmentFromBackStack();
    }

    @OnClick(R.id.regOkBtn)
    public void okBtnClick(){
        if (fieldValidation(userName) && fieldValidation(userEmail) && fieldValidation(userPass)){
            ((MainActivity) getActivity()).getAuth()
                    .createUserWithEmailAndPassword(userEmail.getText().toString(), userPass.getText().toString())
                    .addOnSuccessListener(regSuccessListener)
                    .addOnFailureListener(failureListener);
        }
    }

    private void saveUserDataInDB(User user, String photoUrl){
        user.setUserID(((MainActivity) getActivity()).getAuth().getUid());
        user.setUserName(userName.getText().toString());
        user.setUserEmail(userEmail.getText().toString());
        user.setImageUrl(photoUrl);
        Map<String, Object> userInfoMap = new HashMap<>();
        userInfoMap.put(user.getUserID(), user);
        FirebaseDatabase.getInstance().getReference(Const.USER_INFO).updateChildren(user.toMap());
    }

    private void showChatList() {
        getActivity().sendBroadcast(new Intent(Const.USER_ONLINE));
        isDialogRunning = false;
        super.removeFragmentFromBackStack();
        ((BaseActivity) getActivity()).replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(br);
    }
}