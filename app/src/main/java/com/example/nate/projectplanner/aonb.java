package com.example.nate.projectplanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import com.example.nate.projectplanner.views.CustomView;

public class aonb extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aonb);

        CustomView customView = new CustomView(this);
    }
}
