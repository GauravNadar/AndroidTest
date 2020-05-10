package com.gauravnadar.androidtest.RecyclerViewHelper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravnadar.androidtest.Model.GitHubUser;
import com.gauravnadar.androidtest.Model.User;
import com.gauravnadar.androidtest.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LocalUsersRecyclerAdapter extends RecyclerView.Adapter<LocalUsersRecyclerViewHolder> {


    List<User> list;
    Context cnt;
    LocalUsersRecyclerAdapter.OnItemListener onItemListener;

    //constructor
    public LocalUsersRecyclerAdapter(List<User> list, Context cnt) {
        this.list = list;
        this.cnt = cnt;
    }

    //layout inflator and viewholder instance
    @NonNull
    @Override
    public LocalUsersRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(cnt).inflate(R.layout.single_local_user,  null);

        LocalUsersRecyclerViewHolder viewHolder = new LocalUsersRecyclerViewHolder(view, onItemListener);
        return viewHolder;
    }

    //bining ui views with data
    @Override
    public void onBindViewHolder(@NonNull LocalUsersRecyclerViewHolder holder, int position) {

        User model = list.get(position);

        holder.local_name.setText(model.getName());

       holder.local_email.setText(model.getEmail());
       holder.local_phone.setText(String.valueOf(model.getPhone()));


       //display imsge on imageview
        //load image path from user object
            try {
                File f=new File(model.getPic(), ""+model.getEmail()+".jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.local_pic.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemListener{
        void onItemClick(int position);

    }

}
