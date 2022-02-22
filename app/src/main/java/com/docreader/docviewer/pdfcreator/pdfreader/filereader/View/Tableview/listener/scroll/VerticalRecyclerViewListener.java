package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll;

import android.util.Log;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;

public class VerticalRecyclerViewListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {
    private static final String LOG_TAG = "VerticalRecyclerViewListener";

    /* renamed from: dx */
    private float f1053dx = 0.0f;

    /* renamed from: dy */
    private float f1054dy = 0.0f;
    private CellRecyclerView mCellRecyclerView;
    private RecyclerView mCurrentRVTouched = null;
    private boolean mIsMoved;
    private RecyclerView mLastTouchedRecyclerView;
    private CellRecyclerView mRowHeaderRecyclerView;
    private int mYPosition;

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public VerticalRecyclerViewListener(ITableView iTableView) {
        this.mRowHeaderRecyclerView = iTableView.getRowHeaderRecyclerView();
        this.mCellRecyclerView = iTableView.getCellRecyclerView();
    }

    private boolean verticalDirection(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 2) {
            return true;
        }
        if (this.f1053dx == 0.0f) {
            this.f1053dx = motionEvent.getX();
        }
        if (this.f1054dy == 0.0f) {
            this.f1054dy = motionEvent.getY();
        }
        float abs = Math.abs(this.f1053dx - motionEvent.getX());
        float abs2 = Math.abs(this.f1054dy - motionEvent.getY());
        this.f1053dx = motionEvent.getX();
        this.f1054dy = motionEvent.getY();
        return abs <= abs2;
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        RecyclerView recyclerView2 = this.mCurrentRVTouched;
        if (recyclerView2 != null && recyclerView != recyclerView2) {
            return true;
        }
        if (!verticalDirection(motionEvent)) {
            this.mCurrentRVTouched = null;
            return false;
        }
        if (motionEvent.getAction() == 0) {
            this.mCurrentRVTouched = recyclerView;
            if (recyclerView.getScrollState() == 0) {
                RecyclerView recyclerView3 = this.mLastTouchedRecyclerView;
                if (!(recyclerView3 == null || recyclerView == recyclerView3)) {
                    removeLastTouchedRecyclerViewScrollListener(false);
                }
                this.mYPosition = ((CellRecyclerView) recyclerView).getScrolledY();
                recyclerView.addOnScrollListener(this);
                if (recyclerView == this.mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener added");
                } else if (recyclerView == this.mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener added");
                }
                this.mIsMoved = false;
            }
        } else if (motionEvent.getAction() == 2) {
            this.mCurrentRVTouched = recyclerView;
            this.mIsMoved = true;
        } else if (motionEvent.getAction() == 1) {
            this.mCurrentRVTouched = null;
            if (this.mYPosition == ((CellRecyclerView) recyclerView).getScrolledY() && !this.mIsMoved && recyclerView.getScrollState() == 0) {
                recyclerView.removeOnScrollListener(this);
                if (recyclerView == this.mCellRecyclerView) {
                    Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from up ");
                } else if (recyclerView == this.mRowHeaderRecyclerView) {
                    Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from up");
                }
            }
            this.mLastTouchedRecyclerView = recyclerView;
        }
        return false;
    }

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        if (recyclerView == this.mCellRecyclerView) {
            super.onScrolled(recyclerView, i, i2);
        } else if (recyclerView == this.mRowHeaderRecyclerView) {
            super.onScrolled(recyclerView, i, i2);
            this.mCellRecyclerView.scrollBy(0, i2);
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        super.onScrollStateChanged(recyclerView, i);
        if (i == 0) {
            recyclerView.removeOnScrollListener(this);
            this.mIsMoved = false;
            this.mCurrentRVTouched = null;
            if (recyclerView == this.mCellRecyclerView) {
                Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from onScrollStateChanged");
            } else if (recyclerView == this.mRowHeaderRecyclerView) {
                Log.d(LOG_TAG, "mRowHeaderRecyclerView scroll listener removed from onScrollStateChanged");
            }
        }
    }

    public void removeLastTouchedRecyclerViewScrollListener(boolean z) {
        RecyclerView recyclerView = this.mLastTouchedRecyclerView;
        CellRecyclerView cellRecyclerView = this.mCellRecyclerView;
        if (recyclerView == cellRecyclerView) {
            cellRecyclerView.removeOnScrollListener(this);
            this.mCellRecyclerView.stopScroll();
            Log.d(LOG_TAG, "mCellRecyclerView scroll listener removed from last touched");
            return;
        }
        this.mRowHeaderRecyclerView.removeOnScrollListener(this);
        this.mRowHeaderRecyclerView.stopScroll();
        String str = LOG_TAG;
        Log.d(str, "mRowHeaderRecyclerView scroll listener removed from last touched");
        if (z) {
            this.mCellRecyclerView.removeOnScrollListener(this);
            this.mCellRecyclerView.stopScroll();
            Log.d(str, "mCellRecyclerView scroll listener removed from last touched");
        }
    }
}
