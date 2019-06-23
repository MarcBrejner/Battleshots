package com.example.battleshots;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class Server {
    FirebaseDatabase database;
    DatabaseReference reference;
    Map<String, Object> info, shipInfo;
    DatabaseReference gameRef;

    Server(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        shipInfo = new HashMap<>();
        info = new HashMap<>();
    }

    public void createGame(String gameID, Player player1) {
        info = convertPlayerInfoToMap(player1);
        reference.child("Game").child(gameID).setValue(gameID);
        gameRef = reference.child("Game").child(gameID);
        gameRef.child("Player 1").updateChildren(info);
        gameRef.child("isStarted").setValue(false);
    }

    public Map<String, Object> convertPlayerInfoToMap(Player player) {
        info = new HashMap<>();
        info.put("grid", player.getGrid());
        info.put("playerName", player.getPlayerName());
        return info;
    }

    public void joinGame(String joinGameID, Player player2){
        info = convertPlayerInfoToMap(player2);
        gameRef = reference.child("Game").child(joinGameID);
        gameRef.child("Player 2").updateChildren(info);
    }

    public  void convertShipInfoToMap(Player player, String playerID, Activity context) {
        for (int i = 0; i < player.getShips().size(); i++) {
            String shipName = player.getShips().get(i).getShipName();
            shipInfo.put("points", player.getShips().get(i).getShip());
            shipInfo.put("isDestroyed", player.getShips().get(i).isDestroyed());
            shipInfo.put("size", player.getShips().get(i).getShipSize());
            // Toast.makeText(context, shipInfo.toString(), Toast.LENGTH_SHORT).show();
            gameRef.child("Player " + playerID).child("ships").child(shipName).updateChildren(shipInfo);
        }
    }

    public void addShipToDatabase(final Activity context, Player player, String gameID, String playerID){
        info.put("grid", player.getGrid());
        gameRef = reference.child("Game").child(gameID);
        gameRef.child("Player " + playerID).updateChildren(info);

        convertShipInfoToMap(player, playerID, context);

    }

    public void deleteGameDataBase(String GameID) {
        reference.child("Game").child(GameID).removeValue();
    }

    public DatabaseReference createGameRef(String gameID) {
        gameRef = reference.child("Game").child(gameID);
        return gameRef;
    }
}
