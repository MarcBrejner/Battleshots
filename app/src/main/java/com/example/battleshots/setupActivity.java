package com.example.battleshots;

import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.media.Image;
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
    private int btnID, btnClickAmount = 0, prevbtnID, shipSize, btnDefault = 2131230763;
    Map<String, Object> info;
    Direction direction = Direction.DOWN;
    Map<String, Integer> startPositions, lengthList;
    Map<String, Direction> directionsList;
    private String rotationFlag = "", newShipFlag = "", occupiedFlag = "";
    private int shipOneID, shipTwoID, shipThreeID, shipFourID;
    private boolean isReady = false, ship1placed, ship2placed, ship3placed, ship4placed, allShipIsPlaced;
    ValueEventListener playerListener, otherPlayerListener;
    GameModel gameModel;
    int shipPlaced = 0;
    Server server;
    Handler handler;
    Runnable r;
    ImageView boat1, boat2, boat3, boat4;
    DatabaseReference playerRef, otherPlayerRef;
    String playerName;
//    Bitmap waterColor = BitmapFactory.decodeResource(getResources(), R.drawable.water);
   //  Drawable waterImage =  bitmapToBitmapDrawable(addBorderColor(waterColor,2));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);


        server = new Server();
       startPositions = new HashMap<>();
       directionsList = new HashMap<>();
       lengthList = new HashMap<>();

        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {

                    if (ship1placed) {
                        updateView(startPositions.get("point1"), directionsList.get("direction1"), lengthList.get("length1"));
                    }
                    if (ship2placed) {
                        updateView(startPositions.get("point2"), directionsList.get("direction2"), lengthList.get("length2"));
                    }
                    if (ship3placed) {
                        updateView(startPositions.get("point3"), directionsList.get("direction3"), lengthList.get("length3"));
                    }
                    if (ship4placed) {
                        updateView(startPositions.get("point4"), directionsList.get("direction4"), lengthList.get("length4"));

                }
                handler.postDelayed(r, 1000);
            }
        }; handler.postDelayed(r, 1000);



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

        otherPlayerRef.addValueEventListener(otherPlayerListener = new ValueEventListener() {
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

        boat1 = findViewById(R.id.ship_one);
        boat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship1placed) {
                    shipSize = 1;
                    Toast.makeText(getApplicationContext(), "You picked a patrol boat", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boat2 = findViewById(R.id.ship_two);
        boat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship2placed) {
                    shipSize = 2;
                    Toast.makeText(getApplicationContext(), "You picked a destroyer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boat3 = findViewById(R.id.ship_three);
        boat3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship3placed) {
                    shipSize = 3;
                    Toast.makeText(getApplicationContext(), "You picked a cruiser", Toast.LENGTH_SHORT).show();
                }
            }
        });

        boat4 = findViewById(R.id.ship_four);
        boat4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ship4placed) {
                    shipSize = 4;
                    Toast.makeText(getApplicationContext(), "You picked a battleship", Toast.LENGTH_SHORT).show();
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
        // TODO: BUGS WITH OVERLAP ON SHIPS
        Button btn = (Button) findViewById(view.getId());
        btnID = btn.getId();



        if (shipSize == 0) {
            Toast.makeText(getApplicationContext(), "Pick your naval ship", Toast.LENGTH_SHORT).show();
        } else {
        /* else if(containShip(btnID-btnDefault)) {
            Toast.makeText(getApplicationContext(), ""+ Arrays.toString(shipList.toArray()), Toast.LENGTH_SHORT).show();
        } */
            if (shipSize == 1 && btnID == prevbtnID && shipOneID == btnID && !ship1placed) {
                rotateShip();
            } else if (shipSize == 2 && shipTwoID == btnID && prevbtnID == btnID && !ship2placed) {
                rotateShip();
            } else if (shipSize == 3 && shipThreeID == btnID && prevbtnID == btnID && !ship3placed) {
                rotateShip();
            } else if (shipSize == 4 && shipFourID == btnID && prevbtnID == btnID && !ship4placed) {
                rotateShip();
            } else if (btnID != prevbtnID ) {
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


        if (shipSize == 1 && !ship1placed) {
            // startBtn.setText(Integer.toString(btnID-btnDefault));

            startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
           //  startBtn.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
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
            setStartPosition(shipSize, direction);
        }

        if(shipSize == 2 && !ship2placed) {
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
            setStartPosition(shipSize, direction);
        }

        if(shipSize == 3 && !ship3placed) {
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
            setStartPosition(shipSize, direction);
        }

        if(shipSize == 4 && !ship4placed) {
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
            setStartPosition(shipSize, direction);
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


        public void setStartPosition (int shipSize, Direction direction) {
            // Index 0 gets player1, needs a method to figure out which player is who.
            if (shipSize == 1 && !ship1placed) {
                startPositions.put("point1", btnID-btnDefault);
                directionsList.put("direction1", direction);
                lengthList.put("length1", 1);
            }
            if (shipSize == 2 && !ship2placed) {
                startPositions.put("point2", btnID-btnDefault);
                directionsList.put("direction2", direction);
                lengthList.put("length2",2);

            }
            if (shipSize == 3 && !ship3placed) {
                startPositions.put("point3", btnID-btnDefault);
                directionsList.put("direction3", direction);
                lengthList.put("length3", 3);
            }
            if (shipSize == 4 && !ship4placed) {
                startPositions.put("point4", btnID-btnDefault);
                directionsList.put("direction4", direction);
                lengthList.put("length4", 4);
            }
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


        public void savePositions (View view) {
        if (startPositions.get("point1") != null) {
            if (gameModel.getPlayers().get(0).hasShipInside(gameModel.convertIndexToPoint(startPositions.get("point1")), directionsList.get("direction1"), 1) && !ship1placed) {
                Toast.makeText(getApplicationContext(), "Patrol boat contains other ship", Toast.LENGTH_SHORT).show();
            } else if (!ship1placed) {
                gameModel.getPlayers().get(0).addShip(gameModel.convertIndexToPoint(startPositions.get("point1")), 1, directionsList.get("direction1"), gameModel);
                shipPlaced++;
                ship1placed = true;
                boat1.setVisibility(View.GONE);

                Toast.makeText(getApplicationContext(), "Succesfully added patrol boat", Toast.LENGTH_SHORT).show();
            }
        }

            if (startPositions.get("point2") != null) {
                if (gameModel.getPlayers().get(0).hasShipInside(gameModel.convertIndexToPoint(startPositions.get("point2")), directionsList.get("direction2"), 2) && !ship2placed) {
                    Toast.makeText(getApplicationContext(), "Destroyer contains other ship", Toast.LENGTH_SHORT).show();
                } else if (!ship2placed){
                    gameModel.getPlayers().get(0).addShip(gameModel.convertIndexToPoint(startPositions.get("point2")), 2, directionsList.get("direction2"), gameModel);
                    shipPlaced++;
                    ship2placed = true;
                    boat2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Succesfully added destroyer", Toast.LENGTH_SHORT).show();
                }
            }

            if (startPositions.get("point3") != null) {
                if (gameModel.getPlayers().get(0).hasShipInside(gameModel.convertIndexToPoint(startPositions.get("point3")),  directionsList.get("direction3"), 3) && !ship3placed) {
                    Toast.makeText(getApplicationContext(), "Cruiser contains other ship", Toast.LENGTH_SHORT).show();
                } else if (!ship3placed) {
                    gameModel.getPlayers().get(0).addShip(gameModel.convertIndexToPoint(startPositions.get("point3")), 3, directionsList.get("direction3"), gameModel);
                    shipPlaced++;
                    ship3placed = true;
                    boat3.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Succesfully added cruiser", Toast.LENGTH_SHORT).show();
                }
            }

            if (startPositions.get("point4") != null) {
                if (gameModel.getPlayers().get(0).hasShipInside(gameModel.convertIndexToPoint(startPositions.get("point4")),  directionsList.get("direction4"), 4) && !ship4placed) {
                    Toast.makeText(getApplicationContext(), "Battleship contains other ship", Toast.LENGTH_SHORT).show();
                } else if (!ship4placed) {
                    gameModel.getPlayers().get(0).addShip(gameModel.convertIndexToPoint(startPositions.get("point4")), 4,  directionsList.get("direction4"), gameModel);
                    shipPlaced++;
                    ship4placed = true;
                    boat4.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Succesfully added battleship", Toast.LENGTH_SHORT).show();
                }
            }

        if (ship1placed && ship2placed && ship3placed && ship4placed && shipPlaced == 4) {
            Toast.makeText(getApplicationContext(), "All ships have been placed", Toast.LENGTH_SHORT).show();
            if (shipPlaced == 4) {
                server.addShipToDatabase(this, gameModel.getPlayers().get(0), gameID, playerID);
                allShipIsPlaced = true;

            } else {
                Toast.makeText(getApplicationContext(), "All ships must be placed", Toast.LENGTH_SHORT).show();
             }
        }
    }


        public void readyToBattle (View view){
            if (!allShipIsPlaced) {
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
        otherPlayerRef.removeEventListener(otherPlayerListener);
            handler.removeCallbacksAndMessages(null);
            super.onDestroy();
        }

    private void updateView(int index, Direction direction, int length) {
        Button btn1, btn2, btn3, btn4;
        btn1 = findViewById(btnDefault+index);
        if (length == 1) {
            btn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_one));
            if (direction == Direction.RIGHT || direction  == Direction.LEFT) {
               btn1.setRotation(90);
            } else {
                btn1.setRotation(0);
            }
        } else if (length == 2) {
            btn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_front));
            if (direction == Direction.RIGHT) {
                btn2 = findViewById(btnDefault+index-1);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_end));
                btn1.setRotation(90);
                btn2.setRotation(90);
            } else if (direction == Direction.LEFT) {
                btn2 = findViewById(btnDefault+index+1);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_end));
                btn1.setRotation(270);
                btn2.setRotation(270);
            } else if (direction == Direction.DOWN) {
                btn2 = findViewById(btnDefault+index+8);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_end));
                btn1.setRotation(0);
                btn2.setRotation(0);
            } else if (direction == Direction.UP) {
                btn2 = findViewById(btnDefault-index-8);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_two_end));
                btn1.setRotation(180);
                btn2.setRotation(180);
            }
        } else if (length == 3) {
            btn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_front));
            if (direction == Direction.RIGHT) {
                btn2 = findViewById(btnDefault+index-1);
                btn3 = findViewById(btnDefault+index-2);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
                btn1.setRotation(90);
                btn2.setRotation(90);
                btn3.setRotation(90);
            } else if (direction == Direction.LEFT) {
                btn2 = findViewById(btnDefault+index+1);
                btn3 = findViewById(btnDefault+index+2);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
                btn1.setRotation(270);
                btn2.setRotation(270);
                btn3.setRotation(270);
            } else if (direction == Direction.DOWN) {
                btn2 = findViewById(btnDefault+index+8);
                btn3 = findViewById(btnDefault+index+16);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
                btn1.setRotation(0);
                btn2.setRotation(0);
                btn3.setRotation(0);
            } else if (direction == Direction.UP) {
                btn2 = findViewById(btnDefault+index-8);
                btn3 = findViewById(btnDefault+index-16);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_three_end));
                btn1.setRotation(180);
                btn2.setRotation(180);
                btn3.setRotation(180);
            }
        } else if (length == 4) {
            btn1.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front));
            if (direction == Direction.RIGHT) {
                btn2 = findViewById(btnDefault+index-1);
                btn3 = findViewById(btnDefault+index-2);
                btn4 = findViewById(btnDefault+index-3);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end_middle));
                btn4.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end));
                btn1.setRotation(90);
                btn2.setRotation(90);
                btn3.setRotation(90);
                btn4.setRotation(90);
            } else  if (direction == Direction.LEFT) {
                btn2 = findViewById(btnDefault+index+1);
                btn3 = findViewById(btnDefault+index+2);
                btn4 = findViewById(btnDefault+index+3);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end_middle));
                btn4.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end));
                btn1.setRotation(270);
                btn2.setRotation(270);
                btn3.setRotation(270);
                btn4.setRotation(270);
            } else  if (direction == Direction.DOWN) {
                btn2 = findViewById(btnDefault+index+8);
                btn3 = findViewById(btnDefault+index+16);
                btn4 = findViewById(btnDefault+index+24);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end_middle));
                btn4.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end));
                btn1.setRotation(0);
                btn2.setRotation(0);
                btn3.setRotation(0);
                btn4.setRotation(0);
            } else  if (direction == Direction.UP) {
                btn2 = findViewById(btnDefault+index-8);
                btn3 = findViewById(btnDefault+index-16);
                btn4 = findViewById(btnDefault+index-24);

                btn2.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_front_middle));
                btn3.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end_middle));
                btn4.setBackground(ContextCompat.getDrawable(this, R.mipmap.ship_four_end));
                btn1.setRotation(180);
                btn2.setRotation(180);
                btn3.setRotation(180);
                btn4.setRotation(180);
            }
        }
    }

    public void makeButtonsUnclickable (int index, Direction direction, int length) {

    }

  /*  public BitmapDrawable combineImage(int imgID, int imgID2) {
        Bitmap img1 = BitmapFactory.decodeResource(getResources(), imgID);
        Bitmap img2 = BitmapFactory.decodeResource(getResources(), imgID2);
        Bitmap overlay = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(), img1.getConfig());
        Canvas canvas = new Canvas(overlay);
        img2 = addBorderColor(img2, 1);
        canvas.drawBitmap(img2, new Matrix(), null);
        canvas.drawBitmap( img1,  new Matrix(), null);
        return bitmapToBitmapDrawable(overlay);
    }

    public BitmapDrawable bitmapToBitmapDrawable(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }

    private Bitmap addBorderColor(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() +  borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.parseColor("#23574B"));
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    } */
    @Override
    protected void onPause() {
        handler.removeCallbacks(r);
        super.onPause();
    }

    @Override
    protected void onResume() {
        r.run();
        super.onResume();
    }
}
