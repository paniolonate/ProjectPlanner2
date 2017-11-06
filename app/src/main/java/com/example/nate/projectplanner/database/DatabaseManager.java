package com.example.nate.projectplanner.database;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    // Database
    private DatabaseReference mDatabase;


    public DatabaseManager() {
        // Default constructor
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference fetchUser(String userId) {
        return mDatabase.child("users").child(userId);
    }

    public DatabaseReference fetchProject(String projectId) {
        return mDatabase.child("projects").child(projectId);
    }

    public DatabaseReference fetchEvent(String projectId, String eventId) {
        return fetchProject(projectId).child(eventId);
    }

    public DatabaseReference fetchUserProjects(String userId) {
        return mDatabase.child("user-projects").child(userId);
    }

    public User addNewUser(String userId, String email, String name) {
        User user = new User(name, email);
        mDatabase.child("users").child(userId).setValue(user);

        return user;
    }

    public User addNewUser(String userId, String email) {
        User user = new User(email);
        mDatabase.child("users").child(userId).setValue(user);

        return user;
    }

    public String addNewProject(String userId, String projectName) {
        // Create new project at /user-projects/$userid/$projectid and at
        // /projects/$projectid simultaneously
        String projectId = mDatabase.child("projects").push().getKey();
        Project project = new Project(userId, projectName);
        Map<String, Object> projectValues = project.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/projects/" + projectId, projectValues);
        childUpdates.put("/user-projects/" + userId + "/" + projectId, projectValues);

        mDatabase.updateChildren(childUpdates);

        return projectId;
    }

    public String addNewEvent(String projectId, String eventName) {
        DatabaseReference projectRef = mDatabase.child("projects").child(projectId);
        String eventId = projectRef.push().getKey();
        Event event = new Event(eventName);
        Map<String, Object> eventValues = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/projects/" + projectId + "/" + "events/" + eventId, eventValues);

        mDatabase.updateChildren(childUpdates);

        return eventId;
    }

    public void setTextFromDatabaseReference(final TextView textView, final DatabaseReference ref, String key) {
        ref.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    textView.setText(dataSnapshot.getValue().toString());
                } catch (NullPointerException e) {
                    Log.d(TAG,"setTextFromDatabaseReference:getValue", e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"setTextFromDatabaseReference:onCancelled", databaseError.toException());
            }
        });
    }
}
