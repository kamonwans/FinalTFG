package com.project.finalyear.thaispellinggame.model;


public class GameThree {
    private String word;
    private String type;

    public String getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(String answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    private String answerCorrect;

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

    public GameThree(){}

    public GameThree(String word, String type,String answerCorrect) {
        this.word = word;
        this.type = type;
        this.answerCorrect = answerCorrect;
    }
}
