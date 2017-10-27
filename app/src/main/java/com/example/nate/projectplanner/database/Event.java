package com.example.nate.projectplanner.database;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Event {

    public String eventName;
    public int duration;
    public int urgencyLevel;

    public Event() {

    }

    public Event(String eventName) {
        this(eventName, 10, 0);
    }

    public Event(String eventName, int duration, int urgencyLevel) {
        this.eventName = eventName;
        this.duration = duration;
        this.urgencyLevel = urgencyLevel;
    }

    // Event values in map format for json database
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put("eventName", eventName);
        eventMap.put("duration", duration);
        eventMap.put("urgencyLevel", urgencyLevel);

        return eventMap;
    }

}
