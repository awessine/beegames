package com.beegames.Tetris;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beegames.R;

import java.util.concurrent.locks.ReentrantLock;

public class Train extends Thread {

    private Block block;
    private Block prevBlock;
    private TextView[][] grid;
    private double sleepTime = 0.5;
    boolean exit = false;
    private ReentrantLock locker;

    public Button leftBtn;
    public Button rightBtn;
    public Button downBtn;
    public Button rotateBtn;
    private Generator generator;
    private int[] clrArray;

    private boolean alreadyPressed = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void run() {
        super.run();
        System.out.println("поток запущен");
        tetrisSleep();
        prevBlock = new Block(block.getCoordX(), block.getCoordY());
        prevBlock.setForm(block.getForm());
        prevBlock.setFormName(block.getFormName());
        prevBlock.setRotation(block.getRotation());
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveLeft();
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRight();
            }
        });
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateBlock();
            }
        });

        while (block.getCoordX() < grid.length && !exit && checkOccupiedX()) {
            redraw(block.getForm());
            block.setCoordX(block.getCoordX() + 1);
            if (block.getCoordX() == grid.length - 1) {
                rotateBtn.setOnClickListener(null);
            }
            tetrisSleep();
        }
        rotateBtn.setOnClickListener(null);
        leftBtn.setOnClickListener(null);
        rightBtn.setOnClickListener(null);
        setOccupied();
        generator.addScore(10);
        looseCheck();
        System.out.println("поток не запущен");
        synchronized (this) {
            this.notifyAll();
        }
    }

    public Train(Block block, TextView[][] grid, Button leftBtn, Button rightBtn, Button downBtn, Button rotateBtn, Generator generator, ReentrantLock locker) {
        this.block = block;
        this.grid = grid;
        this.leftBtn = leftBtn;
        this.rotateBtn = rotateBtn;
        this.rightBtn = rightBtn;
        this.downBtn = downBtn;
        this.generator = generator;
        this.locker = locker;
    }

    public boolean checkDistance() {
        if (!checkOccupiedYLeft() && grid[0].length - block.getWidth() - block.getCoordY() <= Math.abs(block.getHeight() - block.getWidth())) {
            return false;
        }
        if (!checkOccupiedYRight() && block.getCoordY() + block.getWidth() - 1 < Math.max(block.getHeight(), block.getWidth())) {
            return false;
        }
        return true;
    }

    public int checkDistBfrOccupied() {
        int count = 0;
        if (!checkOccupiedYLeft()) {
            for (int i = block.getCoordY(); i < grid[0].length; i++) {
                if (grid[block.getCoordX()][i].getTag().equals("occupied")) {
                    break;
                }
                count++;
            }
        } else {
            count = block.getHeight();
        }
        return count;
    }

    public void rotateBlock() {

        if ((checkOccupiedYRight() || checkOccupiedYLeft())
                && checkDistance()
                && checkDistBfrOccupied() >= block.getHeight()
                && checkOccupiedX()
                /*&& block.getCoordX()==0*/)
        {
            block.changeRotation();
            if (!checkOccupiedYRight() || block.getCoordY() + block.getWidth() - 1 >= grid[0].length) {
                block.setCoordY(block.getCoordY() - block.getWidth() + block.getHeight());
            }
            redraw(block.getForm());
            prevBlock.changeRotation();
            if (!checkOccupiedYRight() || prevBlock.getCoordY() + prevBlock.getWidth() - 1 >= grid[0].length) {
                prevBlock.setCoordY(prevBlock.getCoordY() - prevBlock.getWidth() + prevBlock.getHeight());
            }
            redraw(block.getForm());
        }

    }

    public void redraw(int[][] form) {

        int py = prevBlock.getCoordY();
        int px = prevBlock.getCoordX();
        int y = block.getCoordY();
        int x = block.getCoordX();
        int[][] prevForm = prevBlock.getForm();
        for (int i = 0; i < prevForm.length; i++) {
            for (int j = 0; j < prevForm[0].length; j++) {
                final int a = i;
                final int b = j;
                if (prevForm[i][j] == 1 && (px - i) >= 0 && (px - i) < grid.length ) {

                    grid[px - i][py + j].post(new Runnable() {
                        @Override
                        public void run() {
                            grid[px - a][py + b].setBackgroundResource(R.drawable.btn_light_back);
                        }
                    });
                }
            }
        }
        prevBlock.setCoordY(block.getCoordY());
        prevBlock.setCoordX(block.getCoordX());
        for (int i = 0; i < form.length; i++) {
            for (int j = 0; j < form[i].length; j++) {
                final int a = i;
                final int b = j;
                if (form[i][j] == 1 && (x - i) >= 0 && (x - i) < grid.length ) {

                    grid[x - i][y + j].post(new Runnable() {
                        @Override
                        public void run() {
                            grid[x - a][y + b].setBackgroundResource(block.getColor());
                        }
                    });

                }
            }
        }

    }

    public void moveLeft() {
        if (block.getCoordY() > 0 && block.getCoordX() < grid.length && checkOccupiedYLeft()) {
            block.setCoordY(block.getCoordY() - 1);
            redraw(block.getForm());
        }
    }

    public void moveRight() {
        if (block.getCoordY() < grid[0].length - block.getWidth() && block.getCoordX() < grid.length && checkOccupiedYRight()) {
            block.setCoordY(block.getCoordY() + 1);
            redraw(block.getForm());
        }
    }


    public void incSpeed() {
        if (alreadyPressed) {
            sleepTime = 0.8;
            alreadyPressed = false;
        } else {
            sleepTime = 0.1;
            alreadyPressed = true;
        }
    }

    public void tetrisSleep() {
        try {
            Thread.sleep((long) (1000 * sleepTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void tetrisStop() {
        exit = true;
    }

    public synchronized void awaitTetris() {
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void looseCheck() {
        for (int i = 0; i < block.getHeight(); i++) {
            if (block.getCoordX() - i <= 1) {
                generator.GameOver();
            }
        }

    }

    public void setOccupied() {
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                if (block.getForm()[i][j] == 1) {
                    try {
                        grid[block.getCoordX() - i - 1][block.getCoordY() + j].setTag("occupied");
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }
                }
            }
        }
    }

    public boolean checkOccupiedX() {
        int x = block.getCoordX();
        int y = block.getCoordY();

        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                if (x < grid.length
                        && (x - i) > 0
                        && grid[x - i][y + j].getTag() == "occupied"
                        && block.getForm()[i][j] == 1
                ) {
                    return false;
                }
            }

        }

        return true;
    }

    public boolean checkOccupiedYRight() {
        int x = block.getCoordX();
        int y = block.getCoordY();
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                if ((x - i) > 0
                        && (y + j + 1) < grid[0].length
                        && grid[x - i][y + j +1].getTag() == "occupied"
                        && block.getForm()[i][j] == 1
                ) {
                    return false;
                }
            }
        }
        return true;

    }

    public boolean checkOccupiedYLeft() {

        int x = block.getCoordX();
        int y = block.getCoordY();
        for (int i = 0; i < block.getHeight(); i++) {
            for (int j = 0; j < block.getWidth(); j++) {
                if ((x - i) > 0
                        && (y + j - 1) > 0
                        && grid[x - i][y + j - 1].getTag() == "occupied"
                        && block.getForm()[i][j] == 1
                ) {
                    return false;
                }
            }
        }
        return true;
    }


}
