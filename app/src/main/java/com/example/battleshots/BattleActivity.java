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

public class BattleActivity extends AppCompatActivity {

    GameModel gameModel = new GameModel ("gamemodel");
    Server server = new Server();
    String playerID, gameID;
    DatabaseReference playerRef, otherPlayerRef;
    ValueEventListener valueEventListener;
    ArrayList<Point> shipParts;
    HashMap<String, Object> Player1ShipInfo, player1Info,Ship1Info;
    public final int DEFAULT_GRID_ID = 2131230887;
    GridLayout gridLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_mode);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        gridLayout.setRowOrderPreserved(true);


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
        Button btn = (Button) findViewById(view.getId());
        btn.setText(Integer.toString(view.getId()-DEFAULT_GRID_ID));
    }

    public void test(View view) {

        for(int i = 0; i <= 63; i++){

        }

        paintShips(getShips(player1Info));
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

            ImageView viewx = findViewById(DEFAULT_GRID_ID + viewID);
            viewx.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.green));

        }

    }

    public void findID(View view){
        Toast.makeText(getApplicationContext(),Integer.toString( view.getId()), Toast.LENGTH_LONG).show();
    }

    public int convertPointToIndex(Point point) {
        return point.getX() * 8 + point.getY();
    }



}
