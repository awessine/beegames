package com.beegames.menus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.beegames.Models.User;
import com.beegames.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard extends Activity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference users;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private ListView listView;
    private ListView listView2;
    private Spinner spinner;
    private List<String> userList;
    private List<String> scoreList;
    private ArrayAdapter<String> adapterUserList;
    private ArrayAdapter<String> adapterScoreList;
    private ArrayAdapter<CharSequence> adapterSpinner;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_leaderbrd);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");

        init();
        getDataFromDB();

    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        spinner = (Spinner) findViewById(R.id.spinner);

        adapterSpinner = ArrayAdapter.createFromResource(this, R.array.games, R.layout.spinner_selected_item);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_list);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(this);

        userList = new ArrayList<>();
        adapterUserList = new ArrayAdapter<>(this, R.layout.leader_userlist, userList);
        listView.setAdapter(adapterUserList);

        scoreList = new ArrayList<>();
        adapterScoreList = new ArrayAdapter<>(this, R.layout.leader_scorelist, scoreList);
        listView2.setAdapter(adapterScoreList);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
        users = db.getReference("Users");
    }

    private void getDataFromDB() {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (userList.size() > 0) {
                    userList.clear();
                    scoreList.clear();
                }


                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    if (user != null) {
                        userList.add("№ " + user.getName());
                        scoreList.add(user.getScore_2048());
                    }

                }
                sortLead((ArrayList<String>) userList, (ArrayList<String>) scoreList);
                adapterUserList.notifyDataSetChanged();
                adapterScoreList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        users.addValueEventListener(vListener);
    }

    private void getDataFromDB(String game) {
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (userList.size() > 0) {
                    userList.clear();
                    scoreList.clear();
                }

                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    if (user != null) {
                        if (game.equals("2048")) {
                            userList.add("№ " + user.getName());
                            scoreList.add(user.getScore_2048());
                        } else if (game.equals("Mine sweeper")) {
                            userList.add("№ " + user.getName());
                            scoreList.add(user.getScore_minesw());
                        } else if (game.equals("Tic Tac Toe")) {
                            userList.add("№ " + user.getName());
                            scoreList.add(user.getScore_tictac());
                        } else if (game.equals("Tetris")) {
                            userList.add("№ " + user.getName());
                            scoreList.add(user.getScore_tetris());
                        }

                    }
                }
                sortLead((ArrayList<String>) userList, (ArrayList<String>) scoreList);
                adapterUserList.notifyDataSetChanged();
                adapterScoreList.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(this, "Server is not available", Toast.LENGTH_LONG).show();
            }
        };

        users.addValueEventListener(vListener);
    }

    public void sortLead(ArrayList<String> arr, ArrayList<String> scores) {
        int n = arr.size();
        String[] userListBuff = new String[3];
        String[][] words = new String[arr.size()][3];
        String[][] wordsBuff = new String[arr.size()][2];

        for (int i = 0; i < arr.size(); i++) {
            wordsBuff[i] = arr.get(i).split(" ");
            words[i][0] = wordsBuff[i][0];
            words[i][1] = wordsBuff[i][1];
            words[i][2] = scores.get(i);
        }

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (Integer.parseInt(words[j][2]) < Integer.parseInt(words[j + 1][2])) {
                    userListBuff[1] = words[j][1];
                    userListBuff[2] = words[j][2];
                    words[j][1] = words[j + 1][1];
                    words[j][2] = words[j + 1][2];

                    words[j + 1][1] = userListBuff[1];
                    words[j + 1][2] = userListBuff[2];

                    swapped = true;
                }
            }
            if (!swapped) break;
        }

        for (int i = 0; i < arr.size(); i++) {
            arr.set(i, "№" + (i + 1) + " " + words[i][1]);
            scores.set(i, words[i][2]);
        }

    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        getDataFromDB(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
