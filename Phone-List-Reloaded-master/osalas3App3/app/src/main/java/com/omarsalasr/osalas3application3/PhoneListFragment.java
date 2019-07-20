package com.omarsalasr.osalas3application3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PhoneListFragment extends ListFragment {
    // Interface item for fragment communication
    private ListItemSelectionListener myList = null;

    // Interface used for fragment communication
    public interface ListItemSelectionListener{
        public void onListSelection(int index);
    }

    // List item clicked method for List Fragment
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        l.setItemChecked(position,true);
        myList.onListSelection(position); // Pass the index of the item clicked to the activity
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{ // Check if the activity containing the fragment has implemented the communication interface
            myList = (ListItemSelectionListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " needs to implement ListItemSelectionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set the list of phone names for display
        setListAdapter(new ArrayAdapter<String>(getContext(), R.layout.phone_item, MainActivity.phoneNames));
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
