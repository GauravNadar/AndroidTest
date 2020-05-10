package com.gauravnadar.androidtest.RecyclerViewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravnadar.androidtest.Model.GitHubUser;
import com.gauravnadar.androidtest.R;
import com.gauravnadar.androidtest.ScreenFragments.GitHubUsers;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import static java.security.AccessController.getContext;

public class GitHubUsersRecyclerAdapter extends RecyclerView.Adapter<GitHubUsersRecyclerViewHolder> {

    List<GitHubUser> list;
    Context cnt;
    OnItemListener onItemListener;

    //constructor for adapter
    public GitHubUsersRecyclerAdapter(List<GitHubUser> list, Context cnt) {
        this.list = list;
        this.cnt = cnt;
       // this.onItemListener = onItemListener;
    }

    //inflate layout and instantiate viewholder
    @NonNull
    @Override
    public GitHubUsersRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(cnt).inflate(R.layout.single_github_user,  null);

        GitHubUsersRecyclerViewHolder viewHolder = new GitHubUsersRecyclerViewHolder(view, onItemListener);
        return viewHolder;
    }

    //binding of values to screen views
    //piccass library used to load image on imageview
    @Override
    public void onBindViewHolder(@NonNull GitHubUsersRecyclerViewHolder holder, int position) {

        GitHubUser model = list.get(position);

        holder.git_login.setText(model.getLogin());

        Picasso.Builder builder = new Picasso.Builder(cnt);
        builder.downloader(new OkHttp3Downloader(cnt));

        builder.build().load(list.get(position).getAvatar_url())
                .error(R.drawable.ic_launcher_background)
                .into(holder.getGit_avatar());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);

    }

}


