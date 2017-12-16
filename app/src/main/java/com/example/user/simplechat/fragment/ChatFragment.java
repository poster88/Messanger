package com.example.user.simplechat.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.user.simplechat.R;
import com.example.user.simplechat.activity.BaseActivity;
import com.example.user.simplechat.adapter.ChatRecycleAdapter;
import com.example.user.simplechat.listener.ChildValueListener;
import com.example.user.simplechat.model.Message;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Timestamp;
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

    private String chatID;
    private FirebaseDatabase database;
    private Query messageQuery;
    private ArrayList<Message> messageArray;
    private LinearLayoutManager layoutManager;
    private ChatRecycleAdapter adapter;
    private DatabaseReference chatArchiveRef;
    private Bitmap myPhoto;
    private Bitmap receiverPhoto;


    public static ChatFragment newInstance(String receiverID, String chatID, byte[] recPhotoArray){
        ChatFragment cf = new ChatFragment();

        Bundle args = new Bundle();
        args.putString(Const.RECEIVER_ID, receiverID);
        args.putString(Const.CHAT_ID, chatID);
        args.putByteArray(Const.REC_PHOTO_B_KEY, recPhotoArray);
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
        innitPhotoArrays();
        messageQuery = database.getReference(Const.CHAT_ARCHIVE).child(chatID).limitToLast(25);
    }

    private void innitPhotoArrays() {
        SharedPreferences sPref = getContext().getSharedPreferences(Const.USER_DATA, Context.MODE_PRIVATE);
        String image = sPref.getString(Const.USER_IMAGE_KEY, null);
        if (image != null){
            byte[] myPhotoArray = Base64.decode(image, Base64.DEFAULT);
            myPhoto = BitmapFactory.decodeByteArray(myPhotoArray, 0, myPhotoArray.length);
        }
        byte[] receiverPhotoArray = getArguments().getByteArray(Const.REC_PHOTO_B_KEY);
        if (receiverPhotoArray != null){
            receiverPhoto = BitmapFactory.decodeByteArray(receiverPhotoArray, 0, receiverPhotoArray.length);
        }
    }

    private void innitDataForQuery() {
        messageArray = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        chatID = getArguments().getString(Const.CHAT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        bindFragment(this, view);
        layoutManager = new LinearLayoutManager(getContext());
        if (savedInstanceState != null){
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Const.LAYOUT_MANAGER_KEY));
            messageArray = savedInstanceState.getParcelableArrayList(Const.CHAT_LIST_DATA_KEY);
        }
        innitAdapter();
        return view;
    }

    private void innitAdapter() {
        adapter = new ChatRecycleAdapter(messageArray, ((BaseActivity) getActivity()).getAuth().getUid(), myPhoto, receiverPhoto);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
            sendMessage();
        }
    }

    private void sendMessage() {
        Map<String, Object> mapMessage = new HashMap<>();
        chatArchiveRef = database.getReference(Const.CHAT_ARCHIVE).child(chatID);
        mapMessage.put(chatArchiveRef.push().getKey(), setMessageData());
        chatArchiveRef.updateChildren(mapMessage);
        messageEditText.setText(null);
    }

    public Message setMessageData() {
        Message message = new Message();
        message.setAuthorID(((BaseActivity) getActivity()).getAuth().getUid());
        message.setMessageText(messageEditText.getText().toString());
        message.setMessageTime((new Timestamp(System.currentTimeMillis())).getTime());
        return message;
    }
}