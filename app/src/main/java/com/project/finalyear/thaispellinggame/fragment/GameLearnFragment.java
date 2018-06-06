package com.project.finalyear.thaispellinggame.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.model.DatabaseHelper;
import com.project.finalyear.thaispellinggame.model.GameLearnModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameLearnFragment extends Fragment implements View.OnTouchListener,View.OnClickListener {
    private List<GameLearnModel> mGameLearnList;
    private DatabaseHelper mDBHelper;

    private Button[] btn = new Button[9];
    private Button btnPlayAgain;
    private Button btnWin, btnLose;
    private ImageView happy, sad;
    private LinearLayout dragLinear;
    private LinearLayout basketLinear;

    private Handler handler;

    private TextView tvQuestion;
    private ArrayList<String> questionList;
    private ArrayList<String> type;
    private String section;
    private int sum = 0, countToFin = 0, game_over = 0;

    private Random random = new Random();
    private Animation animShake,animscale;
    private MediaPlayer correct;
    private MediaPlayer wrong;
    private MediaPlayer sound;

    private ProgressBar mProgressBar;
    private CountDownTimer mCountDownTimer;
    final static int INTERVAL = 1000; // 1 second
    final static int TIMEOUT = 30000; // 30 seconds
    int progressValue = 0;

    private Button btnSoundEffect;
    private Button btnNoSoundEffect;
    private Button btnNoSoundBG;
    private Button btnSoundBG;
    private Button btnSetting;
    private RelativeLayout soundBG, soundEffect;

    private Button btnBack;

    private Boolean showVisible = false;
    private boolean isEnableSoundEffect = true;
    private boolean isEnableSoundBG;

    private Animation animScale;
    private RelativeLayout bgLearn;


    public GameLearnFragment() {
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
        View view = inflater.inflate(R.layout.fragment_game_learn, container, false);

        initInstance(view);

        animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        animscale = AnimationUtils.loadAnimation(getContext(), R.anim.scale);

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);
        sound = MediaPlayer.create(getContext(), R.raw.sound_game_4);
        sound.start();
        sound.setLooping(true);

        checkDatabase();
//        mDBHelper = new DatabaseHelper(this);
//        mDBHelper.deleteDB(this);
//        Toast.makeText(GameLearnActivity.this, "DB Deleted!", Toast.LENGTH_SHORT).show();

        displayData();
        randomQuestion();
        setOnDragLinear();

        countDownTimer();
        mCountDownTimer.start();

        return view;
    }

    private void showBtnPlayAgain(){
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                btnPlayAgain.setVisibility(View.VISIBLE);
                btnPlayAgain.startAnimation(animShake);
            }
        }, 1500);
    }


    private void checkDatabase() {

        mDBHelper = new DatabaseHelper(getContext());
        try{
            mDBHelper.createDatabase();

            mGameLearnList = mDBHelper.getListGameLearn();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void countDownTimer() {

        mProgressBar.setProgress(progressValue);
        mCountDownTimer = new CountDownTimer(TIMEOUT,INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressValue++;
                mProgressBar.setProgress((int)progressValue * 100 / (TIMEOUT / INTERVAL));
            }

            @Override
            public void onFinish() {
                progressValue++;
                mCountDownTimer.cancel();
                mProgressBar.setProgress(100);

                dragLinear.setVisibility(View.INVISIBLE);

                btnLose.setText("คุณแพ้!");
                btnLose.setVisibility(View.VISIBLE);
                btnLose.setAnimation(animscale);
                btnWin.setVisibility(View.INVISIBLE);

                basketLinear.setVisibility(View.INVISIBLE);

                showBtnPlayAgain();
                //btnPlayAgain.setVisibility(View.VISIBLE);
                //btnPlayAgain.startAnimation(animShake);

                btnPlayAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentGameLearn();

                    }
                });
            }
        };
    }


    private void displayData() {
        questionList = new ArrayList<>();

        // add questions
        questionList.add("แม่กก");
        questionList.add("แม่กด");
        questionList.add("แม่กบ");
        questionList.add("แม่กน");
        questionList.add("แม่กม");
        questionList.add("แม่กง");
        questionList.add("แม่เกย");
        questionList.add("แม่เกอว");

        // random vocabulary
        type = new ArrayList<String>();

        for (int i = 0; i < 9; i++) {
            int index = random.nextInt(mGameLearnList.size());
            btn[i].setText(mGameLearnList.get(index).getWord());

            String text = mGameLearnList.get(index).getType();
            type.add(i, text);

            mGameLearnList.remove(index);
        }

    }

    private void randomQuestion() {

        if (questionList.size() != 0) {
            int index = random.nextInt(questionList.size());
            String typeSpell = (questionList.get(index));
            tvQuestion.setText(typeSpell);
            tvQuestion.startAnimation(animShake);

            questionList.remove(index);

            //check for random question
            for (int i = 0; i < type.size(); i++) {
                if (type.get(i).equals(tvQuestion.getText())) {
                    sum++; //วนลูปเก็บจำนวนคำตอบที่ถูกต้อง
                    Log.v("mySum" + type.get(i), String.valueOf(sum));
                }
            }

            if (sum == 0)
                randomQuestion();
        }
    }

    private void setOnDragLinear() {

        dragLinear.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                // TODO Auto-generated method stub
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // Executed after startDrag() is called.
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        if (checkAnswer(section)) {
                            game_over++;
                            View view = (View) event.getLocalState();
                            view.setVisibility(View.INVISIBLE);

                            soundEffect(true);
                            happy.setVisibility(View.VISIBLE);
                            happy.startAnimation(animscale);
                            happy.setVisibility(View.INVISIBLE);

                            if (game_over == 9) {
                                //Toast.makeText(GameLearnActivity.this, "Game Over", Toast.LENGTH_SHORT).show();
                                tvQuestion.setText("");

                                mCountDownTimer.cancel();
                                mProgressBar.setVisibility(View.INVISIBLE);

                                btnWin.setText("คุณชนะ!");
                                btnWin.setVisibility(View.VISIBLE);
                                btnWin.setAnimation(animscale);
                                btnLose.setVisibility(View.INVISIBLE);

                                basketLinear.setVisibility(View.INVISIBLE);

                                showBtnPlayAgain();
                                //btnPlayAgain.setVisibility(View.VISIBLE);
                                //btnPlayAgain.startAnimation(animShake);

                                btnPlayAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        FragmentGameLearn();

                                    }
                                });
                            }
                        } else {
                            soundEffect(false);
                            sad.setVisibility(View.VISIBLE);
                            sad.startAnimation(animscale);
                            sad.setVisibility(View.INVISIBLE);
                        }

                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // Executed after the Drag Shadow enters the drop area
                        break;
                    case DragEvent.ACTION_DROP:
                        //Executed when user drops the data
                        return (true);
                    case DragEvent.ACTION_DRAG_ENDED:
                        return (true);
                    default:
                        break;
                }
                return true;
            }
        });

        //set OnTouch to button
        for (int i = 0; i < 9; i++) {
            btn[i].setOnTouchListener(this);
        }
    }

    private boolean checkAnswer(String type) {

        if (type.equals(tvQuestion.getText())) {
            countToFin++; // ถ้าตอบถูกให้บวกค่าเพิ่ม

            if (countToFin == sum) {
                // set count and sum = 0 to start new question
                countToFin = 0;
                sum = 0;
                randomQuestion();
            }
            return true;
        }
        return false;
    }

    private void initInstance(View view) {

        btn[0] = (Button) view.findViewById(R.id.one);
        btn[1] = (Button) view.findViewById(R.id.two);
        btn[2] = (Button) view.findViewById(R.id.three);
        btn[3] = (Button) view.findViewById(R.id.four);
        btn[4] = (Button) view.findViewById(R.id.five);
        btn[5] = (Button) view.findViewById(R.id.six);
        btn[6] = (Button) view.findViewById(R.id.seven);
        btn[7] = (Button) view.findViewById(R.id.eight);
        btn[8] = (Button) view.findViewById(R.id.nine);
        dragLinear = (LinearLayout) view.findViewById(R.id.drag_it_linear);
        basketLinear = (LinearLayout) view.findViewById(R.id.drop_here_linear);
        tvQuestion = (TextView) view.findViewById(R.id.tv_question);
        btnPlayAgain = (Button) view.findViewById(R.id.btnPlayAgain);

        happy = (ImageView) view.findViewById(R.id.happy);
        sad = (ImageView) view.findViewById(R.id.sad);
        btnWin = (Button) view.findViewById(R.id.btn_win);
        btnLose = (Button) view.findViewById(R.id.btn_lose);


        btnSetting = (Button) view.findViewById(R.id.btnSetting);
        btnSoundEffect = (Button) view.findViewById(R.id.btnSoundEffect);
        btnNoSoundEffect = (Button) view.findViewById(R.id.btnNoSoundEffect);
        btnSoundBG = (Button) view.findViewById(R.id.btnSoundBG);
        btnNoSoundBG = (Button) view.findViewById(R.id.btnNoSoundBG);
        soundBG = (RelativeLayout) view.findViewById(R.id.soundBG);
        soundEffect = (RelativeLayout) view.findViewById(R.id.soundEffect);

        btnBack = (Button) view.findViewById(R.id.btnBack);

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);

        btnSetting.setOnClickListener(this);
        btnSoundBG.setOnClickListener(this);
        btnNoSoundBG.setOnClickListener(this);
        btnSoundEffect.setOnClickListener(this);
        btnNoSoundEffect.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale);

        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar_timer);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.one:
                    section = type.get(0);
                    setButtonDrag(v);
                    break;
                case R.id.two:
                    section = type.get(1);
                    setButtonDrag(v);
                    break;
                case R.id.three:
                    section = type.get(2);
                    setButtonDrag(v);
                    break;
                case R.id.four:
                    section = type.get(3);
                    setButtonDrag(v);
                    break;
                case R.id.five:
                    section = type.get(4);
                    setButtonDrag(v);
                    break;
                case R.id.six:
                    section = type.get(5);
                    setButtonDrag(v);
                    break;
                case R.id.seven:
                    section = type.get(6);
                    setButtonDrag(v);
                    break;
                case R.id.eight:
                    section = type.get(7);
                    setButtonDrag(v);
                    break;
                case R.id.nine:
                    section = type.get(8);
                    setButtonDrag(v);
                default:
                    break;
            }
            return true;
        }
        return false;
    }
    private void setButtonDrag(View v) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(v);
        v.startDrag(data, shadow, v, 0);
    }
    public void FragmentGameLearn() {
        Fragment fragment = new GameLearnFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMain() {
        Fragment fragment = new LearningMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void Toggle() {
        if (showVisible == false) {
            soundBG.setVisibility(View.VISIBLE);
            soundEffect.setVisibility(View.VISIBLE);
            showVisible = true;

        } else {
            soundBG.setVisibility(View.INVISIBLE);
            soundEffect.setVisibility(View.INVISIBLE);
            showVisible = false;
        }
    }
    public void soundEffect( boolean b) {
        if (isEnableSoundEffect) {
            if (b) {
                correct.start();
            } else {
                wrong.start();
            }
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSoundEffect:
                isEnableSoundEffect = false;
                btnSoundEffect.setVisibility(View.GONE);
                btnNoSoundEffect.setVisibility(View.VISIBLE);
                break;
            case R.id.btnNoSoundEffect:
                isEnableSoundEffect = true;
                btnSoundEffect.setVisibility(View.VISIBLE);
                btnNoSoundEffect.setVisibility(View.GONE);
                break;
            case R.id.btnSoundBG:
                btnSoundBG.setVisibility(View.INVISIBLE);
                btnNoSoundBG.setVisibility(View.VISIBLE);
                sound.pause();
                break;
            case R.id.btnNoSoundBG:
                btnSoundBG.setVisibility(View.VISIBLE);
                btnNoSoundBG.setVisibility(View.INVISIBLE);
                sound.start();
                break;
            case R.id.btnSetting:
                view.startAnimation(animScale);
                Toggle();
                break;
            case R.id.btnBack:
                FragmentLearningMain();
                break;
        }
    }


    // ทำงานเมื่อเปิด app เข้ามา
    public void onResume() {
        super.onResume();
    }

    // ทำงานเมื่อแอปหยุดทำงานชั่วคราว
    public void onPause() {
        super.onPause();
        sound.stop();
    }

    // ทำงานเมื่อแอปถูปิดลง
    public void onDestroy() {
        super.onDestroy();
        sound.stop();
    }
}
