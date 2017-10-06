package com.example.user.messager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.messager.model.Message;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static String CHAT_ID_TABLE = "ChatIDTable";
    public static String CHAT_ARCHIVE = "ChatArchive";
    private final String[] users = {
            "user_id_1",
            "user_id_2",
            "user_id_3",
            "user_id_4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Map<String, Message> messages = new HashMap<>();
        messages.put(database.getReference().push().getKey(), new Message("author", "message_text"));
        messages.put(database.getReference().push().getKey(), new Message("author", "message_text"));
        messages.put(database.getReference().push().getKey(), new Message("author", "message_text"));
        messages.put(database.getReference().push().getKey(), new Message("author", "message_text"));

        Map<String, Map<String, Message>> receiverChat1 = new HashMap<>();
        receiverChat1.put(users[1], messages);
        receiverChat1.put(users[2], messages);
        receiverChat1.put(users[3], messages);

        Map<String, Map<String, Message>> receiverChat2 = new HashMap<>();
        receiverChat2.put(users[0], messages);
        receiverChat2.put(users[2], messages);
        receiverChat2.put(users[3], messages);

        Map<String, Map<String, Message>> receiverChat3 = new HashMap<>();
        receiverChat3.put(users[0], messages);
        receiverChat3.put(users[3], messages);
        receiverChat3.put(users[1], messages);

        Map<String, Map<String, Message>> receiverChat4 = new HashMap<>();
        receiverChat4.put(users[1], messages);
        receiverChat4.put(users[2], messages);
        receiverChat4.put(users[0], messages);

        Map<String, Map<String, Map<String, Message>>> receiversList = new HashMap<>();
        receiversList.put(users[0], receiverChat1);
        receiversList.put(users[1], receiverChat2);
        receiversList.put(users[2], receiverChat3);
        receiversList.put(users[3], receiverChat4);*/

        Map<String, String> receivers = new HashMap<>();
        receivers.put(users[1], "chat_link");

        Map<String, Object> userChats = new HashMap<>();
        userChats.put(users[0], receivers);

        //database.getReference(CHAT_ID_TABLE).updateChildren(userChats);
        /*database.getReference(MESSAGES_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //MessagesTable messagesTable = new MessagesTable((Map<String, Map<String, Map<String, Message>>>) dataSnapshot.getValue());
                //System.out.println(messagesTable.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });*/

        //showChatWithUser("current_user_id_1", "receiver_id_3");
        //sendMessageToUser("user_id_2", "user_id_1", "test text from user_id_2");
        showMyChatList();
    }

    /*public void showChatWithUser(String senderID, String receiverID){
        database.getReference(MESSAGES_TABLE).child(senderID).child(receiverID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public void sendMessageToUser(String senderID, String receiverID, String text){
        Message message = new Message(senderID, text);
        //database.getReference(MESSAGES_TABLE).child(senderID).child(receiverID).push().setValue(message);
        //database.getReference(MESSAGES_TABLE).child(receiverID).child(senderID).push().setValue(message);
    }

    public void showMyChatList(){
        database.getReference(CHAT_ID_TABLE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    System.out.println("chat with " + data.getKey() + " is exist");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
