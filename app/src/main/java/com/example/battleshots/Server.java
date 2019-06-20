package com.example.battleshots;

import android.app.Activity;
import android.nfc.Tag;
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


public class Server {
    FirebaseDatabase database;
    DatabaseReference gameDatabase;

    Server(){
        database = FirebaseDatabase.getInstance();
    }


    public void addShipToDatabase(final Activity context, Ship ship){
        gameDatabase.child("game_model").child(ship.getShipName()).setValue(ship).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void addGameModelToDatabase(GameModel gameModel) {
        gameDatabase = database.getReference();
        gameDatabase.child("game_model").setValue("GameID").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        });
    }

    public void deleteGameDataBase() {
        gameDatabase.removeValue();
    }



    public void updateGame(final DataManager data){
        gameDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
