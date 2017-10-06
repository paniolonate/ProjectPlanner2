package com.example.nate.projectplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListProjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_projects);

        // temporary hardcoded array of project names
        String[] projectNames = {"my_first_project", "hello_world", "another_project"};

        ListAdapter projectNamesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projectNames);
        ListView projectNamesListView = (ListView) findViewById(R.id.projectsList);
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
}
