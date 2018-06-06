package com.project.finalyear.thaispellinggame.model;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.project.finalyear.thaispellinggame.R;

/**
 * Created by Namwan on 3/17/2018.
 */

public class ButtonSetting{

    private Button btnSetting;
    private Button btnSoundEffect;
    private Button btnSoundBG;
    private Button btnNoSoundEffect;
    private Button btnNoSoundBG;

    private Boolean showVisible, showBtnSoundBG, showBtnSoundEffect;
    private RelativeLayout soundBG, soundEffect;

    private Animation animScale;

    public void set(Activity activity){

        btnSetting = (Button) activity.findViewById(R.id.btnSetting);
        btnSoundEffect = (Button) activity.findViewById(R.id.btnSoundEffect);
        btnSoundBG = (Button) activity.findViewById(R.id.btnSoundBG);
        btnNoSoundEffect = (Button) activity.findViewById(R.id.btnNoSoundEffect);
        btnNoSoundBG = (Button) activity.findViewById(R.id.btnNoSoundBG);

        soundBG = (RelativeLayout) activity.findViewById(R.id.soundBG);
        soundEffect = (RelativeLayout) activity.findViewById(R.id.soundEffect);

        animScale = AnimationUtils.loadAnimation(activity, R.anim.scale);

        showVisible = false;
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animScale);
                Toggle();
            }
        });

        btnSoundBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSoundBG.setVisibility(View.INVISIBLE);
                btnNoSoundBG.setVisibility(View.VISIBLE);
            }
        });

        btnNoSoundBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSoundBG.setVisibility(View.VISIBLE);
                btnNoSoundBG.setVisibility(View.INVISIBLE);
            }
        });

        btnSoundEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSoundEffect.setVisibility(View.INVISIBLE);
                btnNoSoundEffect.setVisibility(View.VISIBLE);
            }
        });

        btnNoSoundEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSoundEffect.setVisibility(View.VISIBLE);
                btnNoSoundEffect.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void Toggle() {

        if (showVisible == false) {

            soundBG.setVisibility(View.VISIBLE);
            soundEffect.setVisibility(View.VISIBLE);
            showVisible = true;


        }else {

            soundBG.setVisibility(View.INVISIBLE);
            soundEffect.setVisibility(View.INVISIBLE);
            showVisible = false;

        }

    }

}
