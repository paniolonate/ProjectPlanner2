package com.example.nate.projectplanner.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";

    // Databases
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    public DatabaseManager() {
        // Default constructor
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public DatabaseReference fetchUser(String userId) {
        return mDatabase.child("users").child(userId);
    }

    public DatabaseReference fetchUserProjects(String userId) {
        return mDatabase.child("user-projects").child(userId);
    }

    public void addNewUser(String userId, String email, String name) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void addNewUser(String userId, String email) {
        User user = new User(email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void addNewProject(String userId, String projectName) {
        // Create new post at /user-projects/$userid/$projectid and at
        // /projects/$projectid simultaneously
        String projectId = mDatabase.child("projects").push().getKey();
        Project project = new Project(userId, projectName);
        Map<String, Object> projectValues = project.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/projects/" + projectId, projectValues);
        childUpdates.put("/user-projects/" + userId + "/" + projectId, projectValues);

        mDatabase.updateChildren(childUpdates);
    }

    public String getCurrentUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
