package com.example.nate.projectplanner.tools;

import android.util.Log;

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

}
