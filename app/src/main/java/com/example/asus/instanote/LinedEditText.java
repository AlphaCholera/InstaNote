package com.example.asus.instanote;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

public class LinedEditText extends android.support.v7.widget.AppCompatEditText {
    private Paint paint = new Paint();
    int colorOfLines = R.color.darkOrangeColor;
    private static final String TAG = "LinedEditText";
    public LinedEditText(Context context) {
        super(context);
    }

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColorOfLinedEditText(int colorOfLines) {
        this.colorOfLines = colorOfLines;
        paint.setColor(colorOfLines);
        Log.d(TAG, "setColorOfLinedEditText: "+colorOfLines+" "+paint.getColor());
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // The setColor() method takes a color number as int value, but not a resource id which is an int as well.
        int col = ContextCompat.getColor(getContext(), colorOfLines);
        paint.setColor(col);
        paint.setStyle(Paint.Style.STROKE);
        Log.d(TAG, "onDraw: "+colorOfLines+" "+paint.getColor());
        int left = getLeft();
        int right = getRight();
        int paddingTop = getPaddingTop();
//        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
//        int height = getHeight();
        int lineHeight = getLineHeight();
//        int count = (height-paddingTop-paddingBottom) / lineHeight;
        long count = 9999;
        for (long i = 0; i < count; i++) {
            long baseline = lineHeight * (i+1) + paddingTop;
            canvas.drawLine(left+paddingLeft, baseline, right-paddingRight, baseline, paint);
        }

        super.onDraw(canvas);
    }
}
