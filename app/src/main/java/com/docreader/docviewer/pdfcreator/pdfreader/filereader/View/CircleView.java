package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class CircleView extends View {
    private int color = -16777216;
    private Paint mPaint;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.color = context.obtainStyledAttributes(attributeSet, R.styleable.circleView).getColor(0, -16777216);
        init();
    }

    public CircleView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        this.mPaint = new Paint(1);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        double width = (double) ((getWidth() - getPaddingLeft()) - getPaddingRight());
        Double.isNaN(width);
        double d = width * 0.5d;
        double height = (double) ((getHeight() - getPaddingTop()) - getPaddingBottom());
        Double.isNaN(height);
        double d2 = height * 0.5d;
        double paddingLeft = (double) getPaddingLeft();
        Double.isNaN(paddingLeft);
        double paddingTop = (double) getPaddingTop();
        Double.isNaN(paddingTop);
        this.mPaint.setColor(this.color);
        canvas.drawCircle((float) ((int) (paddingLeft + d)), (float) ((int) (paddingTop + d2)), (float) ((int) Math.min(d, d2)), this.mPaint);
    }
}
