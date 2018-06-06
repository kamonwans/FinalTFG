package com.project.finalyear.thaispellinggame.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;


public class ThreeLettersFragment extends Fragment {

    Button btnBackLeaning;
    CardView tvHighPitched;
    CardView tvMidrange;
    CardView tvLowPitched;


    public ThreeLettersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three_letters,
                container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        btnBackLeaning = (Button) view.findViewById(R.id.btnBackLeaning);
        tvHighPitched = (CardView) view.findViewById(R.id.tvHighPitched);
        tvMidrange = (CardView) view.findViewById(R.id.tvMidrange);
        tvLowPitched = (CardView) view.findViewById(R.id.tvLowPitched);
        btnBackLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMain();
            }
        });
        tvHighPitched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHighPitched();
            }
        });
        tvMidrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMidrange();
            }
        });
        tvLowPitched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLowPitched();
            }
        });


    }

    private void FragmentLearningMain() {
        Fragment fragment = new LearningMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void FragmentHighPitched() {
        Fragment fragment = new HightPitchedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void FragmentLowPitched() {
        Fragment fragment = new LowPitchedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void FragmentMidrange() {
        Fragment fragment = new MidrangeFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
