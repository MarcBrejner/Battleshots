package com.example.battleshots;

import android.os.Bundle;
import android.support.design.animation.Positioning;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class setupActivity extends AppCompatActivity {

    public static int[] playerShips;
    private String TAG = "Setup-UserInterface";
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, shipPlaced, gridSize = 8, btnDefault = 2131230759;
    Direction direction = Direction.DOWN;
    private boolean positionSet = false;
    GameModel gameModel;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);
        shipSize = 0;
        server = new Server();

        ImageView boat1 = findViewById(R.id.ship_one);
        boat1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shipSize = 1;
                }
                return false;
            }
        });

        ImageView boat3 = findViewById(R.id.ship_three);
        boat3.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    shipSize = 3;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //server.addGameModelToDatabase(gameModel);
    }

    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());
        btnID = btn.getId();
        if(btnID != prevbtnID) {
            prevbtnID = btnID;
            if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
                if(shipSize == 1) {
                    btn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
                } else if (shipSize == 3) {
                    placeBigShip(btn, direction);
                }
            } else {
                btn.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            }
        } else {
            btnClickAmount++;
            rotateShip();
        }
       // setStartPosition();
    }

    public void placeBigShip(Button startBtn, Direction direction) {
        int tmpId = startBtn.getId();
        int dir = 0;
        if(direction == Direction.UP) {
            dir = -8;
        } else if (direction == Direction.DOWN){
            dir = 8;
        } else if (direction == Direction.LEFT) {
            dir = -1;
        } else if (direction == Direction.RIGHT) {
            dir = 1;
        }
        Button tmpBtn1 = findViewById(tmpId+1*dir);
        Button tmpBtn2 = findViewById(tmpId+2*dir);
        Button tmpBtn3 = findViewById(tmpId+3*dir);

        if(shipSize == 3) {
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
            tmpBtn2.setBackground(ContextCompat.getDrawable(this,R.mipmap.ship_three_end));
            if(direction == Direction.LEFT) {
                tmpBtn1.setRotation(90);
                tmpBtn2.setRotation(90);
            } else if (direction == Direction.UP) {
                tmpBtn1.setRotation(180);
                tmpBtn1.setRotation(180);
            } else if (direction == Direction.RIGHT) {
                tmpBtn1.setRotation(270);
                tmpBtn1.setRotation(270);
            } else if (direction == Direction.DOWN) {
                tmpBtn1.setRotation(0);
                tmpBtn2.setRotation(0);
            }

        }



    }


    public void rotateShip() {
        Button btn = findViewById(btnID);
        switch (btnClickAmount%4) {
            case 1:
                direction = Direction.LEFT;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                btn.setRotation(90);
                placeBigShip(btn, direction);
                break;
            case 2:
                direction = Direction.UP;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Up", Toast.LENGTH_SHORT).show();
                btn.setRotation(180);
                placeBigShip(btn, direction);
                break;
            case 3:
                direction = Direction.RIGHT;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                btn.setRotation(270);
                placeBigShip(btn, direction);
                break;
            case 4:
                direction = Direction.DOWN;
                Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Down", Toast.LENGTH_SHORT).show();
                btn.setRotation(0);
                placeBigShip(btn, direction);
                break;
        }
    }

    public void savePositions(View view) throws ShipException {
        if(!positionSet) {
            throw new ShipException("Start position of the ship is not found");
        } else {
            // Index 0 gets player1, needs a method to figure out which player is who.
            server.addShipToDatabase(this, gameModel.getPlayers().get(0)
                    .addShip(gameModel.getStartPoint(), shipSize, direction, gameModel));
            positionSet = false;
        }
    }


    public void setStartPosition() {
        // Index 0 gets player1, needs a method to figure out which player is who.
        Point point = gameModel.getPlayers().get(0).getGrid().get(btnID-btnDefault);
        gameModel.setStartPosition(point);
        positionSet = true;
    }
}
