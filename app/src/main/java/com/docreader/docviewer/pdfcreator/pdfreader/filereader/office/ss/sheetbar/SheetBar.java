package com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.ss.sheetbar;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.EventConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.system.IControl;

import java.util.Vector;

public class SheetBar extends HorizontalScrollView implements View.OnClickListener {
    private IControl control;
    private SheetButton currentSheet;
    private LinearLayout sheetbarFrame;
    private int minimumWidth;
    private int sheetbarHeight;

    public SheetBar(Context context) {
        super(context);
    }

    public SheetBar(Context context, IControl iControl, int i) {
        super(context);
        this.control = iControl;
        setVerticalFadingEdgeEnabled(false);
        setFadingEdgeLength(0);
        if (i == getResources().getDisplayMetrics().widthPixels) {
            this.minimumWidth = -1;
        } else {
            this.minimumWidth = i;
        }
        init();
    }

    public void onConfigurationChanged(Configuration configuration) {
        int i = this.minimumWidth;
        if (i == -1) {
            i = getResources().getDisplayMetrics().widthPixels;
        }
        sheetbarFrame.setMinimumWidth(i);
    }

    private void init() {
        Context context = getContext();
        sheetbarFrame = new LinearLayout(context);
        sheetbarFrame.setGravity(80);

        sheetbarFrame.setBackgroundColor(-7829368);
        sheetbarFrame.setOrientation(0);

        int i = this.minimumWidth;
        if (i == -1) {
            i = getResources().getDisplayMetrics().widthPixels;
        }
        sheetbarFrame.setMinimumWidth(i);
        sheetbarHeight = 100;

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, 100);
        sheetbarFrame.addView(new View(context), layoutParams);
        Vector vector = (Vector) control.getActionValue(EventConstant.SS_GET_ALL_SHEET_NAME, null);
        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(-2, 100);
        int size = vector.size();
        for (int i2 = 0; i2 < size; i2++) {
            SheetButton sheetButton = new SheetButton(context, (String) vector.get(i2), i2);
            if (currentSheet == null) {
                currentSheet = sheetButton;
                sheetButton.changeFocus(true);
            }
            sheetButton.setOnClickListener(this);
            sheetbarFrame.addView(sheetButton, layoutParams2);
            if (i2 < size - 1) {
                sheetbarFrame.addView(new View(context), layoutParams2);
            }
        }
        sheetbarFrame.addView(new View(context), layoutParams);
        addView(sheetbarFrame, new FrameLayout.LayoutParams(-2, this.sheetbarHeight));
    }

    public void onClick(View view) {
        currentSheet.changeFocus(false);
        currentSheet = (SheetButton) view;
        currentSheet.changeFocus(true);
        control.actionEvent(EventConstant.SS_SHOW_SHEET, currentSheet.getSheetIndex());
    }

    public void setFocusSheetButton(int i) {
        if (currentSheet.getSheetIndex() != i) {
            int childCount = sheetbarFrame.getChildCount();
            View view = currentSheet;
            int i2 = 0;
            while (true) {
                if (i2 >= childCount) {
                    break;
                }
                view = sheetbarFrame.getChildAt(i2);
                if (view instanceof SheetButton) {
                    currentSheet = (SheetButton) view;
                    if (currentSheet.getSheetIndex() == i) {
                        currentSheet.changeFocus(false);
                        currentSheet.changeFocus(true);
                        break;
                    }
                }
                i2++;
            }
            int width = control.getActivity().getWindowManager().getDefaultDisplay().getWidth();
            int width2 = sheetbarFrame.getWidth();
            if (width2 > width) {
                int left = view.getLeft();
                int right = left - ((width - (view.getRight() - left)) / 2);
                if (right < 0) {
                    right = 0;
                } else if (right + width > width2) {
                    right = width2 - width;
                }
                scrollTo(right, 0);
            }
        }
    }

    public int getSheetbarHeight() {
        return sheetbarHeight;
    }

    public void dispose() {
        currentSheet = null;
        if (sheetbarFrame != null) {
            int childCount = sheetbarFrame.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = sheetbarFrame.getChildAt(i);
                if (childAt instanceof SheetButton) {
                    ((SheetButton) childAt).dispose();
                }
            }
            sheetbarFrame = null;
        }
    }
}
