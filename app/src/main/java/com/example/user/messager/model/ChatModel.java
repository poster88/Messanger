package com.example.user.messager.model;

import java.util.Map;

/**
 * Created by User on 005 05.10.17.
 */

public class ChatModel {
    private String receiverID;
    private Map<String, Message> messageList;

    public ChatModel() {
    }

    public ChatModel(String receiverID, Map<String, Message> messageList) {
        this.receiverID = receiverID;
        this.messageList = messageList;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public Map<String, Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(Map<String, Message> messageList) {
        this.messageList = messageList;
    }
}
