package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class CustomIORatingBar extends LinearLayout {
    public LayoutInflater inflater;
    public RatingChangeListener rcListener = null;
    public View.OnClickListener clickListener = null;
    public TextView currentRating = null;
    public TextView five = null;
    public TextView four = null;
    public TextView one = null;
    public TextView three = null;
    public TextView two = null;

    public CustomIORatingBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public CustomIORatingBar(Context context) {
        super(context);
        init();
    }

    public void setTextColor(int i, TextView textView) {
        textView.setTextColor(i);
    }

    public void setBackgroundDrawable(Drawable drawable, TextView textView) {
        textView.setBackground(drawable);
    }

    @SuppressLint("NonConstantResourceId")
    public int getRating() {
        if (currentRating == null) {
            return 0;
        }
        switch (currentRating.getId()) {
            case R.id.five:
                return 5;
            case R.id.four:
                return 4;
            case R.id.one:
                return 1;
            case R.id.three:
                return 3;
            case R.id.two:
                return 2;
            default:
                return 0;
        }
    }

    private void init() {
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.google_rating_bar, this, true);
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);

        @SuppressLint("ResourceType") View.OnClickListener r0 = view -> {
            if (currentRating == null) {
                TextView textView = (TextView) view;
                setTextColor(getResources().getColor(17170443), textView);
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.circle_color_primary), textView);
                currentRating = textView;
                if (rcListener != null) {
                    rcListener.onRatingChanged(getRating());
                    return;
                }
                return;
            }

            setTextColor(getResources().getColor(R.color.app_color), currentRating);
            setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.stroked_circle), currentRating);
            TextView textView2 = (TextView) view;
            setTextColor(getResources().getColor(17170443), textView2);
            setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.circle_color_primary), textView2);
            currentRating = textView2;
            if (rcListener != null) {
                rcListener.onRatingChanged(getRating());
            }
        };
        clickListener = r0;
        one.setOnClickListener(r0);
        two.setOnClickListener(clickListener);
        three.setOnClickListener(clickListener);
        four.setOnClickListener(clickListener);
        five.setOnClickListener(clickListener);
    }

    public void setRatingChangeListener(RatingChangeListener ratingChangeListener) {
        rcListener = ratingChangeListener;
    }

    public interface RatingChangeListener {
        void onRatingChanged(int i);
    }
}
