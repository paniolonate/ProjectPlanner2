package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.tools.Converters;
import com.example.nate.projectplanner.tools.Utility;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ManageEventActivity extends BaseActivity {

    private static final String TAG = "ManageEventActivity";

    DatabaseManager mDatabaseManager;

    static String projectId;
    static String eventId;

    boolean removeDependenciesMode = false;

    // TextViews
    TextView projectNameTextView;
    TextView mDurationTextView;
    // ListView
    ListView mEventNamesListView;
    // Buttons
    Button mAddDependencyButton;
    Button mRemoveDependenciesModeButton;
    Button mEditDurationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_event);

        Intent intent = getIntent();
        String newProjectId = intent.getStringExtra("projectId");
        String newEventId = intent.getStringExtra("eventId");

        if (newProjectId != null) projectId = newProjectId;
        if (newEventId != null) eventId = newEventId;

        projectNameTextView = (TextView) findViewById(R.id.textview_event_name);
        mDurationTextView = (TextView) findViewById(R.id.textview_duration);
        mEventNamesListView = (ListView) findViewById(R.id.list_dependencies);
        mAddDependencyButton = (Button) findViewById(R.id.button_add_dependency);
        mRemoveDependenciesModeButton = (Button) findViewById(R.id.button_remove_dependency);
        mEditDurationButton = (Button) findViewById(R.id.button_duration);

        mDatabaseManager = new DatabaseManager();
        final DatabaseReference eventRef = mDatabaseManager.fetchEvent(projectId, eventId);
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectNameTextView.setText(Utility.toString(dataSnapshot.child("eventName").getValue()));
                eventRef.child("duration").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            mDurationTextView.setText(
                                    getString(R.string.duration_fmt, Utility.toString(dataSnapshot.getValue()))
                            );
                        } catch (NullPointerException e) {
                            Log.d(TAG,"SetDuration:onDataChange", e);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG,"SetDuration:onCancelled", databaseError.toException());
                    }
                });
                eventRef.child("dependencies").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> eventIds = Converters.convertSnapshotKeysToList(dataSnapshot);
                        mEventNamesListView.setAdapter(Converters.convertSnapshotValuesToListAdapter(
                                ManageEventActivity.this, dataSnapshot
                        ));
                        mEventNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    if (removeDependenciesMode) {
                                        mDatabaseManager.removeDependencyFromEvent(
                                            projectId,
                                            eventId,
                                            eventIds.get(position),
                                            parent.getItemAtPosition(position).toString()
                                        );
                                        Toast.makeText(ManageEventActivity.this, "Removed dependency", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "onClickDependency", e);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRemoveDependenciesModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDependenciesMode = !removeDependenciesMode;
                if (removeDependenciesMode) {
                    mEventNamesListView.setBackgroundResource(R.drawable.border);
                    mRemoveDependenciesModeButton.setBackgroundResource(R.color.colorAccent);
                } else {
                    mEventNamesListView.setBackgroundResource(0);
                    mRemoveDependenciesModeButton.setBackgroundResource(R.color.grey_300);

                }
            }
        });
    }

    public void setDuration(View view) {
        Toast.makeText(this, "Oops can't do that yet!", Toast.LENGTH_SHORT).show();

    }

    public void addDependency(View view) {
        Intent intent = new Intent(this, AddDependencyActivity.class);
        intent.putExtra("projectId", projectId);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }
}
