package com.gauravnadar.androidtest.RecyclerViewHelper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gauravnadar.androidtest.R;

public class LocalUsersRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView local_name, local_email, local_phone;
    ImageView local_pic;
    LocalUsersRecyclerAdapter.OnItemListener onItemListener;

    //casting of screen ui elements
    public LocalUsersRecyclerViewHolder(@NonNull View itemView, final LocalUsersRecyclerAdapter.OnItemListener onItemListener) {
        super(itemView);

        local_name = (TextView) itemView.findViewById(R.id.local_name);
        local_pic = (ImageView) itemView.findViewById(R.id.local_pic);
        local_email = (TextView) itemView.findViewById(R.id.local_email);
        local_phone = (TextView) itemView.findViewById(R.id.local_phone);
        this.onItemListener = onItemListener;
    }


    //getters and setters
    public TextView getLocal_name() {
        return local_name;
    }

    public void setLocal_name(TextView local_name) {
        this.local_name = local_name;
    }

    public ImageView getLocal_pic() {
        return local_pic;
    }

    public void setLocal_pic(ImageView local_pic) {
        this.local_pic = local_pic;
    }

    public TextView getLocal_email() {
        return local_email;
    }

    public void setLocal_email(TextView local_email) {
        this.local_email = local_email;
    }

    public TextView getLocal_phone() {
        return local_phone;
    }

    public void setLocal_phone(TextView local_phone) {
        this.local_phone = local_phone;
    }
}
