package com.example.user.messager.fragment;

import android.support.v4.app.Fragment;
import android.view.View;

import com.example.user.messager.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by User on 011 11.10.17.
 */

public class BaseFragment extends Fragment{
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

    protected void replaceFragment(Fragment fragment){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
