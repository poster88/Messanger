package com.example.user.messager.model;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap(){
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("userID", userID);
        userInfo.put("imageUrl", imageUrl);
        userInfo.put("userName", userName);
        userInfo.put("userEmail", userEmail);

        Map<String, Object> result = new HashMap<>();
        result.put(userID, userInfo);
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
}
