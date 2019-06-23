package com.example.battleshots;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
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
    ArrayList<Point> shipParts;
    HashMap<String, Object> Player1ShipInfo, player1Info,Ship1Info;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_mode);

        //init
        gameID = getIntent().getStringExtra("gameID");
        server.createGameRef(gameID);
        playerID = getIntent().getStringExtra("playerID");

        if (playerID.equals("1")) {
            playerRef = server.reference.child("Game").child(gameID).child("Player 1");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 2");
        } else  {
            playerRef = server.reference.child("Game").child(gameID).child("Player 2");
            otherPlayerRef = server.reference.child("Game").child(gameID).child("Player 1");
        }

        playerRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                player1Info = (HashMap<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });






        // TODO: A lot of stuff....
    }

    public void onClick(View view) {

    }

    public void test(View view) {
        shipParts = getShip1(player1Info);
    }

    public void colorButton(){

    }

    public ArrayList<Point> getShip1(HashMap<String, Object> playerInfo){

         HashMap<String, Object> playerShipInfo = (HashMap<String, Object>) playerInfo.get("ships");

         HashMap<String, Object> playerShip1Info = (HashMap<String, Object>) playerShipInfo.get("Ship_1");

         HashMap<String, HashMap<String, Object>> pointInfo = (HashMap<String, HashMap<String, Object>>) playerShip1Info.get("points");


         ArrayList<Point> shipParts = new ArrayList<>();
         int x;
         int y;

         for(int i = 0; i < Integer.parseInt(playerShip1Info.get("size").toString()); i++ ){
             x = (Integer) pointInfo.get(String.valueOf(i)).get("x");
             y = (Integer) pointInfo.get(String.valueOf(i)).get("y");
             shipParts.add(new Point(x,y));
         }

         return shipParts;

    }

    public int convertPointToIndex(Point point) {
        return point.getX() * 8 + point.getY();
    }



}
