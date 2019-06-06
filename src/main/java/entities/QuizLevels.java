package com.quiz.demoQuiz.entity;

public enum QuizLevels {
    BEGINNER("débutant"),
    CONFIRMED("confirmé"),
    EXPERT("expert");


    private final String value;

    QuizLevels(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
