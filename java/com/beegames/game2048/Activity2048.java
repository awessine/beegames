package com.beegames.game2048;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.beegames.R;
import com.beegames.menus.MainActivity;

import java.util.ArrayList;


public class Activity2048 extends AppCompatActivity {

    private int score = 0;
    private int prevScore;
    private int highscore;
    //public OnSwipeTouchListener swipe = new OnSwipeTouchListener(this);
    private TextView[][] array = new TextView[4][4];
    private TextView[][] prevArray;
    public ConstraintLayout table;
    public TextView textScore;
    public TextView textScore2;
    private boolean firstScoreSet = true;
    private Button newGame;
    private Button back;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_2048);
        GridToArray();
        firstGenerate();
        table = findViewById(R.id.ConstraintLayout);
        textScore = findViewById(R.id.textScore);
        textScore2 = findViewById(R.id.textScore2);
        textScore.setText("Score : " + getScore());
        newGame = (Button) findViewById(R.id.NewGame);
        //back = (Button) findViewById(R.id.Reset);

        if(MainActivity.readScore("score_2048", this) !=0 ){
            setScore(MainActivity.readScore("score_2048", this));
            MainActivity.readGridText("grid_2048", this, array);
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    changeColor(array[i][j]);
                }
            }
            textScore.setText("Score : " + getScore());
        }

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restart();
            }
        });
        

        MainActivity.DownloadFromDB("score_2048", textScore2);
        //setHighscore(0);
        //RunAnimation();
        Swipe();
        System.out.println(textScore2.getText().toString());
        //System.out.println(1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        MainActivity.writeGridText("grid_2048", this, array);
        MainActivity.writeScore("score_2048", this, getScore());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.writeGridText("grid_2048", this, array);
        MainActivity.writeScore("score_2048", this, getScore());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MainActivity.writeGridText("grid_2048", this, array);
        MainActivity.writeScore("score_2048", this, getScore());
        finish();
    }

    public void setArray(TextView[][] array) {
        this.array = array;
    }

    public int getHighscore() {
        return highscore;
    }

    public int getScore() {
        return score;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void firstGenerate() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[i][j].setText("");
                changeColor(array[i][j]);
            }
        }
        int first1 = 0;
        int second1 = 0;
        int first2 = 0;
        int second2 = 0;
        while (first1 == second1 && first2 == second2) {
            first1 = (int) Math.ceil(Math.random() * 3);
            second1 = (int) Math.ceil(Math.random() * 3);
            first2 = (int) Math.ceil(Math.random() * 3);
            first2 = (int) Math.ceil(Math.random() * 3);
        }
        array[first1][second1].setText("2");
        array[first2][second2].setText("2");
        changeColor(array[first1][second1]);
        changeColor(array[first2][second2]);
    }


    public void GridToArray() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String id = "t" + i + j;
                int resId = getResources().getIdentifier(id, "id", getPackageName());
                array[i][j] = (TextView) findViewById(resId);
            }
        }
    }

    public void updateScoreBoard(int arrInt) {
        setScore(getScore() + arrInt);
        textScore.setText("Score : " + getScore());
        setHighscore(Math.max(getHighscore(), getScore()));
        if (firstScoreSet) {
            setHighscore(MainActivity.findScore(textScore2.getText().toString()));
        }
        textScore2.setText("Highscore : " + getHighscore());

        MainActivity.UploadOnDB(getHighscore(), "score_2048");

        firstScoreSet = false;

    }

    @SuppressLint("ClickableViewAccessibility")
    public void Swipe() {
        final boolean[] moved = {false};
        table.setOnTouchListener(new OnSwipeTouchListener(Activity2048.this) {
            public void onSwipeTop() {
                moved[0] = false;
                for (int j = 0; j <= 3; j++) {
                    for (int i = 0; i <= 3; i++) {
                        if ((array[i][j].getText().equals(""))) {
                            for (int k = i + 1; k <= 3; k++) {
                                if (!(array[k][j].getText().equals(""))) {
                                    //RunAnimationUp(array[k][j]);
                                    
                                    array[i][j].setText(array[k][j].getText());
                                    array[k][j].setText("");

                                    changeColor(array[i][j]);
                                    changeColor(array[k][j]);

                                    RunAnimationUp(array[i][j]);
                                    moved[0] = true;

                                    if (i > 0 && array[i][j].getText().equals(array[i - 1][j].getText())) {
                                        
                                        String arrStr = array[i][j].getText().toString();
                                        int arrInt = Integer.parseInt(arrStr) * 2;
                                        array[i - 1][j].setText("" + arrInt);
                                        array[i][j].setText("");
                                        changeColor(array[i][j]);
                                        changeColor(array[i - 1][j]);

                                        RunAnimationUp(array[i][j]);
                                        updateScoreBoard(arrInt);

                                        moved[0] = true;
                                    }
                                    break;
                                }
                            }
                        } else {
                            for (int k = i + 1; k <= 3; k++) {
                                if (array[k][j].getText().equals("")) {

                                } else if (array[k][j].getText().equals(array[i][j].getText())) {
                                    
                                    String arrStr = array[i][j].getText().toString();
                                    int arrInt = Integer.parseInt(arrStr) * 2;
                                    array[i][j].setText("" + arrInt);
                                    array[k][j].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[k][j]);

                                    RunAnimationUp(array[i][j]);
                                    updateScoreBoard(arrInt);
                                    moved[0] = true;
                                    break;
                                } else {
                                    break;
                                }
                            }


                        }
                    }
                }
                if (moved[0]) {
                    Generate();
                }

            }

            public void onSwipeRight() {
                moved[0] = false;
                for (int i = 0; i <= 3; i++) {
                    for (int j = 3; j >= 0; j--) {
                        if ((array[i][j].getText().equals(""))) {
                            for (int k = j - 1; k >= 0; k--) {
                                if (!(array[i][k].getText().equals(""))) {
                                    
                                    array[i][j].setText(array[i][k].getText());
                                    array[i][k].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[i][k]);

                                    RunAnimationRight(array[i][j]);
                                    moved[0] = true;

                                    if (j < 3 && array[i][j].getText().equals(array[i][j + 1].getText())) {
                                        
                                        String arrStr = array[i][j].getText().toString();
                                        int arrInt = Integer.parseInt(arrStr) * 2;
                                        array[i][j + 1].setText("" + arrInt);
                                        array[i][j].setText("");
                                        changeColor(array[i][j]);
                                        changeColor(array[i][j + 1]);

                                        RunAnimationRight(array[i][j]);
                                        updateScoreBoard(arrInt);
                                        moved[0] = true;
                                    }
                                    break;
                                }
                            }
                        } else {
                            for (int k = j - 1; k >= 0; k--) {
                                if (array[i][k].getText().equals("")) {

                                } else if (array[i][k].getText().equals(array[i][j].getText())) {
                                    
                                    String arrStr = array[i][j].getText().toString();
                                    int arrInt = Integer.parseInt(arrStr) * 2;
                                    array[i][j].setText("" + arrInt);
                                    array[i][k].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[i][k]);

                                    RunAnimationRight(array[i][j]);
                                    updateScoreBoard(arrInt);
                                    moved[0] = true;
                                    break;
                                } else {
                                    break;
                                }
                            }


                        }
                    }
                }
                if (moved[0]) {
                    Generate();
                }
            }

            public void onSwipeLeft() {
                moved[0] = false;
                for (int i = 0; i <= 3; i++) {
                    for (int j = 0; j <= 3; j++) {
                        if ((array[i][j].getText().equals(""))) {
                            for (int k = j + 1; k <= 3; k++) {
                                if (!(array[i][k].getText().equals(""))) {
                                    
                                    array[i][j].setText(array[i][k].getText());
                                    array[i][k].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[i][k]);

                                    RunAnimationLeft(array[i][j]);
                                    moved[0] = true;

                                    if (j > 0 && array[i][j].getText().equals(array[i][j - 1].getText())) {
                                        
                                        String arrStr = array[i][j].getText().toString();
                                        int arrInt = Integer.parseInt(arrStr) * 2;
                                        array[i][j - 1].setText("" + arrInt);
                                        array[i][j].setText("");
                                        changeColor(array[i][j]);
                                        changeColor(array[i][j - 1]);

                                        RunAnimationLeft(array[i][j]);
                                        updateScoreBoard(arrInt);
                                        moved[0] = true;
                                    }
                                    break;
                                }
                            }
                        } else {
                            for (int k = j + 1; k <= 3; k++) {
                                if (array[i][k].getText().equals("")) {

                                } else if (array[i][k].getText().equals(array[i][j].getText())) {
                                    
                                    String arrStr = array[i][j].getText().toString();
                                    int arrInt = Integer.parseInt(arrStr) * 2;
                                    array[i][j].setText("" + arrInt);
                                    array[i][k].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[i][k]);

                                    RunAnimationLeft(array[i][j]);
                                    updateScoreBoard(arrInt);
                                    moved[0] = true;
                                    break;
                                } else {
                                    break;
                                }
                            }


                        }
                    }
                }
                if (moved[0]) {
                    Generate();
                }
            }

            public void onSwipeBottom() {
                moved[0] = false;
                for (int j = 0; j <= 3; j++) {
                    for (int i = 3; i >= 0; i--) {
                        if ((array[i][j].getText().equals(""))) {
                            for (int k = i - 1; k >= 0; k--) {
                                if (!(array[k][j].getText().equals(""))) {
                                    
                                    array[i][j].setText(array[k][j].getText());
                                    array[k][j].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[k][j]);
                                    RunAnimationDown(array[i][j]);

                                    moved[0] = true;

                                    if (i < 3 && array[i][j].getText().equals(array[i + 1][j].getText())) {
                                        
                                        String arrStr = array[i][j].getText().toString();
                                        int arrInt = Integer.parseInt(arrStr) * 2;
                                        array[i + 1][j].setText("" + arrInt);
                                        array[i][j].setText("");
                                        changeColor(array[i][j]);
                                        changeColor(array[i + 1][j]);

                                        RunAnimationDown(array[i][j]);
                                        updateScoreBoard(arrInt);
                                        moved[0] = true;
                                    }
                                    break;
                                }
                            }
                        } else {
                            for (int k = i - 1; k >= 0; k--) {
                                if (array[k][j].getText().equals("")) {

                                } else if (array[k][j].getText().equals(array[i][j].getText())) {
                                    
                                    String arrStr = array[i][j].getText().toString();
                                    int arrInt = Integer.parseInt(arrStr) * 2;
                                    array[i][j].setText("" + arrInt);
                                    array[k][j].setText("");
                                    changeColor(array[i][j]);
                                    changeColor(array[k][j]);

                                    RunAnimationDown(array[i][j]);
                                    updateScoreBoard(arrInt);
                                    moved[0] = true;
                                    break;
                                } else {
                                    break;
                                }
                            }


                        }
                    }
                }
                if (moved[0]) {
                    Generate();
                }
            }
        });
    }

    public void Generate() {
        boolean occupied = true;
        ArrayList<TextView> textViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (array[i][j].getText().equals("")) {
                    textViews.add(array[i][j]);
                }
            }
        }
        int number = (int) (Math.random() * textViews.size());
        TextView view = textViews.get(number);
        view.setText("2");
        changeColor(view);
        RunAnimation(view);
        checkGameOver();
    }

    public void checkGameOver() {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!array[i][j].getText().equals("")) {
                    count++;
                }
            }
        }
        if (count == 16) {
            boolean flag = true;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (i > 0 && i < 3 && j > 0 && j < 3) {
                        if (array[i][j].getText().equals(array[i + 1][j].getText()) || array[i][j].getText().equals(array[i - 1][j].getText()) || array[i][j].getText().equals(array[i][j + 1].getText()) || array[i][j].getText().equals(array[i][j - 1].getText())) {
                            flag = false;
                        }
                    } else if (i > 0 && i < 3 && j == 0) {
                        if (array[i][j].getText().equals(array[i + 1][j].getText()) || array[i][j].getText().equals(array[i - 1][j].getText()) || array[i][j].getText().equals(array[i][j + 1].getText())) {
                            flag = false;
                        }
                    } else if (i > 0 && i < 3 && j == 3) {
                        if (array[i][j].getText().equals(array[i + 1][j].getText()) || array[i][j].getText().equals(array[i - 1][j].getText()) || array[i][j].getText().equals(array[i][j - 1].getText())) {
                            flag = false;
                        }
                    } else if (j > 0 && j < 3 && i == 0) {
                        if (array[i][j].getText().equals(array[i][j + 1].getText()) || array[i][j].getText().equals(array[i + 1][j].getText()) || array[i][j].getText().equals(array[i][j - 1].getText())) {
                            flag = false;
                        }
                    } else if (j > 0 && j < 3 && i == 3) {
                        if (array[i][j].getText().equals(array[i][j + 1].getText()) || array[i][j].getText().equals(array[i - 1][j].getText()) || array[i][j].getText().equals(array[i][j - 1].getText())) {
                            flag = false;
                        }
                    } else if (i == 0 && j == 0) {
                        if (array[i][j].getText().equals(array[i][j + 1].getText()) || array[i][j].getText().equals(array[i + 1][j].getText())) {
                            flag = false;
                        }
                    } else if (i == 0 && j == 3) {
                        if (array[i][j].getText().equals(array[i][j - 1].getText()) || array[i][j].getText().equals(array[i + 1][j].getText())) {
                            flag = false;
                        }
                    } else if (i == 3 && j == 0) {
                        if (array[i][j].getText().equals(array[i][j + 1].getText()) || array[i][j].getText().equals(array[i - 1][j].getText())) {
                            flag = false;
                        }
                    } else if (i == 3 && j == 3) {
                        if (array[i][j].getText().equals(array[i][j - 1].getText()) || array[i][j].getText().equals(array[i - 1][j].getText())) {
                            flag = false;
                        }
                    }
                }
            }
            if (flag) {
                onButtonShowPopupWindowClick();
            }
        }
    }

    public void changeColor(TextView view) {
        CharSequence text = view.getText();
        if ("".equals(text)) {
            view.setBackgroundResource(R.drawable.back_0);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.btn_leaderbrd_text) );
        } else if ("2".equals(text)) {
            view.setBackgroundResource(R.drawable.back_1);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
        } else if ("4".equals(text)) {
            view.setBackgroundResource(R.drawable.back_1);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
        } else if ("8".equals(text)) {
            view.setBackgroundResource(R.drawable.back_2);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
        } else if ("16".equals(text)) {
            view.setBackgroundResource(R.drawable.back_2);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.btn_leaderbrd_text));
        } else if ("32".equals(text)) {
            view.setBackgroundResource(R.drawable.back_3);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("64".equals(text)) {
            view.setBackgroundResource(R.drawable.back_3);
            //view.setTextSize(55);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("128".equals(text)) {
            view.setBackgroundResource(R.drawable.back_4);
            //view.setTextSize(40);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("512".equals(text)) {
            view.setBackgroundResource(R.drawable.back_4);
            //view.setTextSize(40);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("1024".equals(text)) {
            view.setBackgroundResource(R.drawable.back_5);
            //view.setTextSize(30);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("2048".equals(text)) {
            view.setBackgroundResource(R.drawable.back_5);
            //view.setTextSize(30);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("4096".equals(text)) {
            view.setBackgroundResource(R.drawable.back_5);
            //view.setTextSize(30);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else if ("8192".equals(text)) {
            view.setBackgroundResource(R.drawable.back_5);
            //view.setTextSize(30);
            view.setTextColor(getResources().getColor(R.color.light_text));
        } else {
            view.setBackgroundResource(R.drawable.back_5);
            //view.setTextSize(30);
            view.setTextColor(getResources().getColor(R.color.light_text));
        }

    }

    private void RunAnimation(View view) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.generation);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }

    private void RunAnimationUp(View view) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.move_up);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }

    private void RunAnimationDown(View view) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.move_down);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }

    private void RunAnimationRight(View view) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.move_right);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }

    private void RunAnimationLeft(View view) {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.move_left);
        a.reset();
        view.clearAnimation();
        view.startAnimation(a);
    }


    public void onButtonShowPopupWindowClick() {

        WinDialog2048 winDialog2048 = new WinDialog2048(this, "You lost", getScore());
        winDialog2048.setCancelable(false);
        winDialog2048.show();
        restart();

    }

    public void restart() {
        setScore(0);
        textScore.setText("Score : " + getScore());
        firstGenerate();
    }

    public void savePreviousGrid(TextView[][] array, int score){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                prevArray[i][j] = array[i][j];
            }
        }
        prevScore = getScore();
    }

    public void backGrid() {
        setScore(prevScore);
        textScore.setText("Score : " + prevScore);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                array[i][j].setText(prevArray[i][j].getText().toString());
                changeColor(array[i][j]);
            }
        }
    }

}