package com.beegames.minesw;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.beegames.R;

public class WinDialogMineSw extends Dialog {
    private String message;
    private final Activity multiplayer;
    private int score = 0;


    public WinDialogMineSw(@NonNull Context context, String message, int score) {
        super(context);
        this.message = message;
        this.multiplayer = (Activity)context;
        this.score = score;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_dialog);
        final TextView msgBox = (TextView) findViewById(R.id.msgBox);
        final TextView gotScoreBox = (TextView) findViewById(R.id.gotScoreBox);
        final Button startBtn = (Button) findViewById(R.id.startBtn);
        gotScoreBox.setText("You have gained " + score + " scores");

        msgBox.setText(message);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                multiplayer.finish();
                getContext().startActivity(new Intent(getContext(), SaperActivity.class));
            }
        });
    }
}
