package com.project.finalyear.thaispellinggame.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.project.finalyear.thaispellinggame.model.Constants.FIRST_COLUMN;
import static com.project.finalyear.thaispellinggame.model.Constants.SECOND_COLUMN;

/**
 * Created by kamonwan on 1/16/2018.
 */

public class SumRoundOneAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    String iconAnswer;
    String iconSeect;
    TextView answerRight;
    TextView answerSelect;
    ImageView imgCorrect;
    ImageView imgIncorrect;


    public SumRoundOneAdapter(Activity activity, ArrayList<HashMap<String,String>> list) {
        super();
        this.activity = activity;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (view == null) {
            view = inflater.inflate(R.layout.item_sum_game_one, null);

            answerRight = (TextView) view.findViewById(R.id.tvAnswerRight);
            answerSelect = (TextView) view.findViewById(R.id.tvAnswerSelect);
            imgCorrect = (ImageView) view.findViewById(R.id.imgCorrect);
            imgIncorrect = (ImageView) view.findViewById(R.id.imgIncorrect);
        }
        HashMap<String, String> map = list.get(position);
        iconSeect = map.get(FIRST_COLUMN);
        iconAnswer = map.get(SECOND_COLUMN);
        answerSelect.setText(map.get(FIRST_COLUMN));
        answerRight.setText(map.get(SECOND_COLUMN));
        Log.d("RightCorrect", String.valueOf(iconAnswer));
        Log.d("RightInCorrect", String.valueOf(iconSeect));
        Log.d("RightIncorrect", String.valueOf(answerRight));
        Log.d("listtttttt", String.valueOf(list));
        if ((iconSeect.equals(iconAnswer))) {
            Log.d("RightCorrect", String.valueOf(answerRight));
            imgCorrect.setVisibility(View.VISIBLE);
            imgIncorrect.setVisibility(View.INVISIBLE);
        } else if ((!iconSeect.equals(iconAnswer))) {
            Log.d("RightIncorrect", String.valueOf(answerRight));
            imgIncorrect.setVisibility(View.VISIBLE);
            imgCorrect.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}
