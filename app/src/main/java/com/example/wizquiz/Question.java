package com.example.wizquiz;

public class Question {
    private String questionText;
    private String[] options;
    private int correctIndex;

    public Question(String questionText, String[] options, int correctIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctIndex = correctIndex;
    }
// metoda per me lexu tekstin, opsionet dhe indeksin korrekt
    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
