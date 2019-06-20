package com.example.battleshots;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class setupActivity extends AppCompatActivity {

    public static int[] playerShips;
    private String TAG = "Setup-UserInterface";
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, gridSize = 8, btnDefault = 2131230759;
    Direction direction;
    private boolean positionSet = false;
    GameModel gameModel;
    Server server;
    private int clicked = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);
        shipSize = 1;
        gameModel = new GameModel(gridSize);
        server = new Server();
    }

    @Override
    protected void onStart() {
        super.onStart();
        server.addGameModelToDatabase(gameModel);
    }

    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());
        btnID = btn.getId();
        if(btnID != prevbtnID) {
            btnClickAmount++;
            prevbtnID = btnID;
            rotateShip();
            if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
                btn.setBackground(ContextCompat.getDrawable(this, R.drawable.chosenbutton));
            } else {
                btn.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            }
        } else {
            btnClickAmount++;
            rotateShip();
        }
        setStartPosition();
    }

    public void rotateShip() {
        switch (btnClickAmount%4) {
            case 1:
                direction = Direction.LEFT;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                direction = Direction.UP;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Up", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                direction = Direction.RIGHT;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                direction = Direction.DOWN;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Down", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void savePositions(View view) throws ShipException {
        if(!positionSet) {
            throw new ShipException("Start position of the ship is not found");
        } else {
            server.addShipToDatabase(this, gameModel.addShip(gameModel.getStartPoint(),
                    shipSize, direction));
            positionSet = false;
        }
    }

    public void setStartPosition() {
        Point point = gameModel.getGrid().get(btnID-btnDefault);
        gameModel.setStartPoisiton(point);
        positionSet = true;
    }
}
