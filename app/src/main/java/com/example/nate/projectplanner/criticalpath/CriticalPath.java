package com.example.nate.projectplanner.criticalpath;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.tools.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CriticalPath {

    private final static String TAG = "CriticalPath";

    private DatabaseManager databaseManager;

    private ArrayList<String> criticalPathIds;
    private int criticalCost;

    private String projectId;

    public CriticalPath(final String projectId) {
        this.projectId = projectId;
        this.databaseManager = new DatabaseManager();
        this.criticalPathIds = new ArrayList<String>();
        this.criticalCost = 0;
    }

    // TODO: make this algorithm more efficient
    public void calculate() {
        databaseManager.fetchProject(projectId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot projectSnapshot) {
                HashSet<String> calculated = new HashSet<String>();
                HashSet<String> remaining = new HashSet<String>();
                HashSet<String> terminals = getTerminalEventIds(projectSnapshot);

                DataSnapshot eventsSnapshot = projectSnapshot.child("events");
                Iterable<DataSnapshot> eventsIterable = eventsSnapshot.getChildren();
                for (DataSnapshot eventSnapshot : eventsIterable) {
                    // if is initial event (no dependencies)
                    if (eventSnapshot.child("dependencies").getChildrenCount() == 0) {
                        int duration = Utility.getIntegerValue(eventSnapshot.child("duration"));
                        eventSnapshot.getRef().child("criticalDuration").setValue(duration);
                        calculated.add(eventSnapshot.getKey());
                    }
                    // if is terminal event (no dependents)
                    else if (!terminals.contains(eventSnapshot.getKey())) {
                        remaining.add(eventSnapshot.getKey());
                    }
                }

                while (!remaining.isEmpty()) {
                    boolean noCycle = false;

                    for (Iterator<String> remainingIter = remaining.iterator(); remainingIter.hasNext();) {
                        DataSnapshot remainingEvent = eventsSnapshot.child(remainingIter.next());
                        HashSet<String> dependencyIds = getDependencyIds(remainingEvent);

                        if (calculated.containsAll(dependencyIds)) {
                            setEventCriticalCost(
                                    remainingEvent, getTrailingCriticalCost(projectSnapshot, dependencyIds)
                            );
                            calculated.add(remainingIter.toString());
                            remainingIter.remove();

                            noCycle = true;
                        }
                    }

                    if(!noCycle) {
                        Log.d(TAG, "Encountered dependency cycle! Stopped!");
                        break;
                    }
                }

                criticalCost = 0;
                String criticalTerminalId = "";
                for (String terminalId : terminals) {
                    DataSnapshot terminalEvent = eventsSnapshot.child(terminalId);
                    int eventCriticalCost = setEventCriticalCost(
                            terminalEvent, getTrailingCriticalCost(
                                    projectSnapshot, getDependencyIds(terminalEvent)
                            )
                    );
                    if (eventCriticalCost > criticalCost) {
                        criticalTerminalId = terminalId;
                        criticalCost = eventCriticalCost;
                    }
                }

                updateCriticalPath(projectSnapshot, criticalTerminalId);
//                Log.d(TAG, String.valueOf(criticalCost));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO
            }
        });
    }

    public ArrayList<String> getCriticalPathIds() { return criticalPathIds; }

    public int getCriticalCost() { return criticalCost; }

    private void updateCriticalPath(DataSnapshot projectSnapshot, String criticalTerminalId) {
        DatabaseReference criticalPathRef = databaseManager.fetchProjectCriticalPath(projectId);
        DatabaseReference criticalEventsRef = criticalPathRef.child("events");
        DatabaseReference criticalDurationRef = criticalPathRef.child("criticalDuration");

        criticalPathIds.clear();
        criticalEventsRef.removeValue();
        criticalDurationRef.removeValue();

        DataSnapshot criticalNode = projectSnapshot.child("events").child(criticalTerminalId);
        addCriticalEvent(criticalEventsRef, criticalNode);
        criticalDurationRef.setValue(criticalNode.child("criticalDuration").getValue());
        while (criticalNode.child("dependencies").hasChildren()) {
            String criticalDependencyId = getCriticalDependencyId(projectSnapshot, criticalNode);
            criticalNode = projectSnapshot.child("events").child(criticalDependencyId);
            addCriticalEvent(criticalEventsRef, criticalNode);
            Log.d(TAG, String.valueOf(criticalNode.child("eventName")));
        }
    }

    private void addCriticalEvent(DatabaseReference criticalEventsRef, DataSnapshot criticalNode) {
        criticalPathIds.add(criticalNode.getKey());
        criticalEventsRef.child(criticalNode.getKey()).setValue(criticalNode.getValue());
    }

    private String getCriticalDependencyId(DataSnapshot projectSnapshot, DataSnapshot eventSnapshot) {
        String criticalDependencyId = "";
        int mostCriticalCost = 0;
        for (DataSnapshot dependency : eventSnapshot.child("dependencies").getChildren()) {
            int criticalCost = Utility.getIntegerValueOfChild(
                    projectSnapshot.child("events").child(dependency.getKey()), "criticalDuration"
            );
            if (criticalCost > mostCriticalCost) {
                mostCriticalCost = criticalCost;
                criticalDependencyId = dependency.getKey();
            }
        }

        return criticalDependencyId;
    }

    private HashSet<String> getDependencyIds(DataSnapshot eventSnapshot) {
        HashSet<String> dependencyIds = new HashSet<String>();
        for (DataSnapshot dependencySnapShot : eventSnapshot.child("dependencies").getChildren()) {
            dependencyIds.add(dependencySnapShot.getKey());
        }

        return dependencyIds;
    }

    private HashSet<String> getTerminalEventIds(DataSnapshot projectSnapshot) {
        final HashSet<String> terminalEventIds = new HashSet<String>();

        for (DataSnapshot eventSnapshot : projectSnapshot.child("events").getChildren()) {
//            Log.d(TAG, "Adding terminal event candidate: " + eventSnapshot.child("eventName"));
            terminalEventIds.add(eventSnapshot.getKey());
        }

        for (DataSnapshot eventSnapshot : projectSnapshot.child("events").getChildren()) {
//            Log.d(TAG, "Removal Round");
            for (DataSnapshot dependencySnapshot : eventSnapshot.child("dependencies").getChildren()) {
                String dependencyId = dependencySnapshot.getKey();
//                Log.d(TAG, "DependencyId: " + dependencyId);
                if (terminalEventIds.contains(dependencyId)) {
//                    Log.d(TAG, "Removing nonterminal event: " + eventSnapshot.child("eventName"));
                    terminalEventIds.remove(dependencyId);
                }
            }
        }

        return terminalEventIds;
    }

    private int getTrailingCriticalCost(DataSnapshot projectSnapshot, HashSet<String> dependencyIds) {
        int critical = 0;
        for (String dependencyId : dependencyIds) {
            DataSnapshot dependencyEvent = projectSnapshot.child("events").child(dependencyId);
            int dependencyCriticalCost = Utility.getIntegerValue(dependencyEvent.child("criticalDuration"));
            if (dependencyCriticalCost > critical) critical = dependencyCriticalCost;
        }

        return critical;
    }

    private int setEventCriticalCost(DataSnapshot eventSnapshot, int trailingCriticalCost) {
        int eventCost = Utility.getIntegerValue(eventSnapshot.child("duration"));
        int eventCriticalCost = eventCost + trailingCriticalCost;
        eventSnapshot.getRef().child("criticalDuration").setValue(eventCriticalCost);

        return eventCriticalCost;
    }
}
