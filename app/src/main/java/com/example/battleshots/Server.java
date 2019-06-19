package com.example.battleshots;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.constraint.Constraints.TAG;

public class Server {
    FirebaseDatabase database;

    public Server(){
        database = FirebaseDatabase.getInstance();
    }


    public void addShipToDatabase(Ship ship){
        DatabaseReference myRef = database.getReference();
        myRef.child("ships").child(ship.getShipName()).setValue(ship);
    }




}
