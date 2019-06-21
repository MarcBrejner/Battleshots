package com.example.battleshots;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

public class CreateGameActivity extends AppCompatActivity {

    public String gameID;
    Server server = new Server();
    public Player player1;
    GameModel gameModel;
    public Player player2;
    Intent startMenuIntent;
    Map<String, Object> playerInfo;
    private boolean hasEnoughPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hasEnoughPlayers = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        gameID = generateGameID();
        startMenuIntent = getIntent();
        gameModel = new GameModel(startMenuIntent.getStringExtra("pName"));
        player1 = gameModel.getPlayers().get(0);
        server.createGame(gameID, player1);

        //add Dummy player to game
        player2 = new Player("Waiting for player 2",8);
        server.joinGame(gameID,player2);
        // playerList = new ArrayList<>();


        TextView gameHostedTextView = (TextView) findViewById(R.id.gamehosted_id);
        gameHostedTextView.setText("Your Game has been hosted");

        TextView gameIdTextView = (TextView) findViewById(R.id.gameidtext_id);
        gameIdTextView.setText("Game ID : " + gameID);

        TextView player1TextView = (TextView) findViewById(R.id.player1_id);
        player1TextView.setText("Player 1 : " + player1.getPlayerName());

        final TextView player2TextView = (TextView) findViewById(R.id.player2_id);
        player2TextView.setText("Player 2 : " + player2);

        server.gameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfo = (HashMap<String, Object>)dataSnapshot.child("Player 2").getValue();
                String playerName = (String) playerInfo.get("playerName");
                if (playerName != null) {
                    player2TextView.setText("Player 2 : " + playerName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });




        /* @Override
    protected void onResume() {
        if(playerList.size() == 2) {
            hasEnoughPlayers = true;
        }
        super.onResume();
    } */

    }

    @Override
    protected void onDestroy() {
        server.deleteGameDataBase();
        super.onDestroy();
    }

    public void startGame(View view){


        if (hasEnoughPlayers) {
            // Intent intent = new Intent(getApplicationContext(), setupActivity.class);
            // startActivity(intent);
        } else {
            // Toast.makeText(getApplicationContext(), "Need two players to start game", Toast.LENGTH_SHORT).show();;
        }
    }



    public String generateGameID(){
        char[] chars1 = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++)
        {
            char c1 = chars1[random.nextInt(chars1.length)];
            stringBuilder.append(c1);
        }
        String generated_gameID = stringBuilder.toString();
        return generated_gameID;
    }

    public void hostGame(String gameId){

    }

}





