package com.omarsalasr.osalas3app1;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Permission code for permission request
    private final int MY_PERM_CODE = 679;
    // Custom permission defined in application 3
    private final String CUSTOM_PERM = "com.omarsalasr.osalas3application3.EDU.UIC.CS478.S19.KABOOM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnLaunchA2 = findViewById(R.id.btnLaunchA2);
        btnLaunchA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the permission to custom permission was granted
                int permCheck = ContextCompat.checkSelfPermission(MainActivity.this, CUSTOM_PERM);
                if(PackageManager.PERMISSION_GRANTED == permCheck)
                    launchA2(); // Launch the next application
                else // Request permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{CUSTOM_PERM}, MY_PERM_CODE);
            }
        });

    }

    public void launchA2(){
        // Intent string broadcast will listen to
        String KABOOM_INTENT = "edu.uic.cs478.s19.kaboom";
        IntentFilter myFilter = new IntentFilter(KABOOM_INTENT);
        // Low priority, will launch after application 2 broadcast receiver
        myFilter.setPriority(10);
        // Create and register new broadcast receiver to listen for custom intent from application 3
        MyReceiver myReceiver = new MyReceiver();
        registerReceiver(myReceiver, myFilter);
        // Retrieve the intent for application 2 if it exists
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.omarsalasr.osalas3application2");
        if(intent != null) {
            startActivity(intent);
        }else // Application 2 isn't installed
            Log.e("Application 1", "Couldn't locate application");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){ // Check if the user granted permission after requesting permission
            case MY_PERM_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    launchA2();
                else // No permission was granted
                    Toast.makeText(MainActivity.this, "You need to grant permission!!!", Toast.LENGTH_LONG).show();
        }
    }
}
