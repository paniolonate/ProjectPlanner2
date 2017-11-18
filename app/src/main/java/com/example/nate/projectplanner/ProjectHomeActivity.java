package com.example.nate.projectplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.nate.projectplanner.criticalpath.CriticalPath;
import com.example.nate.projectplanner.database.DatabaseManager;

public class ProjectHomeActivity extends BaseActivity {

    private static final String TAG = "ProjectHomeActivity";

    DatabaseManager mDatabaseManager;

    // Current projectId
    static String projectId; // TODO make sure all static projectIds are cleared when User logs out

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_home);

        mDatabaseManager = new DatabaseManager();

        // Load project
        String newProjectId = getIntent().getStringExtra("ProjectId");
        if (newProjectId != null) projectId = newProjectId;
    }

    public void calculateCriticalPath(View view) {
        Toast.makeText(this, "Calculating ... ", Toast.LENGTH_SHORT).show();
        CriticalPath criticalPath = new CriticalPath(projectId);
        criticalPath.calculate();
        Intent intent = new Intent(this, CriticalPathActivity.class);
        intent.putExtra("ProjectId", projectId);
        startActivity(intent);
    }

    public void manageProject(View view) {
        Intent intent = new Intent(this, ManageProjectActivity.class);
        intent.putExtra("ProjectId", projectId);
        startActivity(intent);
    }
}
