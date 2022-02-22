package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class AtomSystemView extends ViewGroup {
    public static final int ONE_ELECTRON_VELOCITY = 6000;
    public static boolean onLayoutOccur = false;
    public final Context context;
    public float basicRadius;
    public int color;
    public float[] coordinates = new float[2];
    public float elecRadius;
    public String name;
    public boolean isAnimating = false;
    public String layers;
    public View[] views;

    public AtomSystemView(Context context2) {
        super(context2);
        context = context2;
        onLayoutOccur = false;
    }

    public AtomSystemView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        context = context2;
        onLayoutOccur = false;
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!onLayoutOccur) {
            int i5 = 1;
            onLayoutOccur = true;
            String[] split = layers.split(" ");
            views = new View[(split.length + 1)];
            views[0] = new Layer(context, basicRadius * 2.0f, (String) null);
            while (true) {
                if (i5 >= views.length) {
                    break;
                }
                float f = basicRadius;
                views[i5] = new Layer(context, (f * 2.0f) + (((float) i5) * f), split[i5 - 1]);
                i5++;
            }
            for (View addView : views) {
                addView(addView);
            }
            for (int i6 = 0; i6 < getChildCount(); i6++) {
                getChildAt(i6).layout(0, 0, getWidth(), getHeight());
            }
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        float paddingLeft = ((float) i) - ((float) (getPaddingLeft() + getPaddingRight()));
        float paddingTop = ((float) i2) - ((float) (getPaddingTop() + getPaddingBottom()));
        float[] fArr = coordinates;
        fArr[0] = paddingLeft / 2.0f;
        fArr[1] = paddingTop / 2.0f;
        float min = Math.min(paddingLeft, paddingTop) / 2.0f;
        elecRadius = 0.03f * min;
        basicRadius = min * 0.1f;
    }

    public void setData(String str, String str2, int i) {
        layers = str;
        name = str2;
        color = i;
    }

    public void startAnimation() {
        View[] viewArr = views;
        if (viewArr != null) {
            isAnimating = true;
            int length = (viewArr.length - 1) * ONE_ELECTRON_VELOCITY;
            for (int i = 1; i < views.length; i++) {
                float[] fArr = coordinates;
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 0, fArr[0], 0, fArr[1]);
                rotateAnimation.setRepeatCount(-1);
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setDuration(length - ((long) ((views.length - i) - 1) * ONE_ELECTRON_VELOCITY));
                views[i].startAnimation(rotateAnimation);
            }
        }
    }

    public void stopAnimation() {
        if (views != null) {
            isAnimating = false;
            int i = 1;
            while (true) {
                if (i < views.length) {
                    views[i].clearAnimation();
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    public float getPixelsFromDP(Context context2, int i) {
        return TypedValue.applyDimension(1, (float) i, context2.getResources().getDisplayMetrics());
    }

    private class Layer extends View {
        private final Context context;
        private final float radius;
        private Paint corePaint;
        private Paint coreStrokePaint;
        private Paint coreTextPaint;
        private Paint electronPaint;
        private int electrons = -1;
        private Paint orbitalPaint;

        public Layer(Context context2, float f, String str) {
            super(context2);
            radius = f;
            context = context2;
            if (str != null) {
                electrons = Integer.parseInt(str);
            }
            init();
        }

        private float calcRad(int i) {
            Double.isNaN(i);
            Double.isNaN(i);
            return (float) (((double) i * 3.141592653589793d) / 180.0d);
        }

        @SuppressLint("WrongConstant")
        private void init() {
            if (electrons == -1) {
                corePaint = new Paint();
                corePaint.setStyle(Paint.Style.FILL);
                corePaint.setColor(color);
                corePaint.setAntiAlias(true);
                coreStrokePaint = new Paint();
                coreStrokePaint.setStyle(Paint.Style.STROKE);
                coreStrokePaint.setColor(-16777216);
                coreStrokePaint.setAntiAlias(true);
                coreStrokePaint.setStrokeWidth(getPixelsFromDP(context, 1));
                coreTextPaint = new Paint(1);
                coreTextPaint.setColor(-16777216);
                coreTextPaint.setStyle(Paint.Style.FILL);
                coreTextPaint.setAntiAlias(true);
                coreTextPaint.setTextSize(basicRadius * 1.75f);
                coreTextPaint.setTypeface(Typeface.create("sans-serif-light", 1));
                coreTextPaint.setTextAlign(Paint.Align.CENTER);
                return;
            }
            electronPaint = new Paint();
            electronPaint.setStyle(Paint.Style.FILL);
            electronPaint.setColor(Color.parseColor("#77868B"));
            electronPaint.setAntiAlias(true);
            orbitalPaint = new Paint();
            orbitalPaint.setStyle(Paint.Style.STROKE);
            orbitalPaint.setStrokeWidth(getPixelsFromDP(context, 1));
            orbitalPaint.setColor(Color.parseColor("#77868B"));
            orbitalPaint.setAntiAlias(true);
        }

        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (electrons == -1) {
                canvas.drawCircle(coordinates[0], coordinates[1], radius, corePaint);
                canvas.drawCircle(coordinates[0], coordinates[1], radius, coreStrokePaint);
                canvas.drawText(name, coordinates[0], coordinates[1] + (basicRadius / 1.8f), coreTextPaint);
                return;
            }
            canvas.drawCircle(coordinates[0], coordinates[1], radius, orbitalPaint);
            for (int i = 0; i < electrons; i++) {
                float f = coordinates[0];
                double d = radius;
                int i2 = i * 360;
                double cos = Math.cos(calcRad((i2 / electrons) + 90));
                Double.isNaN(d);
                Double.isNaN(d);
                float f2 = f + ((float) (d * cos));
                float f3 = coordinates[1];
                double d2 = radius;
                double sin = Math.sin(calcRad((i2 / electrons) + 90));
                Double.isNaN(d2);
                Double.isNaN(d2);
                canvas.drawCircle(f2, f3 + ((float) (d2 * sin)), elecRadius, electronPaint);
            }
        }
    }
}
