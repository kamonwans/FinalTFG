package com.project.finalyear.thaispellinggame.model;

/**
 * Created by Namwan on 3/18/2018.
 */

public class LearningQuizModel {

    private String question;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String answer;
    private String image;

    public LearningQuizModel() {
    }

    public LearningQuizModel(String question, String choiceA, String choiceB, String choiceC, String answer, String image) {
        this.question = question;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.answer = answer;
        this.image = image;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA;
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB;
    }

    public String getChoiceC() {
        return choiceC;
    }

    public void setChoiceC(String choiceC) {
        this.choiceC = choiceC;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
