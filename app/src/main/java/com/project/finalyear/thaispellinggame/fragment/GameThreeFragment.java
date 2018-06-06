package com.project.finalyear.thaispellinggame.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.controller.GameThreeController;
import com.project.finalyear.thaispellinggame.model.GameThree;
import com.project.finalyear.thaispellinggame.model.HeaderGame;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class GameThreeFragment extends Fragment implements GameThreeController.GameThreeCallBack {

    RelativeLayout ballOne, ballTwo, ballThree;
    TextView tvOne, tvTwo, tvThree;
    private ArrayList<GameThree> gameThreeArrayList;
    String typeBallOne;
    String typeBallTwo;
    String typeBallThree;
    String answerCorrectOne;
    String answerCorrectTwo;
    String answerCorrectThree;
    int score;
    int counter = 0;

    ImageView imgBonus;
    int height;
    private GameThreeController gameThreeController;
    ArrayList<String> correctAnswer = new ArrayList<String>();
    ArrayList<String> answerSelect = new ArrayList<String>();
    ProgressDialog progressBarLoad;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private DatabaseReference mPlayerDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;
    private String roomId = null;

    String typeAnswer;
    String answer;

    CountDownTimer countDownTimer;
    private TextView tvTimer;

    ImageView imgCorrectOne, imgWrongOne;
    ImageView imgCorrectTwo, imgWrongTwo;
    CircleImageView imgPlayer1, imgPlayer2;
    TextView tvScoreOne, tvScoreTwo;

    public GameThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_three, container, false);
        initInstance(view);

        gameThreeController = new GameThreeController(this);
        loadDataGameThree();

        score = 0;
        CountDownTimer();
        queryUserId();

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

        ballOne = (RelativeLayout) view.findViewById(R.id.ballOne);
        ballTwo = (RelativeLayout) view.findViewById(R.id.ballTwo);
        ballThree = (RelativeLayout) view.findViewById(R.id.ballThree);

        tvOne = (TextView) view.findViewById(R.id.tvOne);
        tvTwo = (TextView) view.findViewById(R.id.tvTwo);
        tvThree = (TextView) view.findViewById(R.id.tvThree);

        imgBonus = (ImageView) view.findViewById(R.id.imgBonus);

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

        Log.d("heightDisplay", String.valueOf(height));

        ballOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtOne = tvOne.getText().toString();
                String typeTxtOne = typeBallOne.toString();
                //----- ส่งค่า คำศัพท์ที่ลือก , typeBallOne ของคำศัพท์ ----//
                checkAnswer(txtOne, typeTxtOne, answerCorrectOne);
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.move);
                ballOne.setAnimation(animation);
                marginTopBallOne = -100;
                // setText ข้อใหม่
                setTextToBallOne(gameThreeArrayList);

            }
        });

        ballTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtTwo = tvTwo.getText().toString();
                String typeTxtTwo = typeBallTwo.toString();
                checkAnswer(txtTwo, typeTxtTwo, answerCorrectTwo);
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.move);
                ballTwo.setAnimation(animation);
                marginTopBallTwo = -150;
                setTextToBallTwo(gameThreeArrayList);
            }
        });

        ballThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtThree = tvThree.getText().toString();
                String typeTxtThree = typeBallThree.toString();
                checkAnswer(txtThree, typeTxtThree, answerCorrectThree);
                final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.move);
                ballThree.setAnimation(animation);
                marginTopBallThree = -50;
                setTextToBallThree(gameThreeArrayList);
            }
        });

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
                saveWord(correctAnswer, answerSelect);
            }
        }.start();
    }

    private void loadDataGameThree() {
        progressBarLoad = new ProgressDialog(getContext());
        progressBarLoad.setMessage("กำลังโหลดคำศัพท์");
        progressBarLoad.show();
        // เรียก pullData จาก GameThreeController
        gameThreeController.pullData();
    }

    private int marginTopBallOne = -100;
    private int marginTopBallTwo = -230;
    private int marginTopBallThree = -10;

    private void fallingBallOne() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ballOne.getLayoutParams();
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                marginTopBallOne += 10;
                params.setMargins(10, marginTopBallOne, 0, 0);
                ballOne.setLayoutParams(params);
                handler.postDelayed(this, 35);//

                // check marginTop > ขนาดหน้าจอ ให้ marginTop = -100
                if (marginTopBallOne > (height - 1000)) {
                    marginTopBallOne = -100;
                    setTextToBallOne(gameThreeArrayList);
                }
            }
        };

        handler.postDelayed(runnable, 100);

    }

    private void fallingBallTwo() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ballTwo.getLayoutParams();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                marginTopBallTwo += 10;
                params.setMargins(50, marginTopBallTwo, 0, 0);
                ballTwo.setLayoutParams(params);
                handler.postDelayed(this, 35);
                // check marginTop > ขนาดหน้าจอ ให้ marginTop = -100
                if (marginTopBallTwo > (height - 1000)) {
                    marginTopBallTwo = -100;
                    setTextToBallTwo(gameThreeArrayList);
                }
            }
        };

        handler.postDelayed(runnable, 100);

    }

    private void fallingBallThree() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ballThree.getLayoutParams();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                marginTopBallThree += 10;
                params.setMargins(0, marginTopBallThree, 0, 0);
                ballThree.setLayoutParams(params);
                handler.postDelayed(this, 35);
                // check marginTop มากกว่าขนาดหน้าจอ ให้ marginTop = -100
                if (marginTopBallThree > (height - 1000)) {
                    marginTopBallThree = -100;
                    setTextToBallThree(gameThreeArrayList);
                }
            }
        };
        handler.postDelayed(runnable, 100);

    }


    private void checkAnswer(String text, String typeClick, String answerCorrect) {
        //คำที่ player เลือก
        answer = text.toString();
        typeAnswer = typeClick.toString();
        answerCorrect = answerCorrect.toString();
        Log.d("typeClick", typeAnswer);
        Log.d("answerClick", answer);
        Log.d("correctClick", answerCorrect);
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        // ถ้าตอบถูก
        if (answer.equals(answerCorrect)) {
            // เรียกใช้ class จาก activity มาใช้ใน fragment
            ((GameActivity) getActivity()).soundEffect(true);
            score += 50;
            updateScore(score);
            showPlayerScore();
            showMarkOne(true);
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

            correctAnswer.add(answerCorrect);
            answerSelect.add(answer);


            // ถ้าตอบผิด
        } else if (!typeAnswer.equals(answerCorrect)) {

            counter = 0;
            ((GameActivity) getActivity()).soundEffect(false);

            imgBonus.setVisibility(View.INVISIBLE);

            score += 0;
            updateScore(score);
            showPlayerScore();
            showMarkOne(false);

            correctAnswer.add(answerCorrect);
            answerSelect.add(answer);
        }
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


    private void saveWord(ArrayList<String> playerAnswer, ArrayList<String> answerRight) {

        addScoreToDatabase();
        Fragment fragment = new SumGameThreeFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("playerSelect", playerAnswer);
        bundle.putStringArrayList("rightAnswer", answerRight);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addScoreToDatabase() {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }
                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("scoreRound4").setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTextToBallOne(ArrayList<GameThree> gameThreeArrayList) {
        //  random ball one
        Random random = new Random();
        int section = random.nextInt(gameThreeArrayList.size());
        Log.d("section", String.valueOf(section));
        tvOne.setText(gameThreeArrayList.get(section).getWord());
        typeBallOne = gameThreeArrayList.get(section).getType();
        answerCorrectOne = gameThreeArrayList.get(section).getAnswerCorrect();


    }

    private void setTextToBallTwo(ArrayList<GameThree> gameThreeArrayList) {
        // random ball Two
        Random random = new Random();
        int section1 = random.nextInt(gameThreeArrayList.size());
        Log.d("section1", String.valueOf(section1));
        tvTwo.setText(gameThreeArrayList.get(section1).getWord());
        typeBallTwo = gameThreeArrayList.get(section1).getType();
        answerCorrectTwo = gameThreeArrayList.get(section1).getAnswerCorrect();
        Log.d("typeCorrect1 : ", typeBallTwo);

    }

    private void setTextToBallThree(ArrayList<GameThree> gameThreeArrayList) {
        //random ball three
        Random random = new Random();
        int section2 = random.nextInt(gameThreeArrayList.size());
        Log.d("section2", String.valueOf(section2));
        tvThree.setText(gameThreeArrayList.get(section2).getWord());
        typeBallThree = gameThreeArrayList.get(section2).getType();
        answerCorrectThree = gameThreeArrayList.get(section2).getAnswerCorrect();

    }


    @Override
    public void displayGameThree(int index, ArrayList<GameThree> gameThreeArrayList) {
        progressBarLoad.dismiss();
        this.gameThreeArrayList = gameThreeArrayList;
        setTextToBallOne(gameThreeArrayList);
        setTextToBallTwo(gameThreeArrayList);
        setTextToBallThree(gameThreeArrayList);


        fallingBallOne();
        fallingBallTwo();
        fallingBallThree();
    }

    @Override
    public void onCancel(String messageError) {

    }
}
