package com.example.user.simplechat.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by User on 011 11.10.17.
 */

public class LoginFragment extends BaseFragment {
    @BindView(R.id.userLoginEdit) EditText userLoginET;
    @BindView(R.id.userPasswordEdit) EditText userPasswordET;

    private boolean isDialogRunning;
    Handler handler;
    Thread t;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    private OnCompleteListener loginTaskListener = new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {
            Log.d(Const.MY_LOG, "loginTaskListener in the method");
            isDialogRunning(false);
            if (task.isSuccessful()){
                Log.d(Const.MY_LOG, "task.isSuccessful: ");
                //showChatList(((MainActivity)getActivity()).getAuth().getCurrentUser());
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
        Log.d(Const.MY_LOG, "onCreate loginFragment ");
        //taksFragment
    }

    private void showChatList(FirebaseUser user) {
        if (user != null){
            getActivity().sendBroadcast(new Intent(Const.USER_ONLINE));
            super.replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_TAG);
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
            Log.d(Const.MY_LOG, "loginAction in the method");
            handler = new Handler(((MainActivity)getActivity()).getHc());
            t = new Thread(myRun);
            t.start();
            Log.d(Const.MY_LOG, "handler.post(runnable);");
        }
    }

    Runnable myRun = new Runnable() {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 7; i++) {
                    TimeUnit.MILLISECONDS.sleep(2000);
                    handler.sendEmptyMessage(i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d(Const.MY_LOG, "run in the method");
            isDialogRunning(true);
            ((MainActivity)getActivity()).getAuth()
                    .signInWithEmailAndPassword(userLoginET.getText().toString(), userPasswordET.getText().toString())
                    .addOnCompleteListener(loginTaskListener);
        }
    };

    @OnClick(R.id.registrationBtn)
    public void regAction(){
        //super.replaceFragments(RegistrationFragment.newInstance(), Const.REGISTRATION_TAG);
        t.interrupt();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.IS_DIALOG_RUNNING_KEY, isDialogRunning);
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
        super.dialogStarted(getContext(),  "Login", "Please wait a moment!",
                true, false, null, null, null, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isDialogRunning){
            super.dialogFinished();
        }
    }

    // This code up to onDetach() is all to get easy callbacks to the Activity.
    private Callbacks call = dummyCallbacks;

    public interface Callbacks{
        public void onTaskFinished();
    }

    private static Callbacks dummyCallbacks = new Callbacks() {
        @Override
        public void onTaskFinished() {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks)){
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        call = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        call = dummyCallbacks;
    }

    static final int TASK_FRAGMENT = 0;
    static final String TASK_FRAGMENT_TAG = "task";

    public static class TaskFragment extends DialogFragment {
        // The task we are running.

    }
}
