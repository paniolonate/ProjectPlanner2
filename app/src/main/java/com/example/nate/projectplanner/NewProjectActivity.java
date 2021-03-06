package com.example.nate.projectplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.nate.projectplanner.database.User;

public class NewProjectActivity extends BaseActivity {

    private static final String TAG = "NewProjectActivity";
    private static final String REQUIRED = "Required";

    // Authentication Handler
    FirebaseAuth mAuth;

    // DatabaseManager
    DatabaseManager mDatabaseManager;

    // Buttons
    Button mCreateNewProjectButton;

    // EditText
    EditText mNewProjectNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        mDatabaseManager = new DatabaseManager();
        mAuth = FirebaseAuth.getInstance();

        mCreateNewProjectButton = (Button) findViewById(R.id.button_create_new_project);
        mNewProjectNameEditText = (EditText) findViewById(R.id.edittext_new_project_name);
    }

    public void createNewProject(View view) {
        final String projectName = mNewProjectNameEditText.getText().toString();
        // project name entry cannot be empty
        if (TextUtils.isEmpty(projectName)) {
            mNewProjectNameEditText.setError(REQUIRED);
            return;
        }

        mNewProjectNameEditText.setEnabled(false);
        Toast.makeText(this, "Creating new project...", Toast.LENGTH_SHORT).show();

        final String userId = mAuth.getCurrentUser().getUid(); // TODO catch exception
        mDatabaseManager.fetchUser(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        if (user == null) {
                            // User is null, show error
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewProjectActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Create new project under the user
                            String projectId = mDatabaseManager.addNewProject(userId, projectName);
                            mNewProjectNameEditText.setEnabled(true); // TODO is this necessary?
                            // Start ManageProjectActivity
                            manageNewProject(projectId);
                        }

                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "fetchUser:onCancelled", databaseError.toException());
                        mNewProjectNameEditText.setEnabled(true);
                    }
                });
    }

    private void manageNewProject(String projectId) {
        Intent intent = new Intent(this, ManageProjectActivity.class);
        intent.putExtra("ProjectId", projectId);
        startActivity(intent);
    }
}
