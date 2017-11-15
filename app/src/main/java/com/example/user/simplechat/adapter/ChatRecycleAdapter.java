package com.example.user.simplechat.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.simplechat.R;
import com.example.user.simplechat.model.Message;
import com.example.user.simplechat.utils.CircleTransform;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by POSTER on 09.11.2017.
 */

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ChatViewHolder>{
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.senderMessageView.setText(messageArray.get(position).getMessageText());
        holder.messageTimeView.setText(formatTime(messageArray.get(position).getMessageTime()));
        //holder.userPhotoView.setImageBitmap(checkBitmapForUser(currentID, messageArray.get(position).getAuthorID()));
        holder.userPhotoView.setImageResource(R.drawable.user_anonymous);

    }

    public void setRoundImageToView(Uri uri, ImageView view) {
        Glide.with(view.getContext())
                .load(uri)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(view.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
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
        return receiverPhoto;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.senderMessage) TextView senderMessageView;
        @BindView(R.id.itemUserPhoto) ImageView userPhotoView;
        @BindView(R.id.messageTime) TextView messageTimeView;
        @BindView(R.id.chatLayout) RelativeLayout chatLayout;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
