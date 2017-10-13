package com.example.user.messager.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by User on 011 11.10.17.
 */

public class BaseFragment extends Fragment{

    protected final static int REQUEST_READ_PERMISSION = 9003;
    protected final static int PHOTO_REQUEST = 9002;
    private Unbinder unbinder;

    @Override
    public void onDestroyView() {
        if(this.unbinder != null){
            this.unbinder.unbind();
        }
        super.onDestroyView();
    }

    protected void bindFragment(View view){
        unbinder = ButterKnife.bind(view);
    }

    protected void replaceFragment(Fragment currentFragment, Fragment fragmentToReplace){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(currentFragment.getId(), fragmentToReplace);
        ft.commit();
    }

    protected boolean validationFields(String login, String password) {
        if (login.length() > 0 && password.length() > 0){
            return true;
        }
        Toast.makeText(getContext(), "Fields aren't valid", Toast.LENGTH_SHORT).show();
        return false;
    }

    protected boolean validationField(String text) {
        if (text.length() > 0 ){
            return true;
        }
        Toast.makeText(getContext(), "Fields aren't valid", Toast.LENGTH_SHORT).show();
        return false;
    }
}
