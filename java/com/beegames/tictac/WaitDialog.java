package com.beegames.tictac;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.beegames.R;

public class WaitDialog extends Dialog {
    private String message;
    private final Multiplayer multiplayer;


    public WaitDialog(@NonNull Context context) {
        super(context);
        this.multiplayer = (Multiplayer)context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_dialog);

        final TextView msgBox = (TextView) findViewById(R.id.msgBox);
        final Button startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                multiplayer.finish();
                getContext().startActivity(new Intent(getContext(), Connection.class));
            }
        });
    }


}
