package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.adapter.GridViewAnswerAdapter;
import com.project.finalyear.thaispellinggame.adapter.GridViewSuggestAdapter;
import com.project.finalyear.thaispellinggame.common.Common;
import com.project.finalyear.thaispellinggame.controller.GameTwoController;
import com.project.finalyear.thaispellinggame.model.GameTwoModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Namwan on 5/4/2018.
 */

public class GameTwoFragment extends Fragment implements GameTwoController.GameTwoCallBack {

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;
    private String roomId = null;

    TextView tvTimer;
    CountDownTimer countDownTimer;
    ImageView imgCorrectOne, imgWrongOne;
    ImageView imgCorrectTwo, imgWrongTwo;
    CircleImageView imgPlayer1, imgPlayer2;
    TextView tvScoreOne, tvScoreTwo;
    ImageView imgBonus;
    Animation animation;

    private int score;
    private int counter = 0;
    ArrayList<String> answerCorrect = new ArrayList();

    private GameTwoController gameTwoController;
    private ArrayList<GameTwoModel> gameTwoArrayList;

    public static void showToast(Context mContext, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public List<String> suggestSource = new ArrayList<>();

    public GridViewAnswerAdapter answerAdapter;
    public GridViewSuggestAdapter suggestAdapter;

    public Button btnSubmit;

    public GridView gridViewAnswer, gridViewSuggest;

    public TextView textQuestion;

    public String answer;

    public String correct_answer;
    public String[] correctAnswerArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game_two, container, false);

        //Init View
        initView(view);

        score = 0;
        CountDownTimer();
        queryUserId();

        gameTwoController = new GameTwoController(this);
        loadDataGameTwo();

        return view;
    }

    private void initView(View view) {

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

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
        imgBonus = (ImageView) view.findViewById(R.id.imgBonus);

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);

        gridViewAnswer = (GridView) view.findViewById(R.id.gridAnswer);
        gridViewSuggest = (GridView) view.findViewById(R.id.gridViewSuggest);

        textQuestion = (TextView) view.findViewById(R.id.txtQuestion);

        //Add SetupList Here
        //setupList();

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i = 0; i < Common.user_submit_answer.length; i++) {
                    result += String.valueOf(Common.user_submit_answer[i]);
                }
                if (result.equals(correct_answer)) {
                    Toast.makeText(getActivity(), "Finish ! This is " + result,
                            Toast.LENGTH_SHORT).show();

                    checkAnswer(true);

                    //Reset
                    Common.count = 0;
                    Common.user_submit_answer = new String[correctAnswerArray.length];

                    //Set Adapter
                    GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(setupNullList(correctAnswerArray), getActivity());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter(suggestSource, getActivity(), GameTwoFragment.this);
                    gridViewSuggest.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();

                    answerCorrect.add(correct_answer);
                    setupList(gameTwoArrayList);
                } else {
                    Toast.makeText(getActivity(), "Incorrect!!", Toast.LENGTH_SHORT).show();

                    answerCorrect.add(correct_answer);
                    setupList(gameTwoArrayList);
                }
            }
        });
    }

    public void checkAnswer(boolean b) {

        if (b == true) {

            score += 50;
            counter++;
            ((GameActivity) getActivity()).soundEffect(true);

            showMarkOne(true);

            if (counter == 5) {
                imgBonus.setVisibility(View.VISIBLE);
                imgBonus.startAnimation(animation);
                score += 250;
                counter = 0; //set counter = 0 เพื่อนับ 1 ใหม่

            } else {
                imgBonus.setVisibility(View.INVISIBLE);
            }


        } else {
            score -= 5;
            counter = 0;

            showMarkOne(false);
            ((GameActivity) getActivity()).soundEffect(false);
        }

        updateScore(score);
        showPlayerScore();

    }

    private void loadDataGameTwo() {
        gameTwoController.dataPull();
    }

    private void setupList(ArrayList<GameTwoModel> gameTwoArrayList) {

        //Random question
        Random random = new Random();
        int vocabSelected = random.nextInt(gameTwoArrayList.size());

        String Question = gameTwoArrayList.get(vocabSelected).getMean();
        textQuestion.setText("ความหมาย: " + Question);

        correct_answer = gameTwoArrayList.get(vocabSelected).getWord();
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/") + 1);

        //-----------------------------------
        correctAnswerArray = new String[correct_answer.length()];

        // วนลูปเพือหาตัววรรณยุกต์
        for (int i = 0; i < correct_answer.length(); i++) {
            char charSequence = correct_answer.charAt(i);
            if (charSequence == 'ิ' || charSequence == 'ี' || charSequence == 'ึ' || charSequence == 'ื' || charSequence == 'ุ'
                    || charSequence == 'ู' || charSequence == '่' || charSequence == '้' || charSequence == '๊'
                    || charSequence == '๋' || charSequence == '็' || charSequence == '์' || charSequence == 'ั') {

                correctAnswerArray[i - 1] += charSequence;

            } else {
                correctAnswerArray[i] = "" + charSequence;
            }
        }

        correctAnswerArray = removeNull(correctAnswerArray);

        answer = correct_answer;
        Common.user_submit_answer = new String[correctAnswerArray.length];

        //Add Answer character to List
        suggestSource.clear();
        for (String item : correctAnswerArray) {
            //Add vocabulary to list
            suggestSource.add(String.valueOf(item));
        }

        //Random and some character to list
        for (int i = correctAnswerArray.length; i < correctAnswerArray.length * 2; i++)
            suggestSource.add(Common.alphabet_character[random.nextInt(Common.alphabet_character.length)]);

        //Sort random
        Collections.shuffle(suggestSource);

        //Set for GridView
        answerAdapter = new GridViewAnswerAdapter(setupNullList(correctAnswerArray), getActivity());
        suggestAdapter = new GridViewSuggestAdapter(suggestSource, getActivity(), this);

        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();

        gridViewSuggest.setAdapter(suggestAdapter);
        gridViewAnswer.setAdapter(answerAdapter);

    }

    //clear null in array
    private String[] removeNull(String[] a) {
        ArrayList<String> removedNull = new ArrayList<String>();
        for (String str : a)
            if (str != null)
                removedNull.add(str);
        return removedNull.toArray(new String[0]);
    }


    private String[] setupNullList(String[] correctAnswer) {
        String result[] = new String[correctAnswer.length];
        for (int i = 0; i < correctAnswer.length; i++)
            result[i] = " ";

        return result;
    }

    //ดึงคำศัพท์จาก firebase มาแสดง
    @Override
    public void displayGameTwo(int index, ArrayList<GameTwoModel> gameTwoArrayList) {
        this.gameTwoArrayList = gameTwoArrayList;
        setupList(gameTwoArrayList);
    }

    @Override
    public void onCancel(String messageError) {

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
                savedWord(answerCorrect);
            }
        }.start();
    }

    public void savedWord(ArrayList<String> correctAnswer) {
        addScoreToDatabase();
        Fragment fragment = new SumGameTwoFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("correctAnswer", correctAnswer);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(R.id.game, fragment);
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

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("scoreRound2").setValue(score);
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
}
