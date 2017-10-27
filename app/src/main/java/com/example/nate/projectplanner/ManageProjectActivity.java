package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.database.Project;
import com.example.nate.projectplanner.tools.Converters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ManageProjectActivity extends BaseActivity {

    private static final String TAG = "ManageProjectActivity";

    DatabaseManager mDatabaseManager;

    // Current projectId
    static String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);

        mDatabaseManager = new DatabaseManager();

        // Load project
        String newProjectId = getIntent().getStringExtra("ProjectId");
        if (newProjectId != null) projectId = newProjectId;

        final DatabaseReference projectRef = mDatabaseManager.fetchProject(projectId);
        projectRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                TextView projectNameTextView = (TextView) findViewById(R.id.textview_project_name);
                projectNameTextView.setText(project.projectName);

                projectRef.child("events").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ListView eventNamesListView = (ListView) findViewById(R.id.list_events);
                        eventNamesListView.setAdapter(Converters.convertSnapshotValuesToListAdapter(
                                ManageProjectActivity.this, dataSnapshot, "eventName"
                        ));
                        eventNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String projectName = String.valueOf(parent.getItemAtPosition(position));
                                String message = "Oops " + projectName + " is unavailable";
                                Toast.makeText(ManageProjectActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "loadEvents:onCancelled", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadProjects:onCancelled", databaseError.toException());
            }
        });
    }

    public void newEvent(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        intent.putExtra("projectId", projectId);
        startActivity(intent);
    }
}
