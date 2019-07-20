package com.omarsalasr.osalas3app1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PhoneImage extends AppCompatActivity {

    private String phoneLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_image);
        ImageView imgPhone = findViewById(R.id.imgPhone);
        // Retrieve intent and check which phone was selected
        Intent i = getIntent();
        String phone = i.getStringExtra(MyReceiver.PHONE_ASSET);
        if (phone.equalsIgnoreCase("apple")){
            imgPhone.setImageResource(R.drawable.iphone_xs_max);
            phoneLink = getResources().getString(R.string.apple);
        }else if (phone.equalsIgnoreCase("lg")){
            imgPhone.setImageResource(R.drawable.lg_v30);
            phoneLink = getResources().getString(R.string.lg);
        }else if (phone.equalsIgnoreCase("samsung")){
            imgPhone.setImageResource(R.drawable.note_9);
            phoneLink = getResources().getString(R.string.samsung);
        }else if (phone.equalsIgnoreCase("oneplus")){
            imgPhone.setImageResource(R.drawable.oneplus_6t);
            phoneLink = getResources().getString(R.string.oneplus);
        }else if (phone.equalsIgnoreCase("google")){
            imgPhone.setImageResource(R.drawable.pixel_3);
            phoneLink = getResources().getString(R.string.google);
        }else if(phone.equalsIgnoreCase("razer")){
            imgPhone.setImageResource(R.drawable.razer_phone);
            phoneLink = getResources().getString(R.string.razer);
        }

        Button btnSite = findViewById(R.id.btnSite);
        btnSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start implicit intent to open the website for the phone selected
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(phoneLink));
                startActivity(i);
            }
        });

    }
}
