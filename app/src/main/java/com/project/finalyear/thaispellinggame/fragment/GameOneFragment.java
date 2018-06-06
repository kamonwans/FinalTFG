package com.project.finalyear.thaispellinggame.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.controller.GameOneController;
import com.project.finalyear.thaispellinggame.model.GameOne;
import com.project.finalyear.thaispellinggame.model.HeaderGame;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class GameOneFragment extends Fragment implements GameOneController.GameOneCallBack {

    CountDownTimer countDownTimer;
    private TextView tvTimer, tvMeaning, tvScoreOne, tvScoreTwo;
    private CircleImageView imgPlayer1, imgPlayer2;
    public Button btnChoiceOne, btnChoiceTwo, btnChoiceThree, button;
    private Firebase firebase;
    private List<String> list = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    private static final String URL_Firebase = "https://thaispellinggame-28cfe.firebaseio.com/Game_one";
    private int currentGameOneIndex;
    private ArrayList<GameOne> gameOneArrayList;
    ArrayList<String> selectAnswer = new ArrayList<String>();
    ArrayList<String> answerRight = new ArrayList<String>();

    // Intent intent;
    int score = 0;
    int counter = 0;
    String scoreText;
    String correctAnswer;

    ImageView imgBonus;
    ImageView imgCorrectOne, imgWrongOne;
    ImageView imgCorrectTwo, imgWrongTwo;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;
    private String roomId = null;

    String answer;
    String selectedWordsFour;
    Context context;

    ProgressDialog progressBarLoad;
    private GameOneController gameOneController;

    public GameOneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_one, container, false);
        Firebase.setAndroidContext(getContext());
        context = getActivity().getApplicationContext();


        initInstance(view);
        gameOneController = new GameOneController(this);
        loadDataGameOne();

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

        tvTimer = (TextView) view.findViewById(R.id.tvTimerHeader);
        btnChoiceOne = (Button) view.findViewById(R.id.btnChoiceOne);
        btnChoiceTwo = (Button) view.findViewById(R.id.btnChoiceTwo);
        btnChoiceThree = (Button) view.findViewById(R.id.btnChoiceThree);
        tvMeaning = (TextView) view.findViewById(R.id.tvMeaning);
        tvScoreOne = (TextView) view.findViewById(R.id.tvScoreOne);
        tvScoreTwo = (TextView) view.findViewById(R.id.tvScoreTwo);
        imgPlayer1 = (CircleImageView) view.findViewById(R.id.imgPlayer1);
        imgPlayer2 = (CircleImageView) view.findViewById(R.id.imgPlayer2);

        imgBonus = (ImageView) view.findViewById(R.id.imgBonus);
        imgCorrectOne = (ImageView) view.findViewById(R.id.imgCorrectOne);
        imgWrongOne = (ImageView) view.findViewById(R.id.imgWrongOne);
        imgCorrectTwo = (ImageView) view.findViewById(R.id.imgCorrectTwo);
        imgWrongTwo = (ImageView) view.findViewById(R.id.imgWrongTwo);

        firebase = new Firebase(URL_Firebase);

        btnChoiceOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(btnChoiceOne);
            }
        });
        btnChoiceTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(btnChoiceTwo);
            }
        });
        btnChoiceThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Click(btnChoiceThree);
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
                savedWord(selectAnswer, answerRight);
            }
        }.start();
    }

    private void addScoreToDatabase() {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }
                roomId = dataSnapshot.child(user_id).child("roomId").getValue().toString();

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("scoreRound1").setValue(score);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadDataGameOne() {
        progressBarLoad = new ProgressDialog(getContext());
        progressBarLoad.setMessage("กำลังโหลดคำศัพท์");
        progressBarLoad.show();
        // เรียก pullData จาก GameThreeController
        gameOneController.dataPull();
    }

    public void Click(Button btn) {
        answerIsRight(btn);

    }


    public void answerIsRight(Button btn) {

        answer = btn.getText().toString();
        // check playerSelect ตรงกับที่เลือกไหม

        String selectedWordsFour = correctAnswer.toString();
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        if (answer.equals(selectedWordsFour)) {

            // เรียกใช้ class จาก activity มาใช้ใน fragment
            ((GameActivity) getActivity()).soundEffect(true);

            counter++;

            // ถ้าตอบติดกัน 5 ข้อ ได้คะแนนโบนส 250 คะแนน
            if (counter == 5) {

                imgBonus.setVisibility(View.VISIBLE);
                imgBonus.startAnimation(animation);
                score += 250;
                counter = 0; //set counter = 0 เพื่อนับ 1 ใหม่
            } else {
                imgBonus.setVisibility(View.INVISIBLE);
            }

            score += 50;
            updateScore(score);
            showPlayerScore();
            showMarkOne(true);

        } else if (!answer.equals(selectedWordsFour)) {

            ((GameActivity) getActivity()).soundEffect(false);

            counter = 0;
            imgBonus.setVisibility(View.INVISIBLE);
            //score += 0;
            //updateScore(score);
            showPlayerScore();
            showMarkOne(false);
        }

        //บันทึกคำตอบที่ผู้เล่นเลือก และเฉลยลงใน array list
        selectAnswer.add(answer);
        answerRight.add(selectedWordsFour);
        setText(gameOneArrayList);
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


    public void savedWord(ArrayList<String> selectAnswer, ArrayList<String> answerRight) {
        addScoreToDatabase();
        Fragment fragment = new SumGameOneFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("arrayListAnswerSelect", selectAnswer);
        bundle.putStringArrayList("arrayListAnswerRight", answerRight);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }


    public void setText(ArrayList<GameOne> gameOneArrayList) {
        Random random = new Random();
        int section = random.nextInt(gameOneArrayList.size());
        btnChoiceOne.setText(gameOneArrayList.get(section).getChoiceA());
        btnChoiceTwo.setText(gameOneArrayList.get(section).getChoiceB());
        btnChoiceThree.setText(gameOneArrayList.get(section).getChoiceC());
        tvMeaning.setText(gameOneArrayList.get(section).getMeaning());
        correctAnswer = (gameOneArrayList.get(section).getCorrectAnswer());
    }

    @Override
    public void displayGameOne(int index, ArrayList<GameOne> gameOneArrayList) {
        progressBarLoad.dismiss();
        this.gameOneArrayList = gameOneArrayList;
        setText(gameOneArrayList);
    }


    @Override
    public void onCancel(String messageError) {

    }

    // ทำงานเมื่อเปิด app เข้ามา
    public void onResume() {
        super.onResume();

    }

    // ทำงานเมื่อแอปหยุดทำงานชั่วคราว
    public void onPause() {
        super.onPause();
//        countDownTimer.cancel();

    }

    // ทำงานเมื่อแอปถูปิดลง
    public void onDestroy() {
        super.onDestroy();

//        countDownTimer.cancel();


    }

}

