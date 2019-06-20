package com.example.battleshots;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.w3c.dom.Text;

public class StartMenuActivity extends AppCompatActivity {


    public String playerName;
    public Player currentPlayer;
    Server server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);
        openNameDialog();

        server = new Server();
        currentPlayer = new Player(playerName,8);



    }

    public void createGame(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateGameActivity.class);
        intent.putExtra("pName",playerName);
        startActivity(intent);

    }

    public void joinGame(View view) {
        Intent intent = new Intent(getApplicationContext(), JoinGameActivity.class);
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
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                playerName = input.getText().toString();

                TextView playerNameText = (TextView) findViewById(R.id.playerName_id);
                playerNameText.setText("Welcome "+playerName);
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
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                server.joinGame(input.getText().toString() , currentPlayer);
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }



}


