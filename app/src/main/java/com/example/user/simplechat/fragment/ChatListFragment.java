package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.simplechat.R;
import com.example.user.simplechat.adapter.UserRecycleAdapter;
import com.example.user.simplechat.listener.ChildValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment {
    @BindView(R.id.userRecycleView) RecyclerView usersRecView;
    private String currentUserID;
    private ArrayList<User> usersListData;
    private RecyclerView.Adapter adapter;
    private Query query;

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    private ChildValueListener myChildListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!dataSnapshot.getValue(User.class).getUserID().equals(currentUserID)){
                usersListData.add(dataSnapshot.getValue(User.class));
                adapter.notifyItemInserted(usersListData.size());
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            for (int i = 0; i < usersListData.size(); i++) {
                if (usersListData.get(i).getUserID().equals(dataSnapshot.getKey())){
                    usersListData.set(i, dataSnapshot.getValue(User.class));
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            super.onChildRemoved(dataSnapshot);
            System.out.println(dataSnapshot.toString() + "onChildRemoved" );
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            super.onChildMoved(dataSnapshot, s);
            System.out.println(dataSnapshot.toString() + "onChildMoved string : " + s);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setRetainInstance(true);
            innitDataForWidget();
        }
    }

    private void innitDataForWidget() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        usersListData = new ArrayList<>();
        adapter = new UserRecycleAdapter(usersListData);
        query = FirebaseDatabase.getInstance().getReferenceFromUrl(Const.REF_USERS).orderByChild(Const.QUERY_NAME_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        bindFragment(this, view);
        usersRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersRecView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (query != null){
            query.addChildEventListener(myChildListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (query != null){
            query.removeEventListener(myChildListener);
        }
    }

    private FirebaseRecyclerOptions<User> setFirebaseRecyclerOptions() {
        Query query = FirebaseDatabase.getInstance().getReferenceFromUrl(Const.REF_USERS).orderByChild(Const.QUERY_NAME_KEY);
        return new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
    }

    /*@Override
    public void onItemClick(String userID) {
        //super.replaceFragments(ChatFragment.newInstance(userID), Const.CHAT_FRAG_TAG);
    }

    @Override
    public void updateItem(int position) {
        System.out.println(position);
        this.position = position;
    }*/
}
