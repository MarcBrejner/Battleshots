package com.example.battleshots;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StartMenuActivity extends AppCompatActivity implements Serializable {


    public String playerName, joinGameID;

    Server server;
    ChildEventListener childEventListener;
    List<String> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        openNameDialog();

        server = new Server();

        server.database.getReference().addChildEventListener(childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    values.add(i.getKey());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    values.add(i.getKey());
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        server.database.getReference().removeEventListener(childEventListener);
        super.onDestroy();
    }


    public void createGame(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateGameActivity.class);
        intent.putExtra("pName", playerName);
        startActivity(intent);
    }


    public void joinGame(View view) {
        openJoinDialog();

        //startActivity(intent);
    }

    public void setupMap(View view) {
        Intent intent = new Intent(getApplicationContext(), setupActivity.class);
        startActivity(intent);
    }

    public void battleMap(View view) {
        Intent intent = new Intent(getApplicationContext(), BattleActivity.class);
        startActivity(intent);
    }


    public void openNameDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Please enter your name");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        //Set up input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);


        // Set OK Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                playerName = input.getText().toString();

                TextView playerNameText = (TextView) findViewById(R.id.playerName_id);
                playerNameText.setText("Welcome " + playerName);
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

    public void openJoinDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Enter game room ID");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        //Set up input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alertDialog.setView(input);


        // Set OK Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (values.contains(input.getText().toString())) {
                    joinGameID = input.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
                    intent.putExtra("joinGameID", joinGameID);
                    intent.putExtra("pName", playerName);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Game ID doesn't exist", Toast.LENGTH_SHORT).show();
                }

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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }




}


