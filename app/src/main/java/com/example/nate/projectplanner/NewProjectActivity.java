package com.example.nate.projectplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        Intent intent = getIntent();
    }

    public void createNewProject(View view) {
        Toast.makeText(NewProjectActivity.this, "Unable to create project", Toast.LENGTH_SHORT).show();
    }
}
