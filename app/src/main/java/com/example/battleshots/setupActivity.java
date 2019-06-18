package com.example.battleshots;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class setupActivity extends AppCompatActivity {

    public static int[] playerShips;
    private String TAG = "Setup-UserInterface";
    private int btnID, shipSize, gridSize = 8, btnDefault = 2131230759;
    Direction direction = Direction.LEFT;
    private boolean positionSetted = false;
    GameModel gameModel;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);
        shipSize = 2;
        gameModel = new GameModel(gridSize);
    }

    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());
        btnID = btn.getId();
        if (btn.getText() != "clicked") {
            btn.setText("clicked");
        } else {
            btn.setText("");
        }

        String text = Integer.toString(btn.getId());

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void rotateShip(View view) {
        switch (view.getId()) {
            //TODO: add cases to switch direction (ID+8 = DOWN, ID-8 = UP, ID+1 = RIGHT, ID-1 = LEFT)
        }

    }

    public void savePositions(View view) throws ShipException {
        if(!positionSetted) {
            throw new ShipException("Start position of the ship is not found");
        } else {
            gameModel.addShip(gameModel.getStartPoint(), shipSize, direction);
            positionSetted = false;
        }
    }

    public void setStartPosition() {
        Point point = gameModel.getGrid().get(btnID-btnDefault);
        gameModel.setStartPoisiton(point);
        positionSetted = true;
    }
}
