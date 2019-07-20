package com.omarsalasr.osalas3application3;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PhoneListFragment.ListItemSelectionListener {

    // FrameLayouts and Fragment objects
    private FrameLayout list_container, image_container;
    private FragmentManager fManager;
    private PhoneImageFragment phoneImageFragment = new PhoneImageFragment();
    private PhoneListFragment phoneListFragment = new PhoneListFragment();

    // Get the match_parent value for linear layout
    int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;
    // Phone name and phone company arrays
    public static String[] phoneNames, companyNames;
    // Intent string key
    public static final String PHONE_ASSET = "PHONE_ASSET";
    // Boolean to check if a phone has been selected
    private boolean itemSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the toolbar, set the toolbar, and empty the title
        Toolbar myToolBar = (Toolbar) findViewById(R.id.appBar);
        setSupportActionBar(myToolBar);
        getSupportActionBar().setTitle("");

        // Retrieve the phone and company arrays from resources
        phoneNames = getResources().getStringArray(R.array.phone_list);
        companyNames = getResources().getStringArray(R.array.company_list);
        // Get the container for each framelayout
        list_container = findViewById(R.id.list_container);
        image_container = findViewById(R.id.image_container);
        // Get the fragment manager and replace the list container with a new phone list
        fManager = getSupportFragmentManager();
        fManager.beginTransaction().replace(R.id.list_container, phoneListFragment).commit();
        // Set the fragment retain instance so they survive destruction
        phoneListFragment.setRetainInstance(true);
        phoneImageFragment.setRetainInstance(true);
        // Display the phone list fragment
        list_container.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        image_container.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        // Set the backstack change listener so that the frame layouts are changed accordingly
        fManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                setLayout();
            }
        });
    }

    // Function to change the frame layouts according to phone orientation
    public void setLayout(){
        if(!phoneImageFragment.isAdded()){ // Check if the image fragment has been added to manager
            // Set the list to fit the whole screen
            list_container.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            image_container.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            itemSelected = false;
        }else{ // Fragment has been added to manager
            //Check for orientation
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                // Set the image to fit the whole screen
                list_container.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                image_container.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            }else{
                // Set the image to occupy a third of the screen
                list_container.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, 1f));
                image_container.setLayoutParams(new LinearLayout.LayoutParams(0, MATCH_PARENT, 2f));
            }
            itemSelected = true;
        }
    }

    // Inflate the options menu for app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check which button has been clicked from options menu
        switch (item.getItemId()){
            case R.id.launch:
                checkPermissionsAndLaunch();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    // Cbeck if the the previous 2 apps have the proper permissions
    public void checkPermissionsAndLaunch(){
        boolean permissions = true; // Assume permissions are all set
        // Custom permission name
        String CUSTOM_PERM = "com.omarsalasr.osalas3application3.EDU.UIC.CS478.S19.KABOOM";
        try {
            /*
             * Use the package manager to seek the first application to check if it exists and
             * retrieve its permissions to find if the custom permission has been granted access
             * by the user. Deny the permission set for this app if not granted.
             */
            String a1Package = "com.omarsalasr.osalas3app1";
            final PackageInfo package1Info = getPackageManager().getPackageInfo(a1Package, PackageManager.GET_PERMISSIONS);
            for(int i = 0; i < package1Info.requestedPermissions.length; i++){
                if(package1Info.requestedPermissions[i].equalsIgnoreCase(CUSTOM_PERM)
                        && package1Info.requestedPermissionsFlags[i] == 1){
                    Toast.makeText(MainActivity.this, "App 1 needs kaboom permission", Toast.LENGTH_LONG).show();
                    permissions = false;
                }
            }
            /*
             * Use the package manager to seek the second application to check if it exists and
             * retrieve its permissions to find if the custom permission has been granted access
             * by the user. Deny the permission set for this app if not granted.
             */
            String a2Package = "com.omarsalasr.osalas3application2";
            final PackageInfo package2Info = getPackageManager().getPackageInfo(a2Package, PackageManager.GET_PERMISSIONS);
            for(int i = 0; i < package2Info.requestedPermissions.length; i++){
                if(package2Info.requestedPermissions[i].equalsIgnoreCase(CUSTOM_PERM)
                        && package2Info.requestedPermissionsFlags[i] == 1){
                    Toast.makeText(MainActivity.this, "App 2 needs kaboom permission", Toast.LENGTH_LONG).show();
                    permissions = false;
                }
            }

        }catch (Exception e){
            Log.e("Need permission: ", e.toString());
            finish();
        }

        // Check if all the permissions needed have been granted
        if(permissions){
            String KABOOM_INTENT = "edu.uic.cs478.s19.kaboom";
            // Check if a phone has been selected
            if(itemSelected){
                // Set the intent with the phone information stored in extras for the broadcast
                Intent intent = new Intent(KABOOM_INTENT);
                intent.putExtra(PHONE_ASSET, companyNames[phoneImageFragment.getCurrentIndex()]);
                // Send ordered broadcast to each application receiving the intent
                sendOrderedBroadcast(intent, CUSTOM_PERM);
            }else{
                Toast.makeText(MainActivity.this, "Please select a phone", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onListSelection(int index) {
        if (!phoneImageFragment.isAdded()) { // Check if the image fragment has been added to the manager
            // Commit new transaction of the image fragment onto the manager and add to backstack to trigger listener
            fManager.beginTransaction().add(R.id.image_container,phoneImageFragment).addToBackStack(null).commit();
            fManager.executePendingTransactions();
        }
        // Update the image of the phone in the fragment
        phoneImageFragment.showImageAtIndex(index);
    }

}