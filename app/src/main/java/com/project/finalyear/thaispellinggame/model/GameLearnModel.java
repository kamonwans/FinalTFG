package com.project.finalyear.thaispellinggame.model;


/**
 * Created by Namwan on 1/17/2018.
 */

public class GameLearnModel {

    private String word;
    private String type;

    public GameLearnModel() {
    }

    public GameLearnModel(String word, String type) {
        this.word = word;
        this.type = type;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
