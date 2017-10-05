package com.example.user.messager.model;

/**
 * Created by User on 005 05.10.17.
 */

public class ChatModel {
    private String senderID;
    private String receiverID;
    private String chatID;

    public ChatModel() {
    }

    public ChatModel(String senderID, String receiverID, String chatID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.chatID = chatID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }
}
