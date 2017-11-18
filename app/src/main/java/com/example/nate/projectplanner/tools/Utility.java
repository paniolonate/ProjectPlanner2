package com.example.nate.projectplanner.tools;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class Utility {

    private static final String TAG = "Utility";

    public static String toString(Object object) {
        try {
            return object.toString();
        } catch (NullPointerException e) {
            Log.d(TAG,"toString:", e);
        }
        return null;
    }

    public static Integer getIntegerValue(DataSnapshot dataSnapshot) {
        try {
            return dataSnapshot.getValue(Integer.class);
        } catch (NullPointerException e) {
            Log.d(TAG,"toInteger:", e);
        }
        return null;
    }

    public static Integer getIntegerValueOfChild(DataSnapshot dataSnapshot, String childName) {
        DataSnapshot child = dataSnapshot.child(childName);
        if (child.exists()) {
            try {
                return child.getValue(Integer.class);
            } catch (NullPointerException e) {
                Log.d(TAG, "getIntegerValueOfChild:", e);
            }
        }
        return null;
    }

}
