package com.example.battleshots;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class BattleActivity extends AppCompatActivity {

    GameModel gameModel = new GameModel ("gamemodel");
    Server server = new Server();
    String playerID, gameID;
    DatabaseReference playerRef, otherPlayerRef;
    ValueEventListener valueEventListener;
    ArrayList<Point> playerShipParts, otherPlayerShipParts;

    HashMap<String, Object> otherPlayerInfo, playerInfo, destroyedInfo, turnInfo;
    public final int DEFAULT_GRID_ID = 2131230763;
    public final int DEFAULT_SHOOTBUTTON_ID = 2131230827;
    int gridSize = 8, hitCount = 0;
    Boolean yourTurn = false;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_mode);

        //init
        gameID = getIntent().getStringExtra("gameID");
        server.createGameRef(gameID);
        playerID = getIntent().getStringExtra("playerID");
        assignPlayers(playerID,gameID);



        //add ValueEventListener to current player
        playerRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                playerInfo = (HashMap<String, Object>) dataSnapshot.getValue();

                if(playerInfo.get("destroyed_parts") != null ){
                    destroyedInfo = (HashMap<String, Object>) playerInfo.get("destroyed_parts");
                    paintDestroyedPart(destroyedInfo);
                    hitCount++;
                    Toast.makeText(getApplicationContext(),"You got hit, do a shot!", Toast.LENGTH_SHORT).show();
                    if(hitCount > 9){
                        openLoseDialog();
                    }
               }

                Boolean goTime = (Boolean) playerInfo.get("turn");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        //add ValueEventListener to enemy player
        otherPlayerRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherPlayerInfo = (HashMap<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


    }

    public void onClick(View view) {
        Button btn = findViewById(view.getId());
        Point point = convertIndexToPoint(view.getId()-DEFAULT_SHOOTBUTTON_ID);

        otherPlayerShipParts = getShips(otherPlayerInfo);

        if(otherPlayerShipParts.contains(point)){
            btn.setBackground(ContextCompat.getDrawable(this,R.drawable.chosenbutton));
            otherPlayerRef.child("destroyed_parts").setValue(point);

        } else {
            btn.setBackground(ContextCompat.getDrawable(this, R.drawable.missedbutton));
        }
    }

    public void test(View view) {
        paintShips(getShips(playerInfo));
        Toast.makeText(getApplicationContext(),"gnomed", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Point> getShips (HashMap<String, Object> playerInfo){

        Map<String, Object> playerShipInfo = (HashMap<String, Object>) playerInfo.get("ships");
        Map<String, Object> playerShipXInfo;
        List<Object> pointInfo;
        Map<String, Object> pointJ;

         int x;
         int y;
         ArrayList<Point> shipParts = new ArrayList<Point>();

         for(int i = 1; i < 5; i++){

             playerShipXInfo = (HashMap<String, Object>) playerShipInfo.get("Ship_"+i);

             pointInfo = (ArrayList<Object>) playerShipXInfo.get("points");

             for(int j = 0; j < i; j++) {

                 pointJ = (HashMap<String, Object>) pointInfo.get(j);
                 x = (int)(long) pointJ.get("x");
                 y = (int)(long) pointJ.get("y");

                 shipParts.add(new Point(x,y));

             }
        }
         return shipParts;
    }

    public void paintShips(ArrayList<Point> shipParts){

        int x;
        int y;
        int viewID;

        for(int i = 0; i < shipParts.size(); i++){

            x = shipParts.get(i).getX();
            y = shipParts.get(i).getY();
            viewID = x + y*gridSize;

          Button btn = findViewById(DEFAULT_GRID_ID + viewID);
          btn.setBackground(ContextCompat.getDrawable(this, R.drawable.chosenbutton));
        }
    }

    public void paintDestroyedPart(HashMap<String,Object> destroyedInfo){
        int x;
        int y;
        int viewID;

        x = (int)(long) destroyedInfo.get("x");
        y = (int)(long) destroyedInfo.get("y");
        viewID = x + y*gridSize;

        Button btn = findViewById(DEFAULT_GRID_ID+viewID);
        btn.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.destroyedbutton));
    }

    public void assignPlayers(String playerID, String gameID){
        if (playerID.equals("1")) {
            playerRef = server.reference.child("Game").child(gameID).child("Player 1");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 2");
            playerRef.child("turn").setValue(true);
            otherPlayerRef.child("turn").setValue(false);
        } else  {
            playerRef = server.reference.child("Game").child(gameID).child("Player 2");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 1");
        }
    }

    public int convertPointToIndex(Point point) {
        return point.getX() * gridSize + point.getY();
    }
    public Point convertIndexToPoint(int index) {
        return new Point(index%gridSize,index/gridSize);
    }


    public void openLoseDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("You lost :(");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);


        // Set OK Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), StartMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(0, 10, 0, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

    }

    protected void onDestroy(){
        playerRef.removeEventListener(valueEventListener);
        otherPlayerRef.removeEventListener(valueEventListener);
        server.deleteGameDataBase(gameID);
        super.onDestroy();
    }





}
