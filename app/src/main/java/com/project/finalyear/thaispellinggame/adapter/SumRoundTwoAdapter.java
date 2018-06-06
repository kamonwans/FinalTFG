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


public class SumRoundTwoAdapter extends BaseAdapter {
    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView answerRight;


    public SumRoundTwoAdapter(Activity activity, ArrayList<HashMap<String,String>> list) {
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
            view = inflater.inflate(R.layout.item_sum_game_two, null);

            answerRight = (TextView) view.findViewById(R.id.tvAnswerRight);
        }

        HashMap<String, String> map = list.get(position);
        answerRight.setText(map.get(FIRST_COLUMN));

        return view;
    }
}
