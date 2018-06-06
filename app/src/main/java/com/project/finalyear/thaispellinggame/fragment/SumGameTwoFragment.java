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
import com.project.finalyear.thaispellinggame.activity.SummaryActivity;
import com.project.finalyear.thaispellinggame.adapter.SumRoundOneAdapter;
import com.project.finalyear.thaispellinggame.adapter.SumRoundTwoAdapter;
import com.project.finalyear.thaispellinggame.controller.RandomGame;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.finalyear.thaispellinggame.model.Constants.FIRST_COLUMN;
import static com.project.finalyear.thaispellinggame.model.Constants.SECOND_COLUMN;

public class SumGameTwoFragment extends Fragment {
    ListView listViewAnswer;
    TextView tvAnswerSelect;
    TextView tvTimer;

    CountDownTimer countDownTimer;
    Context context;

    ArrayList<String> correctAnswer = new ArrayList();
    public SumGameTwoFragment() {
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
       View view = inflater.inflate(R.layout.fragment_sum_game_two, container, false);
        initInstance(view);
        return view;
    }
    private void initInstance(View view) {

        listViewAnswer = (ListView) view.findViewById(R.id.listViewAnswer);
        tvAnswerSelect = (TextView) view.findViewById(R.id.tvAnswerSelect);
        tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        showAnswer();
        countDown();
    }
    private void countDown() {
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
                GameFourTutorialFragment fragment = new GameFourTutorialFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game, fragment)
                        .commit();
//                Intent intent = new Intent(getContext(),SummaryActivity.class);
//                startActivity(intent);
//                getActivity().finish();

            }
        }.start();
    }

    private void showAnswer() {

        // create row and column
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp = new HashMap<String, String>();

        if (getArguments() != null) {
            // ดึงค่าจาก ArrayList มาแสดง
            Bundle bundle = getArguments();
            correctAnswer = bundle.getStringArrayList("correctAnswer");

            for (int j = 0; j < correctAnswer.size(); j++) {
                temp = new HashMap<String, String>();  // new temp
                temp.put(FIRST_COLUMN, correctAnswer.get(j).toString()); // add คำที่เลือกใส่ column
                list.add(temp); // add temp ลงใน list(แถว)
                Log.d("sizeTemp", correctAnswer.get(j).toString());

            }
        }

        SumRoundTwoAdapter adapter = new SumRoundTwoAdapter(getActivity(), list);
        listViewAnswer.setAdapter(adapter);

    }
    // ทำงานเมื่อแอปหยุดทำงานชั่วคราว
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    // ทำงานเมื่อแอปถูปิดลง
    public void onDestroy() {
        super.onDestroy();

        countDownTimer.cancel();


    }

}
