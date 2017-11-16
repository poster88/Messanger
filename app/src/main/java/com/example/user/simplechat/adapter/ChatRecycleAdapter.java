package com.example.user.simplechat.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.simplechat.R;
import com.example.user.simplechat.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by POSTER on 09.11.2017.
 */

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ChatViewHolder>{
    private final int VIEW_TYPE_LEFT = 0;
    private final int VIEW_TYPE_RIGHT = 1;
    private ArrayList<Message> messageArray;
    private String currentID;
    private Bitmap myPhoto;
    private Bitmap receiverPhoto;

    public ChatRecycleAdapter(ArrayList<Message> messageArray, String currentID, Bitmap myPhoto, Bitmap receiverPhoto) {
        this.messageArray = messageArray;
        this.currentID = currentID;
        this.myPhoto = myPhoto;
        this.receiverPhoto = receiverPhoto;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0){
            view = inflater.inflate(R.layout.chat_item_left, parent, false);
        }else {
            view = inflater.inflate(R.layout.chat_item_right, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (messageArray.get(position).getAuthorID().equals(currentID)){
            viewType = VIEW_TYPE_LEFT;
        }else {
            viewType = VIEW_TYPE_RIGHT;
        }
        return viewType;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        getItemViewType(position);
        holder.senderMessageView.setText(messageArray.get(position).getMessageText());
        holder.messageTimeView.setText(formatTime(messageArray.get(position).getMessageTime()));
        holder.userPhotoView.setImageBitmap(checkBitmapForUser(currentID, messageArray.get(position).getAuthorID()));
    }

    private String formatTime(long time) {
        return new SimpleDateFormat("M.dd 'at' HH:mm").format(time);
    }

    @Override
    public int getItemCount() {
        return messageArray == null ? 0 : messageArray.size();
    }

    private Bitmap checkBitmapForUser(String currentID, String userID){
        if (!currentID.equals(userID)){
            return receiverPhoto;
        }
        return myPhoto;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.senderMessage) TextView senderMessageView;
        @BindView(R.id.itemUserPhoto) CircleImageView userPhotoView;
        @BindView(R.id.messageTime) TextView messageTimeView;
        @BindView(R.id.chatLayout) RelativeLayout chatLayout;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
