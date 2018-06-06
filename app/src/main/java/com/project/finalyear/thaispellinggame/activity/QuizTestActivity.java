package com.project.finalyear.thaispellinggame.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.model.DatabaseHelper;
import com.project.finalyear.thaispellinggame.model.GameLearnModel;
import com.project.finalyear.thaispellinggame.model.LearningQuizModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class QuizTestActivity extends AppCompatActivity {

    final static int INTERVAL = 1000; // 1 second
    final static int TIMEOUT = 20000; // 20 seconds
    final static int TOTAL_QUESTION = 10;
    int progressValue = 0;

    private CountDownTimer mCountDownTimer;
    private List<LearningQuizModel> quizList = new ArrayList<>();
    private DatabaseHelper mDBHelper;
    int score = 0, thisQuestion = 0;
    int currentIndex;

    private ProgressBar mProgressBar;
    private ImageView imgQuestion;
    private Button btnA, btnB, btnC;
    private Button btnNext;
    private TextView txtQuestion;
    private String answer;
    private ImageView imgCorrect, imgIncorrect;
    TextView txtScore, txtNumQuestion;

    Random random = new Random();
    Animation animScale;
    AnimationSet animation;

    MediaPlayer wrong;
    MediaPlayer correct;

    ArrayList<String> playerAnswer = new ArrayList();
    ArrayList<String> correctAnswer = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_test);

        initInstance();
        wrong = MediaPlayer.create(this, R.raw.wrong);
        correct = MediaPlayer.create(this, R.raw.correct);

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);

        checkDatabase();

        randomQuiz();

//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (thisQuestion > 10) {
//                    Intent intent = new Intent(QuizTestActivity.this, QuizSumActivity.class);
//                    intent.putExtra("score", score);
//                    startActivity(intent);
//                    finish();
//                }
//
//                displayQuiz();
//
//            }
//        });


    }

    private void checkDatabase() {

        mDBHelper = new DatabaseHelper(this);
        try {
            mDBHelper.createDatabase();

            quizList = mDBHelper.getListQuiz();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void randomQuiz() {

        if(quizList.size() != 0) {

            currentIndex = random.nextInt(quizList.size() - 1);
            displayQuiz(currentIndex);
            quizList.remove(currentIndex);

            Log.d("mySum", Integer.toString(currentIndex) + " " + Integer.toString(quizList.size()));

        }
    }

    private void displayQuiz(int index) {

        thisQuestion++;

        if (thisQuestion > 10) {
            Intent intent = new Intent(QuizTestActivity.this, QuizSumActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("playerAnswer", playerAnswer);
            intent.putExtra("correctAnswer", correctAnswer);
            startActivity(intent);
            finish();

        }else {

            btnA.setBackgroundColor(Color.parseColor("#f5671b"));
            btnB.setBackgroundColor(Color.parseColor("#f5671b"));
            btnC.setBackgroundColor(Color.parseColor("#f5671b"));
            btnA.setEnabled(true);
            btnB.setEnabled(true);
            btnC.setEnabled(true);

            String numQuestion = Integer.toString(thisQuestion);
            txtNumQuestion.setText(numQuestion + "/10");

            //set image question
            Resources res = getResources();
            String mDrawableName = quizList.get(index).getImage();
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID);
            imgQuestion.setImageDrawable(drawable);

            txtQuestion.setText(quizList.get(index).getQuestion());
            btnA.setText(quizList.get(index).getChoiceA());
            btnB.setText(quizList.get(index).getChoiceB());
            btnC.setText(quizList.get(index).getChoiceC());
            answer = quizList.get(index).getAnswer();

            btnA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(animScale);
                    checkAnswer(btnA);
                }
            });

            btnB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(animScale);
                    checkAnswer(btnB);
                }
            });

            btnC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(animScale);
                    checkAnswer(btnC);
                }
            });
        }

    }

    public void setAlphaAnimation(ImageView img) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);

        img.setAnimation(animation);
        img.setVisibility(View.VISIBLE);
    }

    public void setDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s = 1000ms
                randomQuiz();
            }
        }, 1000);
    }

    private void checkAnswer(Button btn) {

        String userAnswer = btn.getText().toString();
        if (userAnswer.equals(answer)) {
            score++;
            String Score = Integer.toString(score);
            txtScore.setText(Score);

            correct.start();
            btn.setBackgroundColor(Color.parseColor("#43c73c"));

            playerAnswer.add(userAnswer);
            correctAnswer.add(answer);

//            setAlphaAnimation(imgCorrect);
//            imgCorrect.setVisibility(View.INVISIBLE);
//            imgIncorrect.setVisibility(View.INVISIBLE);


        } else {

            wrong.start();
            btn.setBackgroundColor(Color.parseColor("#ff8a80"));

            playerAnswer.add(userAnswer);
            correctAnswer.add(answer);

            //show correct answer
            if (btnA.getText().equals(answer)) {
                btnA.setBackgroundColor(Color.parseColor("#43c73c"));

            } else if (btnB.getText().equals(answer)) {
                btnB.setBackgroundColor(Color.parseColor("#43c73c"));

            } else {
                btnC.setBackgroundColor(Color.parseColor("#43c73c"));
            }

        }

        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);

        setDelay();
    }


    private void initInstance() {
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtNumQuestion = (TextView) findViewById(R.id.txtNumQuestion);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        //mProgressBar = (ProgressBar) findViewById(R.id.testProgressBar);
        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        //btnNext = (Button) findViewById(R.id.btnNext);
        imgQuestion = (ImageView) findViewById(R.id.imgQuestion);
        imgCorrect = (ImageView) findViewById(R.id.imgCorrect);
        imgIncorrect = (ImageView) findViewById(R.id.imgIncorrect);
    }
}
