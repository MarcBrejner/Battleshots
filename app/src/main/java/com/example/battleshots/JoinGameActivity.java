package com.example.battleshots;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
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

                 if(isStarting) {
                     //Launch setupActivity
                     Toast.makeText(getApplicationContext(),"Game is Starting", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(getApplicationContext(), setupActivity.class);
                     intent.putExtra("gameID", gameID);
                     intent.putExtra("playerID", "2");
                     intent.putExtra("pName", player2.getPlayerName());
                     startActivity(intent);
                     finish();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }

         });

    }
    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        server.gameRef.removeEventListener(valueEventListener);
        super.onDestroy();
    }
}


