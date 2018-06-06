package com.project.finalyear.thaispellinggame.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Namwan on 3/17/2018.
 */

public class HeaderGame {

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private DatabaseReference mPlayerDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;

    private CircleImageView imgPlayer1, imgPlayer2;
    private TextView tvTimer;
    private TextView tvScoreOne, tvScoreTwo;
    private ImageView imgCorrectOne, imgWrongOne;
    private ImageView imgCorrectTwo, imgWrongTwo;

    CountDownTimer countDownTimer;
    public String showScoreOne = "0";
    public String showScoreTwo = "0";

    public void setPlayerImage(final Activity activity) {

        imgPlayer1 = (CircleImageView) activity.findViewById(R.id.imgPlayer1);
        imgPlayer2 = (CircleImageView) activity.findViewById(R.id.imgPlayer2);
        tvTimer = (TextView) activity.findViewById(R.id.tvTimerHeader);
        tvScoreOne = (TextView) activity.findViewById(R.id.tvScoreOne);
        tvScoreTwo = (TextView) activity.findViewById(R.id.tvScoreTwo);

        imgCorrectOne = (ImageView) activity.findViewById(R.id.imgCorrectOne);
        imgWrongOne = (ImageView) activity.findViewById(R.id.imgWrongOne);
        imgCorrectTwo = (ImageView) activity.findViewById(R.id.imgCorrectTwo);
        imgWrongTwo = (ImageView) activity.findViewById(R.id.imgWrongTwo);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        mRefDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //-------- player 1 -------------

                String image_p1 = dataSnapshot.child(current_uid).child("image").getValue().toString();

                if (image_p1.equals("default_profile_pic")) {
                    Picasso.with(activity).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer1);

                } else {
                    Picasso.with(activity).load(image_p1).fit().centerCrop().into(imgPlayer1);

                }

                //---------- player 2 -----------

                String image_p2 = dataSnapshot.child(user_id).child("image").getValue().toString();

                if (image_p2.equals("default_profile_pic")) {
                    Picasso.with(activity).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer2);

                } else {
                    Picasso.with(activity).load(image_p2).fit().centerCrop().into(imgPlayer2);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void CountDownTimer(final Context context, final Activity nextActivity) {
        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String strTime = String.format("%d", millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(strTime));

            }

            @Override
            public void onFinish() {

                tvTimer.setText("0");

                //addScore();

                Intent intent = new Intent(context, nextActivity.getClass());
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }.start();
    }

    public void updateScore(final int score) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        mRefDatabase.child("players").child(current_uid).child("roomId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomId = dataSnapshot.getValue().toString();

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("score").setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateScorePlayerOne() {

        mRefDatabase.child("Match_player").child(user_id).child(current_uid).child("playing_score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showScoreOne = dataSnapshot.getValue().toString();
                tvScoreOne.setText(showScoreOne);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateScorePlayerTwo(){

        mRefDatabase.child("Match_player").child(current_uid).child(user_id).child("playing_score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                showScoreTwo = dataSnapshot.getValue().toString();
                tvScoreTwo.setText(showScoreTwo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showMarkOne(Context context, boolean b) {

        final Animation animation = AnimationUtils.loadAnimation(context, R.anim.move);

        if (b == true) {

            imgCorrectOne.setVisibility(View.VISIBLE);
            imgWrongOne.setVisibility(View.INVISIBLE);
            imgCorrectOne.startAnimation(animation);

        } else {

            imgWrongOne.setVisibility(View.VISIBLE);
            imgCorrectOne.setVisibility(View.INVISIBLE);
            imgCorrectOne.startAnimation(animation);
        }
    }



}
