package com.omarsalasr.osalas3application3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

public class PhoneImageFragment extends Fragment {

    private int currentIndex = -1; // Current phone index for display
    private ImageView phone_image; // ImageView contained in the framelayout
    // List of phone images for the image fragment
    private ArrayList<Integer> imageArray = new ArrayList<Integer>(Arrays.asList(
            R.drawable.iphone_xs_max, R.drawable.lg_v30, R.drawable.note_9,
            R.drawable.oneplus_6t, R.drawable.pixel_3, R.drawable.razer_phone
    ));

    // Get the current phone selected index
    public int getCurrentIndex(){ return currentIndex; }

    // Update the phone image that must be displayed
    public void showImageAtIndex(int index){
        // Check the index is within range
        if(index < 0 || index >= imageArray.size())
            return;
        currentIndex = index;
        phone_image.setBackgroundResource(imageArray.get(index));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the image layout and retrieve the image view
        View cont = inflater.inflate(R.layout.phone_image,container, false);
        phone_image = cont.findViewById(R.id.phone_image);
        return cont;
    }

}
