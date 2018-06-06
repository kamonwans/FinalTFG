package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.project.finalyear.thaispellinggame.R;


public class LearningMainSectionFragment extends Fragment {

    CardView btnMaekok;
    CardView btnMaekom;
    CardView btnMaekong;
    CardView btnMaekon;
    CardView btnMaekod;
    CardView btnMaekob;
    CardView btnMaekery;
    CardView btnMaeguew;
    Button btnBackLeaning;

    public LearningMainSectionFragment() {
        // Required empty public constructor
    }


    public static LearningMainSectionFragment newInstance(String param1, String param2) {
        LearningMainSectionFragment fragment = new LearningMainSectionFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learning_main_section,
                container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        final Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.scale);
        btnMaeguew = (CardView) view.findViewById(R.id.btnMaeguew);
        btnMaekon = (CardView) view.findViewById(R.id.btnMaekon);
        btnMaekom = (CardView) view.findViewById(R.id.btnMaekom);
        btnMaekod = (CardView) view.findViewById(R.id.btnMaekod);
        btnMaekob = (CardView) view.findViewById(R.id.btnMaekob);
        btnMaekok = (CardView) view.findViewById(R.id.btnMaekok);
        btnMaekery = (CardView) view.findViewById(R.id.btnMaekery);
        btnMaekong = (CardView) view.findViewById(R.id.btnMaekong);
        btnBackLeaning = (Button) view.findViewById(R.id.btnBackLeaning);


        btnMaeguew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            view.startAnimation(animation);
                FragmentLearningMaeGuew();
            }
        });
        btnMaekom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKom();
            }
        });
        btnMaekod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKod();
            }
        });
        btnMaekob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKob();
            }
        });
        btnMaekok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKok();
            }
        });
        btnMaekery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKery();
            }
        });
        btnMaekong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKong();
            }
        });
        btnMaekon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMaeKon();
            }
        });
        btnBackLeaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLearningMain();
            }
        });

    }

    public void FragmentLearningMaeGuew() {
        Fragment fragment = new LearningSectionMaegewFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKok() {
        Fragment fragment = new LearningSectionMaekokFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKon() {
        Fragment fragment = new LearningSectionMaekonFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKom() {
        Fragment fragment = new LearningSectionMaekomFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKod() {
        Fragment fragment = new LearningSectionMaekodFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKob() {
        Fragment fragment = new LearningSectionMaekobFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKery() {
        Fragment fragment = new LearningSectionMaekeryFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMaeKong() {
        Fragment fragment = new LearningSectionMeakongFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void FragmentLearningMain() {
        Fragment fragment = new LearningMainFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
