package com.example.user.simplechat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
        //setRetainInstance(true);
        Log.d(Const.TAG, "LoginFragment : onCreate ");
        if (savedInstanceState == null){
            Log.d(Const.TAG, "*** LoginFragment : savedInstanceState is null ***");
            showChatListFragment(auth.getCurrentUser());
        }else {
            Log.d(Const.TAG, "*** LoginFragment : savedInstanceState is't null ***");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(Const.TAG, "LoginFragment : onCreateView ");
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @OnClick({R.id.loginBtn, R.id.registrationBtn})
    public void loginAction(Button button){
        if (button.getId() == R.id.registrationBtn){
            super.replaceFragments(RegistrationFragment.newInstance(), Const.REGISTRATION_FRAG);
            return;
        }
        if(fieldValidation(userLoginET) && fieldValidation(userPasswordET)){
            isTaskRunning = true;
            setTaskRunning();
            //setRetainInstance(true);
            auth.signInWithEmailAndPassword(userLoginET.getText().toString(), userPasswordET.getText().toString())
                    .addOnCompleteListener(LoginFragment.this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(Const.TAG, "LoginFragment : onActivityCreated ");
        super.onActivityCreated(savedInstanceState);
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
        //setRetainInstance(false);
        if (task.isSuccessful()){
            showChatListFragment(auth.getCurrentUser());
            return;
        }
        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void showChatListFragment(FirebaseUser user) {
        if (user != null){
            LoginFragment.super.replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_FRAG);
            return;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(Const.TAG, "LoginFragment : onSaveInstanceState ");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(Const.TAG, "LoginFragment : onActivityResult ");
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(Const.TAG, "LoginFragment : onAttach ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Const.TAG, "LoginFragment : onStart ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(Const.TAG, "LoginFragment : onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(Const.TAG, "LoginFragment : onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Const.TAG, "LoginFragment : onStop ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Const.TAG, "LoginFragment : onDestroyView ");
    }

    @Override
    public void onDetach() {
        Log.d(Const.TAG, "LoginFragment : onDetach ");
        super.onTaskFinished();
        super.onDetach();

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(Const.TAG, "LoginFragment : onViewStateRestored ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.TAG, "LoginFragment : onDestroy ");
    }
}
