package com.example.user.messager.model;

/**
 * Created by User on 005 05.10.17.
 */

public class User {
    private String userID;
    private String imageUrl;
    private String userName;
    private String userEmail;

    public User() {
    }

    public User(String userID, String imageUrl, String userName, String userEmail) {
        this.userID = userID;
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.userEmail = userEmail;
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
}
