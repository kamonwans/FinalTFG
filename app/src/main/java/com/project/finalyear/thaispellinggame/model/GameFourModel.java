package com.project.finalyear.thaispellinggame.model;

/**
 * Created by Namwan on 2/8/2018.
 */

public class GameFourModel {

    private String word;
    private String type;
    private String answer;

    public GameFourModel() {
    }

    public GameFourModel(String word, String type, String answer) {
        this.word = word;
        this.type = type;
        this.answer = answer;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
