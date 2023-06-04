package com.example.project_sesjopoli.post_objects;

public class PostObjectForAnsweringQuiz {
    public int answerIndex;
    public int questionIndex;
    public int playerID;

    public PostObjectForAnsweringQuiz(int answerIndex, int questionIndex, int playerID) {
        this.answerIndex = answerIndex;
        this.questionIndex = questionIndex;
        this.playerID = playerID;
    }
}
