package com.example.user.simplechat.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 003 03.11.17.
 */

public class ChatTable {
    private String receiverID;
    private String chatID;

    public ChatTable() {
    }

    public ChatTable(String chatID, String receiverID) {
        this.chatID = chatID;
        this.receiverID = receiverID;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> chatLinks = new HashMap<>();
        chatLinks.put(receiverID, chatID);
        return chatLinks;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChatID() {
        return chatID;
    }
}
