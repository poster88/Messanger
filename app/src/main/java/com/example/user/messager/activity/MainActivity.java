package com.example.user.messager.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.user.messager.fragment.ChatListFragment;
import com.example.user.messager.fragment.LoginFragment;
import com.example.user.messager.listener.ChildValueListener;
import com.example.user.messager.R;
import com.example.user.messager.listener.ValueListener;
import com.example.user.messager.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity{
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String CHAT_ID_TABLE = "ChatIDTable";
    public static String CHAT_ARCHIVE = "ChatArchive";
    private final String[] users = {
            "user_id_1",
            "user_id_2",
            "user_id_3",
            "user_id_4"
    };

    private static String userID = null;
    private static String chatLink = null;
    private ArrayList<String> userChatList;

    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check user auth
        auth = FirebaseAuth.getInstance();
        checkUserAuth(auth.getCurrentUser());

        Map<String, String> receivers = new HashMap<>();
        receivers.put(users[0], "chat_link");

        Map<String, Object> userChats = new HashMap<>();
        userChats.put(users[1], receivers);

        Map<String, Object> messages = new HashMap<>();
        messages.put(database.getReference().push().getKey(), new Message(users[0], "text message to user_id_2"));
        messages.put(database.getReference().push().getKey(), new Message(users[0], "text message to user_id_2"));
        messages.put(database.getReference().push().getKey(), new Message(users[0], "text message to user_id_2"));

        Map<String, Object> chatArchive = new HashMap<>();
        chatArchive.put("chat_link", messages);

        userID = users[0];

        if (userID != null){
            showMyChatList();
        }else {
            System.out.println("register first!");
        }
        //database.getReference(CHAT_ARCHIVE).updateChildren(chatArchive);
        //database.getReference(CHAT_ID_TABLE).updateChildren(userChats);
        /*database.getReference(MESSAGES_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //MessagesTable messagesTable = new MessagesTable((Map<String, Map<String, Map<String, Message>>>) dataSnapshot.getValue());
                //System.out.println(messagesTable.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });*/
    }

    /*public void showChatWithUser(String senderID, String receiverID){
        database.getReference(MESSAGES_TABLE).child(senderID).child(receiverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public void sendMessageToUser(String senderID, String receiverID, String text){
        Message message = new Message(senderID, text);
        //database.getReference(MESSAGES_TABLE).child(senderID).child(receiverID).push().setValue(message);
        //database.getReference(MESSAGES_TABLE).child(receiverID).child(senderID).push().setValue(message);
    }


    private ChildValueListener getListData = new ChildValueListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            System.out.println(dataSnapshot + " " + s + " - onChildAdded");
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            System.out.println(dataSnapshot + " " + s + " - onChildChanged");
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            System.out.println(dataSnapshot + " " + " - onChildRemoved");
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            System.out.println(dataSnapshot + " " + s + " - onChildMoved");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            super.onCancelled(databaseError);
        }
    };

    private void innitChatList(){
        database.getReference(CHAT_ARCHIVE).child(chatLink).addChildEventListener(getListData);
    }

    private void showChatList(DataSnapshot data){
        Map<String, Object> chatMap = (Map<String, Object>) data.getValue();
        userChatList = new ArrayList<>();
        for (Map.Entry<String, Object> map: chatMap.entrySet()) {
            userChatList.add(map.getKey());
        }
    }

    private ValueListener showUserChatListListener = new ValueListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()){
                Object o = dataSnapshot.getValue(Object.class);
                showChatList(dataSnapshot);
                return;
            }
            System.out.println("no chat with " + userID);
        }
    };

    private void showMyChatList(){
        database.getReference(CHAT_ID_TABLE).child(userID).addListenerForSingleValueEvent(showUserChatListListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = auth.getCurrentUser();
        checkUserAuth(user);
    }

    private void checkUserAuth(FirebaseUser user) {
        Fragment fragment;
        if (user != null){
            fragment = new ChatListFragment();
        }else {
            fragment = new LoginFragment();
        }
        changeFragment(fragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
