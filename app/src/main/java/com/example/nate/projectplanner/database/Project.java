package com.example.nate.projectplanner.database;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;


public class Project {

    public String userId;
    public String projectName;
    public Map<String, Event> events = new HashMap<>();

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
//        projectMap.put("events", events);

        return projectMap;
    }

}
