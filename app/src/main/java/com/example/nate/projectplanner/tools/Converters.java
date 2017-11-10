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

    public static ListAdapter convertSnapshotValuesToListAdapter(Activity activity, DataSnapshot dataSnapshot) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(Utility.toString(postSnapShot.getValue()));
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotChildValuesToListAdapter", e);
            }
        }

        return convertListToListAdapter(activity, list);
    }

    public static ListAdapter convertSnapshotChildValuesToListAdapter(Activity activity, DataSnapshot dataSnapshot, String childName) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(Utility.toString(postSnapShot.child(childName).getValue()));
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotChildValuesToListAdapter", e);
            }
        }

        return convertListToListAdapter(activity, list);
    }

    public static List<String> convertSnapshotChildValuesToList(DataSnapshot dataSnapshot, String childName) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(Utility.toString(postSnapShot.child(childName).getValue()));
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotValuesToList", e);
            }
        }

        return list;
    }

    public static List<String> convertSnapshotKeysToList(DataSnapshot dataSnapshot) {
        List<String> list = new ArrayList<String>();
        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
            try {
                list.add(postSnapShot.getKey());
            } catch (NullPointerException e){
                Log.d(TAG, "convertSnapshotKeysToList", e);
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
