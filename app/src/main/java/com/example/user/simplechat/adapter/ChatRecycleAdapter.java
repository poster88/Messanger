package com.example.user.simplechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.chatItemView.setText(messageArray.get(position).getMessageText());
    }

    @Override
    public int getItemCount() {
        return messageArray.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.chatItem) TextView chatItemView;

        ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
