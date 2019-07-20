package com.omarsalasr.gopher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class MainMenu extends AppCompatActivity {

    // Public key for retrieving the game mode
    public static final String GAME_MODE = "GAME_MODE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get UI elements
        final RadioGroup radioGroup = findViewById(R.id.game_mode_group);
        Button startGame = findViewById(R.id.btn_start);
        // Set the start game button listener
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = GameBoard.MODE_MbM; // Mode for the intent to pass in extras
                switch(radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_btn_Cont:
                        mode = GameBoard.MODE_CONT; // Continuous mode
                        break;
                    case R.id.radio_btn_MbM:
                        mode = GameBoard.MODE_MbM; // Move-By-Move mode
                        break;
                }
                // Start the GameBoard activity for the main game
                Intent i = new Intent(MainMenu.this, GameBoard.class);
                i.putExtra(GAME_MODE, mode);
                startActivity(i);
            }
        });
    }



}
