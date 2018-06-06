package com.project.finalyear.thaispellinggame.common;

import android.content.Context;
import android.media.MediaPlayer;

public class Util {
    private static MediaPlayer voicePitched;

    public static MediaPlayer playMediaSound(Context context, int resId){
        if(voicePitched != null){
            voicePitched.release();
            voicePitched = null;
        }
        voicePitched = MediaPlayer.create(context, resId);
       // voicePitched.start();
        return voicePitched;
    }

}
