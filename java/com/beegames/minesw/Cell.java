package com.beegames.minesw;

public class Cell {

    private int value;
    private boolean isRevealed;
    private boolean isFlagged;

    public Cell() {
        this.value = 0;
        this.isRevealed = false;
        this.isFlagged = false;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
}

