package com.example.user.messager.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.messager.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class RegistrationFragment extends BaseFragment{
    @BindView(R.id.regUserImage) ImageView userImage;
    @BindView(R.id.regUserNameEdit) EditText userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);
        return view;
    }

    @OnClick(R.id.setImageBtn)
    public void setImageAction(){
        requestPermissions();
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, this.PHOTO_REQUEST);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, this.REQUEST_READ_PERMISSION);
                return;
            }
        }
        openFilePicker();
    }

    @OnClick({R.id.regOkBtn, R.id.regCancelBtn})
    public void buttonAction(Button button){
        if (button.getId() == R.id.regCancelBtn){
            this.replaceFragment(getTargetFragment(), new LoginFragment());
            return;
        }
        if (this.validationField(userName.getText().toString())){

        }
    }
}
