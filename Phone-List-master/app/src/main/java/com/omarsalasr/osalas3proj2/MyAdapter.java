package com.omarsalasr.osalas3proj2;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;

public class MyAdapter extends BaseAdapter {

    // Constant integers for dimensions
    protected static final int PADDING_TOP = 40,PADDING_BOTTOM = 40;
    protected static final int PADDING_LEFT = 25,PADDING_RIGHT = 25;
    private Context context;
    private String[] phoneModels;
    private String[] phoneDetails;
    private ArrayList<Integer> phoneImgs;

    // Constructor that stores all the phone information for a given List item
    public MyAdapter(Context context, String[] models, String[] details, ArrayList<Integer> images){
        this.context = context;
        phoneModels = models;
        phoneDetails = details;
        phoneImgs = images;
    }

    // Return the number of items in the list
    @Override
    public int getCount() {
        return phoneImgs.size();
    }

    // Return the object at the position of the List item which was clicked
    @Override
    public Object getItem(int position) {
        return phoneImgs.get(position);
    }

    // Return the ID of the position of the List item which was clicked
    @Override
    public long getItemId(int position) {
        return phoneImgs.get(position);
    }

    // Return the custom View containing an ImageView and 2 TextViews which will be placed in the List
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)// Check if the menu item is empty for reuse of the view
            // Inflate the custom view from the XML layout file
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item_view,parent,false);

        // Initialize the thumbnail and 2 text views for the custom list view
        ImageView thumbnail = listItem.findViewById(R.id.imgView);
        TextView model = listItem.findViewById(R.id.phoneModel);
        TextView details = listItem.findViewById(R.id.phoneDetails);

        // Initialize the attributes for the items in the custom list view
        thumbnail.setPadding(PADDING_LEFT,PADDING_TOP,PADDING_RIGHT,PADDING_BOTTOM);
        thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        thumbnail.setImageResource(phoneImgs.get(position));
        model.setText(phoneModels[position]);
        model.setTextSize(18);
        model.setPadding(PADDING_LEFT,PADDING_TOP,PADDING_RIGHT,PADDING_BOTTOM/2);
        details.setText(phoneDetails[position]);
        details.setTextSize(14);
        details.setPadding(PADDING_LEFT,0,PADDING_RIGHT,0);

        return listItem;
    }
}
