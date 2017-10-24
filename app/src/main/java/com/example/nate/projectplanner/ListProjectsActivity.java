package com.example.nate.projectplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nate.projectplanner.database.DatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class ListProjectsActivity extends BaseActivity {

    private static final String TAG = "ListProjectsActivity";

    private DatabaseManager mDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_projects);

        mDatabaseManager = new DatabaseManager();

        String userId = mDatabaseManager.getCurrentUserId();
        Query projectsQuery = mDatabaseManager.fetchUserProjects(userId).orderByChild("projectName");
        projectsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> projectNamesList = new ArrayList<String>();
                for (DataSnapshot projectSnapShot : dataSnapshot.getChildren()) {
                    projectNamesList.add(projectSnapShot.child("projectName").getValue().toString()); // TODO catch exception
                }
                String[] projectNamesArray = new String[projectNamesList.size()];
                projectNamesArray = projectNamesList.toArray(projectNamesArray);

                ListAdapter projectNamesAdapter = new ArrayAdapter<String>(ListProjectsActivity.this, android.R.layout.simple_list_item_1, projectNamesArray);
                ListView projectNamesListView = (ListView) findViewById(R.id.list_projects);
                projectNamesListView.setAdapter(projectNamesAdapter);

                projectNamesListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String projectName = String.valueOf(parent.getItemAtPosition(position));
                                String message = "Oops " + projectName + " is unavailable";
                                Toast.makeText(ListProjectsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed
                Log.w(TAG, "loadProjects:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
