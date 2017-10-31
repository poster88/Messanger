package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.simplechat.R;
<<<<<<< HEAD
import com.example.user.simplechat.adapter.FirebaseUserAdapter;
import com.example.user.simplechat.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
=======
>>>>>>> create new branch

import butterknife.BindView;

/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment {
    @BindView(R.id.userRecycleView) RecyclerView usersRecView;

    private DatabaseReference ref;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseUserAdapter usersListAdapter;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setRetainInstance(true);
        if (savedInstanceState == null){
            ref = database.getReferenceFromUrl(REF_USERS);
            options = new FirebaseRecyclerOptions.Builder<User>().setQuery(ref, User.class).build();
            usersListAdapter = new FirebaseUserAdapter(options, R.layout.user_list_item);
            usersListAdapter.startListening();
=======
        if (getArguments() != null && getArguments().containsKey(USER_ID)){
            userID = getArguments().getString(USER_ID);
            loadUserList();
>>>>>>> create new branch
        }
    }

    private void loadUserList() {
        //todo: firebaseListAdapter
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        bindFragment(this, view);
        layoutManager = new LinearLayoutManager(getActivity());
        usersRecView.setLayoutManager(layoutManager);
        usersRecView.setAdapter(usersListAdapter);
        return view;
    }
}
