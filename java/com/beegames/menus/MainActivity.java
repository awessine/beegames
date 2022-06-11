package com.beegames.menus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.beegames.R;
import com.beegames.Tetris.Tetris;
import com.beegames.auth.LoginActivity;
import com.beegames.game2048.Activity2048;
import com.beegames.minesw.SaperActivity;
import com.beegames.tictac.Connection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    private TextView nameField;
    private Button btnLogout;
    private Button btnLeaderBrd;
    private ImageButton btn2048;
    private ImageButton btnTicTac;
    private ImageButton btnSaper;
    private ImageButton btnTetris;
    private String name = "";


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        /*txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);*/
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLeaderBrd = (Button) findViewById(R.id.btnLeaderBrd);
        btn2048 = (ImageButton) findViewById(R.id.imageButton2);
        btnTicTac = (ImageButton) findViewById(R.id.imageButton);
        btnSaper = (ImageButton) findViewById(R.id.imageButton3);
        btnTetris = (ImageButton) findViewById(R.id.imageButton4);
        nameField = (TextView) findViewById(R.id.name);

        setName();
        name = nameField.getText().toString();
        writeString("connection_name", this, name);
        //clearConnections();
        btnTetris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchTetris();
            }
        });

        btn2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launch2048();
            }
        });

        btnTicTac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchTicTac();
            }
        });

        btnSaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchSaper();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToLogIn();
            }
        });

        btnLeaderBrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLeaderBrd();
            }
        });


    }

    public static void clearConnections(String name){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference databaseReference = db.getReference("TicTac");
        databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){

                    for(DataSnapshot s : snapshot.getChildren()){

                        if(s.hasChildren()){
                            for(DataSnapshot b : s.getChildren()){
                                System.out.println(b.child("player_name").getValue(String.class));
                                if(b.child("player_name").getValue(String.class).equals(name)){
                                    System.out.println(databaseReference.child("connections").child(s.getKey()).child(b.getKey()).child("player_value") + " " + System.currentTimeMillis());
                                    databaseReference.child("connections").child(s.getKey()).child(b.getKey()).removeValue();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void goToLeaderBrd() {
        Intent intent = new Intent(MainActivity.this, LeaderBoard.class);
        startActivity(intent);
    }

    private void BackToLogIn() {
        SharedPreferences myPreferences = getSharedPreferences(LoginActivity.IS_LOGGED_IN, LoginActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPreferences.edit();
        editor.putBoolean("LOG_IN", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void LaunchTetris() {
        Intent intent = new Intent(MainActivity.this, Tetris.class);
        startActivity(intent);
    }

    public void Launch2048() {
        Intent intent = new Intent(MainActivity.this, Activity2048.class);
        startActivity(intent);
    }

    public void LaunchTicTac() {
        Intent intent = new Intent(MainActivity.this, Connection.class);
        intent.putExtra("playerName", nameField.getText().toString());
        startActivity(intent);

    }

    public void LaunchSaper() {
        Intent intent = new Intent(MainActivity.this, SaperActivity.class);
        startActivity(intent);
    }

    public void setName(){
        DatabaseReference users;
        FirebaseAuth auth;
        FirebaseDatabase db;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                nameField.setText("  " + map.get("name") + "  " );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(vListener);
        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeEventListener(vListener);
    }

    public static void UploadOnDB(int score, String scorename) {

        String score1 = ((Integer) score).toString();
        Map<String, Object> data = new HashMap<>();
        data.put(scorename, score1);

        DatabaseReference users;
        FirebaseAuth auth;
        FirebaseDatabase db;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data);
    }


    public static void DownloadFromDB(String scorename, TextView textView) {
        DatabaseReference users;
        FirebaseAuth auth;
        FirebaseDatabase db;

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                textView.setText("Highscore : " + map.get(scorename));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(vListener);

    }

    public static int[] DownloadFromDB(String scorename) {
        final Integer[] score = {0};
        DatabaseReference users;
        FirebaseAuth auth;
        FirebaseDatabase db;
        final int[] scoreBuff = new int[1];

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> map = (Map<String, String>) snapshot.getValue();
                scoreBuff[0] =Integer.parseInt(map.get(scorename));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(vListener);
        return scoreBuff;
    }

    public static int findScore(String string) {
        String[] words;
        words = string.split(" ");
        return Integer.parseInt(words[words.length - 1]);
    }

    public static void writeGridText(String filename, Context ctx, TextView[][] array) {
        List<String[]> strArray = new ArrayList<String[]>();
        String[] buff;
        for (int i = 0; i < array.length; i++) {
            buff = new String[array.length];

            for (int j = 0; j < array[i].length; j++) {
                buff[j] = array[i][j].getText().toString();
                if(array[i][j].getText().toString().equals("")){
                    buff[j] = " ";
                }
            }
            strArray.add(buff);
        }

        SharedPreferences prefs = ctx.getSharedPreferences(filename + "_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (String[] s : strArray) {
            for (String i : s){
                sb.append(i).append("<s>");
            }
            sb.append("<a>");
        }
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(filename, sb.toString()).apply();
    }

    public static void writeGridTag(String filename, Context ctx, TextView[][] array) {
        List<String[]> strArray = new ArrayList<String[]>();
        String[] buff;
        for (int i = 0; i < array.length; i++) {
            buff = new String[array.length];

            for (int j = 0; j < array[i].length; j++) {
                buff[j] = array[i][j].getTag().toString();
            }
            strArray.add(buff);
        }

        SharedPreferences prefs = ctx.getSharedPreferences(filename + "_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (String[] s : strArray) {
            for (String i : s){
                sb.append(i).append("<s>");
            }
            sb.append("<a>");
        }
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(filename, sb.toString()).apply();
    }

    public static void writeGridBack(String filename, Context ctx, TextView[][] array) {
        List<String[]> strArray = new ArrayList<String[]>();
        String[] buff;
        for (int i = 0; i < array.length; i++) {
            buff = new String[array.length];

            for (int j = 0; j < array[i].length; j++) {
                //if(array[i][j].getB)
                buff[j] = array[i][j].getBackground().toString();
            }
            strArray.add(buff);
        }

        SharedPreferences prefs = ctx.getSharedPreferences(filename + "_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (String[] s : strArray) {
            for (String i : s){
                sb.append(i).append("<s>");
            }
            sb.append("<a>");
        }
        sb.delete(sb.length() - 3, sb.length());
        editor.putString(filename, sb.toString()).apply();
    }



    public static void readGridTag(String filename, Context ctx, TextView[][] array) {
        SharedPreferences prefs = ctx.getSharedPreferences(filename + "_prefs", MODE_PRIVATE);
        String buff = prefs.getString(filename, "");
        ArrayList<String[]> list = new ArrayList<>();
        String[] strings = buff.split("<a>");
        for(String s : strings){
            list.add(s.split("<s>"));
        }
        for(int i = 0; i< array.length; i ++){
            for ( int j = 0; j < array[i].length; j++){
                String[] str = list.get(i);
                array[i][j].setTag(str[j]);
            }
        }
    }

    public static void readGridText(String filename, Context ctx, TextView[][] array) {
        SharedPreferences prefs = ctx.getSharedPreferences(filename + "_prefs", MODE_PRIVATE);
        String buff = prefs.getString(filename, "");
        ArrayList<String[]> list = new ArrayList<>();
        String[] strings = buff.split("<a>");
        for(String s : strings){
            list.add(s.split("<s>"));
        }
        for(int i = 0; i< array.length; i ++){
            for ( int j = 0; j < array[i].length; j++){
                String[] str = list.get(i);
                array[i][j].setText(str[j]);
                if(str[j].equals(" ")){
                    array[i][j].setText("");
                }
            }
        }
    }

    public static void writeString(String filename, Context ctx, String str) {

        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    ctx.openFileOutput(filename, MODE_PRIVATE)));
            // пишем данные
            bw.write(str);
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String filename, Context ctx) {

        String string = "";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    ctx.openFileInput(filename)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                string = str;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static void writeScore(String filename, Context ctx, int score) {

        try {
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    ctx.openFileOutput(filename, MODE_PRIVATE)));
            // пишем данные
            bw.write(((Integer) score).toString());
            // закрываем поток
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int readScore(String filename, Context ctx) {

        int score = 0;
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    ctx.openFileInput(filename)));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                score = Integer.parseInt(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return score;
    }
}
