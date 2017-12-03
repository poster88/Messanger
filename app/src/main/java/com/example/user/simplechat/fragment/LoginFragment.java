package com.example.user.simplechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment implements OnCompleteListener {
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private boolean isTaskRunning;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            showChatListFragment(auth.getCurrentUser());
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
            super.replaceFragments(RegistrationFragment.newInstance(), Const.REGISTRATION_TAG);
            return;
        }
        if(fieldValidation(userLoginET) && fieldValidation(userPasswordET)){
            isTaskRunning = true;
            setTaskRunning();
            auth.signInWithEmailAndPassword(userLoginET.getText().toString(), userPasswordET.getText().toString())
                    .addOnCompleteListener(LoginFragment.this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            isTaskRunning = savedInstanceState.getBoolean(Const.IS_TASK_RUNNING_KEY);
        }
        if (isTaskRunning){
            setTaskRunning();
        }
    }

    private void setTaskRunning(){
        super.onTaskStarted(getActivity(),  "Login", "Please wait a moment!",
                true, false, null, null,
                null, null);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        super.onTaskFinished();
        isTaskRunning = false;
        if (task.isSuccessful()){
            showChatListFragment(auth.getCurrentUser());
            getActivity().sendBroadcast(new Intent(Const.USER_ONLINE));
            return;
        }
        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.IS_TASK_RUNNING_KEY, isTaskRunning);
    }

    private void showChatListFragment(FirebaseUser user) {
        if (user != null){
            LoginFragment.super.replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_TAG);
        }
    }
}
