package com.example.user.messager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        return view;
    }

    @OnClick({R.id.loginBtn, R.id.registrationBtn})
    public void loginAction(Button button){
        if (!validationFields(userLoginET.getText().toString(), userPasswordET.getText().toString())){
            return;
        }
        Fragment fragment;
        if (button.getId() == R.id.loginBtn){
            fragment = new ChatListFragment();
        }else {
            fragment = new RegistrationFragment();
        }
        replaceFragment(fragment);
    }

    private boolean validationFields(String login, String password) {
        if (login.length() > 0 && password.length() > 0){
            return true;
        }
        Toast.makeText(getContext(), "Fields aren't valid", Toast.LENGTH_SHORT).show();
        return false;
    }


}
