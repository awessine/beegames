package com.beegames.Tetris;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beegames.R;
import com.beegames.menus.MainActivity;

public class Tetris extends AppCompatActivity {

    private static TextView[][] grid = new TextView[20][10];
    private Generator generator;
    public Button leftBtn;
    public Button rightBtn;
    public Button downBtn;
    public Button rotateBtn;
    public TextView scoreView;
    public TextView highscoreView;
    private int score = 0;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tetris);
        scoreView = (TextView) findViewById(R.id.tetrisScore);
        highscoreView = (TextView) findViewById(R.id.tetrisHighscore);
        leftBtn = (Button) findViewById(R.id.leftBtn);
        rightBtn = (Button) findViewById(R.id.rightBtn);
        rotateBtn = (Button) findViewById(R.id.rotateBtn);
        /*ngBtn = (Button) findViewById(R.id.newGameTetris);*/
        getGrid();
        /*ngBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
*/
        MainActivity.DownloadFromDB("score_tetris", highscoreView);

        if (MainActivity.readScore("score_tetris1", this) != 0) {

            setScore(MainActivity.readScore("score_tetris1", this));
            updateScore(0);

            MainActivity.readGridTag("grid_tetris", this, grid);
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j].getTag().equals("occupied")) {
                        grid[i][j].setTag("occupied");
                        grid[i][j].setBackgroundResource(rdmColor());
                    }
                }
            }
        } else {
            newGame();
        }

        generator = new Generator(grid, leftBtn, rightBtn, downBtn, rotateBtn, this);
        generator.start();

    }

    public void setScore(int score) {
        this.score = score;
    }

    public void showGameOver() {
        MainActivity.UploadOnDB(MainActivity.findScore(highscoreView.getText().toString()), "score_tetris");
        WinDialogTetris winDialog = new WinDialogTetris(this, "You lost", getScore());
        winDialog.setCancelable(false);
        try {
            winDialog.show();
        } catch (WindowManager.BadTokenException e) {
        }
        newGame();
    }

    public void restart() {
        MainActivity.UploadOnDB(MainActivity.findScore(highscoreView.getText().toString()), "score_tetris");
        newGame();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void newGame() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].setTag("free");
                grid[i][j].setBackgroundResource(R.drawable.btn_light_back);
            }
        }
        setScore(0);
        MainActivity.writeGridTag("grid_tetris", this, grid);
        MainActivity.writeScore("score_tetris1", this, 0);
        updateScore(0);
    }

    public int rdmColor() {
        int check = (int) Math.ceil(Math.random() * 5);
        if (check == 1) {
            return R.drawable.back_7;
        } else if (check == 2) {
            return R.drawable.back_1;
        } else if (check == 3) {
            return R.drawable.back_2;
        } else if (check == 4) {
            return R.drawable.back_6;
        } else {
            return R.drawable.back_4;
        }
    }

    public void getGrid() {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                String id = "block" + i + (j + 1);
                int resId = getResources().getIdentifier(id, "id", getPackageName());
                grid[i][j] = (TextView) findViewById(resId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.writeGridTag("grid_tetris", this, grid);
        MainActivity.writeScore("score_tetris1", this, getScore());
        generator.stopGen();
        MainActivity.UploadOnDB(MainActivity.findScore(highscoreView.getText().toString()), "score_tetris");
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score1) {
        setScore(getScore() + score1);
        scoreView.setText("Score : " + getScore());
        if (getScore() > MainActivity.findScore(highscoreView.getText().toString())) {
            highscoreView.setText("Highscore : " + getScore());
        }
    }
}
