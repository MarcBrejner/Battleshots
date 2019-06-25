package com.example.battleshots;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateGameActivity extends AppCompatActivity {

    public String gameID;
    Server server = new Server();
    public Player player1;
    public String player2Name = "Waiting for player 2";
    GameModel gameModel;
    Intent startMenuIntent;
    Map<String, Object> playerInfo, gameInfo;
    ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        //Generate random gameID
        gameID = generateGameID();

        //Check if game with GameID already exists
        server.reference.child("Game").child(gameID).addListenerForSingleValueEvent(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    gameID = generateGameID();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //get Player1 from startMenuIntent
        startMenuIntent = getIntent();
        gameModel = new GameModel(startMenuIntent.getStringExtra("pName"));
        player1 = gameModel.getPlayers().get(0);

        //Create game and add player 1
        server.createGame(gameID, player1);

        //Add Dummy player to the game
        Player dummyPlayer = new Player("Waiting for player 2");
        server.joinGame(gameID,dummyPlayer);

        //Set textViews
        TextView gameHostedTextView = findViewById(R.id.gamehosted_id);
        gameHostedTextView.setText("Your Game has been hosted");

        TextView gameIdTextView = findViewById(R.id.gameidtext_id);
        gameIdTextView.setText("Game ID : " + gameID);

        TextView player1TextView = findViewById(R.id.player1_id);
        player1TextView.setText("Player 1 : " + player1.getPlayerName());

        final TextView player2TextView = findViewById(R.id.player2_id);
        player2TextView.setText("Player 2 : " + "Waiting for player 2");

        server.gameRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfo = (HashMap<String, Object>)dataSnapshot.child("Player 2").getValue();
                String playerName = (String) playerInfo.get("playerName");

                if (playerName != null) {
                    player2TextView.setText("Player 2 : " + playerName);
                    player2Name = playerName;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void startGame(View view){
        if (!player2Name.equals("Waiting for player 2")) {
            //test

            //Game is started
            server.gameRef.child("isStarted").setValue(true);

            //Launch setupactivity
            Intent intent = new Intent(getApplicationContext(), setupActivity.class);
            intent.putExtra("gameID", gameID);
            intent.putExtra("playerID", "1");
            intent.putExtra("pName", player1.getPlayerName());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Need two players to start game", Toast.LENGTH_SHORT).show();
        }
    }

    public String generateGameID(){
        char[] chars1 = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            char c1 = chars1[random.nextInt(chars1.length)];
            stringBuilder.append(c1);
        }
        String generated_gameID = stringBuilder.toString();
        return generated_gameID;
    }

    @Override
    protected void onDestroy() {
        server.gameRef.removeEventListener(valueEventListener);
        super.onDestroy();
    }
}





