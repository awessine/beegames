package com.beegames.Tetris;

import android.widget.Button;
import android.widget.TextView;

import com.beegames.R;

import java.util.concurrent.locks.ReentrantLock;

public class Generator extends Thread {

    private boolean gameOver = false;
    private TextView[][] grid;
    private ReentrantLock locker;

    public Button leftBtn;
    public Button rightBtn;
    public Button downBtn;
    public Button rotateBtn;
    private Tetris tetris;

    private Train train;

    public Generator(TextView[][] grid, Button leftBtn, Button rightBtn, Button downBtn, Button rotateBtn, Tetris tetris) {
        this.grid = grid;
        this.leftBtn = leftBtn;
        this.rotateBtn = rotateBtn;
        this.rightBtn = rightBtn;
        this.downBtn = downBtn;
        this.locker = new ReentrantLock();
        this.tetris = tetris;
    }

    @Override
    public void run() {
        super.run();
        while (!gameOver) {

            Block block = new Block();
            int column = (int) Math.ceil(Math.random() * (10 - block.getWidth()));
            block.setCoordX(0);
            block.setCoordY(column);
            train = new Train(block, grid, leftBtn, rightBtn, downBtn, rotateBtn, this, locker);
            System.out.println("поток создан");
            train.start();
            train.awaitTetris();
            checkLine();
        }
    }

    public void GameOver(){
        stopGen();
        tetris.scoreView.post(new Runnable() {
            @Override
            public void run() {
                tetris.showGameOver();
            }
        });
    }

    public void stopGen() {
        gameOver = true;
        train.tetrisStop();
    }

    public void checkLine() {
        int count = 0;
        for (int i = grid.length - 1; i > -1; i--) {
            count = 0;
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j].getTag().equals("occupied")) {
                    count++;
                }
            }
            if (count == grid[0].length) {
                deleteLine(i);
            }
        }
    }


    public void deleteLine(int i) {
        for (int n = 0; n < grid[0].length; n++) {
            final int a = n;
            final int b = i;
            grid[i][n].post(new Runnable() {
                @Override
                public void run() {
                    grid[b][a].setTag("free");
                    grid[b][a].setBackgroundResource(R.drawable.btn_light_back);
                }
            });
        }
        for (int n = i; n > 0; n--) {
            for (int k = 0; k < grid[0].length; k++) {
                final int a = n;
                final int b = k;
                grid[a][b].post(new Runnable() {
                    @Override
                    public void run() {
                        grid[a][b].setTag(grid[a - 1][b].getTag());
                        grid[a][b].setBackground(grid[a - 1][b].getBackground());
                    }
                });
            }
        }

        addScore(200);
    }


    public void addScore(int sum) {
        tetris.scoreView.post(new Runnable() {
            @Override
            public void run() {
                tetris.updateScore(sum);
            }
        });

    }

}
