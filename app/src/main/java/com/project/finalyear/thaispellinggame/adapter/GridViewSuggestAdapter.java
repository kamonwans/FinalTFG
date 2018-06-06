package com.project.finalyear.thaispellinggame.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.project.finalyear.thaispellinggame.common.Common;
import com.project.finalyear.thaispellinggame.fragment.GameTwoFragment;

import java.util.List;

/**
 * Created by Namwan on 5/4/2018.
 */

public class GridViewSuggestAdapter extends BaseAdapter {

    private List<String> suggestSource;
    private Context context;
    private GameTwoFragment gameTwoFragment;
    private String answer;

    public GridViewSuggestAdapter(List<String> suggestSource, Context context, GameTwoFragment gameTwoFragment) {
        this.suggestSource = suggestSource;
        this.context = context;
        this.gameTwoFragment = gameTwoFragment;
        this.answer = gameTwoFragment.answer;
    }

    @Override
    public int getCount() {
        return suggestSource.size();
    }

    @Override
    public Object getItem(int position) {
        return suggestSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            if (suggestSource.get(position).equals("null")) {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(120, 120));
                button.setPadding(10, 10, 10, 10);
                button.setBackgroundColor(Color.alpha(0));
            } else {
                button = new Button(context);
                button.setLayoutParams(new GridView.LayoutParams(120, 120));
                button.setPadding(10, 10, 10, 10);
                button.setBackgroundColor(Color.rgb(128, 199, 229));
                button.setTextColor(Color.WHITE);
                button.setTextSize(18);
                button.setText(suggestSource.get(position));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if correct answer contains character user selected
                        if (answer.contains(suggestSource.get(position))) {
                            String compare = suggestSource.get(position); // Get char

                            for (int i=0; i<gameTwoFragment.correctAnswerArray.length; i++) {
                                if (compare == gameTwoFragment.correctAnswerArray[i])
                                    Common.user_submit_answer[i] = compare;
                            }

                            //Update UI
                            GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(Common.user_submit_answer, context);
                            gameTwoFragment.gridViewAnswer.setAdapter(answerAdapter);
                            answerAdapter.notifyDataSetChanged();

                            //Remove from suggest source
                            gameTwoFragment.suggestSource.set(position, "null");
                            gameTwoFragment.suggestAdapter = new GridViewSuggestAdapter(gameTwoFragment.suggestSource, context, gameTwoFragment);
                            gameTwoFragment.gridViewSuggest.setAdapter(gameTwoFragment.suggestAdapter);
                            gameTwoFragment.suggestAdapter.notifyDataSetChanged();
                        } else {

                            gameTwoFragment.checkAnswer(false);
                            gameTwoFragment.showToast(gameTwoFragment.getContext(), "ตอบผิด!");
                            //Remove from suggest source
//                            mainActivity.suggestSource.set(position,"null");
//                            mainActivity.suggestAdapter = new GridViewSuggestAdapter(mainActivity.suggestSource,context,mainActivity);
//                            mainActivity.gridViewSuggest.setAdapter(mainActivity.suggestAdapter);
//                            mainActivity.suggestAdapter.notifyDataSetChanged();

                        }
                    }
                });
            }
        } else {
            button = (Button) convertView;
        }
        return button;
    }

}
