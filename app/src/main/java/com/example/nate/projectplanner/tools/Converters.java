package com.example.nate.projectplanner.tools;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Converters {

    private static final String TAG = "Converters";

    public static ListAdapter convertSnapshotValuesToListAdapter(Activity activity, DataSnapshot dataSnapshot, String childName) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(postSnapShot.child(childName).getValue().toString());
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotValuesToListAdapter", e);
            }
        }

        return convertListToListAdapter(activity, list);
    }

    public static List<String> convertSnapshotKeysToList(DataSnapshot dataSnapshot) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(postSnapShot.getKey());
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotValuesToListAdapter", e);
            }
        }
        return list;
    }

    public static ListAdapter convertListToListAdapter(Activity activity, List<String> list) {
        String[] array = new String[list.size()];
        array = list.toArray(array);

        return new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, array);
    }
}
