package com.project.finalyear.thaispellinggame.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.model.ScreenShot;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SummaryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSumRoundOneUser1, tvSumRoundOneUser2;
    private TextView tvSumRoundTwoUser1, tvSumRoundTwoUser2;
    private TextView tvSumRoundThreeUser1, tvSumRoundThreeUser2;
    private TextView tvSumRoundFourUser1, tvSumRoundFourUser2;
    private TextView tvTotalScoreUser1, tvTotalScoreUser2;
    private TextView tvAward;
    private ImageView imgWinner, imgLose, imgCapture;
    private CircleImageView imgPlayer1, imgPlayer2;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private DatabaseReference mPlayerDatabase;
    private FirebaseUser mCurrentUser;

    private String current_uid;
    private String user_id = null;
    private String roomId = null;
    ImageView imgHome;
    ImageView imgShare;
    LinearLayout linearSum;
    StorageReference storeProfileImagestorageRef;
    DatabaseReference getUserDatabaseRef;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        FacebookSdk.sdkInitialize(getApplicationContext());
        initInstance();

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();


        queryRoomId();
        //checkWhoWon();

    }

    private void queryRoomId() {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();

                    setPlayerImage(user_id);
                }

                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                queryScorePlayerOne(roomId);
                queryScorePlayerTwo(roomId, user_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void queryScorePlayerOne(String roomId) {

        mRefDatabase.child("rooms").child(roomId).child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int roundOne = dataSnapshot.child("scoreRound1").getValue(int.class);
                int roundTwo = dataSnapshot.child("scoreRound2").getValue(int.class);
                int roundThree = dataSnapshot.child("scoreRound3").getValue(int.class);
                //int roundFour = dataSnapshot.child("scoreRound4").getValue(int.class);

                int totalScoreUser1 = roundOne + roundTwo + roundThree;

                //update total score to Current user
                mRefDatabase.child("Users").child(current_uid).child("score").setValue(totalScoreUser1);

                tvSumRoundOneUser1.setText(Integer.toString(roundOne));
                tvSumRoundTwoUser1.setText(Integer.toString(roundTwo));
                tvSumRoundThreeUser1.setText(Integer.toString(roundThree));
                //tvSumRoundFourUser1.setText(Integer.toString(roundFour));

                tvTotalScoreUser1.setText(Integer.toString(totalScoreUser1));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void queryScorePlayerTwo(String roomId, String user_id) {

        mRefDatabase.child("rooms").child(roomId).child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int roundOne = dataSnapshot.child("scoreRound1").getValue(int.class);
                int roundTwo = dataSnapshot.child("scoreRound2").getValue(int.class);
                int roundThree = dataSnapshot.child("scoreRound3").getValue(int.class);
                //int roundFour = dataSnapshot.child("scoreRound4").getValue(int.class);

                int totalScoreUser2 = roundOne + roundTwo + roundThree;

                tvSumRoundOneUser2.setText(Integer.toString(roundOne));
                tvSumRoundTwoUser2.setText(Integer.toString(roundTwo));
                tvSumRoundThreeUser2.setText(Integer.toString(roundThree));
                //tvSumRoundFourUser2.setText(Integer.toString(roundFour));

                tvTotalScoreUser2.setText(Integer.toString(totalScoreUser2));

                checkWhoWon();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkWhoWon() {

        int scorePlayerOne = Integer.parseInt(tvTotalScoreUser1.getText().toString());
        int scorePlayerTwo = Integer.parseInt(tvTotalScoreUser2.getText().toString());

        if (scorePlayerOne > scorePlayerTwo) {
            tvAward.setText("คุณชนะ!");
            imgWinner.setVisibility(View.VISIBLE);

        } else {

            tvAward.setText("คุณแพ้!");
            imgLose.setVisibility(View.VISIBLE);

        }

        Log.d("mytotalScore1", Integer.toString(scorePlayerOne));
        Log.d("mytotalScore2", Integer.toString(scorePlayerTwo));


    }


    private void setPlayerImage(final String user_id) {

        mRefDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //-------- player 1 -------------

                String image1 = dataSnapshot.child(current_uid).child("image").getValue().toString();

                if (image1.equals("default_profile_pic")) {
                    Picasso.with(SummaryActivity.this).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer1);

                } else {
                    Picasso.with(SummaryActivity.this).load(image1).fit().centerCrop().into(imgPlayer1);

                }

                //-------- player 2 -------------

                String image2 = dataSnapshot.child(user_id).child("image").getValue().toString();

                if (image2.equals("default_profile_pic")) {
                    Picasso.with(SummaryActivity.this).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer2);

                } else {
                    Picasso.with(SummaryActivity.this).load(image2).fit().centerCrop().into(imgPlayer2);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initInstance() {

        imgWinner = (ImageView) findViewById(R.id.imgWinner);
        imgLose = (ImageView) findViewById(R.id.imgLose);
        imgPlayer1 = (CircleImageView) findViewById(R.id.imgPlayer1);
        imgPlayer2 = (CircleImageView) findViewById(R.id.imgPlayer2);
        imgCapture = (ImageView) findViewById(R.id.imgCapture);
        tvAward = (TextView) findViewById(R.id.tvAward);
        tvSumRoundOneUser1 = (TextView) findViewById(R.id.tvSumRoundOneUser1);
        tvSumRoundOneUser2 = (TextView) findViewById(R.id.tvSumRoundOneUser2);
        tvSumRoundTwoUser1 = (TextView) findViewById(R.id.tvSumRoundTwoUser1);
        tvSumRoundTwoUser2 = (TextView) findViewById(R.id.tvSumRoundTwoUser2);
        tvSumRoundThreeUser1 = (TextView) findViewById(R.id.tvSumRoundThreeUser1);
        tvSumRoundThreeUser2 = (TextView) findViewById(R.id.tvSumRoundThreeUser2);
//        tvSumRoundFourUser1 = (TextView) findViewById(R.id.tvSumRoundFourUser1);
//        tvSumRoundFourUser2 = (TextView) findViewById(R.id.tvSumRoundFourUser2);
        tvTotalScoreUser1 = (TextView) findViewById(R.id.tvTotalScoreUser1);
        tvTotalScoreUser2 = (TextView) findViewById(R.id.tvTotalScoreUser2);
        linearSum = (LinearLayout) findViewById(R.id.linearSum);

        imgHome = (ImageView) findViewById(R.id.imgHome);
        imgShare = (ImageView) findViewById(R.id.imgShare);

        imgHome.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        shareDialog = new ShareDialog(SummaryActivity.this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgHome:

                mRefDatabase.child("players").child(current_uid).removeValue();
                Intent intent = new Intent(SummaryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.imgShare:

                // capture หน้าจอ
                Bitmap bitmap = ScreenShot.takescreenshotOfRootView(imgCapture);
                // imgCapture.setImageBitmap(bitmap);

                // share photo to facebook
                SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                shareDialog.show(SummaryActivity.this,content);
                break;
        }
    }

}


