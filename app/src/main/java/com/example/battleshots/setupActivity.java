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
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, shipPlaced, shipAmount, gridSize = 8, btnDefault = 2131230759;
    Direction direction = Direction.DOWN;
    private int shipOneID, shipTwoID, shipThreeID, shipFourID;
    private boolean positionSet = false;
    private boolean hasBoat1, hasBoat2, hasBoat3, hasBoat4 = false;

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
        Button prevBtn = findViewById(prevbtnID);
        btnID = btn.getId();
        if(btnID != prevbtnID) {
            if(prevbtnID != 0 && shipOneID != 0 && shipSize == 1) {
                clearOldShip(shipOneID);
            } else if (prevbtnID != 0 && hasBoat2 && shipSize == 2) {
           //     clearOldShip();
            } else if (prevbtnID != 0 && shipThreeID != 0 && shipSize == 3) {
                 clearOldShip(shipThreeID);
            }
            prevbtnID = btnID;
            btnClickAmount = 0;
            direction = Direction.DOWN;
            if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
                if(shipSize == 1) {
                    placeBigShip(btn, direction);
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
        int prevDir = 0;

        if(direction == Direction.UP) {
            dir = -8;
            prevDir = -1;
        } else if (direction == Direction.DOWN){
            dir = 8;
            prevDir = 1;
        } else if (direction == Direction.LEFT) {
            dir = -1;
            prevDir = 8;
        } else if (direction == Direction.RIGHT) {
            dir = 1;
            if((btnID-btnDefault-(shipSize-1)*8)<=4) {
                prevDir = 0;
            } else {
                prevDir = -8;
            }
        }
        Button tmpBtn1 = findViewById(tmpId+1*dir);
        Button tmpBtn2 = findViewById(tmpId+2*dir);
        Button tmpBtn3 = findViewById(tmpId+3*dir);
        Button prvBtn1 = findViewById(tmpId+1*prevDir);
        Button prvBtn2 = findViewById(tmpId+2*prevDir);
        Button prvBtn3 = findViewById(tmpId+3*prevDir);

        if(shipSize == 1) {
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
            shipOneID = startBtn.getId();

            if(direction == Direction.LEFT) {
                startBtn.setRotation(90);

            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
            } else if (direction == Direction.RIGHT) {
                startBtn.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
            }

        }

        if(shipSize == 3) {
            //startBtn.setText(Integer.toString(btnID-btnDefault));
            shipThreeID = startBtn.getId();
            startBtn.setBackground(ContextCompat.getDrawable(this,R.mipmap.ship_three_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
            tmpBtn2.setBackground(ContextCompat.getDrawable(this,R.mipmap.ship_three_end));
            prvBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prvBtn1.setRotation(0);
            prvBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prvBtn2.setRotation(0);

            if(direction == Direction.LEFT) {
                startBtn.setRotation(90);
                tmpBtn1.setRotation(90);
                tmpBtn2.setRotation(90);
            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
                tmpBtn1.setRotation(180);
                tmpBtn2.setRotation(180);
            } else if (direction == Direction.RIGHT) {
                startBtn.setRotation(270);
                tmpBtn1.setRotation(270);
                tmpBtn2.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
                tmpBtn1.setRotation(0);
                tmpBtn2.setRotation(0);
            }

        }



    }


    public void rotateShip() {
        Button btn = findViewById(btnID);
        if((btnID-btnDefault+4)%8<shipSize-1) {
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 1:
                    direction = Direction.UP;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    clearShipDirection(Direction.DOWN);
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Down", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
            }
        } else if ((btnID-btnDefault+4)%8>8-shipSize){
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    clearShipDirection(Direction.UP);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Up", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 2:
                    direction = Direction.UP;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
            }

        } else if ((btnID-btnDefault-(shipSize-1)*8)<=4){
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Up", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    clearShipDirection(Direction.LEFT);
                    break;
            }

        } else {
            switch (btnClickAmount%4) {
                case 0:
                    direction = Direction.DOWN;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Left", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Up", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 2:
                    direction = Direction.UP;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Right", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
                case 3:
                    direction = Direction.RIGHT;
                    Toast.makeText(getApplicationContext(), "Clicked" + btnClickAmount + "– Direction: Down", Toast.LENGTH_SHORT).show();
                    placeBigShip(btn, direction);
                    break;
            }
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

    private  void clearOldShip(int btnID) {
        Button prevBtn = findViewById(btnID);
        //need fix to delete ship if rotated by the edge and then a new ship of same length is placed after
        //another type of ship is placed
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

        if(prevBtn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_one).getConstantState()) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);
            shipOneID = 0;
        }
        else if(prevBtn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_three_front).getConstantState()) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);
            Button prevBtn1 = findViewById(prevBtn.getId()+1*dir);
            Button prevBtn2 = findViewById(prevBtn.getId()+2*dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            shipThreeID = 0;
        }
        else if(shipSize == 4) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);

            Button prevBtn1 = findViewById(prevBtn.getId()+1*dir);
            Button prevBtn2 = findViewById(prevBtn.getId()+2*dir);
            Button prevBtn3 = findViewById(prevBtn.getId()+3*dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn3.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn3.setRotation(0);
        }

    }


    private void clearShipDirection(Direction direction) {
        if(direction == Direction.DOWN) {
            Button tmbBtn1 = findViewById(btnID+8);
            Button tmbBtn2 = findViewById(btnID+16);
            Button tmbBtn3 = findViewById(btnID+24);

            tmbBtn1.setRotation(0);
            tmbBtn2.setRotation(0);
            tmbBtn3.setRotation(0);
            tmbBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn2.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn3.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        } else if(direction == Direction.UP) {
            Button tmbBtn1 = findViewById(btnID-8);
            Button tmbBtn2 = findViewById(btnID-16);
            Button tmbBtn3 = findViewById(btnID-24);

            tmbBtn1.setRotation(0);
            tmbBtn2.setRotation(0);
            tmbBtn3.setRotation(0);
            tmbBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn2.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn3.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        } else if (direction == Direction.LEFT) {
            Button tmbBtn1 = findViewById(btnID-1);
            Button tmbBtn2 = findViewById(btnID-2);
            Button tmbBtn3 = findViewById(btnID-3);

            tmbBtn1.setRotation(0);
            tmbBtn2.setRotation(0);
            tmbBtn3.setRotation(0);
            tmbBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn2.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            tmbBtn3.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        }
    }

    public void setStartPosition() {
        // Index 0 gets player1, needs a method to figure out which player is who.
        Point point = gameModel.getPlayers().get(0).getGrid().get(btnID-btnDefault);
        gameModel.setStartPosition(point);
        positionSet = true;
    }
}
