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

public class MidrangeFragment extends Fragment {
    ImageView imgNext;
    ImageView imgBack;
    ImageView voiceAoArng;
    ImageView voiceKoKai;
    ImageView voiceJoJan;
    ImageView voiceDoChada;
    ImageView voiceToPatuk;
    ImageView voiceDoDek;
    ImageView voiceToTao;
    ImageView voiceBoBaimai;
    ImageView voicePoPla;
    MediaPlayer voiceMidPitched;

    public MidrangeFragment() {
        // Required empty public constructor
    }


    public static MidrangeFragment newInstance(String param1, String param2) {
        MidrangeFragment fragment = new MidrangeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_midrange,
                container, false);
        initInstances(view);
        return view;
    }

    private void initInstances(View view) {

        imgBack = (ImageView) view.findViewById(R.id.imgBack);
        imgNext = (ImageView) view.findViewById(R.id.imgNext);
        final Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.scale);
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentLowPitched();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHighPitched();
            }
        });

        voiceJoJan = (ImageView) view.findViewById(R.id.voiceJoJan);
        voiceJoJan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                voiceMidPitched = Util.playMediaSound(getActivity(), R.raw.jo_jan);
                voiceMidPitched.start();
            }
        });
        voiceKoKai = (ImageView) view.findViewById(R.id.voiceKoKai);
        voiceKoKai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched =  Util.playMediaSound(getActivity(), R.raw.ko_kai);
                voiceMidPitched.start();
            }
        });
        voiceDoChada = (ImageView) view.findViewById(R.id.voiceDoChada);
        voiceDoChada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched =  Util.playMediaSound(getActivity(), R.raw.do_chada);
                voiceMidPitched.start();
            }
        });
        voiceDoDek = (ImageView) view.findViewById(R.id.voiceDoDek);
        voiceDoDek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched =  Util.playMediaSound(getActivity(), R.raw.do_dek);
                voiceMidPitched.start();
            }
        });
        voiceToTao = (ImageView) view.findViewById(R.id.voiceToTao);
        voiceToTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched =  Util.playMediaSound(getActivity(), R.raw.to_tao);
                voiceMidPitched.start();
            }
        });
        voiceToPatuk = (ImageView) view.findViewById(R.id.voiceToPatuk);
        voiceToPatuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched = Util.playMediaSound(getActivity(), R.raw.to_patuk);
                voiceMidPitched.start();
            }
        });
        voiceBoBaimai = (ImageView) view.findViewById(R.id.voiceBoBaimai);
        voiceBoBaimai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched = Util.playMediaSound(getActivity(), R.raw.bo_baimai);
                voiceMidPitched.start();
            }
        });
        voicePoPla = (ImageView) view.findViewById(R.id.voicePoPla);
        voicePoPla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched = Util.playMediaSound(getActivity(), R.raw.po_pla);
                voiceMidPitched.start();
            }
        });
        voiceAoArng = (ImageView) view.findViewById(R.id.voiceAoArng);
        voiceAoArng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
               voiceMidPitched = Util.playMediaSound(getActivity(), R.raw.ao_arng);
                voiceMidPitched.start();
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
        voiceMidPitched.stop();

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private void FragmentLowPitched() {
        Fragment fragment = new LowPitchedFragment();
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
}
