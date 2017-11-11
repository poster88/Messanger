package com.example.user.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by User on 005 05.10.17.
 */

public class Message implements Parcelable{
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

    public Message(Parcel parcel) {
        authorID = parcel.readString();
        messageText = parcel.readString();
        messageTime = parcel.readString();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (authorID != null ? !authorID.equals(message.authorID) : message.authorID != null)
            return false;
        if (messageText != null ? !messageText.equals(message.messageText) : message.messageText != null)
            return false;
        return messageTime != null ? messageTime.equals(message.messageTime) : message.messageTime == null;
    }

    @Override
    public int hashCode() {
        int result = authorID != null ? authorID.hashCode() : 0;
        result = 31 * result + (messageText != null ? messageText.hashCode() : 0);
        result = 31 * result + (messageTime != null ? messageTime.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(authorID);
        parcel.writeString(messageText);
        parcel.writeString(messageTime);
    }

    public static final Parcelable.Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel parcel) {
            return new Message(parcel);
        }

        @Override
        public Message[] newArray(int i) {
            return new Message[i];
        }
    };
}
