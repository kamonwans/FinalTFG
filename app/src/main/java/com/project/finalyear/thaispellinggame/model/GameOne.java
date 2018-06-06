package com.project.finalyear.thaispellinggame.model;

/**
 * Created by kamonwan on 11/21/2017.
 */

public class GameOne {
    private String ChoiceA;
    private String ChoiceB;
    private String ChoiceC;
    private String meaning;
    private String correctAnswer;
    private boolean creditAlreadyGiven;


    public String  getCorrectAnswer() {
        return correctAnswer;
    }
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
    public boolean isCorrectAnswer(String selectedAnswer) {
        return (selectedAnswer.equals(correctAnswer));
    }

    public String getChoiceA() {
        return ChoiceA;
    }
    public void setChoiceA(String choiceA) {
        ChoiceA = choiceA;
    }

    public String getChoiceB() {
        return ChoiceB;
    }
    public void setChoiceB(String choiceB) {
        ChoiceB = choiceB;
    }

    public String getChoiceC() {
        return ChoiceC;
    }
    public void setChoiceC(String choiceC) {
        ChoiceC = choiceC;
    }

    public String getMeaning() {
        return meaning;
    }
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }



    public boolean isCreditAlreadyGiven() {
        return creditAlreadyGiven;
    }
    public void setCreditAlreadyGiven(boolean creditAlreadyGiven) {
        this.creditAlreadyGiven = creditAlreadyGiven;
    }


    public GameOne() {

    }


    public GameOne(String ChoiceA, String ChoiceB, String ChoiceC, String meaning, String correctAnswer) {
        this.ChoiceA = ChoiceA;
        this.ChoiceB = ChoiceB;
        this.ChoiceC = ChoiceC;
        this.meaning = meaning;
        this.correctAnswer = correctAnswer;
        this.creditAlreadyGiven = false;
    }
}
