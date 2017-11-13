package com.example.nate.projectplanner;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.example.nate.projectplanner.views.CustomView;


public class AONActivity extends BaseActivity {

    private CustomView mCustomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aon);

        mCustomView = (CustomView) findViewById(R.id.customView);

        findViewById(R.id.btn_swap_color).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomView.swapColor();
            }

        });
    }
}
