package com.example.nate.projectplanner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;


/**
 * Class that draws a rectangle
 */

public class DrawEventNode extends View {

    public DrawEventNode(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect myRect = new Rect();
        myRect.set(0, 0, canvas.getWidth()/4, canvas.getHeight()/6);

        Paint blue = new Paint();
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.FILL);

        canvas.drawRect(myRect, blue);
    }

}
