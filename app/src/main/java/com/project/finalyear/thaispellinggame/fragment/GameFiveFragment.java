package com.project.finalyear.thaispellinggame.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;

import com.project.finalyear.thaispellinggame.controller.GameFiveController;

import com.project.finalyear.thaispellinggame.model.GameFive;
import com.project.finalyear.thaispellinggame.model.HeaderGame;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class GameFiveFragment extends Fragment implements GameFiveController.GameFiveCallBack, View.OnTouchListener {
    CountDownTimer countDownTimer;
    TextView tvTimer;
    TextView tvOne, tvTwo, tvThree;
    RelativeLayout snowOne, snowTwo, snowThree;
    ArrayList<GameFive> gameFiveArrayList;
    ImageView imgCar;
    String typeOne;
    String typeTwo;
    String typeThree;
    String answerCorrectOne;
    String answerCorrectTwo;
    String answerCorrectThree;
    private GameFiveController gameFiveController;
    int height;
    int weight;
    int score;
    int counter;
    LinearLayout linearSnowOne;
    LinearLayout linearSnowTwo;
    LinearLayout linearSnowThree;
    LinearLayout linearSnowAll;

    ImageView imgBonus;
    ImageView imgCorrectOne, imgWrongOne;
    ImageView imgCorrectTwo, imgWrongTwo;
    CircleImageView imgPlayer1, imgPlayer2;
    TextView tvScoreOne, tvScoreTwo;

    LinearLayout linearBottom;
    ProgressDialog progressBarLoad;
    LinearLayout linearCar;
    ArrayList<String> playerAnswer = new ArrayList<String>();
    ArrayList<String> answerRight = new ArrayList<String>();

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private DatabaseReference mPlayerDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;
    private String roomId = null;

    String typeAnswer;
    String answer;

    public GameFiveFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_five, container, false);
        initInstance(view);

        gameFiveController = new GameFiveController(this);

        score = 0;
        CountDownTimer();
        queryUserId();

        loadDataGameFive();
        return view;
    }

    private void queryUserId() {
        user_id = null;

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                setPlayerImage(user_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setPlayerImage(final String user_id) {

        mRefDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //-------- player 1 -------------

                String image_p1 = dataSnapshot.child(current_uid).child("image").getValue().toString();

                if (image_p1.equals("default_profile_pic")) {
                    Picasso.with(getActivity()).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer1);

                } else {
                    Picasso.with(getActivity()).load(image_p1).fit().centerCrop().into(imgPlayer1);

                }

                //---------- player 2 -----------

                String image_p2 = dataSnapshot.child(user_id).child("image").getValue().toString();

                if (image_p2.equals("default_profile_pic")) {
                    Picasso.with(getActivity()).load(R.drawable.default_profile_pic)
                            .error(R.drawable.default_profile_pic)
                            .into(imgPlayer2);

                } else {
                    Picasso.with(getActivity()).load(image_p2).fit().centerCrop().into(imgPlayer2);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initInstance(View view) {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        snowOne = (RelativeLayout) view.findViewById(R.id.snowOne);
        snowTwo = (RelativeLayout) view.findViewById(R.id.snowTwo);
        snowThree = (RelativeLayout) view.findViewById(R.id.snowThree);

        tvOne = (TextView) view.findViewById(R.id.tvOne);
        tvTwo = (TextView) view.findViewById(R.id.tvTwo);
        tvThree = (TextView) view.findViewById(R.id.tvThree);

        imgCar = (ImageView) view.findViewById(R.id.imgCar);

        linearCar = (LinearLayout) view.findViewById(R.id.linearCar);
        linearBottom = (LinearLayout) view.findViewById(R.id.linearBottom);

        imgBonus = (ImageView) view.findViewById(R.id.imgBonus);

        linearSnowOne = (LinearLayout) view.findViewById(R.id.linearSnowOne);
        linearSnowTwo = (LinearLayout) view.findViewById(R.id.linearSnowTwo);
        linearSnowThree = (LinearLayout) view.findViewById(R.id.linearSnowThree);
        linearSnowAll = (LinearLayout) view.findViewById(R.id.linearSnowAll);

        //---------- header -------------
        imgPlayer1 = (CircleImageView) view.findViewById(R.id.imgPlayer1);
        imgPlayer2 = (CircleImageView) view.findViewById(R.id.imgPlayer2);
        tvTimer = (TextView) view.findViewById(R.id.tvTimerHeader);
        tvScoreOne = (TextView) view.findViewById(R.id.tvScoreOne);
        tvScoreTwo = (TextView) view.findViewById(R.id.tvScoreTwo);

        imgCorrectOne = (ImageView) view.findViewById(R.id.imgCorrectOne);
        imgWrongOne = (ImageView) view.findViewById(R.id.imgWrongOne);
        imgCorrectTwo = (ImageView) view.findViewById(R.id.imgCorrectTwo);
        imgWrongTwo = (ImageView) view.findViewById(R.id.imgWrongTwo);

        // get คามสูงของหน้าจอ
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        weight = displaymetrics.widthPixels;

        Log.d("weightDisplay", String.valueOf(weight));
        imgCar.setOnTouchListener(this);
    }

    private void loadDataGameFive() {
        progressBarLoad = new ProgressDialog(getContext());
        progressBarLoad.setMessage("กำลังโหลดคำศัพท์");
        progressBarLoad.show();
        gameFiveController.dataPull();
    }

    public void CountDownTimer() {
        countDownTimer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String strTime = String.format("%d", millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(strTime));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                saveWord(playerAnswer,answerRight);
            }
        }.start();
    }
    private int marginTopSnowOne = -150;
    private int marginTopSnowTwo = -50;
    private int marginTopSnowThree = -250;

    private void snowOne() {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) snowOne.getLayoutParams();
        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                marginTopSnowOne += 13;
                params.setMargins(20, marginTopSnowOne, 0, 0);
                snowOne.setLayoutParams(params);
                handler.postDelayed(this, 35);
                if (marginTopSnowOne > (height - 500)) {
                    marginTopSnowOne = -50;
                    setTextSnowOne(gameFiveArrayList);
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

    private void snowTwo() {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) snowTwo.getLayoutParams();
        final Handler handler = new Handler();
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                marginTopSnowTwo += 13;
                params.setMargins(20, marginTopSnowTwo, 0, 0);
                snowTwo.setLayoutParams(params);
                handler.postDelayed(this, 35);
                if (marginTopSnowTwo > (height - 500)) {
                    marginTopSnowTwo = -50;
                    setTextSnowTwo(gameFiveArrayList);
                }
            }
        };
        handler.postDelayed(runnable, 100);

    }

    private void snowThree() {
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) snowThree.getLayoutParams();
        final Handler handler = new Handler();
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                marginTopSnowThree += 13;
                params.setMargins(10, marginTopSnowThree, 0, 20);
                snowThree.setLayoutParams(params);
                handler.postDelayed(this, 35);
                if (marginTopSnowThree > (height - 500)) {
                    marginTopSnowThree = -50;
                    setTextSnowThree(gameFiveArrayList);
                }
            }
        };
        handler.postDelayed(runnable, 100);

    }

    private void saveWord(ArrayList<String> playerAnswer, ArrayList<String> answerRight) {

        addScoreToDatabase();

        Fragment fragment = new SumGameFiveFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("playerSelect", playerAnswer);
        bundle.putStringArrayList("rightAnswer",answerRight);
        FragmentTransaction transaction =  getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


    private void setTextSnowOne(ArrayList<GameFive> gameFiveArrayList) {
        Random random = new Random();
        int section = random.nextInt(gameFiveArrayList.size());
        tvOne.setText(gameFiveArrayList.get(section).getWord());
        typeOne = gameFiveArrayList.get(section).getType();
        answerCorrectOne = gameFiveArrayList.get(section).getAnswerCorrect();
        //checkType(typeOne);

    }

    private void setTextSnowTwo(ArrayList<GameFive> gameFiveArrayList) {
        Random random = new Random();
        int section = random.nextInt(gameFiveArrayList.size());
        tvTwo.setText(gameFiveArrayList.get(section).getWord());
        typeTwo = gameFiveArrayList.get(section).getType();
        answerCorrectTwo = gameFiveArrayList.get(section).getAnswerCorrect();
       // checkType(typeTwo);

    }

    private void setTextSnowThree(ArrayList<GameFive> gameFiveArrayList) {
        Random random = new Random();
        int section = random.nextInt(gameFiveArrayList.size());
        tvThree.setText(gameFiveArrayList.get(section).getWord());
        typeThree = gameFiveArrayList.get(section).getType();
        answerCorrectThree = gameFiveArrayList.get(section).getAnswerCorrect();
      //  checkType(typeThree);
    }

//    private void checkType(String type) {
//        String type1 = type.toString();
//
//        Log.d("typeAll1", type1);
//        if (!type1.equals("correct") && !type1.equals("correct") && !type1.equals("correct")) {
//            Random random = new Random();
//            int section = random.nextInt(gameFiveArrayList.size());
//            int section1 = random.nextInt(gameFiveArrayList.size());
//            int section2 = random.nextInt(gameFiveArrayList.size());
//
//            tvOne.setText(gameFiveArrayList.get(section).getWord());
//            typeOne = gameFiveArrayList.get(section).getType();
//
//            tvTwo.setText(gameFiveArrayList.get(section1).getWord());
//            typeTwo = gameFiveArrayList.get(section1).getType();
//
//            tvThree.setText(gameFiveArrayList.get(section2).getWord());
//            typeThree = gameFiveArrayList.get(section2).getType();
//
//        }
//
//    }


    @Override
    public void displayGameFive(int index, ArrayList<GameFive> gameFiveArrayList) {
        progressBarLoad.dismiss();
        this.gameFiveArrayList = gameFiveArrayList;
        setTextSnowOne(gameFiveArrayList);
        setTextSnowTwo(gameFiveArrayList);
        setTextSnowThree(gameFiveArrayList);
        snowOne();
        snowTwo();
        snowThree();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return onTouchEvent(event);
    }


    private void checkAnswer(String selectText, String typeSelect,String answerCorrect) {
        answer = selectText.toString();
        typeAnswer = typeSelect.toString();
        answerCorrect = answerCorrect.toString();
        Log.d("AnswerSelectClick", answer);
        Log.d("TypeAnswerClick", typeAnswer);
        Log.d("answerCorrectClick", answerCorrect);

        if (answer.equals(answerCorrect)) {
            // เรียกใช้ class จาก activity มาใช้ใน fragment
            ((GameActivity) getActivity()).soundEffect(true);
            score += 50;
            updateScore(score);
            showPlayerScore();
            showMarkOne(true);

            final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

            counter++;
            // ถ้าตอบติดกัน 5 ข้อ ได้คะแนนโบนส 250 คะแนน
            if (counter == 5) {

                imgBonus.setVisibility(View.VISIBLE);
                imgBonus.startAnimation(animation);
                score = score + 250;
                counter = 0; //set counter = 0 เพื่อนับ 1 ใหม่

            } else if (counter != 5) {

                imgBonus.setVisibility(View.INVISIBLE);
            }


            playerAnswer.add(answerCorrect);
            answerRight.add(answer);

        } else if (!answer.equals(answerCorrect)) {
            // เรียกใช้ class จาก activity มาใช้ใน fragment
            ((GameActivity) getActivity()).soundEffect(false);
            counter = 0;

            //score += 0;
            //updateScore(score);
            showPlayerScore();
            showMarkOne(false);

            playerAnswer.add(answerCorrect);
            answerRight.add(answer);
        }

    }

    private void addScoreToDatabase() {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }
                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("scoreRound5").setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateScore(final int score) {

        // query 'roomId' and 'user_id'
        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }
                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("score").setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showPlayerScore() {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                //show score player1
                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("score").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String score = dataSnapshot.getValue().toString();
                        tvScoreOne.setText(score);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //show score player2
                mRefDatabase.child("rooms").child(roomId).child(user_id).child("score").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String score = dataSnapshot.getValue().toString();
                        tvScoreTwo.setText(score);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showMarkOne(boolean b) {

        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

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


    @Override
    public void onCancel(String massageError) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getRawX();

        Log.d("XXXXX", String.valueOf(x));
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        Log.d("heightPhone", String.valueOf(height));
        int heightLayout = linearBottom.getHeight();
        Log.d("heightCar", String.valueOf(heightLayout));
        int snowAll = linearSnowAll.getHeight();
        Log.d("HeightsnowAll", String.valueOf(snowAll));

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.i("TAG", "touched hover move");
                break;
            case MotionEvent.ACTION_DOWN:
                imgCar.setX(x);
                imgCar.getX();
                if (marginTopSnowOne >= (height - marginTopSnowOne)) {
                    if (imgCar.getX() >= ((weight / 3) * 2)) {
                        Log.d("marginTop", String.valueOf((height - marginTopSnowOne)));
                        Log.d("marginTopOne", String.valueOf(marginTopSnowOne));
                        Log.d("weight1", String.valueOf(imgCar.getX() >= (weight / 3) * 2));
                        snowOne.setAnimation(animation);
                        String txtOne = tvOne.getText().toString();
                        String typeTxtOne = typeOne.toString();
                        answerCorrectOne = answerCorrectOne.toString();
                        checkAnswer(txtOne, typeTxtOne,answerCorrectOne);
                        marginTopSnowOne = -150;
                        setTextSnowOne(gameFiveArrayList);
                    }
                } else if (marginTopSnowTwo >= (height - marginTopSnowTwo)) {
                    if (imgCar.getX() < (weight / 3) && imgCar.getX() < (weight / 3) * 2) {
                        Log.d("XCAR", String.valueOf(imgCar.getX()));
                        Log.d("weightSum", String.valueOf(weight / 3));
                        Log.d("marginTop2", String.valueOf((height - marginTopSnowTwo)));
                        Log.d("marginTopTwo", String.valueOf(marginTopSnowTwo >= (height - marginTopSnowTwo)));
                        Log.d("weight2", String.valueOf(imgCar.getX() < (weight / 3) && imgCar.getX() < (weight / 3) * 2));
                        snowTwo.setAnimation(animation);
                        String txtTwo = tvTwo.getText().toString();
                        String typeTxtTwo = typeTwo.toString();
                        answerCorrectTwo = answerCorrectTwo.toString();
                        checkAnswer(txtTwo, typeTxtTwo,answerCorrectTwo);
                        marginTopSnowTwo = -50;
                        setTextSnowTwo(gameFiveArrayList);
                    }

                } else if (marginTopSnowThree >= (height - marginTopSnowThree)) {
                    if (imgCar.getX() >= (weight / 3) && imgCar.getX() < (weight / 3) * 2) {
                        Log.d("marginTop3", String.valueOf((height - marginTopSnowThree)));
                        Log.d("marginTopThree", String.valueOf(marginTopSnowThree));
                        Log.d("weight3", String.valueOf(imgCar.getX() >= (weight / 3) && imgCar.getX() < (weight / 3) * 2));
                        snowThree.setAnimation(animation);
                        String txtThree = tvThree.getText().toString();
                        String typeTxtThree = typeThree.toString();
                        answerCorrectThree = answerCorrectThree.toString();
                        checkAnswer(txtThree, typeTxtThree,answerCorrectThree);
                        marginTopSnowThree = -250;
                        setTextSnowThree(gameFiveArrayList);
                    }

                }

                Log.i("TAG", "touched down");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG", "touched move");
                imgCar.setX(x);
                imgCar.getX();
                if (marginTopSnowOne >= (height - marginTopSnowOne)) {
                    if (imgCar.getX() >= ((weight / 3) * 2)) {
                        Log.d("marginTop", String.valueOf((height - marginTopSnowOne)));
                        Log.d("marginTopOne", String.valueOf(marginTopSnowOne));
                        Log.d("weight1", String.valueOf(imgCar.getX() >= (weight / 3) * 2));
                        snowOne.setAnimation(animation);
                        String txtOne = tvOne.getText().toString();
                        String typeTxtOne = typeOne.toString();
                        answerCorrectOne = answerCorrectOne.toString();
                        checkAnswer(txtOne, typeTxtOne,answerCorrectOne);
                        marginTopSnowOne = -150;
                        setTextSnowOne(gameFiveArrayList);
                    }
                } else if (marginTopSnowTwo >= (height - marginTopSnowTwo)) {
                    if (imgCar.getX() < (weight / 3) && imgCar.getX() < (weight / 3) * 2) {
                        Log.d("XCAR", String.valueOf(imgCar.getX()));
                        Log.d("weightSum", String.valueOf(weight / 3));
                        Log.d("marginTop2", String.valueOf((height - marginTopSnowTwo)));
                        Log.d("marginTopTwo", String.valueOf(marginTopSnowTwo >= (height - marginTopSnowTwo)));
                        Log.d("weight2", String.valueOf(imgCar.getX() < (weight / 3) && imgCar.getX() < (weight / 3) * 2));
                        snowTwo.setAnimation(animation);
                        String txtTwo = tvTwo.getText().toString();
                        String typeTxtTwo = typeTwo.toString();
                        answerCorrectTwo= answerCorrectTwo.toString();
                        checkAnswer(txtTwo, typeTxtTwo,answerCorrectTwo);
                        marginTopSnowTwo = -50;
                        setTextSnowTwo(gameFiveArrayList);
                    }

                } else if (marginTopSnowThree >= (height - marginTopSnowThree)) {
                    if (imgCar.getX() >= (weight / 3) && imgCar.getX() < (weight / 3) * 2) {
                        Log.d("marginTop3", String.valueOf((height - marginTopSnowThree)));
                        Log.d("marginTopThree", String.valueOf(marginTopSnowThree));
                        Log.d("weight3", String.valueOf(imgCar.getX() >= (weight / 3) && imgCar.getX() < (weight / 3) * 2));
                        snowThree.setAnimation(animation);
                        String txtThree = tvThree.getText().toString();
                        String typeTxtThree = typeThree.toString();
                        answerCorrectThree = answerCorrectThree.toString();
                        checkAnswer(txtThree, typeTxtThree,answerCorrectThree);
                        marginTopSnowThree = -250;
                        setTextSnowThree(gameFiveArrayList);
                    }

                }

        }
        return true;
    }
}
