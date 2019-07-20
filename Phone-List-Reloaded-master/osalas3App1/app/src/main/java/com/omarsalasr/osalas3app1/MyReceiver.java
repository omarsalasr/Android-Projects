package com.omarsalasr.osalas3app1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    public static final String PHONE_ASSET = "PHONE_ASSET";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Create intent for image activity
        Intent i = new Intent(context,PhoneImage.class);
        // Retrieve phone name from broadcast intent received from application 3 and place into new intent
        i.putExtra(PHONE_ASSET, intent.getStringExtra(PHONE_ASSET));
        context.startActivity(i);
    }

}
