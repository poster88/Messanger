package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.simplechat.R;
import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by POSTER on 02.11.2017.
 */

public class ChatFragment extends BaseFragment{
    private String receiverID;
    private FirebaseDatabase database;

    public static ChatFragment newInstance(String receiverID){
        ChatFragment cf = new ChatFragment();

        Bundle args = new Bundle();
        args.putString(Const.RECEIVER_ID, receiverID);
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            receiverID = getArguments().getString(Const.RECEIVER_ID);
            database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(Const.CHAT_ID_TABLE).child(FirebaseAuth.getInstance().getUid());
            ref.addListenerForSingleValueEvent(new ValueListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            //add
                        }
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }
}
