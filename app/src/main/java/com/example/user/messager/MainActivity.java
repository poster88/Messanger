package com.example.user.messager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.messager.model.ChatModel;
import com.example.user.messager.model.Chats;
import com.example.user.messager.model.Message;
import com.example.user.messager.model.MessagesTable;
import com.example.user.messager.model.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String DB_NAME = "MessagesTable";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Message message = new Message();
        message.setMessageTime();


        User user1 = new User();
        user1.setUserID(database.getReference().getKey());
        user1.setImageUrl("test_url");
        user1.setUserName("test_name");

        User user2 = new User();
        user2.setUserID(database.getReference().getKey());
        user2.setImageUrl("test_url");
        user2.setUserName("test_name");

        ChatModel chatModel1 = new ChatModel();
        chatModel1.setChatID("test_chat_id_1");
        chatModel1.setSenderID("test_user_1_id");
        chatModel1.setReceiverID("test_user_2_id");

        ChatModel chatModel2 = new ChatModel();
        chatModel2.setChatID("test_chat_id_2");
        chatModel2.setSenderID("test_user_1_id");
        chatModel2.setReceiverID("test_user_2_id");

        ArrayList<ChatModel> chatModels = new ArrayList<>();
        chatModels.add(chatModel1);
        chatModels.add(chatModel2);

        Chats chats = new Chats();
        chats.setChatID("test_chat_id");

        ArrayList<Message> messages = new ArrayList<>();
        messages.add(new Message("user_1", "user_2", "message text 1", "time 1"));
        messages.add(new Message("user_2", "user_1", "message text 2", "time 2"));
        chats.setMessages(messages);

        ArrayList<Chats> chatsArrayList = new ArrayList<>();
        chatsArrayList.add(chats);

        MessagesTable messagesTable = new MessagesTable();
        messagesTable.setChatModels(chatModels);
        messagesTable.setChatsArrayList(chatsArrayList);

        database.getReference(DB_NAME).setValue(messagesTable);
    }
}
