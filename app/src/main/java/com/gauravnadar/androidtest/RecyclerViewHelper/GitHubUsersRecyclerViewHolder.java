package com.gauravnadar.androidtest.RecyclerViewHelper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravnadar.androidtest.R;

public class GitHubUsersRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView git_login;
    ImageView git_avatar;
    GitHubUsersRecyclerAdapter.OnItemListener onItemListener;

    //casting views with ids
    public GitHubUsersRecyclerViewHolder(@NonNull View itemView, final GitHubUsersRecyclerAdapter.OnItemListener onItemListener) {
        super(itemView);

        git_login = (TextView) itemView.findViewById(R.id.git_login);
        git_avatar = (ImageView) itemView.findViewById(R.id.git_avatar);
        this.onItemListener = onItemListener;

        //on click listener for single list item
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //getters and setters
    public TextView getGit_login() {
        return git_login;
    }

    public void setGit_login(TextView git_login) {
        this.git_login = git_login;
    }

    public ImageView getGit_avatar() {
        return git_avatar;
    }

    public void setGit_avatar(ImageView git_avatar) {
        this.git_avatar = git_avatar;
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(getAdapterPosition());
    }
}
