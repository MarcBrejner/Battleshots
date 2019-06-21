package com.example.battleshots;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class Server {
    public FirebaseDatabase database;
    DatabaseReference reference;
    String gameID;
    Map<String, Object> info;
    DatabaseReference gameRef;

    Server(){
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    public void createGame(String gameID, Player player1) {
        this.gameID = gameID;
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
        info.put("ships", player.getShips());
        return info;
    }

    public void joinGame(String joinGameID, Player player2){
        this.gameID = joinGameID;
        info = convertPlayerInfoToMap(player2);
        gameRef = reference.child("Game").child(joinGameID);
        gameRef.child("Player 2").updateChildren(info);;
    }

    public void addShipToDatabase(final Activity context, Ship ship){
        reference.child("Game").child(ship.getShipName()).setValue(ship).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    public String getGameID() {
        return gameID;
    }

    public void addGameModelToDatabase(GameModel gameModel) {
        reference = database.getReference();
        reference.child("game_model").setValue("GameID").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    public void deleteGameDataBase() {
        gameRef.removeValue();
    }

    public DatabaseReference getGameRef() {
        return gameRef;
    }


    public void startGame() {
        gameRef.child("isStarted").setValue(true);
    }
}
