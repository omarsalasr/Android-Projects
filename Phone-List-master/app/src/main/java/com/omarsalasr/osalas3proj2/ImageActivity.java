package com.omarsalasr.osalas3proj2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Get the intent to retrieve the title, image reference and store links
        Intent i = getIntent();
        String title = i.getStringExtra(Intent.EXTRA_TITLE);
        final String link = i.getStringExtra(MainActivity.EXTRA_URL);
        int image = i.getIntExtra(MainActivity.EXTRA_IMAGE,-999);
        // Error check: image reference wasn't retrieved correctly
        if(image == -999){
            Log.e("ImageActivity","Unable to load image from Extras");
            finish();
        }
        setTitle(title);

        // Initialize the phone image view and attributes
        ImageView phoneImg = findViewById(R.id.phoneImage);
        phoneImg.setPadding(MyAdapter.PADDING_LEFT*5,MyAdapter.PADDING_TOP*5,MyAdapter.PADDING_RIGHT*5,MyAdapter.PADDING_BOTTOM*5);
        phoneImg.setImageResource(image);
        phoneImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // Set the on click listener for the image
                // Start an implicit intent for opening the store link for the phone in a web browser
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });

    }
}
