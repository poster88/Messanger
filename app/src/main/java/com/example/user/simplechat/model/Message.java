package com.example.user.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 005 05.10.17.
 */

public class Message implements Parcelable{
    private String authorID;
    private String messageText;
    private long messageTime;

    public Message() {
    }

    public Message(String authorID, String messageText, long messageTime) {
        this.authorID = authorID;
        this.messageText = messageText;
        this.messageTime = messageTime;
    }

    public Message(Parcel parcel) {
        authorID = parcel.readString();
        messageText = parcel.readString();
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public long getMessageTime() {
        return messageTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageTime != message.messageTime) return false;
        if (authorID != null ? !authorID.equals(message.authorID) : message.authorID != null)
            return false;
        return messageText != null ? messageText.equals(message.messageText) : message.messageText == null;
    }

    @Override
    public int hashCode() {
        int result = authorID != null ? authorID.hashCode() : 0;
        result = 31 * result + (messageText != null ? messageText.hashCode() : 0);
        result = 31 * result + (int) (messageTime ^ (messageTime >>> 32));
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
