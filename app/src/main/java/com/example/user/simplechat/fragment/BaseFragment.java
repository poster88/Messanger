package com.example.user.simplechat.fragment;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by User on 011 11.10.17.
 */

public class BaseFragment extends Fragment {
    private Unbinder unbinder;

    protected void bindFragment(Object target, View view){
        unbinder = ButterKnife.bind(target, view);
    }

    protected void setImageToView(Uri uri, ImageView view, Fragment fragment) {
        Glide.with(fragment)
                .load(uri)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    protected byte[] setByteArrayFromImage(CircleImageView userImage){
        userImage.setDrawingCacheEnabled(true);
        userImage.buildDrawingCache();

        Bitmap bitmap = userImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    protected void remove(FragmentManager fm, Fragment fragment) {
        if(!fm.isDestroyed()){
            fm.beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
        }
    }

    protected boolean fieldValidation(EditText field){
        return TextUtils.isEmpty(field.getText().toString()) ? false : true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null){
            unbinder.unbind();
        }
    }
}
