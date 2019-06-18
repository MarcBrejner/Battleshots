package com.example.battleshots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class setupActivity extends AppCompatActivity {

    public static int[] playerShips;
    private String TAG = "Setup-UserInterface";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);
    }

    public void onClick(View view) {
        Button btn = (Button) findViewById(view.getId());

        if (btn.getText() != "clicked") {
            btn.setText("clicked");
        } else {
            btn.setText("");
        }

        String text = Integer.toString(btn.getId());

        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void savePositions(View view) {
        for(int i = 0; i < 25; i++) {
            int btnId = 2131230759 + i;
            Button btn = (Button) findViewById(btnId);
            if(btn.getText() == "clicked") {
                playerShips[i] = btnId;
            }
        }

        System.out.println(Arrays.toString(playerShips));
    }
}
