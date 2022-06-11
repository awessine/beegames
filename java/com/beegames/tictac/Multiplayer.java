package com.beegames.tictac;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beegames.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Multiplayer extends AppCompatActivity {
    private final String[] boxesSelectedBy = {"", "", "", "", "", "", "", "", ""};
    private final List<int[]> combinationsList = new ArrayList();

    public String connectionId = "";

    public String connectionUniqueId;
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://beegames-4b280-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference databaseReference = db.getReference("TicTac");

    public final List<String> doneBoxes = new ArrayList();

    public ImageView image1;

    public ImageView image2;

    public ImageView image3;

    public ImageView image4;

    public ImageView image5;

    public ImageView image6;

    public ImageView image7;

    public ImageView image8;

    public ImageView image9;

    public boolean opponentFound = false;

    public String opponentUniqueId = "0";
    private LinearLayout player1Layout;
    private TextView player1TV;
    private LinearLayout player2Layout;

    public TextView player2TV;

    public String playerTurn = "";

    public String playerUniqueId = "0";

    public String status = "matching";
    ValueEventListener turnsEventListener;
    ValueEventListener wonEventListener;
    final String getPlayerName ="";
    private ValueEventListener valueEventListener;
    private TextView turnView;

    /* access modifiers changed from: protected */
    @SuppressLint("SourceLockedOrientationActivity")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_tictac);
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
        this.player1TV = (TextView) findViewById(R.id.Player1);
        this.player2TV = (TextView) findViewById(R.id.Player2);
        this.combinationsList.add(new int[]{0, 1, 2});
        this.combinationsList.add(new int[]{3, 4, 5});
        this.combinationsList.add(new int[]{6, 7, 8});
        this.combinationsList.add(new int[]{0, 3, 6});
        this.combinationsList.add(new int[]{1, 4, 7});
        this.combinationsList.add(new int[]{2, 5, 8});
        this.combinationsList.add(new int[]{2, 4, 6});
        this.combinationsList.add(new int[]{0, 4, 8});

        WaitDialog waitDialog = new WaitDialog(Multiplayer.this);
        waitDialog.setCancelable(false);
        waitDialog.show();

        String valueOf = String.valueOf(System.currentTimeMillis());
        this.playerUniqueId = valueOf;

        this.player1TV.setText(getPlayerName);
        valueEventListener = this.databaseReference.child("connections").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Iterator<DataSnapshot> it;
                Iterator<DataSnapshot> it2;
                boolean playerFound;
                DataSnapshot dataSnapshot = snapshot;
                if (Multiplayer.this.opponentFound) {
                    return;
                }
                if (snapshot.hasChildren()) {
                    Iterator<DataSnapshot> it3 = snapshot.getChildren().iterator();
                    while (it3.hasNext()) {
                        DataSnapshot connections = it3.next();
                        String conId = connections.getKey();
                        int getPlayersCount = (int) connections.getChildrenCount();
                        if (!Multiplayer.this.status.equals("waiting")) {
                            it = it3;
                            if (getPlayersCount == 1) {
                                boolean samePlayer = false;
                                Iterator<DataSnapshot> it4 = connections.getChildren().iterator();
                                while (true) {
                                    if (it4.hasNext()) {
                                        if (it4.next().getKey().equals(Multiplayer.this.playerUniqueId)) {
                                            samePlayer = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (!samePlayer) {
                                    connections.child(Multiplayer.this.playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                                    Iterator<DataSnapshot> it5 = connections.getChildren().iterator();
                                    if (it5.hasNext()) {
                                        DataSnapshot players = it5.next();
                                        String unused = Multiplayer.this.opponentUniqueId = players.getKey();
                                        Multiplayer multiplayer = Multiplayer.this;
                                        String unused2 = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                                        Multiplayer multiplayer2 = Multiplayer.this;
                                        multiplayer2.applyPlayerTurn(multiplayer2.playerTurn);
                                        Multiplayer.this.player2TV.setText((String) players.child("player_name").getValue(String.class));
                                        String unused3 = Multiplayer.this.connectionId = conId;
                                        boolean unused4 = Multiplayer.this.opponentFound = true;
                                        Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).addValueEventListener(Multiplayer.this.turnsEventListener);
                                        Multiplayer.this.databaseReference.child("won").child(Multiplayer.this.connectionId).addValueEventListener(Multiplayer.this.wonEventListener);
                                        if (waitDialog.isShowing()) {
                                            waitDialog.dismiss();
                                        }
                                        Multiplayer.this.databaseReference.child("connections").removeEventListener((ValueEventListener) this);
                                    }
                                }
                            }
                        } else if (getPlayersCount == 2) {
                            Multiplayer multiplayer3 = Multiplayer.this;
                            String unused5 = multiplayer3.playerTurn = multiplayer3.playerUniqueId;
                            Multiplayer multiplayer4 = Multiplayer.this;
                            multiplayer4.applyPlayerTurn(multiplayer4.playerTurn);
                            boolean playerFound2 = false;
                            for (DataSnapshot players2 : connections.getChildren()) {
                                if (players2.getKey().equals(Multiplayer.this.playerUniqueId)) {
                                    playerFound2 = true;
                                    it2 = it3;
                                } else {
                                    if (playerFound2) {
                                        it2 = it3;
                                        playerFound = playerFound2;
                                        String unused6 = Multiplayer.this.opponentUniqueId = players2.getKey();
                                        Multiplayer.this.player2TV.setText((String) players2.child("player_name").getValue(String.class));
                                        String unused7 = Multiplayer.this.connectionId = conId;
                                        boolean unused8 = Multiplayer.this.opponentFound = true;
                                        Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).addValueEventListener(Multiplayer.this.turnsEventListener);
                                        Multiplayer.this.databaseReference.child("won").child(Multiplayer.this.connectionId).addValueEventListener(Multiplayer.this.wonEventListener);
                                        if (waitDialog.isShowing()) {
                                            waitDialog.dismiss();
                                        }
                                        Multiplayer.this.databaseReference.child("connections").removeEventListener((ValueEventListener) this);
                                    } else {
                                        it2 = it3;
                                        playerFound = playerFound2;
                                    }
                                    playerFound2 = playerFound;
                                }
                                it3 = it2;
                            }
                            it = it3;
                            boolean z = playerFound2;
                        } else {
                            it = it3;
                        }
                        it3 = it;
                    }
                    if (!Multiplayer.this.opponentFound && !Multiplayer.this.status.equals("waiting")) {
                        String unused9 = Multiplayer.this.connectionUniqueId = String.valueOf(System.currentTimeMillis());
                        dataSnapshot.child(Multiplayer.this.connectionUniqueId).child(Multiplayer.this.playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                        String unused10 = Multiplayer.this.status = "waiting";
                        return;
                    }
                    return;
                }
                String unused11 = Multiplayer.this.connectionUniqueId = String.valueOf(System.currentTimeMillis());
                dataSnapshot.child(Multiplayer.this.connectionUniqueId).child(Multiplayer.this.playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                String unused12 = Multiplayer.this.status = "waiting";
            }

            public void onCancelled(DatabaseError error) {
            }
        });
        this.turnsEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getChildrenCount() == 2) {
                        int getBoxPosition = Integer.parseInt((String) dataSnapshot.child("box_position").getValue(String.class));
                        String getPlayerId = (String) dataSnapshot.child("player_id").getValue(String.class);
                        if (!Multiplayer.this.doneBoxes.contains(String.valueOf(getBoxPosition))) {
                            Multiplayer.this.doneBoxes.add(String.valueOf(getBoxPosition));
                            if (getBoxPosition == 1) {
                                Multiplayer multiplayer = Multiplayer.this;
                                multiplayer.selectBox(multiplayer.image1, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 2) {
                                Multiplayer multiplayer2 = Multiplayer.this;
                                multiplayer2.selectBox(multiplayer2.image2, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 3) {
                                Multiplayer multiplayer3 = Multiplayer.this;
                                multiplayer3.selectBox(multiplayer3.image3, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 4) {
                                Multiplayer multiplayer4 = Multiplayer.this;
                                multiplayer4.selectBox(multiplayer4.image4, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 5) {
                                Multiplayer multiplayer5 = Multiplayer.this;
                                multiplayer5.selectBox(multiplayer5.image5, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 6) {
                                Multiplayer multiplayer6 = Multiplayer.this;
                                multiplayer6.selectBox(multiplayer6.image6, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 7) {
                                Multiplayer multiplayer7 = Multiplayer.this;
                                multiplayer7.selectBox(multiplayer7.image7, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 8) {
                                Multiplayer multiplayer8 = Multiplayer.this;
                                multiplayer8.selectBox(multiplayer8.image8, getBoxPosition, getPlayerId);
                            } else if (getBoxPosition == 9) {
                                Multiplayer multiplayer9 = Multiplayer.this;
                                multiplayer9.selectBox(multiplayer9.image9, getBoxPosition, getPlayerId);
                            }
                        }
                    }
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        };
        this.wonEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                WinDialogTicTac winDialogTicTac;
                if (snapshot.hasChild("player_id")) {
                    if (((String) snapshot.child("player_id").getValue(String.class)).equals(Multiplayer.this.playerUniqueId)) {
                        winDialogTicTac = new WinDialogTicTac(Multiplayer.this, "You won the game", 200);
                    } else {
                        winDialogTicTac = new WinDialogTicTac(Multiplayer.this, "Opponent won the game", 50);
                    }
                    winDialogTicTac.setCancelable(false);
                    winDialogTicTac.show();
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).removeEventListener(Multiplayer.this.turnsEventListener);
                    Multiplayer.this.databaseReference.child("won").child(Multiplayer.this.connectionId).removeEventListener(Multiplayer.this.wonEventListener);
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        };
        this.image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("1") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("1");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("2") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("2");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("3") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("3");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("4") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("4");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("5") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("5");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("6") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("6");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("7") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("7");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("8") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("8");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
        this.image9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Multiplayer.this.doneBoxes.contains("9") && Multiplayer.this.playerTurn.equals(Multiplayer.this.playerUniqueId)) {
                    ((ImageView) v).setImageResource(R.drawable.cross);
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("box_position").setValue("9");
                    Multiplayer.this.databaseReference.child("turns").child(Multiplayer.this.connectionId).child(String.valueOf(Multiplayer.this.doneBoxes.size() + 1)).child("player_id").setValue(Multiplayer.this.playerUniqueId);
                    Multiplayer multiplayer = Multiplayer.this;
                    String unused = multiplayer.playerTurn = multiplayer.opponentUniqueId;
                }
            }
        });
    }


    public void applyPlayerTurn(String playerUniqueId2) {
        if (playerUniqueId2.equals(this.playerUniqueId)) {
            turnView.setText("Your turn");
            return;
        }
        else{
            turnView.setText("Your opponent's turn");
        }

    }


    public void selectBox(ImageView imageView, int selectedBoxPosition, String selectedByPlayer) {
        this.boxesSelectedBy[selectedBoxPosition - 1] = selectedByPlayer;
        if (selectedByPlayer.equals(this.playerUniqueId)) {
            imageView.setImageResource(R.drawable.cross);
            this.playerTurn = this.opponentUniqueId;
        } else {
            imageView.setImageResource(R.drawable.circle);
            this.playerTurn = this.playerUniqueId;
        }
        applyPlayerTurn(this.playerTurn);
        if (checkPlayerWin(selectedByPlayer)) {
            this.databaseReference.child("won").child(this.connectionId).child("player_id").setValue(selectedByPlayer);
        }
        if (this.doneBoxes.size() == 9) {
            WinDialogTicTac winDialogTicTac = new WinDialogTicTac(this, "It is a Draw!", 100);
            winDialogTicTac.setCancelable(false);
            winDialogTicTac.show();
        }
    }

    private boolean checkPlayerWin(String playerId) {
        boolean isPlayerWon = false;
        for (int i = 0; i < this.combinationsList.size(); i++) {
            int[] combination = this.combinationsList.get(i);
            if (this.boxesSelectedBy[combination[0]].equals(playerId) && this.boxesSelectedBy[combination[1]].equals(playerId) && this.boxesSelectedBy[combination[2]].equals(playerId)) {
                isPlayerWon = true;
            }
        }
        return isPlayerWon;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.databaseReference.child("connections").child(this.connectionId).removeValue();
        this.databaseReference.child("turns").child(this.connectionId).removeValue();
        this.databaseReference.child("won").child(this.connectionId).removeValue();
        Multiplayer.this.databaseReference.child("connections").removeEventListener(valueEventListener);
    }

    public void onStop() {
        super.onStop();
        this.databaseReference.child("connections").child(this.connectionId).removeValue();
        this.databaseReference.child("turns").child(this.connectionId).removeValue();
        this.databaseReference.child("won").child(this.connectionId).removeValue();
        Multiplayer.this.databaseReference.child("connections").removeEventListener(valueEventListener);
    }
}