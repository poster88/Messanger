package com.example.user.simplechat.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by User on 005 05.10.17.
 */

public class Message {
    private String authorID;
    private String messageText;
    private String messageTime;

    public Message() {
    }

    public Message(String authorID, String messageText) {
        this.authorID = authorID;
        this.messageText = messageText;
        this.messageTime = setMessageTime();
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
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
