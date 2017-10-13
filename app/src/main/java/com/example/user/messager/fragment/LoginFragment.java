package com.example.user.messager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.messager.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment{
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        bindFragment(view);
        return view;
    }

    @OnClick({R.id.loginBtn, R.id.registrationBtn})
    public void loginAction(Button button){
        if (button.getId() == R.id.registrationBtn){
            //replaceFragment(getTargetFragment(), new RegistrationFragment());
            return;
        }
        if (!validationFields(userLoginET.getText().toString(), userPasswordET.getText().toString())){
            //// TODO: 012 12.10.17 add auth
            replaceFragment(getTargetFragment(), new ChatListFragment());
        }
    }




}
