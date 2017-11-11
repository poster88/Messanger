package com.example.user.simplechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.simplechat.R;
import com.example.user.simplechat.model.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by POSTER on 09.11.2017.
 */

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ChatViewHolder>{
    private ArrayList<Message> messageArray;
    private String currentID;

    public ChatRecycleAdapter(ArrayList<Message> messageArray, String currentID) {
        this.messageArray = messageArray;
        this.currentID = currentID;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.senderNameView.setText(messageArray.get(position).getAuthorID());
        holder.senderMessageView.setText(messageArray.get(position).getMessageText());
        holder.messageTimeView.setText(messageArray.get(position).getMessageTime());
        holder.chatLayout.setGravity(checkLayoutGravity(currentID, messageArray.get(position).getAuthorID()));
    }

    @Override
    public int getItemCount() {
        return messageArray == null ? 0 : messageArray.size();
    }

    private int checkLayoutGravity(String currentID, String userID){
        if (!currentID.equals(userID)){
            return Gravity.END;
        }
        return Gravity.NO_GRAVITY;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.senderName) TextView senderNameView;
        @BindView(R.id.senderMessage) TextView senderMessageView;
        @BindView(R.id.messageTime) TextView messageTimeView;
        @BindView(R.id.chatLayout) LinearLayout chatLayout;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
