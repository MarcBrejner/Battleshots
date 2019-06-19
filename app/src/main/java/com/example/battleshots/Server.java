package com.example.battleshots;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.constraint.Constraints.TAG;

public class Server {
    FirebaseDatabase database;
    DatabaseReference gameDatabase;

    public Server(){
        database = FirebaseDatabase.getInstance();
    }


    public void addShipToDatabase(Ship ship){
        gameDatabase.child("game_model").child(ship.getShipName()).setValue(ship);
    }

    public void addGameModelToDatabase(GameModel gameModel) {
        gameDatabase = database.getReference();
        gameDatabase.child("game_model").child("grid").setValue(gameModel.getGrid());
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
