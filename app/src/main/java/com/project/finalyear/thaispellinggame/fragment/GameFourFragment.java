package com.project.finalyear.thaispellinggame.fragment;

import android.content.ClipData;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import com.project.finalyear.thaispellinggame.activity.SummaryActivity;
import com.project.finalyear.thaispellinggame.model.GameFourModel;
import com.project.finalyear.thaispellinggame.model.HeaderGame;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class GameFourFragment extends Fragment implements View.OnTouchListener {


    Button btnVocab, btnCactus, btnCactusLong, btnCamel;
    LinearLayout leftLinear, rightLinear, midLinear;
    CountDownTimer countDownTimer;
    MediaPlayer soundGameFour;
    ImageView imgBonus;
    Animation animShake;

    MediaPlayer wrong;
    MediaPlayer correct;
    MediaPlayer soundWow;

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private String user_id = null;
    private String roomId = null;

    private String word;
    private String type;
    private String answer;
    TextView tvTimer;
    ArrayList<String> answerCorrect = new ArrayList();
    ArrayList<String> playerSelect = new ArrayList();

    ImageView imgCorrectOne, imgWrongOne;
    ImageView imgCorrectTwo, imgWrongTwo;
    CircleImageView imgPlayer1, imgPlayer2;
    TextView tvScoreOne, tvScoreTwo;

    int score;
    int counter = 0;

    public GameFourFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_four, container, false);

        initInstance(view);

        score = 0;
        CountDownTimer();
        queryUserId();

        randomImageButton();
        setVocabulary();

        leftLinear.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:

                        final Animation animMoveLeft = AnimationUtils.loadAnimation(getContext(), R.anim.move_left);
                        btnVocab.startAnimation(animMoveLeft);

                        if (checkAnswerLeft(type)) {

                            // เรียกใช้ class จาก activity มาใช้ใน fragment
                            ((GameActivity) getActivity()).soundEffect(true);
                            score += 50;
                            updateScore(score);
                            showPlayerScore();
                            showMarkOne(true);


                            answerCorrect.add(answer);
                            playerSelect.add(word);

                        } else {
                            // เรียกใช้ class จาก activity มาใช้ใน fragment
                            ((GameActivity) getActivity()).soundEffect(false);
                            //score += 0;
                            //updateScore(score);
                            showPlayerScore();
                            showMarkOne(false);

                            answerCorrect.add(answer);
                            playerSelect.add(word);
                        }

                        randomImageButton();
                        btnVocab.setAnimation(animShake);
                        setVocabulary();

                        break;
                    case DragEvent.ACTION_DROP:
                        return (true);
                    case DragEvent.ACTION_DRAG_ENDED:

                        return (true);
                    default:
                        break;
                }
                return true;
            }
        });

        rightLinear.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final int action = event.getAction();

                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        final Animation animMoveRight = AnimationUtils.loadAnimation(getContext(), R.anim.move_right);
                        btnVocab.startAnimation(animMoveRight);

                        if (checkAnswerRight(type)) {

                            // เรียกใช้ class จาก activity มาใช้ใน fragment
                            ((GameActivity) getActivity()).soundEffect(true);
                            counter++;

                            // ถ้าตอบติดกัน 5 ข้อ ได้คะแนนโบนัส 250 คะแนน
                            final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
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

                            answerCorrect.add(answer);
                            playerSelect.add(word);
                            Log.d("answerCorrect", String.valueOf(answerCorrect));
                            Log.d("playerSelect", String.valueOf(playerSelect));


                        } else {
                            // เรียกใช้ class จาก activity มาใช้ใน fragment
                            ((GameActivity) getActivity()).soundEffect(false);
                            counter = 0;

                            //score += 0;
                            //updateScore(score);
                            showPlayerScore();
                            showMarkOne(false);

                            answerCorrect.add(answer);
                            playerSelect.add(word);


                        }

                        randomImageButton();
                        btnVocab.setAnimation(animShake);
                        setVocabulary();

                        break;
                    case DragEvent.ACTION_DROP:
                        return (true);
                    case DragEvent.ACTION_DRAG_ENDED:

                        return (true);
                    default:
                        break;
                }
                return true;

            }
        });
        return view;
    }

    private void initInstance(View view) {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        btnCactus = (Button) view.findViewById(R.id.btnCactus);
        btnCactusLong = (Button) view.findViewById(R.id.btnCactusLong);
        btnCamel = (Button) view.findViewById(R.id.btnCamel);

        leftLinear = (LinearLayout) view.findViewById(R.id.drop_left);
        rightLinear = (LinearLayout) view.findViewById(R.id.drop_right);
        midLinear = (LinearLayout) view.findViewById(R.id.drop_mid);
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
                savedWord(answerCorrect, playerSelect);
            }
        }.start();
    }

    private void randomImageButton() {
        int[] image = {R.drawable.camel, R.drawable.cactus, R.drawable.cactus_long};
        Random random = new Random();
        int n = random.nextInt(3);

        btnCamel.setVisibility(View.INVISIBLE);
        btnCactus.setVisibility(View.INVISIBLE);
        btnCactusLong.setVisibility(View.INVISIBLE);


        switch (n) {
            case 0:
                btnCamel.setVisibility(View.VISIBLE);
                btnVocab = btnCamel;
                btnVocab.setBackgroundResource(image[n]);

                break;
            case 1:
                btnCactus.setVisibility(View.VISIBLE);
                btnVocab = btnCactus;
                btnVocab.setBackgroundResource(image[n]);
                break;
            case 2:
                btnCactusLong.setVisibility(View.VISIBLE);
                btnVocab = btnCactusLong;
                btnVocab.setBackgroundResource(image[n]);
                break;
            default:
                break;
        }

        animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        btnVocab.setAnimation(animShake);

        btnVocab.setOnTouchListener(this);

    }

    private void setVocabulary() {
        mRefDatabase.child("Game_four").addListenerForSingleValueEvent(new ValueEventListener() {

            GameFourModel gameFourModel = new GameFourModel();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int vocabCount = (int) dataSnapshot.getChildrenCount();

                Random random = new Random();
                int rand = random.nextInt(vocabCount);

                Iterator itr = dataSnapshot.getChildren().iterator();
                for (int i = 0; i < rand; i++) {
                    itr.next();
                }
                DataSnapshot childSnapshot = (DataSnapshot) itr.next();
                gameFourModel = childSnapshot.getValue(GameFourModel.class);

                Log.v("myValue", gameFourModel.toString());


                btnVocab.setText(gameFourModel.getWord());

                word = gameFourModel.getWord();
                type = gameFourModel.getType();
                answer = gameFourModel.getAnswer();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private Boolean checkAnswerLeft(String type) {

        if (type.equals("wrong")) {
            return true;
        }
        return false;

    }

    private Boolean checkAnswerRight(String type) {

        if (type.equals("correct")) {
            return true;
        }
        return false;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
            v.startDrag(data, shadow, v, 0);
        }
        return false;
    }

    public void savedWord(ArrayList<String> playerAnswer, ArrayList<String> correctAnswer) {
        addScoreToDatabase();
        Fragment fragment = new SumGameFourFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("playerSelect", playerAnswer);
        bundle.putStringArrayList("correctAnswer", correctAnswer);
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

                mRefDatabase.child("rooms").child(roomId).child(current_uid).child("scoreRound3").setValue(score);
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
}
