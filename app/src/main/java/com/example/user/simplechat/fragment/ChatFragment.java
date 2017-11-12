package com.example.user.simplechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.simplechat.R;
import com.example.user.simplechat.adapter.ChatRecycleAdapter;
import com.example.user.simplechat.listener.ChildValueListener;
import com.example.user.simplechat.model.Message;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by POSTER on 02.11.2017.
 */

public class ChatFragment extends BaseFragment{
    @BindView(R.id.chatContainer) RecyclerView chatRecyclerView;
    @BindView(R.id.messageArea) EditText messageEditText;

    private String currentID;
    private String chatID;
    private FirebaseDatabase database;
    private Query messageQuery;
    private ArrayList<Message> messageArray;
    private LinearLayoutManager layoutManager;
    private ChatRecycleAdapter adapter;
    private DatabaseReference chatArchiveRef;


    public static ChatFragment newInstance(String receiverID, String chatID){
        ChatFragment cf = new ChatFragment();

        Bundle args = new Bundle();
        args.putString(Const.RECEIVER_ID, receiverID);
        args.putString(Const.CHAT_ID, chatID);
        cf.setArguments(args);
        return cf;
    }

    private ChildValueListener chatDataListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!messageArray.contains(dataSnapshot.getValue(Message.class))){
                messageArray.add(dataSnapshot.getValue(Message.class));
                adapter.notifyItemInserted(messageArray.size());
                layoutManager.scrollToPosition(messageArray.size() - 1);
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        innitDataForQuery();
        messageQuery = database.getReference(Const.CHAT_ARCHIVE).child(chatID).limitToLast(25);
    }

    private void innitDataForQuery() {
        messageArray = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        currentID = FirebaseAuth.getInstance().getUid();
        chatID = getArguments().getString(Const.CHAT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        bindFragment(this, view);
        layoutManager = new LinearLayoutManager(getContext());
        if (savedInstanceState == null) innitAdapter();
        return view;
    }

    private void innitAdapter() {
        adapter = new ChatRecycleAdapter(messageArray, currentID);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Const.LAYOUT_MANAGER_KEY));
            messageArray = savedInstanceState.getParcelableArrayList(Const.CHAT_LIST_DATA_KEY);
            innitAdapter();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (messageQuery != null){
            messageQuery.addChildEventListener(chatDataListener);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Const.LAYOUT_MANAGER_KEY, layoutManager.onSaveInstanceState());
        outState.putParcelableArrayList(Const.CHAT_LIST_DATA_KEY, messageArray);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (messageQuery != null) {
            messageQuery.removeEventListener(chatDataListener);
        }
    }

    @OnClick(R.id.sendMassageBtn)
    public void setMessage(){
        if (messageEditText.length() != 0){
            Map<String, Object> mapMessage = new HashMap<>();
            chatArchiveRef = database.getReference(Const.CHAT_ARCHIVE).child(chatID);
            mapMessage.put(chatArchiveRef.push().getKey(), new Message(currentID, messageEditText.getText().toString()));
            chatArchiveRef.updateChildren(mapMessage);
            messageEditText.setText(null);
        }
    }
}
