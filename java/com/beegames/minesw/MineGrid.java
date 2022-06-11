package com.beegames.minesw;

import java.util.Random;

public class MineGrid {
    private int size;
    private Cell[][] cells;


    public MineGrid(int size) {
        this.size = size;
        this.cells = new Cell[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++){
                cells[i][j] = new Cell();
        }
    }

    public void generateGrid(int totalBombs, int xbomb, int ybomb) {
        int bombsPlaced = 0;
        while (bombsPlaced < totalBombs) {
            int x = new Random().nextInt(size);
            int y = new Random().nextInt(size);
            if (cellAt(x, y).getValue() == 0 && x != xbomb && y != ybomb) {
                cellAt(x, y).setValue(-1);
                bombsPlaced++;
            }
        }
        int countBombs;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                countBombs = 0;
                if (cellAt(x, y).getValue() == 0) {
                    countBombs = adjacentCells(x, y);
                    if (countBombs > 0) {
                        cells[x][y].setValue(countBombs);
                    }
                }
            }
        }
    }

    public Cell cellAt(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            return null;
        }
        else{
            return cells[x][y];
        }
    }

    public int adjacentCells(int x, int y) {
        int adjacentBomb = 0;
        if (cellAt(x-1, y-1) != null) {
            if (cellAt(x - 1, y - 1).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x, y-1) != null) {
            if (cellAt(x, y - 1).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x+1, y-1) != null) {
            if (cellAt(x+1, y-1).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x-1, y) != null) {
            if (cellAt(x-1, y).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x+1, y) != null) {
            if (cellAt(x+1, y).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x-1, y+1) != null) {
            if (cellAt(x-1, y+1).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x, y+1) != null) {
            if (cellAt(x, y+1).getValue() == -1)
                adjacentBomb++;
        }
        if (cellAt(x+1, y+1) != null) {
            if (cellAt(x+1, y+1).getValue() == -1)
                adjacentBomb++;
        }
        return adjacentBomb;
    }
}
