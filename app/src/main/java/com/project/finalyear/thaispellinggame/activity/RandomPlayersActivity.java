package com.project.finalyear.thaispellinggame.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.controller.RandomGame;
import com.project.finalyear.thaispellinggame.model.MyBounceInterpolator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;



public class RandomPlayersActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mRef;
    private FirebaseUser mCurrentUser;

    private TextView playerName, player2Name;
    private TextView playerScore, player2Score;
    private TextView playerRank, player2Rank;
    private TextView playerLevel, player2Level;
    private TextView tvNoPlayer;
    private CircleImageView imgPlayerOne, imgPlayerTwo;
    private LinearLayout linearPlayerTwo;

    private Animation animBounce;
    private Button btnRandom, btnCancel, btnStart;
    private ProgressBar loadPlayer;
    private ProgressDialog progress;

    private String user_id;
    private String current_uid;
    CountDownTimer countDownTimer;

    public class Room {
        public String player_one;
        public String player_two;

        public Room() {
        }

        public Room(String player_one, String player_two) {
            this.player_one = player_one;
            this.player_two = player_two;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_player);

        initInstance();
        setPlayerOne();

        randomPlayer();
        startCountDown();


        animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animBounce.setInterpolator(interpolator);

    }

    public void startCountDown() {
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                createRoom();
            }
        }.start();
    }

    public void CountDownTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                mRef.child("players").child(user_id).child("start_play").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean statusPlay = dataSnapshot.getValue(Boolean.class);

                        if (statusPlay == true) {
                            progress.dismiss();
                            countDownTimer.cancel();

                            //randomGame();
                            Intent intent = new Intent(RandomPlayersActivity.this, GameActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onFinish() {
                progress.dismiss();

                Toast.makeText(RandomPlayersActivity.this, "Random Again!", Toast.LENGTH_SHORT).show();

                btnStart.setEnabled(false);
                btnStart.setVisibility(INVISIBLE);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_start:
                v.startAnimation(animBounce);
                setBtnStart();
                break;

            case R.id.btn_cancel:
                v.startAnimation(animBounce);
                setCancel();
                RandomPlayersActivity.super.onBackPressed();
                finish();
                break;

//            case R.id.btn_random:
//                v.startAnimation(animBounce);
//                Toast.makeText(this, "ButtonRandom Clicked", Toast.LENGTH_SHORT).show();
//                break;

            default:
                break;
        }

    }

    private void setCancel() {

        if (mRef.child("pair_players").child(current_uid) != null) {
            mRef.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        user_id = child.getKey();
                    }
                    final String roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                    mRef.removeEventListener(this);

                    //delete 'pair_players'
                    if (mRef.child("pair_players").child(current_uid) != null) {
                        mRef.child("pair_players").child(current_uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                mRef.child("rooms").child(roomId).child(current_uid).removeValue();

                            }
                        });

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mRef.child("players").child(current_uid).removeValue();

    }

    private void setBtnStart() {
        mRef.child("players").child(current_uid).child("start_play").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                if (user_id != null) {
                    mRef.child("players").child(user_id).child("start_play").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean statusPlay = dataSnapshot.getValue(Boolean.class);

                            if (statusPlay == true) {

                                Intent intent = new Intent(RandomPlayersActivity.this, GameActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                progress = ProgressDialog.show(RandomPlayersActivity.this, "Wait Player", "Waiting...", true);
                                CountDownTimer();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void setPlayerOne() {

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String level = dataSnapshot.child("level").getValue().toString();
                String rank = dataSnapshot.child("rank").getValue().toString();
                String score = dataSnapshot.child("score").getValue().toString();
                String imageOne = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                if (imageOne.equals("default_profile_pic")) {
                    Picasso.with(RandomPlayersActivity.this).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayerOne);

                } else {
                    Picasso.with(RandomPlayersActivity.this).load(imageOne).fit().centerCrop().into(imgPlayerOne);
                }

                playerName.setText(name);
                playerLevel.setText("เลเวล: " + level);
                playerRank.setText("อันดับที่: " + rank);
                playerScore.setText("คะแนน: " + score);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setPlayerTwo(String user_id) {

        if (user_id != "null") {

            mRef.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue().toString();
                    String level = dataSnapshot.child("level").getValue().toString();
                    String rank = dataSnapshot.child("rank").getValue().toString();
                    String score = dataSnapshot.child("score").getValue().toString();
                    String imageTwo = dataSnapshot.child("image").getValue().toString();
                    String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                    if (imageTwo.equals("default_profile_pic")) {
                        Picasso.with(RandomPlayersActivity.this).load(R.drawable.default_profile_pic)
                                .error(R.drawable.default_profile_pic)
                                .into(imgPlayerTwo);

                    } else {


                        Picasso.with(RandomPlayersActivity.this).load(imageTwo).fit().centerCrop().into(imgPlayerTwo);
                    }

                    player2Name.setText(name);
                    player2Level.setText("เลเวล: " + level);
                    player2Rank.setText("อันดับที่: " + rank);
                    player2Score.setText("คะแนน: " + score);

                    tvNoPlayer.setVisibility(INVISIBLE);
                    loadPlayer.setVisibility(INVISIBLE);

                    //btnRandom.setVisibility(VISIBLE);
                    linearPlayerTwo.setVisibility(VISIBLE);
                    loadPlayer.setVisibility(INVISIBLE);

                    btnStart.setVisibility(VISIBLE);
                    btnStart.setEnabled(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            //btnRandom.setVisibility(VISIBLE);
            tvNoPlayer.setVisibility(VISIBLE);

            linearPlayerTwo.setVisibility(INVISIBLE);
            loadPlayer.setVisibility(INVISIBLE);

            btnStart.setVisibility(INVISIBLE);
            btnStart.setEnabled(false);

        }

    }

    private void randomPlayer() {
        user_id = null;

        linearPlayerTwo.setVisibility(INVISIBLE);
        //btnRandom.setVisibility(INVISIBLE);

        btnStart.setEnabled(false);

        loadPlayer.setVisibility(VISIBLE);

        Query query = mRef.child("players").orderByChild("status").equalTo(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final List<String> list = new ArrayList<String>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (!(child.getKey().equals(current_uid))) {
                        list.add(child.getKey());
                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    Random random = new Random();
                    int index = random.nextInt(list.size());
                    user_id = list.get(index);
                }

                pairPlayer(user_id);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void pairPlayer(final String user_id) {

        if (user_id != null) {
            mRef.child("players").child(current_uid).child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (current_uid != null) {
                        if (dataSnapshot.getValue().equals(true)) {

                            //--------------------- Create Match Players ----------------------
                            mRef.child("pair_players").child(current_uid).child(user_id).child("roomId").setValue("null")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            mRef.child("players").child(user_id).child("status").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (current_uid != null) {
                                                        if (dataSnapshot.getValue().equals(true)) {
                                                            mRef.child("pair_players").child(user_id).child(current_uid).child("roomId")
                                                                    .setValue("null").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    mRef.child("players").child(current_uid).child("status").setValue(false);
                                                                    mRef.child("players").child(user_id).child("status").setValue(false);
                                                                }
                                                            });

                                                            setPlayerTwo(user_id);
                                                            generateRoomId(user_id);

                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    });
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    private void generateRoomId(final String user_id) {

        mRef.child("pair_players").child(current_uid).child(user_id).child("roomId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String roomId = dataSnapshot.getValue().toString();

                if (roomId.equals("null")) {
                    final String key = mRef.child("rooms").push().getKey();
                    mRef.child("pair_players").child(current_uid).child(user_id).child("roomId").setValue(key);
                    mRef.child("pair_players").child(user_id).child(current_uid).child("roomId").setValue(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void createRoom() {

        mRef.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                if (user_id != null) {

                    mRef.child("pair_players").child(current_uid).child(user_id).child("roomId").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String key = dataSnapshot.getValue().toString();

                            //Room room = new Room(current_uid, user_id);
                            mRef.child("rooms").child(key).child(current_uid).child("score").setValue(0);
                            mRef.child("rooms").child(key).child(user_id).child("score").setValue(0);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void initInstance() {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();

        mRef = FirebaseDatabase.getInstance().getReference();
        mUserDatabase = mRef.child("Users").child(current_uid);

        playerName = (TextView) findViewById(R.id.player_name);
        playerScore = (TextView) findViewById(R.id.player_score);
        playerRank = (TextView) findViewById(R.id.player_rank);
        playerLevel = (TextView) findViewById(R.id.player_level);
        imgPlayerOne = (CircleImageView) findViewById(R.id.img_player_one);

        player2Name = (TextView) findViewById(R.id.player2_name);
        player2Score = (TextView) findViewById(R.id.player2_score);
        player2Rank = (TextView) findViewById(R.id.player2_rank);
        player2Level = (TextView) findViewById(R.id.player2_level);
        imgPlayerTwo = (CircleImageView) findViewById(R.id.img_player_two);

        loadPlayer = (ProgressBar) findViewById(R.id.load_player_progressbar);
        tvNoPlayer = (TextView) findViewById(R.id.tv_no_player);
        linearPlayerTwo = (LinearLayout) findViewById(R.id.linear_player_two);

        btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setEnabled(false);
        //btnRandom = (Button) findViewById(R.id.btn_random);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnStart.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
       //btnRandom.setOnClickListener(this);

    }

}
