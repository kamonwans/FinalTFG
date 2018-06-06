package com.project.finalyear.thaispellinggame.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.project.finalyear.thaispellinggame.R;

public class GameFourTutorialFragment extends Fragment {
    CountDownTimer countDownTimer;
    private static TextView tvTimer;
    CircleProgressbar circleProgressbar;

    public GameFourTutorialFragment() {
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
        View view = inflater.inflate(R.layout.fragment_game_four_tutorial, container, false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view) {
        tvTimer = (TextView) view.findViewById(R.id.tvTimer);
         circleProgressbar = (CircleProgressbar) view.findViewById(R.id.yourCircularProgressbar);
        circleProgressbar.setForegroundProgressColor(Color.CYAN);
        circleProgressbar.setBackgroundProgressWidth(15);
        circleProgressbar.setForegroundProgressWidth(20);
        circleProgressbar.enabledTouch(true);
        circleProgressbar.setRoundedCorner(true);
        circleProgressbar.setClockwise(true);
        int animationDuration = 10000; // 10000ms = 10s
        circleProgressbar.setProgressWithAnimation(100, animationDuration);
        CountDownTimer();
    }

    private void CountDownTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String strTime = String.format("%d", millisUntilFinished / 1000);
                tvTimer.setText(String.valueOf(strTime));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");
                FragmentGameFour();
            }
        }.start();
    }
    public void FragmentGameFour() {
        Fragment fragment = new GameFourFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
