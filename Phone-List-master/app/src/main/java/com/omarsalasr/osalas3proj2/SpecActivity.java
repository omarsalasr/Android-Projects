package com.omarsalasr.osalas3proj2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec);

        // Get the intent to retrieve the phone title, spec array and store list
        Intent i = getIntent();
        String title = i.getStringExtra(Intent.EXTRA_TITLE);
        final String link = i.getStringExtra(MainActivity.EXTRA_URL);
        String[] specs = i.getStringArrayExtra(MainActivity.EXTRA_SPEC_ARRAY);
        // Error check: image reference wasn't retrieved correctly
        int image = i.getIntExtra(MainActivity.EXTRA_IMAGE,-999);
        if(image == -999){
            Log.e("ImageActivity","Unable to load image from Extras");
            finish();
        }

        setTitle(title);
        // Initialize the image view
        ImageView specImg = findViewById(R.id.specImage);
        specImg.setImageResource(image);
        specImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Initialize implicit intent to open phone store website
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });

        /*
         *  Display each specification in its own text view inside a vertical linear layout.
         *  The string parsing is done using the Html.fromHTML method which parses an html
         *  formatted string with all its working tags.
         *  This was used to format the spec title in bold and the rest in regular font.
         *
         */
        ((TextView) findViewById(R.id.dimensionText)).setText(Html.fromHtml(
                "<a><font><b>Dimensions: </b></font>" + specs[0] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.weightText)).setText(Html.fromHtml(
                "<a><font><b>Weight: </b></font>" + specs[1] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.displayText)).setText(Html.fromHtml(
                "<a><font><b>Display: </b></font>" + specs[2] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.processorText)).setText(Html.fromHtml(
                "<a><font><b>Processor: </b></font>" + specs[3] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.cameraText)).setText(Html.fromHtml(
                "<a><font><b>Camera: </b></font>" + specs[4] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.batteryText)).setText(Html.fromHtml(
                "<a><font><b>Battery: </b></font>" + specs[5] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.ramText)).setText(Html.fromHtml(
                "<a><font><b>RAM: </b></font>" + specs[6] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.storageText)).setText(Html.fromHtml(
                "<a><font><b>Storage: </b></font>" + specs[7] + "</a>",Html.FROM_HTML_MODE_COMPACT));
        ((TextView) findViewById(R.id.costText)).setText(Html.fromHtml(
                "<a><font><b>Cost: </b></font>" + specs[8] + "</a>",Html.FROM_HTML_MODE_COMPACT));

    }
}
