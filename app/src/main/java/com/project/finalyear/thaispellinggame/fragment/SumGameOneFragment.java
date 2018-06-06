package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.adapter.SumRoundOneAdapter;
import com.project.finalyear.thaispellinggame.controller.RandomGame;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.finalyear.thaispellinggame.model.Constants.FIRST_COLUMN;
import static com.project.finalyear.thaispellinggame.model.Constants.SECOND_COLUMN;

public class SumGameOneFragment extends Fragment {
    ListView listViewAnswer;
    ArrayList<String> answerSelect;
    ArrayList<String> answerRight;
    TextView tvAnswerSelect;

    CountDownTimer countDownTimer;
    RandomGame randomGame;
    Context context;

    TextView tvTimer;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    public SumGameOneFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sum_game_one,
                container, false);
        initInstance(view);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        return view;
    }

    private void initInstance(View view) {
        listViewAnswer = (ListView) view.findViewById(R.id.listViewAnswer);
        tvAnswerSelect = (TextView) view.findViewById(R.id.tvAnswerSelect);
        tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        showAnswer();
        countDown();
    }
    public void countDown() {
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

                GameTwoTutorialFragment fragment = new GameTwoTutorialFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.game, fragment)
                        .commit();

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
            answerRight = bundle.getStringArrayList("arrayListAnswerRight");
            answerSelect = bundle.getStringArrayList("arrayListAnswerSelect");
            Log.d("sizeTemp", answerSelect.toString());

            for (int j = 0; j < answerSelect.size(); j++) {
                temp = new HashMap<String, String>();  // new temp
                temp.put(FIRST_COLUMN, answerSelect.get(j).toString()); // add คำที่เลือกใส่ column
                temp.put(SECOND_COLUMN, answerRight.get(j).toString()); // add คำที่ถูกต้องใส่ column
                list.add(temp); // add temp ลงใน list(แถว)
                Log.d("sizeTemp", answerSelect.get(j).toString());


            }
        }


        Log.d("temp", String.valueOf(temp.size()));
        Log.d("tempList", String.valueOf(list.size()));
        SumRoundOneAdapter adapter = new SumRoundOneAdapter(getActivity(), list);
        listViewAnswer.setAdapter(adapter);

    }

}
