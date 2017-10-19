package com.example.user.messager.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.messager.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.OnClick;


/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment{

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chatlist_fragment, container, false);
        bindFragment(this, view);
        return view;
    }

    @OnClick(R.id.logOutBtn)
    public void logOutAction(){
        FirebaseAuth.getInstance().signOut();
        super.replaceFragments(LoginFragment.newInstance());
    }
}
