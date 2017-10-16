package com.example.user.messager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.messager.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment{
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                super.replaceFragments(new ChatListFragment());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @OnClick({R.id.loginBtn, R.id.registrationBtn})
    public void loginAction(Button button){
        if (button.getId() == R.id.registrationBtn){
            super.replaceFragments(RegistrationFragment.newInstance());
            return;
        }
    }




}
