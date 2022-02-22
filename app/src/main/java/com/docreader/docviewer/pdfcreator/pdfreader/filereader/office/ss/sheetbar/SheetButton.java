package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.ss.sheetbar;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

public class SheetButton extends LinearLayout {
    Context context;
    private View left;
    private View right;
    private final int sheetIndex;
    private TextView textView;

    public SheetButton(Context context2, String str, int i) {
        super(context2);
        this.context = context2;
        setOrientation(0);
        this.sheetIndex = i;
        init(context2, str);
    }

    private void init(Context context2, String str) {
        left = new View(context2);
        addView(left);
        textView = new TextView(context2);
        textView.setBackground(ContextCompat.getDrawable(context2, R.drawable.sheet_bg));
        textView.setText(str);
        textView.setTextSize(16.0f);
        textView.setGravity(17);
        textView.setTextColor(Color.parseColor("#444444"));
        textView.setPadding(5, 0, 5, 0);
        addView(textView, new LinearLayout.LayoutParams(Math.max((int) textView.getPaint().measureText(str), 120), -1));
        right = new View(context2);
        right.setBackgroundColor(-16776961);
        addView(right);
    }

    public void changeFocus(boolean z) {
        textView.setBackground(ContextCompat.getDrawable(this.context, z ? R.drawable.sheet_bg_pressed : R.drawable.sheet_bg));
        textView.setTextColor(Color.parseColor(z ? "#217346" : "#444444"));
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void dispose() {
        left = null;
        textView = null;
        right = null;
    }
}
