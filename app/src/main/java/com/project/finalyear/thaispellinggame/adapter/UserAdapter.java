package com.project.finalyear.thaispellinggame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.model.UserData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Namwan on 11/23/2017.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserData> list;
    public final String img_profile_default_url = "https://firebasestorage.googleapis.com/v0/b/thaispellinggame-28cfe.appspot.com/o/Profile_Images%2Fdefault_profile_pic.png?alt=media&token=e7b8453d-82dd-431a-a93f-fb793081359b";

    public UserAdapter(List<UserData> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_layout,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        UserData data = list.get(position);
        holder.mName.setText(data.getName());

        if (data.getImage() != null) {
            if (data.getImage().equals("default_profile_pic")) {
                Picasso.with(holder.mImage.getContext())
                        .load(img_profile_default_url)
                        .resize(70, 70)
                        .centerCrop()
                        .into(holder.mImage);
            } else {
                Picasso.with(holder.mImage.getContext())
                        .load(data.getImage())
                        .resize(70, 70)
                        .centerCrop()
                        .into(holder.mImage);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        ImageView mImage,defaultImg;

        public ViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.user_single_name);
            mImage = (ImageView) itemView.findViewById(R.id.user_single_image);
            defaultImg = (ImageView) itemView.findViewById(R.id.user_single_image);
        }
    }
}
