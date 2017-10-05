package com.example.user.messager.model;


import java.util.ArrayList;

/**
 * Created by User on 005 05.10.17.
 */

public class MessagesTable {
    private ArrayList<ChatModel> chatModels;
    private ArrayList<Chats> chatsArrayList;

    public ArrayList<ChatModel> getChatModels() {
        return chatModels;
    }

    public void setChatModels(ArrayList<ChatModel> chatModels) {
        this.chatModels = chatModels;
    }

    public ArrayList<Chats> getChatsArrayList() {
        return chatsArrayList;
    }

    public void setChatsArrayList(ArrayList<Chats> chatsArrayList) {
        this.chatsArrayList = chatsArrayList;
    }
}
