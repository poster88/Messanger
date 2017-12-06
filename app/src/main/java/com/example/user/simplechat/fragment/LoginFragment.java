package com.example.user.simplechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.MainActivity;
import com.example.user.simplechat.utils.Const;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    private boolean isDialogRunning;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    private OnCompleteListener loginTaskListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            isDialogRunning(false);
            if (task.isSuccessful()){
                showChatList(((MainActivity)getActivity()).getAuth().getCurrentUser());
                return;
            }
            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null){
            showChatList(((MainActivity)getActivity()).getAuth().getCurrentUser());
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
            isDialogRunning(true);
            ((MainActivity)getActivity()).getAuth()
                    .signInWithEmailAndPassword(userLoginET.getText().toString(), userPasswordET.getText().toString())
                    .addOnCompleteListener(loginTaskListener);
        }
    }

    @OnClick(R.id.registrationBtn)
    public void regAction(){
        super.replaceFragments(RegistrationFragment.newInstance(), Const.REGISTRATION_TAG);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            isDialogRunning(savedInstanceState.getBoolean(Const.IS_DIALOG_RUNNING_KEY, false));
        }
    }

    private void isDialogRunning(boolean flag){
        isDialogRunning = flag;
        if (!flag){
            super.dialogFinished();
            return;
        }
        super.dialogStarted(getActivity(),  "Login", "Please wait a moment!",
                true, false, null, null, null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.IS_DIALOG_RUNNING_KEY, isDialogRunning);
    }

    private void showChatList(FirebaseUser user) {
        if (user != null){
            super.replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_TAG);
            getActivity().sendBroadcast(new Intent(Const.USER_ONLINE));
        }
    }
}
