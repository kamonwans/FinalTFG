package com.project.finalyear.thaispellinggame.controller;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.model.GameOne;
import com.project.finalyear.thaispellinggame.model.GameTwoModel;

import java.util.ArrayList;

public class GameTwoController {

    private DatabaseReference databaseReference;
    private int currentGameTwoIndex;
    private GameTwoCallBack callBack;
    private ArrayList<GameTwoModel> gameTwoArrayList;

    public GameTwoController(GameTwoCallBack callBack) {
        this.callBack = callBack;
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Game_Two");
        gameTwoArrayList = new ArrayList<GameTwoModel>();

    }

    // load data to firebase
    public void dataPull() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    gameTwoArrayList.add(data.getValue(GameTwoModel.class));

                }
                currentGameTwoIndex = 0;
                // ส่ง parameter currentGameThreeIndex and gameThreeArrayList ไปหน้า view
                callBack.displayGameTwo(currentGameTwoIndex, gameTwoArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<GameTwoModel> gameTwoArrayList() {
        return gameTwoArrayList;
    }

    public void setGameTwoArrayList(ArrayList<GameTwoModel> gameTwoArrayList) {
        this.gameTwoArrayList = gameTwoArrayList;
    }

    // ส่งข้อมูลกลับไปหน้า view
    public interface GameTwoCallBack {
        void displayGameTwo(int index, ArrayList<GameTwoModel> gameTwoArrayList);

        void onCancel(String messageError);

    }
}
