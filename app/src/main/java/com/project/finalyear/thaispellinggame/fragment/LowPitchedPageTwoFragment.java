package com.project.finalyear.thaispellinggame.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.common.Util;

public class LowPitchedPageTwoFragment extends Fragment {
    ImageView imgBack;
    ImageView imgNext;
    ImageView voiceToTunk;
    ImageView voiceNoNu;
    ImageView voicePoPan;
    ImageView voiceFoFun;
    ImageView voicePoSuumpao;
    ImageView voiceMoMar;
    ImageView voiceYoYark;
    ImageView voiceRoRue;
    ImageView voiceLoLing;
    ImageView voiceWoWan;
    ImageView voiceRoJura;
    ImageView voiceHoNukhuk;
    MediaPlayer voiceLowPitched;

    public LowPitchedPageTwoFragment() {
        // Required empty public constructor
    }


    public static LowPitchedPageTwoFragment newInstance(String param1, String param2) {
        LowPitchedPageTwoFragment fragment = new LowPitchedPageTwoFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_low_pitched_page_two,
                container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {
        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgNext = (ImageView) view.findViewById(R.id.imgNext);
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentThreeLetter();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLowPitched();
            }
        });
        voiceToTunk = (ImageView) view.findViewById(R.id.voiceToTunk);
        voiceToTunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.to_tunk);
                voiceLowPitched.start();
            }
        });
        voiceNoNu = (ImageView) view.findViewById(R.id.voiceNoNu);
        voiceNoNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.no_nu);
                voiceLowPitched.start();
            }
        });
        voicePoPan = (ImageView) view.findViewById(R.id.voicePoPan);
        voicePoPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.po_pan);
                voiceLowPitched.start();
            }
        });
        voiceFoFun = (ImageView) view.findViewById(R.id.voiceFoFun);
        voiceFoFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.fo_fun);
                voiceLowPitched.start();
            }
        });
        voicePoSuumpao = (ImageView) view.findViewById(R.id.voicePoSuumpao);
        voicePoSuumpao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.po_sumpao);
                voiceLowPitched.start();
            }
        });
        voiceMoMar = (ImageView) view.findViewById(R.id.voiceMoMar);
        voiceMoMar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.mo_mar);
                voiceLowPitched.start();
            }
        });
        voiceYoYark = (ImageView) view.findViewById(R.id.voiceYoYark);
        voiceYoYark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.yo_yark);
                voiceLowPitched.start();
            }
        });
        voiceRoRue = (ImageView) view.findViewById(R.id.voiceRoRue);
        voiceRoRue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.ro_reu);
                voiceLowPitched.start();
            }
        });
        voiceLoLing = (ImageView) view.findViewById(R.id.voiceLoLing);
        voiceLoLing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.lo_ling);
                voiceLowPitched.start();
            }
        });
        voiceWoWan = (ImageView) view.findViewById(R.id.voiceWoWan);
        voiceWoWan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.wo_wan);
                voiceLowPitched.start();
            }
        });
        voiceRoJura = (ImageView) view.findViewById(R.id.voiceRoJura);
        voiceRoJura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.ro_jura);
                voiceLowPitched.start();
            }
        });
        voiceHoNukhuk = (ImageView) view.findViewById(R.id.voiceHoNukhuk);
        voiceHoNukhuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceLowPitched = Util.playMediaSound(getActivity(), R.raw.ho_nukhuk);
                voiceLowPitched.start();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void onPause() {
        super.onPause();


    }

    // ทำงานเมื่อแอปถูปิดลง
    public void onDestroy() {
        super.onDestroy();
        voiceLowPitched.stop();

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void FragmentLowPitched() {
        Fragment fragment = new LowPitchedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void FragmentThreeLetter() {
        Fragment fragment = new ThreeLettersFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
