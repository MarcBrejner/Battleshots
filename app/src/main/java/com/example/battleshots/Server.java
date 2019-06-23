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

    public  Map<String, Object> convertShipInfoToMap(Player player) {
        shipInfo = new HashMap<>();
        shipInfo.put("points", player.getShips().get(player.getShips().size()-1).getShip());
        shipInfo.put("isDestroyed", player.getShips().get(player.getShips().size()-1).isDestroyed());
        shipInfo.put("size", player.getShips().get(player.getShips().size()-1).getShipSize());
        return shipInfo;
    }

    public void addShipToDatabase(final Activity context, Player player, String gameID, String playerID){
        info = new HashMap<>();
        info.put("grid", player.getGrid());
        gameRef = reference.child("Game").child(gameID);
        gameRef.child("Player " + playerID).updateChildren(info);

        shipInfo = convertShipInfoToMap(player);
        String shipName = player.getShips().get(player.getShips().size()-1).getShipName();
        gameRef.child("Player " + playerID).child("ships").child(shipName).updateChildren(shipInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context.getApplicationContext(), "Ship deployed", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), "Failed to deploy ship", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteGameDataBase(String GameID) {
        reference.child("Game").child(GameID).removeValue();
    }

    public DatabaseReference createGameRef(String gameID) {
        gameRef = reference.child("Game").child(gameID);
        return gameRef;
    }
}
