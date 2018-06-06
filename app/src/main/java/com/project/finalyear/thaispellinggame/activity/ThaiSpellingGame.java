package com.project.finalyear.thaispellinggame.activity;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.project.finalyear.thaispellinggame.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;



public class ThaiSpellingGame extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/SanamDeklen_chaya.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


    }
}
