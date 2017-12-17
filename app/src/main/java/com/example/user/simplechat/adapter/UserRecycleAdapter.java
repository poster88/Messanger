package com.example.user.simplechat.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.simplechat.R;
import com.example.user.simplechat.fragment.impl.MyClickListener;
import com.example.user.simplechat.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by POSTER on 07.11.2017.
 */

public class UserRecycleAdapter extends RecyclerView.Adapter<UserRecycleAdapter.UserViewHolder> {
    private ArrayList<User> usersListData;
    private ArrayList<String> enabledChatUsersData;
    private MyClickListener myClickListener;

    public UserRecycleAdapter(ArrayList<User> usersListData, ArrayList<String> enabledChatUsersData) {
        this.usersListData = usersListData;
        this.enabledChatUsersData = enabledChatUsersData;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        setUserImage(holder, usersListData.get(position));
        holder.userName.setText(usersListData.get(position).getUserName());
        holder.chatStatus.setText(setChatEnabledStatus(position));
        holder.onlineStatus.setImageResource(setOnlineStatusToImage(usersListData.get(position).getIsOnline()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClickListener.onItemClick(
                        usersListData.get(holder.getAdapterPosition()).getUserID(),
                        setByteArrayFromImage(holder.userImage)
                );
            }
        });
    }

    public byte[] setByteArrayFromImage(CircleImageView userImage){
        userImage.setDrawingCacheEnabled(true);
        userImage.buildDrawingCache();
        Bitmap bitmap = userImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private int setOnlineStatusToImage(boolean isOnline) {
        return isOnline ? R.drawable.ic_is_online_24dp : R.drawable.ic_is_offline_24dp;
    }

    private String setChatEnabledStatus(int position){
        return enabledChatUsersData.contains(usersListData.get(position).getUserID()) ? "continue chat" : "no chat";
    }

    @Override
    public int getItemCount() {
        return usersListData.size();
    }

    private void setUserImage(UserViewHolder holder, User model) {
        if (model.getImageUrl() == null){
            setImageDefault(holder.userImage, holder.progressBar);
            return;
        }
        setRoundImageToView(Uri.parse(model.getImageUrl()), holder.userImage, holder.progressBar);
    }

    private void setRoundImageToView(Uri uri, final CircleImageView view, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(view.getContext())
                .load(uri)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        setImageDefault(view, progressBar);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        view.setImageDrawable(resource);
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    private void setImageDefault(CircleImageView imageView, ProgressBar progressBar) {
        imageView.setImageResource(R.drawable.user_anonymous);
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemUserImage) CircleImageView userImage;
        @BindView(R.id.itemUserName) TextView userName;
        @BindView(R.id.progressBarItemList) ProgressBar progressBar;
        @BindView(R.id.chatStatus) TextView chatStatus;
        @BindView(R.id.onlineStatus) CircleImageView onlineStatus;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
