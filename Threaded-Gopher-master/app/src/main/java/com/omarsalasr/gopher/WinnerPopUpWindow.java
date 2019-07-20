package com.omarsalasr.gopher;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class WinnerPopUpWindow extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_pop_up_window);

        // Get the phone screen dimensions in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        // Set a new layout of the activity from the center
        getWindow().setLayout((int)(width * 0.7),(int)(height * 0.6));
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = -20;
        getWindow().setAttributes(layoutParams);

        // Get the winner text from the intent extras
        Intent i = getIntent();
        ((TextView) findViewById(R.id.txt_winner)).setText(i.getStringExtra(GameBoard.WINNER));

        ((Button) findViewById(R.id.btn_main_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send back result for GameBoard intent and show the main menu
                Intent returnIntent = new Intent();
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}
