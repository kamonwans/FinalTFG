package com.project.finalyear.thaispellinggame.controller;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.finalyear.thaispellinggame.model.GameOne;
import com.project.finalyear.thaispellinggame.model.GameThree;

import java.util.ArrayList;

public class GameOneController {

    private DatabaseReference databaseReference;
    private int currentGameOneIndex;
    private GameOneCallBack callBack;
    private ArrayList<GameOne> gameOneArrayList;

    public GameOneController(GameOneCallBack callBack) {
        this.callBack = callBack;
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Game_one");
        gameOneArrayList = new ArrayList<GameOne>();

    }

    // load data to firebase
    public void dataPull() {
        databaseReference.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                for (com.google.firebase.database.DataSnapshot data : dataSnapshot.getChildren()) {
                    gameOneArrayList.add(data.getValue(GameOne.class));

                }
                currentGameOneIndex = 0;
                // ส่ง parameter currentGameThreeIndex and gameThreeArrayList ไปหน้า view
                callBack.displayGameOne(currentGameOneIndex, gameOneArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<GameOne> gameOneArrayList() {
        return gameOneArrayList;
    }

    public void setGameOneArrayList(ArrayList<GameOne> gameOneArrayList) {
        this.gameOneArrayList = gameOneArrayList;
    }

    // ส่งข้อมูลกลับไปหน้า view
    public interface GameOneCallBack {
        void displayGameOne(int index, ArrayList<GameOne> gameOneArrayList);

        void onCancel(String messageError);

    }
}
