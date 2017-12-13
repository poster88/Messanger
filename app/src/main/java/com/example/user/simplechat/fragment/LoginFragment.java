package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.BaseActivity;
import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.fragment.tasks.SingInTaskFragment;
import com.example.user.simplechat.utils.Const;


import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShouldLoadLoginFragment(savedInstanceState);
    }

    private void isShouldLoadLoginFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            if (((BaseActivity) getActivity()).getAuth() != null){
                ((MainActivity) getActivity()).getHandler().sendEmptyMessage(Const.SIGN_IN_OK);
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

    @OnClick(R.id.loginBtn)
    public void loginAction(){
        if (fieldValidation(userLoginET) && fieldValidation(userPasswordET)){
            SingInTaskFragment singInTaskFragment = SingInTaskFragment.newInstance(
                    userLoginET.getText().toString(),
                    userPasswordET.getText().toString()
            );
            ((BaseActivity)getActivity()).addFragment(singInTaskFragment);
        }
    }

    @OnClick(R.id.registrationBtn)
    public void regAction(){
        ((BaseActivity)getActivity()).replaceFragments(RegistrationFragment.newInstance(), Const.REGISTRATION_TAG);
    }
}