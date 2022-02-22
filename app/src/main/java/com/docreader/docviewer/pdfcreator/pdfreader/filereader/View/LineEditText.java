package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;

import androidx.appcompat.widget.AppCompatEditText;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public final class LineEditText extends AppCompatEditText {
    private final TypedArray array;
    private final Paint mPaint = new Paint();
    private final Rect mRect = new Rect();

    public LineEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.array = context.obtainStyledAttributes(attributeSet, R.styleable.LineEditText);
    }

    @SuppressLint("ResourceType")
    public void onDraw(Canvas canvas) {
        ViewParent parent = getParent();
        if (parent != null) {
            int height = ((View) parent).getHeight();
            int lineHeight = getLineHeight();
            int i = height / lineHeight;
            Rect rect = this.mRect;
            Paint paint = this.mPaint;
            paint.setStyle(Paint.Style.FILL);
            int id = getId();
            if (id == 0) {
                Paint.Style style = Paint.Style.FILL;
            } else if (id == 1) {
                Paint.Style style2 = Paint.Style.STROKE;
            } else if (id == 2) {
                Paint.Style style3 = Paint.Style.FILL_AND_STROKE;
            }
            this.mPaint.setStrokeWidth(this.array.getFloat(2, 2.0f));
            this.mPaint.setColor(this.array.getInt(0, -16777216));
            int lineBounds = getLineBounds(0, rect);
            if (i >= 0) {
                int i2 = lineBounds;
                int i3 = 0;
                while (true) {
                    int i4 = i3 + 1;
                    float f = (float) (i2 + 1);
                    canvas.drawLine((float) rect.left, f, (float) rect.right, f, paint);
                    i2 += lineHeight;
                    if (i3 == i) {
                        break;
                    }
                    i3 = i4;
                }
            }
            super.onDraw(canvas);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    public final void setStrok(float f) {
        Float mWidth = f;
        requestLayout();
    }

    public final void setColor(int i) {
        Integer mColor = i;
        requestLayout();
    }
}
