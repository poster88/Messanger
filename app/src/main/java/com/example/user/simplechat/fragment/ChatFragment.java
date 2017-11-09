package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.simplechat.R;
import com.example.user.simplechat.listener.ChildValueListener;
import com.example.user.simplechat.model.ChatTable;
import com.example.user.simplechat.model.Message;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by POSTER on 02.11.2017.
 */

public class ChatFragment extends BaseFragment{
    private String receiverID;
    private String chatID;
    private FirebaseDatabase database;

    public static ChatFragment newInstance(String receiverID, String chatID){
        ChatFragment cf = new ChatFragment();

        Bundle args = new Bundle();
        args.putString(Const.RECEIVER_ID, receiverID);
        args.putString(Const.CHAT_ID, chatID);
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            receiverID = getArguments().getString(Const.RECEIVER_ID);
            chatID = getArguments().getString(Const.CHAT_ID, null);
            database = FirebaseDatabase.getInstance();
            if (chatID != null){
                DatabaseReference chatRef = database.getReference(Const.CHAT_ARCHIVE).child(chatID);
                chatRef.addChildEventListener(new ChildValueListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        super.onChildAdded(dataSnapshot, s);
                        System.out.println(dataSnapshot.toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        super.onChildChanged(dataSnapshot, s);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        super.onChildRemoved(dataSnapshot);
                    }
                });
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        return view;
    }

    private void createChatWithUser(String id){
        DatabaseReference ref = database.getReference(Const.CHAT_ID_TABLE).child(FirebaseAuth.getInstance().getUid());
        ChatTable chatTable = new ChatTable(ref, FirebaseAuth.getInstance().getUid(), receiverID);
        chatTable.updateChildren(chatTable.toMap());
    }

    private void createFakeDialog(){
        DatabaseReference chatCreate = FirebaseDatabase.getInstance().getReference(Const.CHAT_ARCHIVE);
        Map<String, Object> map = new HashMap();
        Map<String, Object> mapMessage = new HashMap<>();
        mapMessage.put(chatCreate.push().getKey(), new Message(" get id", "new message: Hello"));
        map.put(chatCreate.push().getKey(), mapMessage);
        chatCreate.updateChildren(map);
    }
}
