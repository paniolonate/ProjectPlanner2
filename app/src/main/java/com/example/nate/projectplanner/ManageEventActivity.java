package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ManageEventActivity extends BaseActivity {

    private static final String TAG = "ManageEventActivity";

    DatabaseManager mDatabaseManager;

    static String projectId;
    static String eventId;

    // TextView
    TextView mDurationEditText;
    // Buttons
    Button mAddDependencyButton;
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

        mDurationEditText = (TextView) findViewById(R.id.textview_duration);
        mAddDependencyButton = (Button) findViewById(R.id.button_add_dependency);
        mEditDurationButton = (Button) findViewById(R.id.button_duration);

        mDatabaseManager = new DatabaseManager();
        final DatabaseReference eventRef = mDatabaseManager.fetchEvent(projectId, eventId);
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                eventRef.child("duration").addValueEventListener();
                mDatabaseManager.setTextFromDatabaseReference(mDurationEditText, eventRef, "duration");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        mDurationEditText.setText(eventRef.child("duration").toString());
//        mUrgencyButton.setText(eventRef.child("urgency").toString());


    }

    public void setDuration(View view) {
        Toast.makeText(this, "Oops can't do that yet!", Toast.LENGTH_SHORT).show();
    }

    public void addDependency(View view) {
        Toast.makeText(this, "Oops can't do that yet!", Toast.LENGTH_SHORT).show();
    }
}
