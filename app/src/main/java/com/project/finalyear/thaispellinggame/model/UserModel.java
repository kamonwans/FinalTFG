package com.project.finalyear.thaispellinggame.model;

/**
 * Created by Namwan on 11/20/2017.
 */
import android.view.View;

import com.project.finalyear.thaispellinggame.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserModel {

    public String name;
    public String image;
//    public String bestScore;
//    public String email;
//    public String image;
//    public String level;
//    public String rank;
//    public String score;
//    public String thumb_image;
//    public Boolean online;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
