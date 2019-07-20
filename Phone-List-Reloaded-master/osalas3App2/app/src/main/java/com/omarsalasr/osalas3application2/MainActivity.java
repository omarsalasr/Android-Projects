package com.omarsalasr.osalas3application2;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    // Custom permission defined in application 3
    private final String CUSTOM_PERM = "com.omarsalasr.osalas3application3.EDU.UIC.CS478.S19.KABOOM";
    // Permission code used for checking if the user granted permission
    private final int MY_PERM_CODE = 679;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         * Use the package manager to retrieve all the applications installed in the phone, then check
         * if application 1 exists and retrieve all the permissions granted in application 1.
         * Finally, check if the application granted permission to the custom permission defined in
         * application 3. Deny the application to continue if permission was not granted.
         */
        try {
            String a1Package = "com.omarsalasr.osalas3app1";
            final PackageInfo packageInfo = getPackageManager().getPackageInfo(a1Package, PackageManager.GET_PERMISSIONS);
            for(int i = 0; i < packageInfo.requestedPermissions.length; i++){
                if(packageInfo.requestedPermissions[i].equalsIgnoreCase(CUSTOM_PERM)
                    && packageInfo.requestedPermissionsFlags[i] == 1){
                    Toast.makeText(MainActivity.this, "App 1 needs kaboom permission", Toast.LENGTH_LONG).show();
                    finish();
                }
            }


        }catch (Exception e){
            Log.e("Application 2: ", e.toString());
            finish();
        }

        // Permission for application 1 was granted and now set the button and onClick listener to launch application 3
        Button btnLaunchA3 = findViewById(R.id.btnLaunch);
        btnLaunchA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if permission was granted
                int perm = ContextCompat.checkSelfPermission(MainActivity.this, CUSTOM_PERM);
                if(perm == PackageManager.PERMISSION_GRANTED)
                    launchA3(); // Launch application 3
                else // Request permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{CUSTOM_PERM}, MY_PERM_CODE);
            }
        });
    }

    public void launchA3(){
        // Create intent filter for listening to custom permission intent
        String KABOOM_INTENT = "edu.uic.cs478.s19.kaboom";
        IntentFilter myFilter = new IntentFilter(KABOOM_INTENT);
        // Set the priority higher than application 1 so this executes first
        myFilter.setPriority(20);
        // Create broadcast receiver and register it to listen to the custom permission broadcast
        MyReceiver myReceiver = new MyReceiver();
        registerReceiver(myReceiver, myFilter);
        // Use the package manager to get the intent to launch application 3
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.omarsalasr.osalas3application3");
        if(intent != null) {
            startActivity(intent);
        }else // Application 3 isn't installed
            Log.e("Application 2", "Couldn't locate application");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){ // Request permission for the custom permission
            case MY_PERM_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    launchA3();
                else{
                    Toast.makeText(MainActivity.this, "You need to grant permission!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }
}
