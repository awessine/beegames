package com.beegames.tictac;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beegames.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SinglePlayer extends AppCompatActivity {
    private static final int[] computerTurns = {1, 3, 2, 9, 5, 7, 4, 6, 8};
    private static final int[] computerTurns2 = {3, 7, 5, 1, 4, 9, 2, 6, 8};
    private static final int[] computerTurns3 = {1, 5, 9, 7, 5, 3, 2, 4, 6};
    private static final int[] computerTurns4 = {1, 7, 4, 3, 5, 9, 8, 6, 2};
    private final String[] boxesSelectedBy = {"", "", "", "", "", "", "", "", ""};
    private final List<int[]> combinationsList = new ArrayList();
    private int[] data = null;
    /* access modifiers changed from: private */
    public final List<String> doneBoxes = new ArrayList();
    /* access modifiers changed from: private */
    public ImageView image1;
    /* access modifiers changed from: private */
    public ImageView image2;
    /* access modifiers changed from: private */
    public ImageView image3;
    /* access modifiers changed from: private */
    public ImageView image4;
    /* access modifiers changed from: private */
    public ImageView image5;
    /* access modifiers changed from: private */
    public ImageView image6;
    /* access modifiers changed from: private */
    public ImageView image7;
    /* access modifiers changed from: private */
    public ImageView image8;
    /* access modifiers changed from: private */
    public ImageView image9;
    private LinearLayout player1Layout;
    private LinearLayout player2Layout;
    /* access modifiers changed from: private */
    public int playerTurn = 1;
    private TextView turnView;

    /* access modifiers changed from: protected */
    @SuppressLint("SourceLockedOrientationActivity")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView((int) R.layout.activity_tictac);
        final String getPlayerName = getIntent().getStringExtra("playerName");
        turnView = (TextView) findViewById(R.id.turnView);
        this.image1 = (ImageView) findViewById(R.id.cell00);
        this.image2 = (ImageView) findViewById(R.id.cell01);
        this.image3 = (ImageView) findViewById(R.id.cell02);
        this.image4 = (ImageView) findViewById(R.id.cell10);
        this.image5 = (ImageView) findViewById(R.id.cell11);
        this.image6 = (ImageView) findViewById(R.id.cell12);
        this.image7 = (ImageView) findViewById(R.id.cell20);
        this.image8 = (ImageView) findViewById(R.id.cell21);
        this.image9 = (ImageView) findViewById(R.id.cell22);
        TextView player1TV = (TextView) findViewById(R.id.Player1);
        TextView player2TV = (TextView) findViewById(R.id.Player2);
        int position = new Random().nextInt(4);
        if (position == 0) {
            this.data = computerTurns;
        } else if (position == 1) {
            this.data = computerTurns2;
        } else if (position == 2) {
            this.data = computerTurns3;
        } else {
            this.data = computerTurns4;
        }
        Log.e("klajsfkljafssaf", "asd" + Arrays.toString(this.data));
        this.combinationsList.add(new int[]{0, 1, 2});
        this.combinationsList.add(new int[]{3, 4, 5});
        this.combinationsList.add(new int[]{6, 7, 8});
        this.combinationsList.add(new int[]{0, 3, 6});
        this.combinationsList.add(new int[]{1, 4, 7});
        this.combinationsList.add(new int[]{2, 5, 8});
        this.combinationsList.add(new int[]{2, 4, 6});
        this.combinationsList.add(new int[]{0, 4, 8});
        player1TV.setText(getPlayerName);
        player2TV.setText("Computer");
        this.image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("1")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image1, 1, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("2")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image2, 2, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("3")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image3, 3, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("4")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image4, 4, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("5")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image5, 5, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("6")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image6, 6, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("7")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image7, 7, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("8")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image8, 8, SinglePlayer.this.playerTurn);
                }
            }
        });
        this.image9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (SinglePlayer.this.playerTurn == 1 && !SinglePlayer.this.doneBoxes.contains("9")) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image9, 9, SinglePlayer.this.playerTurn);
                }
            }
        });
    }

    private void applyPlayerTurn() {
        if (this.playerTurn == 1) {
            turnView.setText("Your turn");
            return;
        }
        else{
            turnView.setText("Computer's turn");
        }

        if (this.doneBoxes.size() < 9) {
            performComputer();
        }
    }

    /* access modifiers changed from: private */
    public void selectBox(ImageView imageView, int selectedBoxPosition, int playerTurn2) {
        WinDialogTicTac winDialogTicTac;
        this.boxesSelectedBy[selectedBoxPosition - 1] = String.valueOf(playerTurn2);
        if (playerTurn2 == 1) {
            imageView.setImageResource(R.drawable.cross);
            this.playerTurn = 0;
        } else {
            imageView.setImageResource(R.drawable.circle);
            this.playerTurn = 1;
        }
        this.doneBoxes.add(String.valueOf(selectedBoxPosition));
        if (checkPlayerWin(playerTurn2)) {
            if (playerTurn2 == 1) {
                winDialogTicTac = new WinDialogTicTac(this, "You won the game",  100);
            } else {
                winDialogTicTac = new WinDialogTicTac(this, "Computer won the game", 25);
            }
            winDialogTicTac.setCancelable(false);
            winDialogTicTac.show();
        } else {
            applyPlayerTurn();
        }
        if (this.doneBoxes.size() == 9 && !checkPlayerWin(playerTurn2)) {
            WinDialogTicTac winDialogTicTac2 = new WinDialogTicTac(this, "It is a Draw!", 50);
            winDialogTicTac2.setCancelable(false);
            winDialogTicTac2.show();
        }
    }

    private void performComputer() {
        int[] combination = new int[0];
        int selectedToWin = 0;
        for (int i = 0; i < this.combinationsList.size(); i++) {
            combination = this.combinationsList.get(i);
            selectedToWin = 0;
            if (this.boxesSelectedBy[combination[0]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[2]].equals(String.valueOf(1))) {
                selectedToWin = 0 + 1;
            }
            if (this.boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[0]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[2]].equals(String.valueOf(1))) {
                selectedToWin++;
            }
            if (this.boxesSelectedBy[combination[2]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[0]].equals(String.valueOf(1))) {
                selectedToWin++;
            }
            if (selectedToWin == 2) {
                break;
            }
        }
        if (selectedToWin != 2) {
            for (int i2 = 0; i2 < this.combinationsList.size(); i2++) {
                combination = this.combinationsList.get(i2);
                int selectedToWin2 = 0;
                if (this.boxesSelectedBy[combination[0]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[2]].equals(String.valueOf(2))) {
                    selectedToWin2 = 0 + 1;
                }
                if (this.boxesSelectedBy[combination[1]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[0]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[2]].equals(String.valueOf(2))) {
                    selectedToWin2++;
                }
                if (this.boxesSelectedBy[combination[2]].equals(String.valueOf(1)) && !this.boxesSelectedBy[combination[1]].equals(String.valueOf(2)) && !this.boxesSelectedBy[combination[0]].equals(String.valueOf(2))) {
                    selectedToWin2++;
                }
                if (selectedToWin == 2) {
                    break;
                }
            }
        }
        if (selectedToWin != 2) {
            int l = 1;
            while (true) {
                int[] iArr = this.data;
                if (l > iArr.length) {
                    return;
                }
                if (!this.doneBoxes.contains(String.valueOf(iArr[l]))) {
                    selectBoxOfComputer(this.data[l]);
                    return;
                }
                l++;
            }
        } else {
            for (int i3 : combination) {
                if (!this.doneBoxes.contains(String.valueOf(i3 + 1))) {
                    selectBoxOfComputer(i3 + 1);
                    return;
                }
            }
        }
    }

    private void selectBoxOfComputer(final int l) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                int i = l;
                if (i == 1) {
                    SinglePlayer singlePlayer = SinglePlayer.this;
                    singlePlayer.selectBox(singlePlayer.image1, l, 2);
                } else if (i == 2) {
                    SinglePlayer singlePlayer2 = SinglePlayer.this;
                    singlePlayer2.selectBox(singlePlayer2.image2, l, 2);
                } else if (i == 3) {
                    SinglePlayer singlePlayer3 = SinglePlayer.this;
                    singlePlayer3.selectBox(singlePlayer3.image3, l, 2);
                } else if (i == 4) {
                    SinglePlayer singlePlayer4 = SinglePlayer.this;
                    singlePlayer4.selectBox(singlePlayer4.image4, l, 2);
                } else if (i == 5) {
                    SinglePlayer singlePlayer5 = SinglePlayer.this;
                    singlePlayer5.selectBox(singlePlayer5.image5, l, 2);
                } else if (i == 6) {
                    SinglePlayer singlePlayer6 = SinglePlayer.this;
                    singlePlayer6.selectBox(singlePlayer6.image6, l, 2);
                } else if (i == 7) {
                    SinglePlayer singlePlayer7 = SinglePlayer.this;
                    singlePlayer7.selectBox(singlePlayer7.image7, l, 2);
                } else if (i == 8) {
                    SinglePlayer singlePlayer8 = SinglePlayer.this;
                    singlePlayer8.selectBox(singlePlayer8.image8, l, 2);
                } else {
                    SinglePlayer singlePlayer9 = SinglePlayer.this;
                    singlePlayer9.selectBox(singlePlayer9.image9, l, 2);
                }
            }
        }, 2000);
    }

    private boolean checkPlayerWin(int playerTurn2) {
        boolean isPlayerWon = false;
        for (int i = 0; i < this.combinationsList.size(); i++) {
            int[] combination = this.combinationsList.get(i);
            if (this.boxesSelectedBy[combination[0]].equals(String.valueOf(playerTurn2)) && this.boxesSelectedBy[combination[1]].equals(String.valueOf(playerTurn2)) && this.boxesSelectedBy[combination[2]].equals(String.valueOf(playerTurn2))) {
                isPlayerWon = true;
            }
        }
        return isPlayerWon;
    }
}
