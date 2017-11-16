package com.example.user.simplechat.fragment;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.simplechat.R;
import com.example.user.simplechat.adapter.UserRecycleAdapter;
import com.example.user.simplechat.listener.ChildValueListener;
import com.example.user.simplechat.listener.ValueListener;
import com.example.user.simplechat.model.ChatTable;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;

/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment implements UserRecycleAdapter.MyClickListener{
    @BindView(R.id.userRecycleView) RecyclerView usersRecView;

    private String currentUserID;
    private ArrayList<User> usersListData;
    private ArrayList<String> enabledChatUsersData;
    private UserRecycleAdapter adapter;
    private LinearLayoutManager layoutManager;
    private DatabaseReference chatTableRef;
    private FirebaseDatabase database;
    private Query query;
    private byte[] myArrayImage;

    public static ChatListFragment newInstance(){
        return new ChatListFragment();
    }

    private ChildValueListener usersInfoListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!usersListData.contains(dataSnapshot.getValue(User.class))){
                if (!dataSnapshot.getValue(User.class).getUserID().equals(currentUserID)) {
                    usersListData.add(dataSnapshot.getValue(User.class));
                    adapter.notifyItemInserted(usersListData.size());
                }
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

    public void getUserImageUri(){
        DatabaseReference ref = database.getReference(Const.USER_INFO).child(currentUserID);
        ref.addListenerForSingleValueEvent(new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DownloadImageTask task = new DownloadImageTask();
                try {
                    URL url = new URL(dataSnapshot.getValue(User.class).getImageUrl());
                    task.execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(URL... params) {
            URL imageURL = params[0];
            Bitmap downloadedBitmap = null;
            try {
                downloadedBitmap  = BitmapFactory.decodeStream((InputStream)imageURL.getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return downloadedBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            myArrayImage = baos.toByteArray();
        }
    }


    private ChildValueListener chatIDTableListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (!enabledChatUsersData.contains(dataSnapshot.getKey())){
                enabledChatUsersData.add(dataSnapshot.getKey());
                for (int i = 0; i < usersListData.size(); i++) {
                    if (usersListData.get(i).getUserID().equals(dataSnapshot.getKey())){
                        adapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
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
        getUserImageUri();
    }

    private void innitDataForQuery() {
        currentUserID = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        usersListData = new ArrayList<>();
        enabledChatUsersData = new ArrayList<>();
        query = FirebaseDatabase.getInstance().getReferenceFromUrl(Const.REF_USERS).orderByChild(Const.QUERY_NAME_KEY);
        chatTableRef = database.getReference(Const.CHAT_ID_TABLE).child(currentUserID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        bindFragment(this, view);
        layoutManager = new LinearLayoutManager(getActivity());
        if (savedInstanceState == null){
            innitAdapter();
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Const.USER_LIST_DATA_KEY, usersListData);
        outState.putStringArrayList(Const.CHAT_ID_TABLE_DATA_KEY, enabledChatUsersData);
        outState.putParcelable(Const.LAYOUT_MANAGER_KEY, layoutManager.onSaveInstanceState());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            usersListData = savedInstanceState.getParcelableArrayList(Const.USER_LIST_DATA_KEY);
            enabledChatUsersData = savedInstanceState.getStringArrayList(Const.CHAT_ID_TABLE_DATA_KEY);
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Const.LAYOUT_MANAGER_KEY));
            innitAdapter();
        }
    }

    public void innitAdapter(){
        adapter = new UserRecycleAdapter(usersListData, enabledChatUsersData);
        adapter.setMyClickListener(ChatListFragment.this);
        usersRecView.setLayoutManager(layoutManager);
        usersRecView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(final String userID, final  byte[] recPhotoArray) {
        chatTableRef.child(userID).addListenerForSingleValueEvent(new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ChatListFragment.super.replaceFragments(
                        ChatFragment.newInstance(userID, checkDataSnapshot(dataSnapshot, chatTableRef, userID), myArrayImage, recPhotoArray),
                        Const.CHAT_FRAG_TAG
                );
            }
        });
    }

    private String checkDataSnapshot(DataSnapshot dataSnapshot, DatabaseReference ref, String receiverID) {
        String chatID = dataSnapshot.getValue(String.class);
        if (chatID == null){
            chatID = ref.push().getKey();
            createChatWithUser(currentUserID, receiverID, chatID);
            createChatWithUser(receiverID, currentUserID, chatID);
        }
        return chatID;
    }

    private void createChatWithUser(String currentID, String receiverID, String chatID){
        ChatTable chatTable = new ChatTable(chatID, receiverID);
        database.getReference(Const.CHAT_ID_TABLE).child(currentID).updateChildren(chatTable.toMap());
    }

    @Override
    public void onStart() {
        super.onStart();
        if (query != null && chatTableRef != null){
            query.addChildEventListener(usersInfoListener);
            chatTableRef.addChildEventListener(chatIDTableListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (query != null && chatTableRef != null){
            query.removeEventListener(usersInfoListener);
            chatTableRef.removeEventListener(chatIDTableListener);
        }
    }
}
