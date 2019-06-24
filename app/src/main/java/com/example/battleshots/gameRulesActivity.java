package com.example.battleshots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class gameRulesActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_shots_rules);
    }

    public void onClick(View view) {
        super.finish();
    }
}
