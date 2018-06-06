package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.activity.SummaryActivity;
import com.project.finalyear.thaispellinggame.adapter.SumRoundOneAdapter;
import com.project.finalyear.thaispellinggame.controller.RandomGame;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.finalyear.thaispellinggame.model.Constants.FIRST_COLUMN;
import static com.project.finalyear.thaispellinggame.model.Constants.SECOND_COLUMN;

public class SumGameThreeFragment extends Fragment {
    ListView listViewAnswer;
    TextView tvAnswerSelect;
    TextView tvTimer;
    String totalScore;

    CountDownTimer countDownTimer;
    RandomGame randomGame;
    Context context;

    ArrayList<String> playerAnswer = new ArrayList<>();
    ArrayList<String> correctAnswer = new ArrayList<>();


    public SumGameThreeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sum_game_three, container, false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view) {
        listViewAnswer = (ListView) view.findViewById(R.id.listViewAnswer);
        tvAnswerSelect = (TextView) view.findViewById(R.id.tvAnswerSelect);
        tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        showAnswer();
        countDownTimer();
    }
    public void countDownTimer() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

                String time = String.format("%d", l / 1000);
                tvTimer.setText(String.valueOf(time));

            }

            @Override
            public void onFinish() {
                tvTimer.setText("0");

                //randomGame = new RandomGame((GameActivity) getActivity());
                GameFiveFragment fragment = new GameFiveFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game, fragment)
                        .commit();


            }
        }.start();
    }
    private void showAnswer() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp = new HashMap<String, String>();
        if (getArguments() != null) {
            // ดึงค่าจาก ArrayList มาแสดง
            Bundle bundle = getArguments();
            playerAnswer = bundle.getStringArrayList("playerSelect");
            correctAnswer = bundle.getStringArrayList("rightAnswer");
//            totalScore = bundle.getString("scoreRoundThree");
            Log.d("sizeTemp2", playerAnswer.toString());

            for (int i = 0; i < correctAnswer.size(); i++) {
                temp = new HashMap<String, String>();  // new temp
                temp.put(FIRST_COLUMN,correctAnswer.get(i).toString()); // add คำที่เลือกใส่ column
                temp.put(SECOND_COLUMN, playerAnswer.get(i).toString()); // add คำที่ถูกต้องใส่ column
                list.add(temp); // add temp ลงใน list(แถว)
                Log.d("sizeTemp", playerAnswer.get(i).toString());
                Log.d("sizeTemp1", correctAnswer.get(i).toString());
            }
        }
        SumRoundOneAdapter adapter = new SumRoundOneAdapter(getActivity(), list);
        listViewAnswer.setAdapter(adapter);
    }
}
