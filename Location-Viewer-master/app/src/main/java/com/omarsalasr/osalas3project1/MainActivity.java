/*
 * Omar Salas-Rodriguez
 * CS 478 Spring 2019
 * Project 1
 *
 */

package com.omarsalasr.osalas3project1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {

    private Button btnAddress; // Address button
    private Button btnMap;     // Map Button
    private int returnResult;  // Return result code ( OK, CANCELED, etc)
    private  Toast toast;
    private String address;    // Address retrieved

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize widgets
        btnAddress = findViewById(R.id.btnAddress);
        btnMap = findViewById(R.id.btnMap);

        // Initialize default values
        returnResult = 999; // Random value to check if no button has been pressed yet
        address = "";

        // Add listeners to the Address button
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start a new activity for result to retrieve the address for the Maps activity
                Intent i = new Intent(MainActivity.this, AddressActivity.class);
                startActivityForResult(i,2);

            }
        });

        // Add listeners to the Map button
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check what actions have occurred
                if(returnResult == 999){ // No buttons has been clicked
                    toast = Toast.makeText(MainActivity.this, "Please press the \"ADDRESS\" button", Toast.LENGTH_LONG);
                    toast.show();
                }else if(returnResult == RESULT_CANCELED){ // Result from the second activity was empty
                    toast = Toast.makeText(MainActivity.this, "Invalid input, please click \"ADDRESS\" button", Toast.LENGTH_LONG);
                    toast.show();
                }else if(returnResult == RESULT_OK){ // Result from the second activity was non-empty
                    // Start new activity with implicit intent that handles geo scheme for URI
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(address)));
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        super.onActivityResult(requestCode, resultCode, i);
        address = "";
        // Check that the request code and result code are correct to retrieve the address from extras
        if (requestCode == 2)
            if (resultCode == RESULT_OK)
                if(i != null)
                    address = i.getStringExtra(Intent.EXTRA_TEXT); // Retrieve address

        returnResult = resultCode;
    }

}
