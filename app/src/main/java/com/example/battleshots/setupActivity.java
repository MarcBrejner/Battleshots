package com.example.battleshots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class setupActivity extends AppCompatActivity {

    private String TAG = "Setup-UserInterface";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_setup_map);
    }

    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "button clicked", Toast.LENGTH_SHORT).show();
    }

}
