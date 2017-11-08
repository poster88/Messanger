package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.os.Parcelable;
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
    private ArrayList<String> enabledChatUsersData;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private Query query;
    private Query queryChatID;
    private Parcelable state;

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    private ChildValueListener usersInfoListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!dataSnapshot.getValue(User.class).getUserID().equals(currentUserID)){
                usersListData.add(dataSnapshot.getValue(User.class));
                adapter.notifyItemInserted(usersListData.size());
                System.out.println("inserted");
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
            int index = usersListData.indexOf(dataSnapshot.getValue(User.class));
            usersListData.remove(index);
            adapter.notifyItemRemoved(index);
        }
    };

    private ChildValueListener chatIDTableListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            enabledChatUsersData.add(dataSnapshot.getKey());
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            enabledChatUsersData.remove(dataSnapshot.getKey());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        innitDataForQuery();
    }

    private void innitDataForQuery() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        usersListData = new ArrayList<>();
        enabledChatUsersData = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new UserRecycleAdapter(usersListData, enabledChatUsersData);
        query = FirebaseDatabase.getInstance().getReferenceFromUrl(Const.REF_USERS).orderByChild(Const.QUERY_NAME_KEY);
        queryChatID = FirebaseDatabase.getInstance().getReference(Const.CHAT_ID_TABLE).child(currentUserID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        bindFragment(this, view);
        usersRecView.setLayoutManager(layoutManager);
        usersRecView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        state = layoutManager.onSaveInstanceState();
        outState.putParcelable(Const.SCROLL_POSITION_KEY, state);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            state = savedInstanceState.getParcelable(Const.SCROLL_POSITION_KEY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (query != null && queryChatID != null){
            query.addChildEventListener(usersInfoListener);
            queryChatID.addChildEventListener(chatIDTableListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (query != null && queryChatID != null){
            query.removeEventListener(usersInfoListener);
            queryChatID.removeEventListener(chatIDTableListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (state != null){
            usersRecView.getLayoutManager().onRestoreInstanceState(state);
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
