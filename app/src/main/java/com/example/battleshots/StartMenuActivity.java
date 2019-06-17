package com.example.battleshots;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartMenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
    }

    public void createGame(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateGameActivity.class);
        startActivity(intent);
    }

    public void joinGame(View view) {
        Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
        startActivity(intent);
    }
}
