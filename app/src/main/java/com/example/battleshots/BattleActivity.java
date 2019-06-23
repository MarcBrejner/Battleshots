package com.example.battleshots;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
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
    ArrayList<Point> playerShipParts, otherPlayerShipParts;

    HashMap<String, Object> otherPlayerInfo, playerInfo;
    public final int DEFAULT_GRID_ID = 2131230763;
    public final int DEFAULT_SHOOTBUTTON_ID = 2131230827;
    GridLayout gridLayout;
    int gridSize = 8;


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
                playerInfo = (HashMap<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        otherPlayerRef.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                otherPlayerInfo = (HashMap<String, Object>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });






        // TODO: A lot of stuff....
    }

    public void onClick(View view) {
        Button btn = findViewById(view.getId());
        Point point = convertIndexToPoint(view.getId()-DEFAULT_SHOOTBUTTON_ID);

        otherPlayerShipParts = getShips(otherPlayerInfo);

        if(otherPlayerShipParts.contains(point)){
            btn.setBackground(ContextCompat.getDrawable(this,R.drawable.chosenbutton));
            otherPlayerRef.child("destroyed_parts").setValue(point);
        } else {
            Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void test(View view) {
        paintShips(getShips(playerInfo));
        Toast.makeText(getApplicationContext(),"gnomed", Toast.LENGTH_SHORT).show();
    }

    public void colorButton(){

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
            viewID = x + y*8;

          Button btn = findViewById(DEFAULT_GRID_ID + viewID);
          btn.setBackground(ContextCompat.getDrawable(this, R.drawable.chosenbutton));
        }
    }

    public void findID(View view){
        Toast.makeText(getApplicationContext(),Integer.toString( view.getId()), Toast.LENGTH_LONG).show();
    }

    public int convertPointToIndex(Point point) {
        return point.getX() * gridSize + point.getY();
    }
    public Point convertIndexToPoint(int index) {
        return new Point(index%gridSize,index/gridSize);
    }





}
