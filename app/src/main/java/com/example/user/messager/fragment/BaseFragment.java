package com.example.user.messager.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.messager.R;
import com.example.user.messager.utils.CircleTransform;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by User on 011 11.10.17.
 */

public class BaseFragment extends Fragment{
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

    protected void replaceFragments(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(fragment.getTag());
        ft.commit();
    }

    protected void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
