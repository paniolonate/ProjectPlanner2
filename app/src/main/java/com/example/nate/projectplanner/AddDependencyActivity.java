package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.tools.Converters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddDependencyActivity extends BaseActivity {

    private static final String TAG = "AddDependencyActivity";

    private static String projectId;
    private static String eventId;

    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependency);

        mDatabaseManager = new DatabaseManager();

        Intent intent = getIntent();
        String newProjectId = intent.getStringExtra("projectId");
        String newEventId = intent.getStringExtra("eventId");

        if (newProjectId != null) projectId = newProjectId;
        if (newEventId != null) eventId = newEventId;

        DatabaseReference eventRef = mDatabaseManager.fetchEvent(projectId, eventId);
        eventRef.child("non-dependencies").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ListView eventNamesListView = (ListView) findViewById(R.id.list_events);
                final List<String> eventIds = Converters.convertSnapshotKeysToList(dataSnapshot);
                eventNamesListView.setAdapter(Converters.convertSnapshotValuesToListAdapter(
                        AddDependencyActivity.this, dataSnapshot
                ));
                eventNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            mDatabaseManager.addDependencyToEvent(
                                    projectId,
                                    eventId,
                                    eventIds.get(position),
                                    parent.getItemAtPosition(position).toString()
                            );
                            Toast.makeText(AddDependencyActivity.this, "Added dependency", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d(TAG, "onClickNonDependency", e);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
