/*
 * Omar Salas-Rodriguez
 * CS 478 Spring 2019
 * Project 1
 *
 */

package com.omarsalasr.osalas3project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class AddressActivity extends AppCompatActivity{

    private String address;
    private TextView txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        // Initialize widget
        txtAddress = findViewById(R.id.txtAddress);

        // Add EditorListener for return key event
        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // Get the address
                address = txtAddress.getText().toString();
                Intent rIntent = new Intent();
                // Place the address in Extras
                rIntent.putExtra(Intent.EXTRA_TEXT,address);
                // Set Result value
                if(address.length() == 0)
                    setResult(RESULT_CANCELED, rIntent);
                else
                    setResult(RESULT_OK, rIntent);
                finish();
                return false;
            }
        });
    }


}
