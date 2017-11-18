package com.example.nate.projectplanner.database;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Event {

    public String eventName;
    public int duration;
    public ArrayList<String> dependencyIds;

    public Event() {

    }

    public Event(String eventName) {
        this(eventName, 1, null);
    }

    public Event(String eventName, int duration) {
        this(eventName, duration, null);
    }

    public Event(String eventName, int duration, ArrayList<String> dependencyIds) {
        this.eventName = eventName;
        this.duration = duration;
        this.dependencyIds = dependencyIds;
    }

    // Event values in map format for json database
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventName", eventName);
        eventMap.put("duration", duration);
        eventMap.put("dependencies", dependencyIds);

        return eventMap;
    }

}
