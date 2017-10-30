package com.example.user.simplechat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.simplechat.R;
import com.example.user.simplechat.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 030 30.10.17.
 */

public class FirebaseUserAdapter extends FirebaseRecyclerAdapter<User, FirebaseUserAdapter.UserViewHolder> {
    private int resource;

    public FirebaseUserAdapter(FirebaseRecyclerOptions<User> options, int resource) {
        super(options);
        this.resource = resource;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(UserViewHolder holder, int position, User model) {
        holder.userName.setText(model.getUserName());
        holder.userImage.setImageResource(R.drawable.user_anonymous);
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemUserImage) ImageView userImage;
        @BindView(R.id.itemUserName) TextView userName;

        UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
