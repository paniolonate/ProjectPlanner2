package com.example.nate.projectplanner.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.nate.projectplanner.R;

/**
 * Created by Nathan Nahina on 11/12/2017.
 * Resource: https://www.youtube.com/watch?v=cfwO0Yui43g
 */

public class CustomView extends View {

    private int rectWidth = 200;
    private int rectHeight = 100;
    private int spacing = 50;

    private Rect mRectNode;
    private Rect mRectNode2;
    private Paint mPaintNode;

    private int mNodeColor;
    private int mNodeSize;

    //Default constructors for super class, init() was added
    public CustomView(Context context) {
        super(context);

        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);

    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    private void init(@Nullable AttributeSet set){
        mRectNode = new Rect();
        mRectNode2 = new Rect();
        mPaintNode = new Paint(Paint.ANTI_ALIAS_FLAG); // This flag makes objects less pixelated
        mPaintNode.setColor(Color.GREEN);

        if(set == null) // avoid null pointer exception
            return;

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.CustomView);

        mNodeColor = ta.getColor(R.styleable.CustomView_node_color, Color.GREEN);

        ta.recycle();

    }

    public void swapColor(){
        mPaintNode.setColor(mPaintNode.getColor() == mNodeColor ? Color.RED : mNodeColor); // if green, set to red, otherwise its green

        postInvalidate(); // won't block UI, can use Invalidate() but it runs synchronously
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.LTGRAY);

        mRectNode.left = 400;
        mRectNode.top = 100;
        mRectNode.right = mRectNode.left + rectWidth;
        mRectNode.bottom = mRectNode.top + rectHeight;

        canvas.drawRect(mRectNode, mPaintNode);

        mRectNode2.left = 400;                       //x coordinate for top left of rect
        mRectNode2.top = 100 + spacing + rectHeight; //y coordinate for top left of rect
        mRectNode2.right = mRectNode2.left + rectWidth;  //x for bottom right
        mRectNode2.bottom = mRectNode2.top + rectHeight; // y for bottom right

        canvas.drawRect(mRectNode2, mPaintNode);

    }
}
