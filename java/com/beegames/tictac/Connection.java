package com.beegames.tictac;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.beegames.R;
import com.beegames.menus.MainActivity;

public class Connection extends AppCompatActivity {

    private TextView scoreView;
    private Button findBtn;
    private Button SPBtn;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_connection);
        scoreView = (TextView) findViewById(R.id.scoreView);
        findBtn = (Button) findViewById(R.id.findBtn);
        SPBtn = (Button) findViewById(R.id.SPbtn);
        String PlayerName = "";
        if(getIntent().hasExtra("playerName")){
            PlayerName =getIntent().getStringExtra("playerName");
        }
        else
        {
            PlayerName = MainActivity.readString("connection_name",this);
        }
        final String getPlayerName = PlayerName;

        MainActivity.writeString("connection_name", this, getPlayerName);

        MainActivity.DownloadFromDB("score_tictac", scoreView);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connection.this, Multiplayer.class);
                intent.putExtra("playerName", getPlayerName);
                startActivity(intent);
                finish();
                //Connection.this.finish();
            }
        });

        SPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Connection.this, SinglePlayer.class);
                intent.putExtra("playerName", getPlayerName);
                startActivity(intent);
                finish();
            }
        });
    }
}
