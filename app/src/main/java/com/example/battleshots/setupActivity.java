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

import java.util.ArrayList;
import java.util.List;

public class setupActivity extends AppCompatActivity {

    public static int[] playerShips;
    private String gameID, playerID;
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, gridSize = 8, btnDefault = 2131230759;
    List<Integer> btnList = new ArrayList<>();
    Direction direction = Direction.DOWN;
    private int shipOneID, shipTwoID, shipThreeID, shipFourID;
    private boolean positionSet = false;
    private String rotationFlag = "";
    private boolean hasBoat1, hasBoat2, hasBoat3, hasBoat4 = false;

    GameModel gameModel;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);

        gameID = getIntent().getStringExtra("gameID");
        playerID = getIntent().getStringExtra("playerID");

        server = new Server();

        makeListOfButtons();

        ImageView boat1 = findViewById(R.id.ship_one);
        boat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipSize = 1;
                Toast.makeText(getApplicationContext(), "Ship Size is 1", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView boat3 = findViewById(R.id.ship_three);
        boat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipSize = 3;
                Toast.makeText(getApplicationContext(), "Ship Size is 3", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //server.addGameModelToDatabase(gameModel);
    }

    public void makeListOfButtons() {
        for(int i = 0; i < gridSize*gridSize; i++) {
            btnList.add(btnDefault+i);
        }
    }

    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());
        Button prevBtn = findViewById(prevbtnID);
        btnID = btn.getId();
        if (shipSize == 0) {
            Toast.makeText(getApplicationContext(), "Pick a ship", Toast.LENGTH_SHORT).show();
        }
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
                    placeBigShip(btn, direction, rotationFlag);
                } else if (shipSize == 3) {
                    placeBigShip(btn, direction, rotationFlag);
                }
            } else {
                btn.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            }
        } else if (btnID == prevbtnID) {
            btnClickAmount++;
            rotateShip();
        }
       // setStartPosition();
    }

    public void placeBigShip(Button startBtn, Direction direction, String rotationFlag) {
        int tmpId = startBtn.getId();
        /*if (isOccupied(startBtn)) {
            Toast.makeText(getApplicationContext(), "There is already a ship", Toast.LENGTH_SHORT).show();
            return;
        }*/
        int dir = 0;
        int prevDir = 0;

        if(direction == Direction.UP) {
            dir = -8;
            if(rotationFlag == "BOTTOM_LEFT") {
                prevDir = 1;
            } else if (rotationFlag == "LEFT"){
                prevDir = 8;
            } else {
                prevDir = -1;
            }
        } else if (direction == Direction.LEFT){
            dir = -1;
            if(rotationFlag == "BOTTOM") {
                prevDir = 1;
            } else if (rotationFlag == "BOTTOM_RIGHT"){
                prevDir = -8;
            } else {
                prevDir = 8;
            }

        } else if (direction == Direction.DOWN) {
            if (btnID-btnDefault+(shipSize-1)*8>67 && (btnID-btnDefault+4)%8<shipSize-1) {
                dir = -8;
                direction=Direction.UP;
                prevDir = 1;
            } else if (btnID-btnDefault+(shipSize-1)*8>67 && (btnID-btnDefault+4)%8>8-shipSize){
                dir = -1;
                prevDir = -8;
                direction = Direction.LEFT;
            } else if (btnID-btnDefault+(shipSize-1)*8>67){
                dir = -1;
                direction=Direction.LEFT;
                prevDir = 1;
            } else {
                dir = 8;
                prevDir = 1;
            }
        } else if (direction == Direction.RIGHT) {
            dir = 1;
            if(rotationFlag == "TOP") {
                prevDir = -1;
            } else if(rotationFlag == "TOP_LEFT") {
                prevDir = 8;
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
           // startBtn.setText(Integer.toString(btnID-btnDefault));
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
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
            tmpBtn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
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
        if ((btnID-btnDefault+4)%8<shipSize-1 && (btnID-btnDefault-(shipSize-1)*8)<4) {
            switch (btnClickAmount%2) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, "TOP_LEFT");
                    clearShipDirection(Direction.DOWN);
                    break;
            }
        }
        else if((btnID-btnDefault+4)%8<shipSize-1 && (btnID-btnDefault+(shipSize-1)*8)>67) {
            switch (btnClickAmount%2) {
                case 0:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, "BOTTOM_LEFT");
                    clearShipDirection(Direction.RIGHT);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
            }
        }else if((btnID-btnDefault+4)%8>8-shipSize && (btnID-btnDefault+(shipSize-1)*8)>67) {
            switch (btnClickAmount%2) {
                case 0:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, "BOTTOM_RIGHT");
                    clearShipDirection(Direction.UP);
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
            }
        } else if((btnID-btnDefault+4)%8>8-shipSize && (btnID-btnDefault-(shipSize-1)*8)<4) {
            switch (btnClickAmount%2) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, "TOP_RIGHT");
                    clearShipDirection(Direction.LEFT);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
            }
        } else if((btnID-btnDefault+4)%8<shipSize-1) {
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, "LEFT");
                    clearShipDirection(Direction.DOWN);
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
            }
        } else if ((btnID-btnDefault+4)%8>8-shipSize){
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, "RIGHT");
                    clearShipDirection(Direction.UP);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 2:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
            }

        } else if ((btnID-btnDefault-(shipSize-1)*8)<=4){
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, "TOP");
                    clearShipDirection(Direction.LEFT);
                    break;
            }

        } else if ((btnID-btnDefault+(shipSize-1)*8)>67){
            switch (btnClickAmount%3) {
                case 0:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, "BOTTOM");
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 2:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, "BOTTOM");
                    clearShipDirection(Direction.LEFT);
                    break;
            }

        }else {
            switch (btnClickAmount%4) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 2:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag);
                    break;
                case 3:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag);
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

        int dir = 0;
        if(prevBtn.getRotation() == 180) {
            dir = -8;
        } else if (prevBtn.getRotation()==0){
            dir = 8;
        } else if (prevBtn.getRotation()==90) {
            dir = -1;
        } else if (prevBtn.getRotation()==270) {
            dir = 1;
        }

        Button prevBtn1;
        Button prevBtn2;
        Button prevBtn3;

        if(prevBtn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_one).getConstantState()) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);
            shipOneID = 0;
        }
        else if(btnID == shipThreeID) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);
            prevBtn1 = findViewById(prevBtn.getId()+1*dir);
            prevBtn2 = findViewById(prevBtn.getId()+2*dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            shipThreeID = 0;
        }
        else if(shipSize == 4) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);

            prevBtn1 = findViewById(prevBtn.getId()+1*dir);
            prevBtn2 = findViewById(prevBtn.getId()+2*dir);
            prevBtn3 = findViewById(prevBtn.getId()+3*dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn3.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn3.setRotation(0);
        }

    }


    private void clearShipDirection(Direction direction) {
        Button prevBtn = findViewById(btnID);

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

        if(shipSize == 2) {
            Button prevBtn1 = findViewById(btnID+1*dir);
            prevBtn1.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        } else if (shipSize == 3) {
            Button prevBtn1 = findViewById(btnID+1*dir);
            Button prevBtn2 = findViewById(btnID+2*dir);
            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        } else if (shipSize == 4) {
            Button prevBtn1 = findViewById(btnID+1*dir);
            Button prevBtn2 = findViewById(btnID+2*dir);
            Button prevBtn3 = findViewById(btnID+3*dir);
            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn3.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
            prevBtn3.setBackground(ContextCompat.getDrawable(this,R.drawable.defaultbutton));
        }
    }

    public void setStartPosition() {
        // Index 0 gets player1, needs a method to figure out which player is who.
        Point point = gameModel.getPlayers().get(0).getGrid().get(btnID-btnDefault);
        gameModel.setStartPosition(point);
        positionSet = true;
    }

    public boolean isOccupied(Button btn) {
        boolean Occu = true;
        if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
          Occu = false;
        }
        return Occu;
    }
}
