package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.VerticalRecyclerViewListener;

public class CellRecyclerView extends RecyclerView {
    private static final String LOG_TAG = "CellRecyclerView";
    private boolean mIsHorizontalScrollListenerRemoved = true;
    private boolean mIsVerticalScrollListenerRemoved = true;
    private int mScrolledX = 0;
    private int mScrolledY = 0;

    public CellRecyclerView(Context context) {
        super(context);
        setHasFixedSize(false);
        setNestedScrollingEnabled(false);
        setItemViewCacheSize(context.getResources().getInteger(R.integer.default_item_cache_size));
        setDrawingCacheEnabled(true);
        setDrawingCacheQuality(1048576);
    }

    public void onScrolled(int i, int i2) {
        this.mScrolledX += i;
        this.mScrolledY += i2;
        super.onScrolled(i, i2);
    }

    public int getScrolledX() {
        return this.mScrolledX;
    }

    public void clearScrolledX() {
        this.mScrolledX = 0;
    }

    public int getScrolledY() {
        return this.mScrolledY;
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        if (onScrollListener instanceof HorizontalRecyclerViewListener) {
            if (this.mIsHorizontalScrollListenerRemoved) {
                this.mIsHorizontalScrollListenerRemoved = false;
                super.addOnScrollListener(onScrollListener);
                return;
            }
            Log.w(LOG_TAG, "mIsHorizontalScrollListenerRemoved has been tried to add itself before remove the old one");
        } else if (!(onScrollListener instanceof VerticalRecyclerViewListener)) {
            super.addOnScrollListener(onScrollListener);
        } else if (this.mIsVerticalScrollListenerRemoved) {
            this.mIsVerticalScrollListenerRemoved = false;
            super.addOnScrollListener(onScrollListener);
        } else {
            Log.w(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to add itself before remove the old one");
        }
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        if (onScrollListener instanceof HorizontalRecyclerViewListener) {
            if (this.mIsHorizontalScrollListenerRemoved) {
                Log.e(LOG_TAG, "HorizontalRecyclerViewListener has been tried to remove itself before add new one");
                return;
            }
            this.mIsHorizontalScrollListenerRemoved = true;
            super.removeOnScrollListener(onScrollListener);
        } else if (!(onScrollListener instanceof VerticalRecyclerViewListener)) {
            super.removeOnScrollListener(onScrollListener);
        } else if (this.mIsVerticalScrollListenerRemoved) {
            Log.e(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to remove itself before add new one");
        } else {
            this.mIsVerticalScrollListenerRemoved = true;
            super.removeOnScrollListener(onScrollListener);
        }
    }

    public boolean isHorizontalScrollListenerRemoved() {
        return this.mIsHorizontalScrollListenerRemoved;
    }

    public boolean isScrollOthers() {
        return !this.mIsHorizontalScrollListenerRemoved;
    }

    public boolean fling(int i, int i2) {
        return super.fling(i, i2);
    }
}
