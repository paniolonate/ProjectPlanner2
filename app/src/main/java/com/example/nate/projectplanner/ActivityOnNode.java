package com.example.nate.projectplanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ActivityOnNode extends View {

    public float duration;


    public ActivityOnNode(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect myNode = new Rect();
        myNode.set(0, 0, canvas.getWidth()/4, canvas.getHeight()/6);

        Paint blue = new Paint();
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.FILL);

        canvas.drawRect(myNode, blue);
    }


}

