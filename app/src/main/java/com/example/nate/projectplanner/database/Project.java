package com.example.nate.projectplanner.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;


public class Project {

    public String userId;
    public String projectName;

    public Project() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Project(String userId, String projectName) {
        this.userId = userId;
        this.projectName = projectName;

    }

    // Project values in map format for json database
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> projectMap = new HashMap<>();
        projectMap.put("userId", userId);
        projectMap.put("projectName", projectName);

        return projectMap;
    }

}
