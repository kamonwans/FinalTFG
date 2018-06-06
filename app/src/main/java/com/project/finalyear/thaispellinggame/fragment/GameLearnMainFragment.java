package com.project.finalyear.thaispellinggame.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.project.finalyear.thaispellinggame.R;

public class GameLearnMainFragment extends Fragment implements View.OnClickListener{
    private ImageView frameAnimation;
    private ImageView cloud1, cloud2;
    private Button startGame;
    private Button btnBack;

    private Animation animScale;

    private Button btnSoundEffect;
    private Button btnNoSoundEffect;
    private Button btnNoSoundBG;
    private Button btnSoundBG;
    private Button btnSetting;
    private RelativeLayout soundBG, soundEffect;
    private Boolean showVisible = false;
    private boolean isEnableSoundEffect = true;
    private MediaPlayer correct;
    private MediaPlayer wrong;
    private MediaPlayer sound;
    public GameLearnMainFragment() {
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
        View view = inflater.inflate(R.layout.fragment_game_learn_main, container, false);

        initInstance(view);

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);
//        sound = MediaPlayer.create(getContext(), R.raw.sound_game_4);
//        sound.start();
//        sound.setLooping(true);

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                v.startAnimation(animScale);
//
////                GameLearnMainActivity.super.onBackPressed();
////                finish();
//
//            }
//        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                FragmentGameLearn();

            }
        });

        TranslateAnimation animLeft = new TranslateAnimation(0f, 1500f, 0f, 0f);
        animLeft.setInterpolator(new LinearInterpolator());
        animLeft.setRepeatCount(Animation.INFINITE);
        animLeft.setDuration(4000);

        TranslateAnimation animRight = new TranslateAnimation(1000f, -1500f, 0f, 0f);
        animRight.setInterpolator(new LinearInterpolator());
        animRight.setRepeatCount(Animation.INFINITE);
        animRight.setDuration(4000);

        cloud1.startAnimation(animLeft);
        cloud2.startAnimation(animRight);

//        frameAnimation = (ImageView)findViewById(R.id.frameAnimation);
//        frameAnimation.setBackgroundResource(R.drawable.animation_learn);
//        AnimationDrawable myFrameAnimation = (AnimationDrawable) frameAnimation.getBackground();
//
//        myFrameAnimation.start();

        return view;
    }
    private void initInstance(View view) {

        cloud1 = (ImageView)view.findViewById(R.id.cloud1);
        cloud2 = (ImageView)view.findViewById(R.id.cloud2);
        startGame = (Button)view.findViewById(R.id.startGame);


        btnSetting = (Button) view.findViewById(R.id.btnSetting);
        btnSoundEffect = (Button) view.findViewById(R.id.btnSoundEffect);
        btnNoSoundEffect = (Button) view.findViewById(R.id.btnNoSoundEffect);
        btnSoundBG = (Button) view.findViewById(R.id.btnSoundBG);
        btnNoSoundBG = (Button) view.findViewById(R.id.btnNoSoundBG);
        soundBG = (RelativeLayout) view.findViewById(R.id.soundBG);
        soundEffect = (RelativeLayout) view.findViewById(R.id.soundEffect);

        correct = MediaPlayer.create(getContext(), R.raw.correct);
        wrong = MediaPlayer.create(getContext(), R.raw.wrong);
//        btnSetting.setOnClickListener(this);
//        btnSoundBG.setOnClickListener(this);
//        btnNoSoundBG.setOnClickListener(this);
//        btnSoundEffect.setOnClickListener(this);
//        btnNoSoundEffect.setOnClickListener(this);

        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.scale);


    }
    public void FragmentGameLearn() {
        Fragment fragment = new GameLearnFragment();
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
        }
    }
}
