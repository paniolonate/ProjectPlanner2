package com.example.nate.projectplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nate.projectplanner.criticalpath.CriticalPath;
import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.tools.Converters;
import com.example.nate.projectplanner.tools.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CriticalPathActivity extends BaseActivity {

    private final static String TAG = "CriticalPathActivity";

    DatabaseManager mDatabaseManager;

    DatabaseReference mCriticalPath;

    static String projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critical_path);

        mDatabaseManager = new DatabaseManager();

        // Load project
        String newProjectId = getIntent().getStringExtra("ProjectId");
        if (newProjectId != null) projectId = newProjectId;

        mCriticalPath = mDatabaseManager.fetchProjectCriticalPath(projectId);

        mDatabaseManager.fetchProject(projectId).child("projectName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView projectNameTextView = (TextView) findViewById(R.id.textview_project_name);
                projectNameTextView.setText(Utility.toString(dataSnapshot.getValue()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        });

        mCriticalPath.child("criticalDuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView criticalDurationTextView = (TextView) findViewById(R.id.textview_critical_duration);
                criticalDurationTextView.setText(getString(
                        R.string.critical_duration_fmt, Utility.toString(dataSnapshot.getValue())));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        });

        mCriticalPath.child("events").orderByChild("criticalDuration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListView eventNamesListView = (ListView) findViewById(R.id.list_critical_events);
                eventNamesListView.setAdapter(Converters.convertSnapshotChildValuesToListAdapter(
                        CriticalPathActivity.this, dataSnapshot, "eventName"
                ));
                final List<String> eventIdsList = Converters.convertSnapshotKeysToList(dataSnapshot);

                eventNamesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    Toast.makeText(CriticalPathActivity.this, R.string.under_construction, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.d(TAG, "onClickCriticalEvent:failed", e);
                                }
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        });
    }
}
