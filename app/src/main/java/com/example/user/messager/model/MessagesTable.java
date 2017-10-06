package com.example.user.messager.model;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 005 05.10.17.
 */

public class MessagesTable {
    private Map<String, Map<String, Map<String, Message>>> ReceiverList;
    private Map<String, Map<String, Message>> receiverChat;
    private Map<String, Message> messages;


    public MessagesTable() {

    }

    public MessagesTable(Map<String, Map<String, Map<String, Message>>> receiverList) {
        ReceiverList = receiverList;
    }

    public Map<String, Map<String, Map<String, Message>>> ReceiverListToMap(String currentUserID, Map<String, Map<String, Message>> receiverChat){
        Map<String, Map<String, Map<String, Message>>> result = new HashMap<>();
        result.put(currentUserID, receiverChat);
        return result;
    }

    //public void showChat(String currentUserID, String receiverUserID, )

    public void setReceiverList(Map<String, Map<String, Map<String, Message>>> receiverList) {
        ReceiverList = receiverList;
    }

    public Map<String, Map<String, Map<String, Message>>> getReceiverList() {
        return ReceiverList;
    }
}
