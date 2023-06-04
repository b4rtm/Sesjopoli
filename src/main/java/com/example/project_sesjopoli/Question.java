package com.example.project_sesjopoli;

import java.util.ArrayList;

public class Question {
    public String question;
    public ArrayList<String> answers;
    public int correctAnswerIndex;

    public Question(String question, ArrayList<String> answers, int correctAnswerIndex) {
        this.question = question;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }
}
