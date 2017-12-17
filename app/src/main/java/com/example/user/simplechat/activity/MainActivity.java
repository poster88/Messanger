package com.example.user.simplechat.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.user.simplechat.fragment.ChatFragment;
import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.example.user.simplechat.fragment.impl.AsyncTaskCallbacks;
import com.example.user.simplechat.service.MyTaskService;
import com.example.user.simplechat.utils.Const;


public class MainActivity extends BaseActivity implements AsyncTaskCallbacks {
    private Handler handler;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null){
                checkIntentAction(intent);
            }
        }
    };
    private Handler.Callback hc = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == Const.SIGN_IN_OK){
                showChatListFragment();
            }
            if (message.what == Const.REG_OK){
                MainActivity.super.removeFragmentFromBackStack();
                showChatListFragment();
            }
            if (message.what == Const.TASK_FAIL){
                MainActivity.super.showToast(MainActivity.this, message.obj.toString());
            }
            return false;
        }
    };

    private void showChatListFragment() {
        updateUserProfile(super.getAuth().getUid());
        sendBroadcast(new Intent(Const.USER_ONLINE));
        super.replaceFragments(ChatListFragment.newInstance(), Const.CHAT_LIST_TAG);
    }

    private void updateUserProfile(String currentID) {
        Intent intent = new Intent(MainActivity.this, MyTaskService.class);
        intent.setAction(Const.UPDATE_PROFILE);
        intent.putExtra(Const.CURRENT_ID_KEY, currentID);
        startService(intent);
    }

    private void checkIntentAction(Intent intent) {
        boolean isOnline = false;
        String currentID = super.getAuth().getUid();
        if (intent.getAction().equals(Const.USER_ONLINE)){
            isOnline = true;
        }
        if (intent.getAction().equals(Const.USER_OFFLINE)){
            isOnline = false;
        }
        if (intent.getAction().equals(Const.USER_LOG_OFF)){
            super.getAuth().signOut();
        }
        startMyService(isOnline, currentID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(hc);
        innitBrReceiver();
        showLoginFragment(savedInstanceState);
    }

    private void showLoginFragment(Bundle savedInstanceState){
        if (savedInstanceState == null){
            super.addFragment(LoginFragment.newInstance());
        }
    }

    private void innitBrReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Const.USER_ONLINE);
        filter.addAction(Const.USER_OFFLINE);
        filter.addAction(Const.USER_LOG_OFF);
        registerReceiver(br, filter);
    }

    private void startMyService(boolean isOnline, String currentID){
        Intent intent = new Intent(MainActivity.this, MyTaskService.class);
        intent.setAction(Const.UPDATE_ONLINE_STATUS);
        intent.putExtra(Const.CURRENT_ID_KEY, currentID);
        intent.putExtra(Const.ONLINE_STATUS_KEY, isOnline);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setIsOnlineStatus(Const.USER_ONLINE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setIsOnlineStatus(Const.USER_OFFLINE);
    }

    private void setIsOnlineStatus(String action) {
        if (super.getAuth().getCurrentUser() != null){
            sendBroadcast(new Intent(action));
        }
    }

    @Override
    public void onBackPressed() {
        if (super.getFm().getBackStackEntryCount() > 0){
            if ((super.getFm().findFragmentById(R.id.container)) instanceof ChatListFragment){
                setIsOnlineStatus(Const.USER_LOG_OFF);
            }
            if ((super.getFm().findFragmentById(R.id.container)) instanceof ChatFragment){
                //TODO: delete chat link if message array is null
            }
            super.getFm().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
        dialogFinished();
    }

    private void setLoginPrDialog(){
        super.dialogStarted(MainActivity.this,  "Login", "Please wait a moment!",
                true, false, null, null, null, null);
    }

    @Override
    public void onPreExecute() {
        setLoginPrDialog();
    }

    @Override
    public void onProgressUpdate() {
        if (super.getProgressDialog() == null){
            setLoginPrDialog();
        }
    }

    @Override
    public void onPostExecute(int result, String message) {
        super.dialogFinished();
        if (result == Const.TASK_FAIL){
            Message msg = handler.obtainMessage(result, message);
            handler.sendMessage(msg);
            return;
        }
        handler.sendEmptyMessage(result);
    }

    @Override
    public void onCancelled() {
        super.dialogFinished();
    }

    public Handler getHandler() {
        return handler;
    }
}