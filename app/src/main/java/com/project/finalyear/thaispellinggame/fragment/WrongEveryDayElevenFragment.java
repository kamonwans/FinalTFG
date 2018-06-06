package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.finalyear.thaispellinggame.R;

public class WrongEveryDayElevenFragment extends Fragment {
    ImageView imgNext;
    ImageView imgBack;
    public WrongEveryDayElevenFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wrong_every_day_eleven, container, false);
        initInstance(view);
        return view;
    }

    private void initInstance(View view) {
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgNext = (ImageView) view.findViewById(R.id.imgNext);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentWrongTen();
            }
        });
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentWrongEveryDay();
            }
        });
    }

    private void FragmentWrongEveryDay() {
        Fragment fragment = new WrongEveryDayFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void FragmentWrongTen() {
        Fragment fragment = new WrongEveryDayTenFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
