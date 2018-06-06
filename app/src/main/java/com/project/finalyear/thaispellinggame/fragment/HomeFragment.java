package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.project.finalyear.thaispellinggame.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.activity.RandomPlayersActivity;
import com.project.finalyear.thaispellinggame.model.MyBounceInterpolator;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    // Android Layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView tvRank, tvLevel;
    private TextView tvBestScore, tvScore;
    private ImageView progressBar;
    private Button btnPlayGame;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;

    Context context;
    private RatingBar rbRating;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        //Typeface font  = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RSU_BOLD.ttf");

        btnPlayGame = (Button) view.findViewById(R.id.btn_play_game);

        final Animation animBounce = AnimationUtils.loadAnimation(getContext(), R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animBounce.setInterpolator(interpolator);

        btnPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckInternet();
                view.startAnimation(animBounce);

                DatabaseReference userRef = mRefDatabase.child("players").child(current_uid);
                userRef.child("status").setValue(true)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    mRefDatabase.child("players").child(current_uid).child("start_play").setValue(false);

                                    Intent intent = new Intent(getActivity(), RandomPlayersActivity.class);
                                    startActivity(intent);


                                }else
                                    Toast.makeText(context,"กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        initInstances(view);


        return view;
    }

    private void initInstances(View view) {

        mName = (TextView) view.findViewById(R.id.tvProfile);
        mDisplayImage = (CircleImageView) view.findViewById(R.id.profilePic);
        tvRank = (TextView) view.findViewById(R.id.tvRank);
        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
        tvScore = (TextView) view.findViewById(R.id.tv_score);
        tvBestScore = (TextView) view.findViewById(R.id.tv_best_score);
        progressBar = (ImageView) view.findViewById(R.id.progressBar);

        rbRating = (RatingBar) view.findViewById(R.id.rbRating);
        rbRating.setMax(100);
        checkLevel();


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue(String.class);
                String image = dataSnapshot.child("image").getValue(String.class);
                String level = dataSnapshot.child("level").getValue().toString();
                String rank = dataSnapshot.child("rank").getValue().toString();
                String score = dataSnapshot.child("score").getValue().toString();
                String bestScore = dataSnapshot.child("bestScore").getValue().toString();


                if (image.equals("default_profile_pic")) {
                    Picasso.with(context).load(R.drawable.default_profile_pic)
                            .error(context.getResources().getDrawable(R.drawable.default_profile_pic)).into(mDisplayImage);

                } else {
                    Picasso.with(getContext()).load(image).resize(150, 150)
                            .centerCrop().into(mDisplayImage);
                }

                int scoreUp = Integer.parseInt(score);
                Log.d("scoreUp", String.valueOf(scoreUp));
                // ถ้า scoreUp เท่ากับช่วงคะแนนไหน ให้ setValue ให้อัพขึ้น firebase
                if (scoreUp > 450000) {
                    tvLevel.setText("10");
                    mUserDatabase.child("level").setValue(10);
                } else if (scoreUp > 400000) {
                    tvLevel.setText("9");
                    mUserDatabase.child("level").setValue(9);
                } else if (scoreUp > 350000) {
                    tvLevel.setText("8");
                    mUserDatabase.child("level").setValue(8);
                } else if (scoreUp > 300000) {
                    tvLevel.setText("7");
                    mUserDatabase.child("level").setValue(7);
                } else if (scoreUp > 250000) {
                    tvLevel.setText("6");
                    mUserDatabase.child("level").setValue(6);
                } else if (scoreUp > 200000) {
                    tvLevel.setText("5");
                    mUserDatabase.child("level").setValue(5);
                } else if (scoreUp > 150000) {
                    tvLevel.setText("4");
                    mUserDatabase.child("level").setValue(4);
                } else if (scoreUp > 100000) {
                    tvLevel.setText("3");
                    mUserDatabase.child("level").setValue(3);
                } else if (scoreUp > 50000) {
                    tvLevel.setText("2");
                    mUserDatabase.child("level").setValue(2);
                } else {
                    tvLevel.setText("1");
                    mUserDatabase.child("level").setValue(1);
                }


                mName.setText(name);
                tvRank.setText(rank);
                // tvLevel.setText(level);
                tvScore.setText(score);
                tvBestScore.setText(bestScore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void checkLevel() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                String score = dataSnapshot.child("score").getValue().toString();
                int scoreRating = Integer.parseInt(score);
                String level = dataSnapshot.child("level").getValue().toString();
                Log.d("level", level);
                Log.d("scoreRating", String.valueOf(scoreRating));
                int levelRating = Integer.parseInt(level);
                // check ว่าอยู่ level ไหน ให้เอาคะแนนแต่ละเลเวลมาลบออกให้เหลือ 50,000 ทุกครั้ง

                if (levelRating == 10) {
                    scoreRating =Math.abs(450000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating9", String.valueOf(levelRating));
                } else if (levelRating == 9) {
                    scoreRating = Math.abs(400000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating8", String.valueOf(levelRating));
                } else if (levelRating == 8) {
                    scoreRating = Math.abs(350000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating7", String.valueOf(levelRating));
                } else if (levelRating == 7) {
                    scoreRating =Math.abs(300000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating6", String.valueOf(levelRating));
                } else if (levelRating == 6) {
                    scoreRating =Math.abs(250000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating5", String.valueOf(levelRating));
                } else if (levelRating == 5) {
                    scoreRating =Math.abs(200000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating4", String.valueOf(levelRating));
                } else if (levelRating == 4) {
                    scoreRating =Math.abs(150000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating3", String.valueOf(levelRating));
                } else if (levelRating == 3) {
                    scoreRating = Math.abs(100000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating2", String.valueOf(levelRating));
                } else if (levelRating == 2) {
                    scoreRating = Math.abs(50000-scoreRating);
                    checkRating(scoreRating);
                    Log.d("checkRating1", String.valueOf(levelRating));
                } else {
                    checkRating(scoreRating);
                    Log.d("checkRating", String.valueOf(levelRating));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkRating(final int scoreRating) {
//       final int scoreCheck = scoreRating;
        Log.d("scoreCheck", String.valueOf(scoreRating));
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // set progress bar

                // เอาคะแนน 50,000 ที่ทำการลบจากแต่ละเลเวล มาคำนวณแถบ progress
                // ทำเป็น 100%
                int rating = (scoreRating * 100) / 50000;
                rbRating.setProgress(rating);

            }

            @Override
            public void onCancelled (DatabaseError databaseError){

            }
        });


}

    public void CheckInternet() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "กรุณาเชื่อมต่ออินเทอร์เน็ตด้วยค่ะ !", Toast.LENGTH_SHORT).show();

        }

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

}
