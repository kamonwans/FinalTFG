package com.project.finalyear.thaispellinggame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

/**
 * Created by Namwan on 5/4/2018.
 */

public class GridViewAnswerAdapter extends BaseAdapter {

    private String[] answerCharacter;
    private Context context;

    public GridViewAnswerAdapter(String[] answerCharacter, Context context) {
        this.answerCharacter = answerCharacter;
        this.context = context;
    }

    @Override
    public int getCount() {
        return answerCharacter.length;
    }

    @Override
    public Object getItem(int position) {
        return answerCharacter[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null){
            //Create new button
            button = new Button(context);
            if(answerCharacter[position] != null){
                button.setLayoutParams(new GridView.LayoutParams(95,95));
                button.setPadding(8,8,8,8);
                button.setBackgroundColor(Color.LTGRAY);
                button.setTextColor(Color.BLACK);
                button.setText(String.valueOf(answerCharacter[position]));
            }

        }
        else
            button = (Button) convertView;

        return button;
    }
}