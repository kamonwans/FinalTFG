package com.project.finalyear.thaispellinggame.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.finalyear.thaispellinggame.R;


public class LearningMainFragment extends Fragment{
    CardView btnLearning;
    CardView btnThreeLetterLearning;
    CardView btnEveryDayLearning;
    CardView btnGameLearn;


    public LearningMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_main,
                container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(final View view){
        btnLearning = (CardView) view.findViewById(R.id.btnLearning);
        btnLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMainSection();
            }
        });
        btnThreeLetterLearning = (CardView) view.findViewById(R.id.btnThreeLetterLearning);
        btnThreeLetterLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentThreeLetter();
            }
        });
        btnEveryDayLearning = (CardView) view.findViewById(R.id.btnEveryDayLearning);
        btnEveryDayLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentWrongEveryDay();
            }
        });

        btnGameLearn = (CardView) view.findViewById(R.id.btnGameLearn);
        btnGameLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameLearnMainFragment();
            }
        });
    }

//    public void FragmentLearningMain() {
//        Fragment fragment = new FrequentlyLearningFragment();
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.content_main, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    public void GameLearnMainFragment() {
        Fragment fragment = new GameLearnMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMainSection() {
        Fragment fragment = new LearningMainSectionFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentThreeLetter() {
        Fragment fragment = new ThreeLettersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void FragmentWrongEveryDay() {
        Fragment fragment = new WrongEveryDayFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
