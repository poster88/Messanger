package com.example.user.messager.model;


import java.util.ArrayList;

/**
 * Created by User on 005 05.10.17.
 */

public class MessagesTable {
    private ChatUserInfo chatUserInfo;
    private Chats chats;


    private class ChatUserInfo {
        private ArrayList<ChatModel> chatModels;
    }

    private class Chats {
        private ArrayList<Message> messages;
    }
}
