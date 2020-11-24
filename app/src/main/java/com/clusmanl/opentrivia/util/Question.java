package com.clusmanl.opentrivia.util;

public class Question {

    private String question;
    private String[] answers;
    private int correctAnswerId;

    public Question(String question, String[] answers, int correctAnswerId) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerId = correctAnswerId;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public int getCorrectAnswerId(){
        return correctAnswerId;
    }
}
