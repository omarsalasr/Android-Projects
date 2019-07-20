package com.omarsalasr.osalas3application2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    // Phone information key for intent extras
    public static final String PHONE_ASSET = "PHONE_ASSET";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create toast message to display the phone selected in application 3 after receiving broadcast
        Toast.makeText(context,"App 2: " + intent.getStringExtra(PHONE_ASSET) + " phone selected", Toast.LENGTH_LONG).show();
    }

}
