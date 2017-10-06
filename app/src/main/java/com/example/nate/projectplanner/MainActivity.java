package com.example.nate.projectplanner;

import android.content.Intent;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.nate.projectplanner.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** List existing projects */
    public void listProjects(View view) {
        Intent intent = new Intent(this, ListProjectsActivity.class);
        startActivity(intent);
    }

    /** Create new project */
    public void newProject(View view) {
        Intent intent = new Intent(this, NewProjectActivity.class);
        startActivity(intent);
    }
}
