package com.project.finalyear.thaispellinggame.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.finalyear.thaispellinggame.R;
import com.project.finalyear.thaispellinggame.activity.GameActivity;
import com.project.finalyear.thaispellinggame.activity.SummaryActivity;
import com.project.finalyear.thaispellinggame.fragment.GameFiveTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameFourTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameOneTutorialFragment;
import com.project.finalyear.thaispellinggame.fragment.GameThreeTutorialFragment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Namwan on 3/5/2018.
 */

public class RandomGame {

    private FirebaseAuth mAuth;
    private DatabaseReference mRefDatabase;
    private FirebaseUser mCurrentUser;
    private String current_uid;
    private ArrayList gameName = new ArrayList();
    private String user_id;
    private ArrayList<String> gameList;

    public RandomGame(GameActivity gameActivity) {
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        current_uid = mCurrentUser.getUid();
        mRefDatabase = FirebaseDatabase.getInstance().getReference();

        gameName.add("gameOne");
        gameName.add("gameThree");
        gameName.add("gameFour");
        gameName.add("gameFive");

        gameList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            int number = random.nextInt(gameName.size());
            gameList.add(gameName.get(number).toString());
            gameName.remove(number);
        }

        Log.d("myGameList", gameList.toString());
        saveGameToDatabase(gameActivity, gameList);
        gamePlaying(gameActivity);
        getGamePlaying(gameActivity);
    }

    private void saveGameToDatabase(final GameActivity gameActivity, final ArrayList gameList) {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                mRefDatabase.child("pair_players").child(current_uid).child(user_id).child("roomId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String roomKey = dataSnapshot.getValue().toString();

                        mRefDatabase.child("rooms").child(roomKey).child("Game").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.hasChildren()) {

                                    mRefDatabase.child("rooms").child(roomKey).child("Game").child("round1").setValue(gameList.get(0));
                                    mRefDatabase.child("rooms").child(roomKey).child("Game").child("round2").setValue(gameList.get(1));
                                    mRefDatabase.child("rooms").child(roomKey).child("Game").child("round3").setValue(gameList.get(2));

                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round1").setValue("null");
                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round2").setValue("null");
                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round3").setValue("null");
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void gamePlaying(final GameActivity gameActivity) {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                mRefDatabase.child("pair_players").child(current_uid).child(user_id).child("roomId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String roomKey = dataSnapshot.getValue().toString();

                        mRefDatabase.child("rooms").child(roomKey).child("playing").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String round = child.getKey();
                                    Log.d("myRound", child.getKey());

                                    switch (round) {
                                        case "round1":
                                            if (child.getValue().equals("null")){
                                                mRefDatabase.child("rooms").child(roomKey).child("playing").child("round1").setValue(gameList.get(0));
                                            }
                                        case "round2":
                                            if (child.getValue().equals("null")){
                                                mRefDatabase.child("rooms").child(roomKey).child("playing").child("round2").setValue(gameList.get(1));
                                            }
                                        case "round3":
                                            if (child.getValue().equals("null")){
                                                mRefDatabase.child("rooms").child(roomKey).child("playing").child("round3").setValue(gameList.get(2));
                                            }
                                    }
                                }

//                                if (!dataSnapshot.hasChild("round1")) {
//                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round1").setValue(gameList.get(0));
//
//                                }else if (!dataSnapshot.hasChild("round2")){
//                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round2").setValue(gameList.get(1));
//
//                                }else if (!dataSnapshot.hasChild("round3")){
//                                    mRefDatabase.child("rooms").child(roomKey).child("playing").child("round3").setValue(gameList.get(2));
//                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getGamePlaying(final GameActivity gameActivity) {

        mRefDatabase.child("pair_players").child(current_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    user_id = child.getKey();
                }

                mRefDatabase.child("pair_players").child(current_uid).child(user_id).child("roomId").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String roomKey = dataSnapshot.getValue().toString();

                        //----------- roomKey ------------
                        mRefDatabase.child("rooms").child(roomKey).child("playing").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (dataSnapshot.getChildrenCount() == 0) {
                                    String game = dataSnapshot.child("round1").getValue().toString();
                                    loadFragment(gameActivity, game);

                                }else if (dataSnapshot.getChildrenCount() == 1) {
                                    String game = dataSnapshot.child("round2").getValue().toString();
                                    loadFragment(gameActivity, game);

                                }else if (dataSnapshot.getChildrenCount() == 2) {
                                    String game = dataSnapshot.child("round3").getValue().toString();
                                    loadFragment(gameActivity, game);
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void loadFragment(GameActivity gameActivity, String game) {

        Fragment fragments = null;

        switch (game) {
            case "gameOne":
                fragments = new GameOneTutorialFragment();
                break;
            case "gameThree":
                fragments = new GameThreeTutorialFragment();
                break;
            case "gameFour":
                fragments = new GameFourTutorialFragment();
                break;
            case "gameFive":
                fragments = new GameFiveTutorialFragment();
                break;

        }


        Fragment fragment = fragments;
        FragmentTransaction transaction = ((AppCompatActivity) gameActivity).getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();

//        ((AppCompatActivity) gameActivity).getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.game, fragment)
//                .commit();

    }

    private void summaryGame(Context context) {
        Intent intent = new Intent(context, SummaryActivity.class);
        context.startActivity(intent);

    }


}
