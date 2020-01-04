package com.psych.game.model;

public enum GameMode {

    IS_IT_A_FACT(1),
    UNSCRAMBLE(2),
    WORD_UP(3);

    private int value;

    GameMode(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
