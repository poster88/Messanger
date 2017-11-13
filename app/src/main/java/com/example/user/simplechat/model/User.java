package com.example.user.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 005 05.10.17.
 */

public class User implements Parcelable{
    private String userID;
    private String imageUrl;
    private String userName;
    private String userEmail;
    private boolean isOnline;

    public User() {
    }

    public User(String userID, String imageUrl, String userName, String userEmail, boolean isOnline) {
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.userEmail = userEmail;
        this.isOnline = isOnline;
    }

    public User(Parcel parcel) {
        userID = parcel.readString();
        imageUrl = parcel.readString();
        userName = parcel.readString();
        userEmail = parcel.readString();
        isOnline = parcel.readByte() != 0;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("userID", userID);
        userInfo.put("imageUrl", imageUrl);
        userInfo.put("userName", userName);
        userInfo.put("userEmail", userEmail);
        userInfo.put("isOnline", isOnline);

        Map<String, Object> result = new HashMap<>();
        result.put(userID, userInfo);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isOnline != user.isOnline) return false;
        if (userID != null ? !userID.equals(user.userID) : user.userID != null) return false;
        if (imageUrl != null ? !imageUrl.equals(user.imageUrl) : user.imageUrl != null)
            return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null)
            return false;
        return userEmail != null ? userEmail.equals(user.userEmail) : user.userEmail == null;
    }

    @Override
    public int hashCode() {
        int result = userID != null ? userID.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (isOnline ? 1 : 0);
        return result;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userID);
        parcel.writeString(imageUrl);
        parcel.writeString(userName);
        parcel.writeString(userEmail);
        parcel.writeByte((byte) (isOnline ? 1 : 0));
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>(){

        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int i) {
            return new User[i];
        }
    };
}
