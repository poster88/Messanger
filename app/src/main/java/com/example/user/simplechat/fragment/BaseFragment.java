package com.example.user.simplechat.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.simplechat.R;
import com.example.user.simplechat.utils.CircleTransform;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by User on 011 11.10.17.
 */

public class BaseFragment extends FragmentTitle {
    private Unbinder unbinder;
    protected ProgressDialog progressDialog;

    protected void bindFragment(Object target, View view){
        unbinder = ButterKnife.bind(target, view);
    }

    protected void setRoundImageToView(Uri uri, ImageView view) {
        Glide.with(this)
                .load(uri)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    protected void replaceFragments(Fragment fragment, String tag){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(tag);
        ft.commit();
    }

    public void onTaskStarted(
            Activity activity, String title, String message, boolean isIndeterminate, boolean isCancelable,
            DialogInterface.OnClickListener negativeBtn, String negativeBtnLabel,
            DialogInterface.OnClickListener positiveBtn, String positiveBtnLabel
    ) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(isIndeterminate);
        progressDialog.setCancelable(isCancelable);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, negativeBtnLabel, negativeBtn);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, positiveBtnLabel, positiveBtn);
        progressDialog.show();
    }

    public void onTaskFinished() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected boolean fieldValidation(EditText field){
        if (!TextUtils.isEmpty(field.getText().toString())){
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
