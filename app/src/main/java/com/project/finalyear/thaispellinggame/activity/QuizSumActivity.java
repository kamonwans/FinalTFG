package com.project.finalyear.thaispellinggame.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.adapter.SumRoundOneAdapter;
import com.project.finalyear.thaispellinggame.adapter.SumRoundTwoAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.finalyear.thaispellinggame.model.Constants.FIRST_COLUMN;
import static com.project.finalyear.thaispellinggame.model.Constants.SECOND_COLUMN;



public class QuizSumActivity extends AppCompatActivity {

    TextView txtScore;
    ListView listViewAnswer;

    ArrayList<String> playerAnswer = new ArrayList<>();
    ArrayList<String> correctAnswer = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_sum);

        txtScore = (TextView)findViewById(R.id.txtScore);
        listViewAnswer = (ListView) findViewById(R.id.listViewAnswer);

        Bundle bundle = getIntent().getExtras();
        int score = bundle.getInt("score");
        playerAnswer = bundle.getStringArrayList("playerAnswer");
        correctAnswer = bundle.getStringArrayList("correctAnswer");

        txtScore.setText(Integer.toString(score)+"/10");


        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> temp = new HashMap<String, String>();

        for (int j = 0; j < playerAnswer.size(); j++) {
            temp = new HashMap<String, String>();  // new temp
            temp.put(FIRST_COLUMN, playerAnswer.get(j).toString()); // add คำที่เลือกใส่ column
            temp.put(SECOND_COLUMN, correctAnswer.get(j).toString()); // add คำที่ถูกต้องใส่ column
            list.add(temp); // add temp ลงใน list(แถว)

        }
        SumRoundOneAdapter adapter = new SumRoundOneAdapter(this, list);
        listViewAnswer.setAdapter(adapter);


    }


}
