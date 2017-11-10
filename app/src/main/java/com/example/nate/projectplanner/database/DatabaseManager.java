package com.example.nate.projectplanner.database;

import android.util.Log;

import com.example.nate.projectplanner.tools.Utility;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    public static final String USERS_KEY = "users";
    public static final String PROJECTS_KEY = "projects";
    public static final String USER_PROJECTS_KEY = "user-projects";
    public static final String EVENTS_KEY = "events";
    public static final String DEPENDENCIES_KEY = "dependencies";
    public static final String NON_DEPENDENCIES_KEY = "non-dependencies";

    // Database
    private DatabaseReference mDatabase;

    public DatabaseManager() {
        // Default constructor
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference fetchUser(String userId) {
        return mDatabase.child(USERS_KEY).child(userId);
    }

    public DatabaseReference fetchProject(String projectId) {
        return mDatabase.child(PROJECTS_KEY).child(projectId);
    }

    public DatabaseReference fetchEvent(String projectId, String eventId) {
        return fetchProject(projectId).child(EVENTS_KEY).child(eventId);
    }

    public DatabaseReference fetchUserProjects(String userId) {
        return mDatabase.child(USER_PROJECTS_KEY).child(userId);
    }

    public User addNewUser(String userId, String email, String name) {
        User user = new User(name, email);
        mDatabase.child(USERS_KEY).child(userId).setValue(user);

        return user;
    }

    public User addNewUser(String userId, String email) {
        User user = new User(email);
        mDatabase.child(USERS_KEY).child(userId).setValue(user);

        return user;
    }

    public String addNewProject(String userId, String projectName) { // TODO ensure that project names are unique
        // Create new project at /user-projects/$userid/$projectid and at /projects/$projectid simultaneously
        String projectId = mDatabase.child(PROJECTS_KEY).push().getKey();
        Project project = new Project(userId, projectName);
        Map<String, Object> projectValues = project.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + PROJECTS_KEY + "/" + projectId, projectValues);
        childUpdates.put("/" + USER_PROJECTS_KEY + "/" + userId + "/" + projectId, projectValues);

        mDatabase.updateChildren(childUpdates);

        return projectId;
    }

    public String addNewEvent(final String projectId, final String eventName) { // TODO ensure that event names are unique
        DatabaseReference projectRef = mDatabase.child(PROJECTS_KEY).child(projectId);
        final String eventId = projectRef.push().getKey();
        final Event event = new Event(eventName);
        Map<String, Object> eventValues = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + PROJECTS_KEY + "/" + projectId + "/" + EVENTS_KEY + "/" + eventId, eventValues);

        mDatabase.updateChildren(childUpdates);

        projectRef.child(EVENTS_KEY).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final String keyOfEventAdded = dataSnapshot.getKey();
                final String nameOfEventAdded = Utility.toString(dataSnapshot.child("eventName").getValue());
                if (!eventId.equals(keyOfEventAdded)) {
                    Log.d(TAG,"onChildAdded: [" + keyOfEventAdded + "] = " + nameOfEventAdded + " for listener on " + eventName);
                    fetchEvent(projectId, eventId).child(NON_DEPENDENCIES_KEY).child(keyOfEventAdded).setValue(nameOfEventAdded);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                final String keyOfEventChanged = dataSnapshot.getKey();
//                final String nameOfEventChanged = Utility.toString(dataSnapshot.child("eventName").getValue());
//                if (!eventId.equals(keyOfEventChanged)) {
//                    Log.d(TAG,"onChildChanged: [" + keyOfEventChanged + "] = " + nameOfEventChanged + " for listener on " + eventName);
//                    fetchEvent(projectId, eventId).child(NON_DEPENDENCIES_KEY).child(keyOfEventChanged).setValue(nameOfEventChanged);
//                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                final String keyOfEventRemoved = dataSnapshot.getKey();
                if (!eventId.equals(keyOfEventRemoved)) {
                    // TODO does this remove child key as well?
                    Log.d(TAG,"onChildRemoved: [" + keyOfEventRemoved + "] = " + Utility.toString(dataSnapshot.child("eventName").getValue()) + " for listener on " + eventName);
                    fetchEvent(projectId, eventId).child(NON_DEPENDENCIES_KEY).child(keyOfEventRemoved).removeValue();
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return eventId;
    }

    public void addDependencyToEvent(String projectId, String eventId, String dependencyId, String dependencyName) {
        DatabaseReference eventRef = fetchEvent(projectId, eventId);
        eventRef.child(DEPENDENCIES_KEY).child(dependencyId).setValue(dependencyName);

        // also remove from non-dependencies
        eventRef.child(NON_DEPENDENCIES_KEY).child(dependencyId).removeValue();
    }

    public void removeDependencyFromEvent(String projectId, String eventId, String dependencyId, String dependencyName) {
        DatabaseReference eventRef = fetchEvent(projectId, eventId);
        eventRef.child(DEPENDENCIES_KEY).child(dependencyId).removeValue();

        // also add to non-dependencies
        eventRef.child(NON_DEPENDENCIES_KEY).child(dependencyId).setValue(dependencyName);
    }

    public void singleCopy(DatabaseReference fromRef, final DatabaseReference toRef)
    {
        fromRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toRef.setValue(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
            }
        });
    }

    public void copy(DatabaseReference fromRef, final DatabaseReference toRef)
    {
        fromRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toRef.setValue(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
            }
        });
    }
}
