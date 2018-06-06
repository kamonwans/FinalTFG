package com.project.finalyear.thaispellinggame.model;

/**
 * Created by Namwan on 5/4/2018.
 */

public class GameTwoModel {
    private String mean;
    private String word;

    public GameTwoModel() {
    }

    public GameTwoModel(String mean, String word) {
        this.mean = mean;
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
