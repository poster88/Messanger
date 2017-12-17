package com.example.user.simplechat.fragment;

import android.Manifest;
import android.content.Intent;
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

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.BaseActivity;
import com.example.user.simplechat.fragment.tasks.RegTaskFragment;
import com.example.user.simplechat.utils.Const;


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

    public static RegistrationFragment newInstance(){
        return new RegistrationFragment();
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
            checkPhotoUri((Uri) savedInstanceState.getParcelable(Const.URI_PHOTO_KEY));
        }
    }

    private void checkPhotoUri(Uri uri){
        photoUri = uri;
        if (photoUri != null) super.setImageToView(photoUri, userImage, RegistrationFragment.this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Const.URI_PHOTO_KEY, photoUri);
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
        ((BaseActivity) getActivity()).removeFragmentFromBackStack();
    }

    @OnClick(R.id.regOkBtn)
    public void okBtnClick(){
        if (fieldValidation(userName) && fieldValidation(userEmail) && fieldValidation(userPass)){
            RegTaskFragment regTaskFragment = RegTaskFragment.newInstance(
                    userEmail.getText().toString(),
                    userPass.getText().toString(),
                    userName.getText().toString(),
                    super.setByteArrayFromImage(userImage),
                    checkURI()
            );
            ((BaseActivity)getActivity()).addFragment(regTaskFragment);
        }
    }

    private String checkURI(){
         return photoUri != null ? photoUri.toString() : null;
    }
}