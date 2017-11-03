package com.example.user.simplechat.model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 003 03.11.17.
 */

public class ChatTable {
    private String senderID;
    private String receiverID;
    private DatabaseReference ref;

    public ChatTable(DatabaseReference ref, String senderID, String receiverID) {
        this.ref = ref;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    private String genChatLink(){
        return ref.push().getKey();
    }

    public Map<String, Object> toMap(){
        Map<String, Object> chatLinks = new HashMap<>();
        chatLinks.put(receiverID, genChatLink());

        return chatLinks;
    }

    public void updateChildren(Map<String, Object> map){
        ref.updateChildren(map);
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setRef(DatabaseReference ref) {
        this.ref = ref;
    }

    public DatabaseReference getRef() {
        return ref;
    }
}
