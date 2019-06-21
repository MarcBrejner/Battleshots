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

public class JoinGameActivity extends AppCompatActivity {

    public String gameID;
    Server server = new Server();

    public Player player2;
    Intent startMenuIntent;

    Map<String, Object> info;
    ValueEventListener valueEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);


        startMenuIntent = getIntent();
        gameID = startMenuIntent.getStringExtra("joinGameID");


        player2 = new Player(startMenuIntent.getStringExtra("pName"));
        server.joinGame(gameID, player2);


        final TextView gameIdTextView = (TextView) findViewById(R.id.gameidtext_id);
        gameIdTextView.setText("Game ID : " + gameID);

         server.gameRef.addValueEventListener(valueEventListener = new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 info = (HashMap<String, Object>) dataSnapshot.getValue();
                 Boolean isStarting = (Boolean) info.get("isStarted");
                 //Toasttest
                 Toast.makeText(getApplicationContext(),"Game is Starting", Toast.LENGTH_SHORT).show();

                 if(isStarting) {
                     //Launch setupActivity
                     Intent intent = new Intent(getApplicationContext(), setupActivity.class);
                     intent.putExtra("gameID", gameID);
                     intent.putExtra("playerID", 2);
                     startActivity(intent);
                     // startGame();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }

         });

     /*   server.gameRef.child(gameID).addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfo = (HashMap<String, Object>) dataSnapshot.child("Player 2").getValue();
                String playerName = (String) playerInfo.get("playerName");
                if (playerName != null) {
                    player1TextView.setText("Player 2 : " + playerName);
                }
            }
  });


  /*  @Override
    protected void onResume() {
        if(playerList.size() == 2) {
            hasEnoughPlayers = true;
        }
        super.onResume();
    }*/

    }
    @Override
    protected void onPause(){
        server.gameRef.removeEventListener(valueEventListener);
        super.onPause();
    }

    public void startGame(){
        Toast.makeText(getApplicationContext(), "Game is Starting", Toast.LENGTH_SHORT).show();
    }




}


