package com.beegames.minesw;

public class MineSweeperGame {
    private MineGrid mineGrid;
    private boolean gameOver;
    private boolean flagMode;
    private boolean clearMode;
    private int flagCount;
    private int numberBombs;
    private boolean timeExpired;
    private int size;

    public MineSweeperGame(int size, int numberBombs, int x, int y) {
        this.gameOver = false;
        this.size = size;
        this.flagMode = false;
        this.clearMode = true;
        this.timeExpired = false;
        this.flagCount = 0;
        this.numberBombs = numberBombs;
        mineGrid = new MineGrid(size);
        mineGrid.generateGrid(numberBombs, x, y);
    }

    public boolean isGameWon() {
        int numbersUnrevealed = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (getMineGrid().cellAt(x, y).getValue() != -1 && getMineGrid().cellAt(x, y).getValue() != 0 && !getMineGrid().cellAt(x, y).isRevealed()) {
                    numbersUnrevealed++;
                }
            }
        }
        if (numbersUnrevealed == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void toggleMode() {
        clearMode = !clearMode;
        flagMode = !flagMode;
    }

    public void outOfTime() {
        timeExpired = true;
    }

    public boolean getOutOfTime() {
        return timeExpired;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void GameOver() {
         gameOver = true;
    }

    public MineGrid getMineGrid() {
        return mineGrid;
    }

    public boolean isFlagMode() {
        return flagMode;
    }

    public boolean isClearMode() {
        return clearMode;
    }

    public int getFlagCount() {
        return flagCount;
    }

    public void setFlagCount(int a) {
        this.flagCount = flagCount + a;
    }

    public int getNumberBombs() {
        return numberBombs;
    }

}
