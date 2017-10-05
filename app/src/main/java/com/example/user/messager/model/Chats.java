package com.example.user.messager.model;

import java.util.ArrayList;

/**
 * Created by User on 005 05.10.17.
 */

public class Chats {
    private String chatID;
    private ArrayList<Message> messages;

    public Chats() {
    }

    public Chats(String chatID, ArrayList<Message> messages) {
        this.chatID = chatID;
        this.messages = messages;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
