package com.example.nate.projectplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends BaseActivity {

    private static final String TAG = "HomeActivity";

    private Button mEmailSignInButton;
    private Button mListProjectsButton;
    private Button mNewProjectButton;

    private LinearLayout mSignedInLayout;
    private LinearLayout mSignedOutLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Layouts
        mSignedOutLayout = (LinearLayout) findViewById(R.id.signed_out_buttons);
        mSignedInLayout = (LinearLayout) findViewById(R.id.signed_in_buttons);

        // Buttons
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mListProjectsButton = (Button) findViewById(R.id.button_list_projects);
        mNewProjectButton = (Button) findViewById(R.id.button_new_project);
        // TODO implement OnClickListener
//        mEmailSignInButton.setOnClickListener(this);
//        mListProjectsButton.setOnClickListener(this);
//        mNewProjectButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Update UI accordingly
        updateUI(currentUser);
    }

    /** Login via email and password */
    public void loginEmailPassword(View view) {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startActivity(intent);
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

    public void updateUI(FirebaseUser user) {
        hideProgressDialog();

        if (user != null) { // signed in
            mSignedInLayout.setVisibility(View.VISIBLE);
            mSignedOutLayout.setVisibility(View.GONE);
//            mEmailSignInButton.setVisibility(View.GONE);
//            mListProjectsButton.setVisibility(View.VISIBLE);
//            mNewProjectButton.setVisibility(View.VISIBLE);

        } else { // signed out
            mSignedInLayout.setVisibility(View.GONE);
            mSignedOutLayout.setVisibility(View.VISIBLE);
//            mEmailSignInButton.setVisibility(View.VISIBLE);
//            mListProjectsButton.setVisibility(View.GONE);
//            mNewProjectButton.setVisibility(View.GONE);
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        updateUI(null);
    }
}
