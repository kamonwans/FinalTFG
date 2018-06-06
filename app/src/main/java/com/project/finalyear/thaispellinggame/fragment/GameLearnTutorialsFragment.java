package com.project.finalyear.thaispellinggame.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.finalyear.thaispellinggame.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Namwan on 4/18/2018.
 */

public class GameLearnTutorialsFragment extends Fragment {

    private GifImageView gameLearnTutorials;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_learn_tutorials, container, false);

        initInstance(view);

//        gameLearnTutorials.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentGameLearn();
//            }
//        });

        return view;
    }

    public void FragmentGameLearn() {
        Fragment fragment = new GameLearnFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initInstance(View view) {

        //gameLearnTutorials = (GifImageView) view.findViewById(R.id.gameLearnTutorials);
    }
}
