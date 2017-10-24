package com.example.nate.projectplanner.database;

import java.util.HashMap;
import java.util.Map;

public class Event {

    public String projectName;
    public String eventName;
    public Map<String, String> Dependencies = new HashMap<>();

    public Event() {
        // default constructor
    }

    public Event(String eventName, String projectName) {
        this.eventName = eventName;
        this.projectName = projectName;
    }

}
