package com.omarsalasr.osalas3proj2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    // Constant keywords for intent extras
    protected static final String EXTRA_IMAGE = "IMAGE_ASSET";
    protected static final String EXTRA_URL = "URL_ASSET";
    protected static final String EXTRA_SPEC_ARRAY = "SPEC_ASSET";

    // Special ArrayList containing a full array of specs for each phone
    private ArrayList<String[]> phoneSpecs;

    // Arrays for storing all the phone information
    private String[] phoneModels;
    private String[] phoneDetails;
    private String[] phoneLinks;

    // ArrayList for storing the images
    private ArrayList<Integer> phoneImgs = new ArrayList<Integer>(Arrays.asList(
            R.drawable.note_9, R.drawable.pixel_3,
            R.drawable.oneplus_6t, R.drawable.iphone_xs_max,
            R.drawable.razer_phone, R.drawable.lg_v30
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all the arrays containing the data
        phoneModels = getResources().getStringArray(R.array.phone_model);
        phoneDetails = getResources().getStringArray(R.array.phone_details);
        phoneLinks = getResources().getStringArray(R.array.phone_links);
        phoneSpecs = new ArrayList<String[]>(Arrays.<String[]>asList(
                getResources().getStringArray(R.array.note9_specs),
                getResources().getStringArray(R.array.google_pixel_3_specs),
                getResources().getStringArray(R.array.oneplus_6t_specs),
                getResources().getStringArray(R.array.iphone_xs_max_specs),
                getResources().getStringArray(R.array.razer_phone_specs),
                getResources().getStringArray(R.array.lg_v30_specs)

        ));

        // Initialize the List View and set the adapter and listener for each item
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new MyAdapter(this, phoneModels, phoneDetails, phoneImgs));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Initialize the intent for the expanded image Activity
                Intent i = new Intent(MainActivity.this, ImageActivity.class);
                // Store the selected phone title, image and sore URL
                i.putExtra(Intent.EXTRA_TITLE, phoneModels[position]);
                i.putExtra(EXTRA_IMAGE, phoneImgs.get(position));
                i.putExtra(EXTRA_URL, phoneLinks[position]);
                startActivity(i);
            }
        });

        // Register context menu for the List View items
        registerForContextMenu(listView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // Initialize the inflater menu XML layout file
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Menu");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Get the item selected information for the context menu option
        AdapterView.AdapterContextMenuInfo info	= (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent i;
        switch(item.getItemId()){
            case(R.id.specs): // Start an explicit intent for a view containing the phone specs
                i = new Intent(MainActivity.this, SpecActivity.class);
                // Store title, image, store URL and spec array for the selected phone
                i.putExtra(Intent.EXTRA_TITLE, phoneModels[info.position]);
                i.putExtra(EXTRA_IMAGE, phoneImgs.get(info.position));
                i.putExtra(EXTRA_URL, phoneLinks[info.position]);
                i.putExtra(EXTRA_SPEC_ARRAY, phoneSpecs.get(info.position));
                startActivity(i);
                break;
            case(R.id.image): // Start an explicit intent for a view containing a bigger image for the phone selected
                i = new Intent(MainActivity.this, ImageActivity.class);
                i.putExtra(Intent.EXTRA_TITLE, phoneModels[info.position]);
                i.putExtra(EXTRA_IMAGE, phoneImgs.get(info.position));
                i.putExtra(EXTRA_URL, phoneLinks[info.position]);
                startActivity(i);
                break;
            case(R.id.store): // Start an implicit intent to open a web browser to the store page of the phone selected
                i = new Intent(Intent.ACTION_VIEW, Uri.parse(phoneLinks[info.position]));
                startActivity(i);
                break;

            default:
                return false;
        }
        return true;
    }
}