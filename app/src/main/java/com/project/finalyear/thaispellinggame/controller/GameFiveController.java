package com.project.finalyear.thaispellinggame.controller;

import android.view.MotionEvent;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.finalyear.thaispellinggame.model.GameFive;

import java.util.ArrayList;

public class GameFiveController {
    private DatabaseReference databaseReference;
    private int currentGameFiveIndex;
    private GameFiveCallBack callBack;
    private ArrayList<GameFive> gameFiveArrayList;

    public GameFiveController(GameFiveCallBack callBack) {
        this.callBack = callBack;
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Game_three");
        gameFiveArrayList = new ArrayList<GameFive>();
    }
    // load data firebase
    public void dataPull() {
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                gameFiveArrayList = new ArrayList<GameFive>();

                for (com.google.firebase.database.DataSnapshot data : dataSnapshot.getChildren()) {
                    gameFiveArrayList.add(data.getValue(GameFive.class));

                }

                currentGameFiveIndex = 0;
                callBack.displayGameFive(currentGameFiveIndex,gameFiveArrayList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<GameFive> getGameFiveArrayList() {
        return gameFiveArrayList;
    }

    public void setGameFiveArrayList(ArrayList<GameFive> gameFiveArrayList) {
        this.gameFiveArrayList = gameFiveArrayList;
    }

    public interface GameFiveCallBack {
        void displayGameFive(int index, ArrayList<GameFive> gameFiveArrayList);

        boolean onTouchEvent(MotionEvent event);

        void onCancel(String massageError);
    }

}
