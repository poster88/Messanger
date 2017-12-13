package com.example.user.simplechat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.user.simplechat.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by POSTER on 13.12.2017.
 */

public class BaseActivity extends AppCompatActivity{
    private FragmentManager fm = getSupportFragmentManager();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;

    protected void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    protected void dialogStarted(
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

    public void addFragment(Fragment fragment){
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, fragment);
        ft.commit();
    }

    public void replaceFragments(Fragment fragment, String tag){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(tag);
        ft.commit();
    }

    public FragmentManager getFm() {
        return fm;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }
}
