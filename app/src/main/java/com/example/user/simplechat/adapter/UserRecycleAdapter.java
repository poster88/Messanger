package com.example.user.simplechat.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.user.simplechat.R;
import com.example.user.simplechat.model.User;
import com.example.user.simplechat.utils.CircleTransform;
import com.example.user.simplechat.utils.Const;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by POSTER on 07.11.2017.
 */

public class UserRecycleAdapter extends RecyclerView.Adapter<UserRecycleAdapter.UserViewHolder>{
    private ArrayList<User> usersListData;

    public UserRecycleAdapter(ArrayList<User> usersListData) {
        this.usersListData = usersListData;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        setUserImage(holder, usersListData.get(position));
        holder.userName.setText(usersListData.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return usersListData.size();
    }

    private void setUserImage(UserViewHolder holder, User model) {
        if (model.getImageUrl() == null || model.getImageUrl().equals(Const.DEFAULT_IMAGE_KEY)){
            setImageDefault(holder.userImage, holder.progressBar);
            return;
        }
        setRoundImageToView(Uri.parse(model.getImageUrl()), holder.userImage, holder.progressBar);
    }

    private void setRoundImageToView(Uri uri, final ImageView view, final ProgressBar progressBar) {
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
                .bitmapTransform(new CircleTransform(view.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(view);
    }

    private void setImageDefault(ImageView imageView, ProgressBar progressBar) {
        imageView.setImageResource(R.drawable.user_anonymous);
        if (progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.GONE);
        }
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.itemUserImage) ImageView userImage;
        @BindView(R.id.itemUserName) TextView userName;
        @BindView(R.id.progressBarItemList) ProgressBar progressBar;
        @BindView(R.id.chatStatus) TextView chatStatus;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
