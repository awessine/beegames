package com.beegames.minesw;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.beegames.R;
import com.beegames.menus.MainActivity;

public class SaperActivity extends AppCompatActivity {
    public static int time = 999000;
    public static int bomb = 20;
    public static int size = 8;
    private TextView smiley, timer, flag, flagsLeft;
    private MineSweeperGame mineSweeperGame;
    private int secondsElapsed;
    private boolean timerStarted;
    private boolean firstpick = false;
    private TextView scoreView;
    private int[] score;
    //WinDialogMineSw outOfTimeDialog = new WinDialogMineSw(this, "Out of time, you lost", 50);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.saper_activity);

        createGrid();
        scoreView = (TextView) findViewById(R.id.mineSwScore);
        MainActivity.DownloadFromDB("score_minesw", scoreView);
        score = MainActivity.DownloadFromDB("score_minesw");
        System.out.println(score[0]);

        flagsLeft = findViewById(R.id.activity_main_flagsleft);
        if (firstpick){
            flagsLeft.setText( "Flags left : " + (mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
        }
        else{
            flagsLeft.setText( "Flags left : " + bomb);
        }



        smiley = findViewById(R.id.activity_main_smiley);
        smiley.setOnClickListener(view -> {
            flag.setBackgroundResource(R.drawable.flag);
            firstpick = false;
            flagsLeft.setText("Flags left : " +  bomb);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    findViewById(x * size + y).setBackgroundResource(R.drawable.btn_light_back);
                    ((Button) findViewById(x * size + y)).setText("");
                }
            }
        });

        flag = findViewById(R.id.activity_main_flag);
        flag.setOnClickListener(view -> {
            if (firstpick){
                mineSweeperGame.toggleMode();
                if (mineSweeperGame.isFlagMode()) {
                    flag.setBackgroundResource(R.drawable.flag_pressed);
                } else {
                    flag.setBackgroundResource(R.drawable.flag);
                }
            }
        });
    }

    private void openemptyCell(View view) {
        int x = getidX(view);
        int y = getidY(view);
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y - 1) != null) {
            openCell(findViewById((y - 1) * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x, y - 1) != null) {
            openCell(findViewById((y - 1) * size + x));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y - 1) != null) {
            openCell(findViewById((y - 1) * size + (x + 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y) != null) {
            openCell(findViewById(y * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y) != null) {
            openCell(findViewById(y * size + (x + 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x - 1, y + 1) != null) {
            openCell(findViewById((y + 1) * size + (x - 1)));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x, y + 1) != null) {
            openCell(findViewById((y + 1) * size + x));
        }
        if (mineSweeperGame.getMineGrid().cellAt(x + 1, y + 1) != null) {
            openCell(findViewById((y + 1) * size + (x + 1)));
        }
    }

    private void openCell(View view) {
        Cell cell;
        cell = mineSweeperGame.getMineGrid().cellAt(getidX(view), getidY(view));
        if (!cell.isRevealed() && !cell.isFlagged() && mineSweeperGame.isClearMode()) {
            if (cell.getValue() == -1) {
                ((Button) findViewById(view.getId())).setText(R.string.bomb);
                view.setBackgroundResource(R.drawable.btn_superdark_back);
                WinDialogMineSw winDialog = new WinDialogMineSw(this, "You lost", 10);
                winDialog.setCancelable(false);
                winDialog.show();
                MainActivity.UploadOnDB(score[0] + 10,"score_minesw");
                revealAllBombs();
                mineSweeperGame.GameOver();
            } else if (cell.getValue() == 0) {
                cell.setRevealed(true);
                openemptyCell(view);
                view.setBackgroundResource(R.drawable.btn_dark_back);
            } else {
                String s = String.valueOf(cell.getValue());
                ((Button) findViewById(view.getId())).setText(s);
                view.setBackgroundResource(R.drawable.btn_dark_back);
                if (cell.getValue() == 1) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 2) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 3) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 4) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 5) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 6) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 7) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
                if (cell.getValue() == 8) {
                    ((Button) findViewById(view.getId())).setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
                }
            }
            cell.setRevealed(true);
        }
    }


    private void clickCell(Button button) {
        button.setOnClickListener(view -> {
            if (!firstpick){
                mineSweeperGame = new MineSweeperGame(size, bomb, getidX(view), getidY(view));
                firstpick = true;
            }
            if (!mineSweeperGame.getOutOfTime() && !mineSweeperGame.isGameWon()) {
                if (mineSweeperGame.isGameOver()) {
                    WinDialogMineSw winDialog = new WinDialogMineSw(this, "You lost", 10);
                    MainActivity.UploadOnDB(score[0] + 10,"score_minesw");
                    winDialog.setCancelable(false);
                    winDialog.show();
                } else {
                    Cell cell;
                    cell = mineSweeperGame.getMineGrid().cellAt(getidX(view), getidY(view));
                    openCell(view);
                    if (mineSweeperGame.isFlagMode() && !cell.isRevealed()) {
                        if (mineSweeperGame.getFlagCount() >= mineSweeperGame.getNumberBombs() && !cell.isFlagged()) {
                            Toast.makeText(getApplicationContext(), "Out of flags", Toast.LENGTH_SHORT).show();
                        } else {
                            if (cell.isFlagged()) {
                                mineSweeperGame.setFlagCount(-1);
                                ((Button) findViewById(view.getId())).setText("");
                            } else {
                                ((Button) findViewById(view.getId())).setText(R.string.flag);
                                mineSweeperGame.setFlagCount(1);
                            }
                            cell.setFlagged(!cell.isFlagged());
                        }

                        flagsLeft.setText("Flags left : " + (mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()));
                    }
                }
            }
            if (mineSweeperGame.isGameWon()) {
                WinDialogMineSw winDialog = new WinDialogMineSw(this, "You won!", 500);
                MainActivity.UploadOnDB(score[0] + 500,"score_minesw");
                winDialog.setCancelable(false);
                winDialog.show();
                revealAllBombs();
            }
        });
    }

    private void revealAllBombs() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (mineSweeperGame.getMineGrid().cellAt(x, y).getValue() == -1) {
                    ((Button) findViewById(y * size + x)).setText(R.string.bomb);
                    mineSweeperGame.getMineGrid().cellAt(x, y).setRevealed(true);
                }
            }
        }
    }

    public void createGrid() {
        TableLayout tableLayout = findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        for (int x = 0; x < size; x++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            for (int y = 0; y < size; y++) {
                Button button = new Button(this);
                button.setId(x * size + y);
                button.setBackgroundResource(R.drawable.btn_light_back);
                button.setLayoutParams(new TableRow.LayoutParams((int) 850 / size, (int) 850 / size));
                clickCell(button);
                tableRow.addView(button, y);
            }
            tableLayout.addView(tableRow, x);
        }
    }

    private int getidX(View view) {
        int id = view.getId();
        int a = id - id / size * size;
        return a;
    }

    private int getidY(View view) {
        int id = view.getId();
        int a = id / size;
        return a;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}