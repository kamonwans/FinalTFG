package com.project.finalyear.thaispellinggame.controller;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.model.GameThree;

import java.util.ArrayList;

public class GameThreeController {

    private DatabaseReference databaseReference;
    private int currentGameThreeIndex;
    private GameThreeCallBack callBack;
    private ArrayList<GameThree> gameThreeArrayList;


    public GameThreeController(GameThreeCallBack callBack) {
        this.callBack = callBack;
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Game_three");
        gameThreeArrayList = new ArrayList<GameThree>();

    }

    // load data firebase
    public void pullData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    gameThreeArrayList.add(data.getValue(GameThree.class));

                }
                currentGameThreeIndex = 0;
                // ส่ง parameter currentGameThreeIndex and gameThreeArrayList ไปหน้า view
                callBack.displayGameThree(currentGameThreeIndex, gameThreeArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public ArrayList<GameThree> getGameThreeArrayList() {
        return gameThreeArrayList;
    }

    public void setGameThreeArrayList(ArrayList<GameThree> gameThreeArrayList) {
        this.gameThreeArrayList = gameThreeArrayList;
    }


    // ส่งข้อมูลกลับไปหน้า view
    public interface GameThreeCallBack {
        void displayGameThree(int index, ArrayList<GameThree> gameThreeArrayList);

        void onCancel(String messageError);
    }
}
