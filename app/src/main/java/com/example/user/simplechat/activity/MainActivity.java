package com.example.user.simplechat.activity;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.user.simplechat.fragment.ChatListFragment;
import com.example.user.simplechat.fragment.LoginFragment;
import com.example.user.simplechat.R;
import com.example.user.simplechat.fragment.MyDialogFragment;
import com.example.user.simplechat.service.MyTaskService;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity implements MyDialogFragment.DialogCallbacks{
    private FragmentManager fm = getSupportFragmentManager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null){
                checkIntentAction(intent);
            }
        }
    };

    private ProgressDialog progressDialog;

    public Handler handler;
    private Handler.Callback hc = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Log.d(Const.MY_LOG, "handleMessage : " + message.what);
            /*if (message.what == 0){
                dialogStarted(MainActivity.this,  "Login", "Please wait a moment!",
                        true, false, null, null, null, null);
            }
            if (message.what == 6){
                dialogFinished();
            }*/

            //Toast.makeText(getApplicationContext(), "handleMessage : " + message.what, Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private void checkIntentAction(Intent intent) {
        boolean isOnline = false;
        String currentID = auth.getUid();

        if (intent.getAction().equals(Const.USER_ONLINE)){
            isOnline = true;
        }
        if (intent.getAction().equals(Const.USER_OFFLINE)){
            isOnline = false;
        }
        if (intent.getAction().equals(Const.USER_LOG_OFF)){
            auth.signOut();
        }
        startMyService(isOnline, currentID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        innitBrReceiver();

        innitSavedInstanceState(savedInstanceState);
        if (savedInstanceState == null){

        }
    }

    private void innitSavedInstanceState(Bundle savedInstanceState){
        if (savedInstanceState == null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.container, LoginFragment.newInstance());
            ft.commit();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        setIsOnlineStatus(Const.USER_OFFLINE);
    }

    private void setIsOnlineStatus(String action) {
        if (auth.getCurrentUser() != null){
            sendBroadcast(new Intent(action));
        }
    }

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0){
            if ((fm.findFragmentById(R.id.container)) instanceof ChatListFragment){
                setIsOnlineStatus(Const.USER_LOG_OFF);
            }
            fm.popBackStack();
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

    public FragmentManager getFm() {
        return fm;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public Handler.Callback getHc() {
        return hc;
    }

    public void dialogStarted(
            Context context, String title, String message, boolean isIndeterminate, boolean isCancelable,
            DialogInterface.OnClickListener negativeBtn, String negativeBtnLabel,
            DialogInterface.OnClickListener positiveBtn, String positiveBtnLabel
    ) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(isIndeterminate);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeBtnLabel, negativeBtn);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveBtnLabel, positiveBtn);
        progressDialog.show();
    }

    public void dialogFinished() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPreExecute() {
        dialogStarted(MainActivity.this,  "Login", "Please wait a moment!",
                true, false, null, null, null, null);
    }

    @Override
    public void onProgressUpdate() {
        if (progressDialog == null){
            dialogStarted(MainActivity.this,  "Login", "Please wait a moment!",
                    true, false, null, null, null, null);
        }
    }

    @Override
    public void onCancelled() {
        dialogFinished();
    }

    @Override
    public void onPostExecute() {
        dialogFinished();
    }
}