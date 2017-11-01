package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.database.Project;
import com.example.nate.projectplanner.database.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class NewEventActivity extends BaseActivity {

    private static final String TAG = "NewEventActivity";
    private static final String REQUIRED = "Required";

    // Authentication Handler
    FirebaseAuth mAuth;

    // DatabaseManager
    DatabaseManager mDatabaseManager;

    // Buttons
    Button mCreateNewEventButton;

    // EditText
    EditText mNewEventNameEditText;

    // Parent ProjectId
    String parentProjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        parentProjectId = getIntent().getStringExtra("projectId");

        mDatabaseManager = new DatabaseManager();
        mAuth = FirebaseAuth.getInstance();

        mCreateNewEventButton = (Button) findViewById(R.id.button_create_new_event);
        mNewEventNameEditText = (EditText) findViewById(R.id.edittext_new_event_name);
    }

    public void createNewEvent(View view) {
        final String eventName = mNewEventNameEditText.getText().toString();
        // Event name entry cannot be empty
        if (TextUtils.isEmpty(eventName)) {
            mNewEventNameEditText.setError(REQUIRED);
            return;
        }

        mNewEventNameEditText.setEnabled(false);

        mDatabaseManager.fetchProject(parentProjectId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Project project = dataSnapshot.getValue(Project.class);

                        if (project == null) {
                            // Project is null, show error
                            Log.e(TAG, "Project " + parentProjectId + " is unexpectedly null");
                            Toast.makeText(NewEventActivity.this,
                                    "Error: could not fetch project.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Create new event
                            String eventId = mDatabaseManager.addNewEvent(parentProjectId, eventName);
                            mNewEventNameEditText.setEnabled(true); // TODO is this necessary?
                            // Start ManageEventActivity
                            manageNewEvent(eventId);
                        }

                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "fetchProject:onCancelled", databaseError.toException());
                        mNewEventNameEditText.setEnabled(true);
                    }
                }
        );
    }

    private void manageNewEvent(String eventId) {
        Intent intent = new Intent(this, ManageEventActivity.class);
        intent.putExtra("projectId", parentProjectId);
        intent.putExtra("eventId", eventId);
        startActivity(intent);
    }
}
