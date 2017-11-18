package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.example.nate.projectplanner.tools.Converters;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListProjectsActivity extends BaseActivity {

    private static final String TAG = "ListProjectsActivity";

    private FirebaseAuth mAuth;
    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_projects);

        mDatabaseManager = new DatabaseManager();
        mAuth = FirebaseAuth.getInstance();

        String userId = mAuth.getCurrentUser().getUid(); // TODO catch exception
        Query projectsQuery = mDatabaseManager.fetchUserProjects(userId).orderByChild("projectName");
        projectsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListView projectNamesListView = (ListView) findViewById(R.id.list_projects);
                projectNamesListView.setAdapter(Converters.convertSnapshotChildValuesToListAdapter(
                        ListProjectsActivity.this, dataSnapshot, "projectName"
                ));
                final List<String> projectIdsList = Converters.convertSnapshotKeysToList(dataSnapshot);

                projectNamesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    projectHome(projectIdsList.get(position));
                                } catch (Exception e) {
                                    Log.d(TAG, "projectHome:failed", e);
                                }
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
                Log.w(TAG, "loadProjects:onCancelled", databaseError.toException());
            }
        });
    }

    private void projectHome(String projectId) {
        Intent intent = new Intent(this, ProjectHomeActivity.class);
        intent.putExtra("ProjectId", projectId);
        startActivity(intent);
    }
}
