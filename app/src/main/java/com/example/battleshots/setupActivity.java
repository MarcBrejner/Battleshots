package com.example.battleshots;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.*;

import android.content.Intent;
import com.google.firebase.database.ValueEventListener;

public class setupActivity extends AppCompatActivity {

    private String gameID, playerID;
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, gridSize = 8, btnDefault = 2131230763;
    HashSet<Point> shipList;
    Map<String, Object> info;
    Direction direction = Direction.DOWN;
    private String rotationFlag, occupiedFlag, newShipFlag = "";
    private int shipOneID, shipTwoID, shipThreeID, shipFourID;
    private boolean isReady = false, positionSet = false, ship1placed, ship2placed, ship3placed, ship4placed;

    GameModel gameModel;
    Server server;
    DatabaseReference playerRef, otherPlayerRef;
    String playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);

        server = new Server();
        shipList = new HashSet<>();

        if (getIntent().getStringExtra("gameID") != null) {
            gameID = getIntent().getStringExtra("gameID");
            playerID = getIntent().getStringExtra("playerID");
            playerName = getIntent().getStringExtra("pName");
            Log.d("AllIDS", gameID + " + " + playerID + " + " + playerName + " +");
            gameModel = new GameModel(playerName);
        }
        // FOR TESTING
        else {
            gameModel = new GameModel("test");
            gameID = "1";
            playerID = "1";
            server.createGame(gameID, gameModel.getPlayers().get(0));
            server.joinGame(gameID, new Player("dummy"));
        }
        // REMOVE WHEN TESTING IS DONE


        // TODO: Make a list of saved ship index based on the button indexes to
        //  ensure that ships stay on the screen when placed

        /*
        btnList = new List<>();
         */

        if (playerID.equals("1")) {
            playerRef = server.reference.child("Game").child(gameID).child("Player 1");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 2");
        } else  {
            playerRef = server.reference.child("Game").child(gameID).child("Player 2");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 1");
        }

        otherPlayerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info = (HashMap<String, Object>) dataSnapshot.getValue();
                //Log.d("information", info.toString());
                if (info != null) {
                    Boolean otherPlayerIsReady = (Boolean) info.get("isReady");
                    Boolean beginGame = (Boolean) info.get("gameStarted");
                    if (otherPlayerIsReady != null) {
                        if (otherPlayerIsReady & isReady) {
                            Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                            intent.putExtra("gameID", gameID);
                            intent.putExtra("playerID", playerID);
                            intent.putExtra("pName", playerName);
                            playerRef.child("gameStarted").setValue(true);
                            startActivity(intent);
                            finish();
                        }
                    } else if (beginGame != null) {
                        if (beginGame && isReady) {
                            Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
                            intent.putExtra("gameID", gameID);
                            intent.putExtra("playerID", playerID);
                            intent.putExtra("pName", playerName);
                            playerRef.child("gameStarted").setValue(true);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageView boat1 = findViewById(R.id.ship_one);
        boat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship1placed) {
                    shipSize = 1;
                    Toast.makeText(getApplicationContext(), "You picked a patrol boat", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You have already placed this ship", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView boat2 = findViewById(R.id.ship_two);
        boat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship2placed) {
                    shipSize = 2;
                    Toast.makeText(getApplicationContext(), "Ship Size is 2", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You have already placed this ship", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView boat3 = findViewById(R.id.ship_three);
        boat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship3placed) {
                    shipSize = 3;
                    Toast.makeText(getApplicationContext(), "You picked a cruiser", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You have already placed this ship", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView boat4 = findViewById(R.id.ship_four);
        boat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship4placed) {
                    shipSize = 4;
                    Toast.makeText(getApplicationContext(), "Ship Size is 4", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You have already placed this ship", Toast.LENGTH_SHORT).show();
                }
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
        if (shipSize == 0) {
            Toast.makeText(getApplicationContext(), "Pick your naval ship", Toast.LENGTH_SHORT).show();
        } else {
        /* else if(containShip(btnID-btnDefault)) {
            Toast.makeText(getApplicationContext(), ""+ Arrays.toString(shipList.toArray()), Toast.LENGTH_SHORT).show();
        } */
            setStartPosition();
            if (btnID == prevbtnID && shipOneID == btnID && !ship1placed) {
                rotateShip();
            } else if (shipSize == 2 && shipTwoID == btnID && prevbtnID == btnID && !ship2placed) {
                rotateShip();
            } else if (shipSize == 3 && shipThreeID == btnID && prevbtnID == btnID && !ship3placed) {
                rotateShip();
            } else if (shipSize == 4 && shipFourID == btnID && prevbtnID == btnID && !ship4placed) {
                rotateShip();
            } else if (btnID != prevbtnID ) { // Skrives for at skibet ikke forsvinder
                if (prevbtnID != 0 && shipOneID != 0 && shipSize == 1) {
                    clearOldShip(shipOneID);
                } else if (prevbtnID != 0 && shipTwoID != 0 && shipSize == 2) {
                    clearOldShip(shipTwoID);
                } else if (prevbtnID != 0 && shipThreeID != 0 && shipSize == 3) {
                    clearOldShip(shipThreeID);
                } else if (prevbtnID != 0 && shipFourID != 0 && shipSize == 4) {
                    clearOldShip(shipFourID);
                }
                prevbtnID = btnID;
                btnClickAmount = 0;
                direction = Direction.DOWN;
                if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
                    placeBigShip(btn, direction, rotationFlag, "NEW_SHIP");
                }
            }
        }
    }

    public void placeBigShip(Button startBtn, Direction direction, String rotationFlag, String newShipFlag) {
        int tmpId = startBtn.getId();
        /*if (isOccupied(startBtn)) {
            Toast.makeText(getApplicationContext(), "There is already a ship", Toast.LENGTH_SHORT).show();
            return;
        }*/

        int dir = 0;
        int prevDir = 0;

        if (direction == Direction.UP) {
            dir = -8;
            if (rotationFlag == "BOTTOM_LEFT") {
                prevDir = 1;
            } else if (rotationFlag == "LEFT") {
                prevDir = 8;
            } else {
                prevDir = -1;
            }
        } else if (direction == Direction.RIGHT) {
            dir = -1;
            if (rotationFlag == "BOTTOM") {
                prevDir = 1;
            } else if (rotationFlag == "BOTTOM_RIGHT") {
                prevDir = -8;
            } else {
                prevDir = 8;
            }

        } else if (direction == Direction.DOWN) {
            if (btnID - btnDefault + (shipSize - 1) * 8 > 63 && (btnID - btnDefault) % 8 < shipSize - 1) {
                dir = -8;
                direction = Direction.UP;
                prevDir = 1;
            } else if (btnID - btnDefault + (shipSize - 1) * 8 > 63 && (btnID - btnDefault) % 8 > 8 - shipSize) {
                dir = -1;
                prevDir = -8;
                direction = Direction.RIGHT;
            } else if (btnID - btnDefault + (shipSize - 1) * 8 > 63) {
                dir = -1;
                direction = Direction.RIGHT;
                prevDir = 1;
            } else {
                dir = 8;
                prevDir = 1;
            }
        } else if (direction == Direction.LEFT) {
            dir = 1;
            if (rotationFlag == "TOP") {
                prevDir = -1;
            } else if (rotationFlag == "TOP_LEFT") {
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


        if (shipSize == 1) {
            // startBtn.setText(Integer.toString(btnID-btnDefault));
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
            shipOneID = startBtn.getId();

            if (direction == Direction.RIGHT) {
                startBtn.setRotation(90);

            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
            } else if (direction == Direction.RIGHT) {
                startBtn.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
            }
            btnClickAmount++;
        }

        if(shipSize == 2) {
            //startBtn.setText(Integer.toString(btnID-btnDefault));
            /*if (isOccupied(tmpBtn1)) {
                Toast.makeText(getApplicationContext(), "There is already a ship", Toast.LENGTH_SHORT).show();
                return;
            }*/
            shipTwoID = startBtn.getId();
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_end));
            if(newShipFlag != "NEW_SHIP") {
                prvBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn1.setRotation(0);
            }

            if (direction == Direction.RIGHT) {
                startBtn.setRotation(90);
                tmpBtn1.setRotation(90);
            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
                tmpBtn1.setRotation(180);
            } else if (direction == Direction.LEFT) {
                startBtn.setRotation(270);
                tmpBtn1.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
                tmpBtn1.setRotation(0);
            }
            btnClickAmount++;
        }

        if(shipSize == 3) {
            //startBtn.setText(Integer.toString(btnID-btnDefault));
            /*if (isOccupied(tmpBtn1) || isOccupied(tmpBtn2)) {
                Toast.makeText(getApplicationContext(), "There is already a ship", Toast.LENGTH_SHORT).show();
                return;
            }*/
            shipThreeID = startBtn.getId();
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
            tmpBtn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
            if (newShipFlag != "NEW_SHIP") {
                prvBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn1.setRotation(0);
                prvBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn2.setRotation(0);
            }

            if (direction == Direction.RIGHT) {
                startBtn.setRotation(90);
                tmpBtn1.setRotation(90);
                tmpBtn2.setRotation(90);
            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
                tmpBtn1.setRotation(180);
                tmpBtn2.setRotation(180);
            } else if (direction == Direction.LEFT) {
                startBtn.setRotation(270);
                tmpBtn1.setRotation(270);
                tmpBtn2.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
                tmpBtn1.setRotation(0);
                tmpBtn2.setRotation(0);
            }
            btnClickAmount++;
        }

        if(shipSize == 4) {
            //startBtn.setText(Integer.toString(btnID-btnDefault));
            /*if (isOccupied(tmpBtn1) || isOccupied(tmpBtn2) || isOccupied(tmpBtn3)) {
                Toast.makeText(getApplicationContext(), "There is already a ship", Toast.LENGTH_SHORT).show();
                return;
            }*/
            shipFourID = startBtn.getId();
            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front));
            tmpBtn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front_middle));
            tmpBtn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end_middle));
            tmpBtn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end));
            if(newShipFlag != "NEW_SHIP") {
                prvBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn1.setRotation(0);
                prvBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn2.setRotation(0);
                prvBtn3.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
                prvBtn3.setRotation(0);
            }

            if(direction == Direction.RIGHT) {
                startBtn.setRotation(90);
                tmpBtn1.setRotation(90);
                tmpBtn2.setRotation(90);
                tmpBtn3.setRotation(90);
            } else if (direction == Direction.UP) {
                startBtn.setRotation(180);
                tmpBtn1.setRotation(180);
                tmpBtn2.setRotation(180);
                tmpBtn3.setRotation(180);
            } else if (direction == Direction.LEFT) {
                startBtn.setRotation(270);
                tmpBtn1.setRotation(270);
                tmpBtn2.setRotation(270);
                tmpBtn3.setRotation(270);
            } else if (direction == Direction.DOWN) {
                startBtn.setRotation(0);
                tmpBtn1.setRotation(0);
                tmpBtn2.setRotation(0);
                tmpBtn3.setRotation(0);
            }
            btnClickAmount++;
        }
    }


    public void rotateShip() {
        Button btn = findViewById(btnID);
        if ((btnID - btnDefault) % 8 < shipSize - 1 && (btnID - btnDefault - (shipSize - 1) * 8) <= 0) {
            switch (btnClickAmount % 2) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, "TOP_LEFT", newShipFlag);
                    clearShipDirection(Direction.DOWN);
                    break;
            }
        } else if ((btnID - btnDefault) % 8 < shipSize - 1 && (btnID - btnDefault + (shipSize - 1) * 8) > 63) {
            switch (btnClickAmount % 2) {
                case 0:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, "BOTTOM_LEFT", newShipFlag);
                    clearShipDirection(Direction.LEFT);
                    break;
                case 1:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }
        } else if ((btnID - btnDefault) % 8 > 8 - shipSize && (btnID - btnDefault + (shipSize - 1) * 8) > 63) {
            switch (btnClickAmount % 2) {
                case 0:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, "BOTTOM_RIGHT", newShipFlag);
                    clearShipDirection(Direction.UP);
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }
        } else if ((btnID - btnDefault) % 8 > 8 - shipSize && (btnID - btnDefault - (shipSize - 1) * 8) <= 0) {
            switch (btnClickAmount % 2) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, "TOP_RIGHT", newShipFlag);
                    clearShipDirection(Direction.RIGHT);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }
        } else if ((btnID - btnDefault) % 8 < shipSize - 1 || occupiedFlag == "SHIP_LEFT") {
            switch (btnClickAmount % 3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, "LEFT", newShipFlag);
                    clearShipDirection(Direction.DOWN);
                    break;
                case 2:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }
        } else if ((btnID - btnDefault) % 8 > 8 - shipSize) {
            switch (btnClickAmount % 3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, "RIGHT", newShipFlag);
                    clearShipDirection(Direction.UP);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 2:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }

        } else if ((btnID - btnDefault - (shipSize - 1) * 8) <= 0) {
            switch (btnClickAmount % 3) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 2:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, "TOP", newShipFlag);
                    clearShipDirection(Direction.RIGHT);
                    break;
            }

        } else if ((btnID - btnDefault + (shipSize - 1) * 8) > 63) {
            switch (btnClickAmount % 3) {
                case 0:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, "BOTTOM", newShipFlag);
                    break;
                case 1:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 2:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, "BOTTOM", newShipFlag);
                    clearShipDirection(Direction.RIGHT);
                    break;
            }

        } else {
            switch (btnClickAmount % 4) {
                case 0:
                    direction = Direction.DOWN;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 1:
                    direction = Direction.RIGHT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 2:
                    direction = Direction.UP;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
                case 3:
                    direction = Direction.LEFT;
                    placeBigShip(btn, direction, rotationFlag, newShipFlag);
                    break;
            }
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
            prevBtn1 = findViewById(prevBtn.getId()+dir);
            prevBtn2 = findViewById(prevBtn.getId()+2*dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            shipThreeID = 0;
        }
        else if(btnID == shipTwoID) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);
            prevBtn1 = findViewById(prevBtn.getId()+dir);

            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));

            prevBtn1.setRotation(0);
            shipTwoID = 0;
        }
        else if(shipSize == 4) {
            prevBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn.setRotation(0);

            prevBtn1 = findViewById(prevBtn.getId()+dir);
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
        int dir = 0;
        if (direction == Direction.UP) {
            dir = -8;
        } else if (direction == Direction.DOWN) {
            dir = 8;
        } else if (direction == Direction.RIGHT) {
            dir = -1;
        } else if (direction == Direction.LEFT) {
            dir = 1;
        }
        if (shipSize == 2) {
            Button prevBtn1 = findViewById(btnID + dir);
            prevBtn1.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
        } else if (shipSize == 3) {
            Button prevBtn1 = findViewById(btnID + dir);
            Button prevBtn2 = findViewById(btnID + 2 * dir);
            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
        } else if (shipSize == 4) {
            Button prevBtn1 = findViewById(btnID + dir);
            Button prevBtn2 = findViewById(btnID + 2 * dir);
            Button prevBtn3 = findViewById(btnID + 3 * dir);

            prevBtn1.setRotation(0);
            prevBtn2.setRotation(0);
            prevBtn3.setRotation(0);
            prevBtn1.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn2.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
            prevBtn3.setBackground(ContextCompat.getDrawable(this, R.drawable.defaultbutton));
        }
    }


        public void setStartPosition () {
            // Index 0 gets player1, needs a method to figure out which player is who.
            Point point = gameModel.getPlayers().get(0).getGrid().get(btnID - btnDefault);
            gameModel.setStartPosition(point);
            positionSet = true;
        }

        public boolean isOccupied (Button btn){
            boolean Occu = true;
            if (btn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.defaultbutton).getConstantState()) {
                Occu = false;
            } else if (shipSize == 1 && btn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_one).getConstantState()) {
                Occu = false;
            } else if (shipSize == 2 && btn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_two_front).getConstantState()) {
                Occu = false;
            } else if (shipSize == 3 && btn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_three_front).getConstantState()) {
                Occu = false;
            } else if (shipSize == 4 && btn.getBackground().getConstantState() == getResources().getDrawable(R.mipmap.ship_four_front).getConstantState()) {
                Occu = false;
            }
            return Occu;
        }


        public void savePositions (View view){
            if (!positionSet) {
                Toast.makeText(getApplicationContext(), "Ship is not found", Toast.LENGTH_SHORT).show();
            } else {
                // Index 0 gets player1, needs a method to figure out which player is who.
                gameModel.getPlayers().get(0).addShip(gameModel.getStartPoint(), shipSize, direction, gameModel);
          /*  for (Ship ship:  gameModel.getPlayers().get(0).getShips()) {
                for(Point shippoints : ship.getShip()) {
                    shipList.add(shippoints);
                }
            }*/

                if (gameModel.getPlayers().get(0).hasShip) {
                    Toast.makeText(getApplicationContext(), "Denied ship placement", Toast.LENGTH_SHORT).show();
                    gameModel.getPlayers().get(0).hasShip = false;
                } else if (gameModel.getPlayers().get(0).noShipsLeft) {
                    Toast.makeText(getApplicationContext(), "All ships have been placed", Toast.LENGTH_SHORT).show();
                } else {
                    server.addShipToDatabase(this, gameModel.getPlayers().get(0), gameID, playerID);
                    positionSet = false;

                    if (shipSize == 1) {
                        ship1placed = true;
                        Toast.makeText(getApplicationContext(),"Ship 1 has been placed", Toast.LENGTH_SHORT).show();
                    } else if (shipSize == 2) {
                        ship2placed = true;
                        Toast.makeText(getApplicationContext(),"Ship 2 has been placed", Toast.LENGTH_SHORT).show();
                    } else if (shipSize == 3) {
                        ship3placed = true;
                        Toast.makeText(getApplicationContext(),"Ship 3 has been placed", Toast.LENGTH_SHORT).show();
                    } else if (shipSize == 4) {
                        ship4placed = true;
                        Toast.makeText(getApplicationContext(),"Ship 4 has been placed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        public void readyToBattle (View view){
            if (!ship1placed || !ship2placed || !ship3placed || !ship4placed) {
                // Also need shipTwoID and shipThreeID
                Toast.makeText(getApplicationContext(), "Not all ships have been placed", Toast.LENGTH_SHORT).show();
            } else {
                playerRef.child("isReady").setValue(true);
                isReady = true;
                Toast.makeText(getApplicationContext(), "You are now ready for battle!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onDestroy () {
            server.deleteGameDataBase(gameID);
            super.onDestroy();
        }

    /* public boolean containShip(int index) {
        if (direction == Direction.DOWN) {
            for(int i = 0; i < shipSize; i++) {
                if(shipList.contains(gameModel.convertIndexToPoint(index-i*8))) {
                    return true;
                }
            }
        } else if (direction == Direction.UP) {
            for (int i = 0; i < shipSize; i++) {
                if(shipList.contains(gameModel.convertIndexToPoint(index+i*8))) {
                    return true;
                }
            }
        } else if (direction == Direction.LEFT) {
            for (int i = 0; i < shipSize; i++) {
                if(shipList.contains(gameModel.convertIndexToPoint(index+i))) {
                    return true;
                }
            }
        } else if (direction == Direction.RIGHT) {
            for (int i = 0; i < shipSize; i++) {
                if(shipList.contains(gameModel.convertIndexToPoint(index-i))) {
                    return true;
                }
            }
        }
        return  false;
    } */

    /* public BitmapDrawable combineImage(int imgID, int imgID2) {
        Bitmap img1 = BitmapFactory.decodeResource(getResources(), imgID);
        Bitmap img2 = BitmapFactory.decodeResource(getResources(), imgID2);
        Bitmap overlay = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(), img1.getConfig());
        Canvas canvas = new Canvas(overlay);
        img2 = addWhiteBorder(img2, 15);
        canvas.drawBitmap(img2, new Matrix(), null);
        canvas.drawBitmap( img1,  new Matrix(), null);
        return bitmapToBitmapDrawable(overlay);
    }

    public BitmapDrawable bitmapToBitmapDrawable(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }

    private Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() +  borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    } */
}
