package com.example.user.messager.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by User on 005 05.10.17.
 */

public class Message extends ChatModel{
    private String authorID;
    private String messageID;
    private String messageText;
    private String messageTime;

    public Message() {
    }

    public Message(String authorID, String messageID, String messageText, String messageTime) {
        this.authorID = authorID;
        this.messageID = messageID;
        this.messageText = messageText;
        this.messageTime = messageTime;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String setMessageTime() {
        messageTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        return messageTime;
    }
}
