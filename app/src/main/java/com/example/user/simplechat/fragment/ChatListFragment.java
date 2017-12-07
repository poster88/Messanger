package com.example.user.simplechat.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by User on 011 11.10.17.
 */

public class ChatListFragment extends BaseFragment implements UserRecycleAdapter.MyClickListener, TaskListener{
    @BindView(R.id.userRecycleView) RecyclerView usersRecView;

    private boolean isTaskIsRunning = false;
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

    private ValueListener imageUrlListener = new ValueListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                String link = dataSnapshot.getValue(User.class).getImageUrl();
                if (link != null){
                    setDataForAsyncTask(link);
                }
            }
        }
    };

    private ChildValueListener chatIDTableListener = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot.exists() && !enabledChatUsersData.contains(dataSnapshot.getKey())){
                enabledChatUsersData.add(dataSnapshot.getKey());
                updateAdapterItems(dataSnapshot.getKey());
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            enabledChatUsersData.remove(dataSnapshot.getKey());
        }
    };

    private void setDataForAsyncTask(String link) {
        try {
            URL url = new URL(link);
            DownloadImageTask task = new DownloadImageTask(ChatListFragment.this);
            task.execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void updateAdapterItems(String data){
        for (int i = 0; i < usersListData.size(); i++) {
            if (usersListData.get(i).getUserID().equals(data)){
                adapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        innitDataForQuery();
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
        checkUserImage();
        if (savedInstanceState == null){
            innitAdapter();
        }
        return view;
    }

    private void checkUserImage() {
        if (!isTaskIsRunning && myArrayImage == null){
            getUserImageUri();
        }
    }

    public void getUserImageUri(){
        database.getReference(Const.USER_INFO).child(currentUserID)
                .addListenerForSingleValueEvent(imageUrlListener);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Const.USER_LIST_DATA_KEY, usersListData);
        outState.putStringArrayList(Const.CHAT_ID_TABLE_DATA_KEY, enabledChatUsersData);
        outState.putParcelable(Const.LAYOUT_MANAGER_KEY, layoutManager.onSaveInstanceState());
        outState.putBoolean(Const.IS_DIALOG_RUNNING_KEY, isTaskIsRunning);
        outState.putByteArray(Const.MY_PHOTO_B_KEY, myArrayImage);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            usersListData = savedInstanceState.getParcelableArrayList(Const.USER_LIST_DATA_KEY);
            enabledChatUsersData = savedInstanceState.getStringArrayList(Const.CHAT_ID_TABLE_DATA_KEY);
            isTaskIsRunning = savedInstanceState.getBoolean(Const.IS_DIALOG_RUNNING_KEY);
            if (isTaskIsRunning){
                myArrayImage = savedInstanceState.getByteArray(Const.MY_PHOTO_B_KEY);

            }
            layoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Const.LAYOUT_MANAGER_KEY));
            innitAdapter();
        }
    }

    public void innitAdapter(){
        adapter = new UserRecycleAdapter(usersListData, enabledChatUsersData);
        adapter.setMyClickListener(ChatListFragment.this);
        usersRecView.setLayoutManager(layoutManager);
        usersRecView.setAdapter(adapter);
        usersRecView.addItemDecoration(setItemDecoration());
    }

    private DividerItemDecoration setItemDecoration() {
        return new DividerItemDecoration(usersRecView.getContext(), layoutManager.getOrientation());
    }

    @Override
    public void onItemClick(final String userID, final  byte[] recPhotoArray) {
        chatTableRef.child(userID).addListenerForSingleValueEvent(new ValueListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setDataForFragment(userID, recPhotoArray, dataSnapshot);
            }
        });
    }

    private void setDataForFragment(String userID, byte[] recPhotoArray, DataSnapshot dataSnapshot){
        ChatListFragment.super.replaceFragments(
                ChatFragment.newInstance(userID, checkDataSnapshot(dataSnapshot, chatTableRef, userID), myArrayImage, recPhotoArray),
                Const.CHAT_FRAG_TAG);
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

    @Override
    public void onTaskStarted() {
        isTaskIsRunning = true;
        System.out.println("task is started ! task is " + isTaskIsRunning);
    }

    @Override
    public void onTaskFinished(Bitmap result) {
        isTaskIsRunning = false;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        myArrayImage = baos.toByteArray();
        System.out.println("task is finish ! task is " + isTaskIsRunning);
    }

    public class DownloadImageTask extends AsyncTask<URL, Void, Bitmap> {
        private TaskListener taskListener;

        public DownloadImageTask(TaskListener taskListener) {
            this.taskListener = taskListener;
        }

        @Override
        protected void onPreExecute() {
            taskListener.onTaskStarted();
        }

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
            taskListener.onTaskFinished(result);
        }
    }
}
