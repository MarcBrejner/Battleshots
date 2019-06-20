package com.example.battleshots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class CreateGameActivity extends AppCompatActivity {

    public String gameID;
    Server server = new Server();
    public String player1;
    public String player2 = "Søren Ryge";
    Intent startMenuIntent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        gameID = generateGameID();

        startMenuIntent = getIntent();
        player1 = startMenuIntent.getExtras().getString("pName");

        TextView gameHostedTextView = (TextView) findViewById(R.id.gamehosted_id);
        gameHostedTextView.setText("Your Game has been hosted");

        TextView gameIdTextView = (TextView) findViewById(R.id.gameidtext_id);
        gameIdTextView.setText("Game ID : "+gameID);

        TextView player1TextView = (TextView) findViewById(R.id.player1_id);
        player1TextView.setText("Player 1 : "+player1);

        TextView player2TextView = (TextView) findViewById(R.id.player2_id);
        player2TextView.setText("Player 2 : "+player2);


    }


    public String generateGameID(){
        char[] chars1 = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890".toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++)
        {
            char c1 = chars1[random.nextInt(chars1.length)];
            stringBuilder.append(c1);
        }
        String generated_gameID = stringBuilder.toString();
        return generated_gameID;
    }


}




