package com.project.finalyear.thaispellinggame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.model.RankData;
import com.project.finalyear.thaispellinggame.model.RankModel;
import com.project.finalyear.thaispellinggame.model.RoundOneModel;
import com.project.finalyear.thaispellinggame.model.UserData;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<RankData> rankData;
    public final String img_profile_default_url = "https://firebasestorage.googleapis.com/v0/b/thaispellinggame-28cfe.appspot.com/o/Profile_Images%2Fdefault_profile_pic.png?alt=media&token=e7b8453d-82dd-431a-a93f-fb793081359b";

    public RankAdapter(List<RankData> rankData) {
        this.rankData = rankData;
    }

    @Override
    public RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rank, parent, false);

        RankViewHolder viewHolder = new RankViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {

        RankData data = rankData.get(position);
        holder.mName.setText(data.getName());
        holder.mScore.setText(String.valueOf(data.getScore()));
        holder.mLevel.setText(String.valueOf(data.getLevel()));


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

    @Override
    public int getItemCount() {
        return rankData.size();
    }

    public class RankViewHolder extends RecyclerView.ViewHolder {

        TextView mName;
        TextView mScore;
        TextView mLevel;
        ImageView mImage, defaultImg;
        TextView rank;

        public RankViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.user_name);
            mScore = (TextView) itemView.findViewById(R.id.user_score);
            mLevel = (TextView) itemView.findViewById(R.id.user_level);
            mImage = (ImageView) itemView.findViewById(R.id.user_image);
            defaultImg = (ImageView) itemView.findViewById(R.id.user_single_image);
            rank = (TextView) itemView.findViewById(R.id.rank);


        }
    }
}
