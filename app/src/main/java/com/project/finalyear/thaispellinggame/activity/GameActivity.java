package com.project.finalyear.thaispellinggame.activity;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.controller.RandomGame;
import com.project.finalyear.thaispellinggame.fragment.GameFiveTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameFourTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameOneTutorialFragment;

import com.project.finalyear.thaispellinggame.fragment.GameThreeTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameTwoFragment;
import com.project.finalyear.thaispellinggame.fragment.GameTwoTutorialFragment;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSoundEffect;
    private Button btnNoSoundEffect;
    private Button btnNoSoundBG;
    private Button btnSoundBG;
    private Button btnSetting;
    private RelativeLayout soundBG, soundEffect;

    CountDownTimer countDownTimer;
    TextView tvTimer;

    private Animation animScale;
    public MediaPlayer soundCorrect;
    public MediaPlayer soundWrong;
    public MediaPlayer soundWow;
    MediaPlayer playSoundBG;

    private Boolean showVisible = false;
    private boolean isEnableSoundEffect = true;

    private RandomGame randomGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        playSoundBG = MediaPlayer.create(this, R.raw.sound_game_1);
        playSoundBG.start();
        playSoundBG.setLooping(true);

        //randomGame = new RandomGame(this);
        //FragmentGameFiveTutorial();
        FragmentGameOneTutorial();
        //FragmentGameFourTutorial();
        //FragmentGameTwoTutorial();
        //FragmentGameThreeTutorial();
        //FragmentGameBlank();

        initInstance();
    }

    private void initInstance() {
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSoundEffect = (Button) findViewById(R.id.btnSoundEffect);
        btnNoSoundEffect = (Button) findViewById(R.id.btnNoSoundEffect);
        btnSoundBG = (Button) findViewById(R.id.btnSoundBG);
        btnNoSoundBG = (Button) findViewById(R.id.btnNoSoundBG);
        tvTimer = (TextView) findViewById(R.id.tvTimerHeader);

        animScale = AnimationUtils.loadAnimation(this, R.anim.scale);
        soundBG = (RelativeLayout) findViewById(R.id.soundBG);
        soundEffect = (RelativeLayout) findViewById(R.id.soundEffect);
        soundCorrect = MediaPlayer.create(this, R.raw.correct);
        soundWrong = MediaPlayer.create(this, R.raw.wrong);


        btnSetting.setOnClickListener(this);
        btnSoundBG.setOnClickListener(this);
        btnNoSoundBG.setOnClickListener(this);
        btnSoundEffect.setOnClickListener(this);
        btnNoSoundEffect.setOnClickListener(this);
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
                soundCorrect.start();
            } else {
                soundWrong.start();
            }
        }
    }

    public void FragmentGameOneTutorial() {
        Fragment fragment = new GameOneTutorialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    public void FragmentGameTwoTutorial() {
        Fragment fragment = new GameTwoTutorialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

    }

    public void FragmentGameThreeTutorial() {
        Fragment fragment = new GameThreeTutorialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }


    public void FragmentGameFourTutorial() {
        Fragment fragment = new GameFourTutorialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void FragmentGameFiveTutorial() {
        Fragment fragment = new GameFiveTutorialFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
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
                playSoundBG.pause();
                break;
            case R.id.btnNoSoundBG:
                btnSoundBG.setVisibility(View.VISIBLE);
                btnNoSoundBG.setVisibility(View.INVISIBLE);
                playSoundBG.start();
                break;
            case R.id.btnSetting:
                view.startAnimation(animScale);
                Toggle();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        playSoundBG.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playSoundBG.pause();
    }
}
