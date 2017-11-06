package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.simplechat.R;
import com.example.user.simplechat.adapter.FirebaseUserAdapter;
import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment implements FirebaseUserAdapter.MyClickListener{
    @BindView(R.id.userRecycleView) RecyclerView usersRecView;

    private FirebaseUserAdapter usersListAdapter;

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            setRetainInstance(true);
            String currentUserID = FirebaseAuth.getInstance().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messager-c419d.firebaseio.com/ChatIDTable/" + currentUserID);
            /*ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (Map.Entry<String, String> data: ((Map<String, String>) dataSnapshot.getValue()).entrySet()) {
                        usersIDArray.add(data.getKey());
                        System.out.println("has chat with : " + data.getKey());
                    }
                    usersListAdapter = new FirebaseUserAdapter(setFirebaseRecyclerOptions(), FirebaseAuth.getInstance().getUid(), usersIDArray);
                    usersListAdapter.setMyOnClickListener(ChatListFragment.this);
                    usersListAdapter.startListening();
                    //usersListAdapter.notifyItemChanged();
                    setRecycleView();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/
            ref.addValueEventListener(new ValueListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    super.onDataChange(dataSnapshot);
                    ArrayList<String> usersIDArray = new ArrayList<>();
                    for (Map.Entry<String, String> data: ((Map<String, String>) dataSnapshot.getValue()).entrySet()) {
                        usersIDArray.add(data.getKey());
                        System.out.println("has chat with ***********: " + data.getKey());
                        usersListAdapter = new FirebaseUserAdapter(setFirebaseRecyclerOptions(), FirebaseAuth.getInstance().getUid(), usersIDArray);
                        usersListAdapter.setMyOnClickListener(ChatListFragment.this);
                        usersListAdapter.startListening();
                        ObservableSnapshotArray<User> list = usersListAdapter.getSnapshots();
                        //usersListAdapter.notifyItemChanged();
                        setRecycleView();
                    }
                }
            });
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> usersIDArray = new ArrayList<>();
                    for (Map.Entry<String, String> data: ((Map<String, String>) dataSnapshot.getValue()).entrySet()) {
                        usersIDArray.add(data.getKey());
                        System.out.println("has chat with : " + data.getKey());
                    }
                   /* usersListAdapter = new FirebaseUserAdapter(setFirebaseRecyclerOptions(), FirebaseAuth.getInstance().getUid(), usersIDArray);
                    usersListAdapter.setMyOnClickListener(ChatListFragment.this);
                    usersListAdapter.startListening();
                    //usersListAdapter.notifyItemChanged();
                    setRecycleView();*/
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        bindFragment(this, view);
        if (usersListAdapter != null){
            setRecycleView();
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        usersListAdapter.stopListening();
    }

    private FirebaseRecyclerOptions<User> setFirebaseRecyclerOptions() {
        Query query = FirebaseDatabase.getInstance().getReferenceFromUrl(Const.REF_USERS)/*.orderByChild(Const.QUERY_NAME_KEY)*/;
        return new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
    }

    private void setRecycleView() {
        usersRecView.setLayoutManager(new LinearLayoutManager(getActivity()));
        usersRecView.setAdapter(usersListAdapter);
    }

    @Override
    public void onItemClick(String userID) {
        super.replaceFragments(ChatFragment.newInstance(userID), Const.CHAT_FRAG_TAG);
    }

    @Override
    public void updateItem(int position) {
        System.out.println(position);
        //usersListAdapter.notifyItemChanged(position);
        //usersListAdapter.notifyDataSetChanged();
        //usersListAdapter.notifyItemChanged(position);
        //usersListAdapter.notifyItemChanged(position);
        //usersRecView.getAdapter().notifyItemChanged(position);
    }
}
