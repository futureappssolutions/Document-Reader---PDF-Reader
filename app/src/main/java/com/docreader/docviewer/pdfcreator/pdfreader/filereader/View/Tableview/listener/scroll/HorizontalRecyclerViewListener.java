package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll;

import android.util.Log;
import android.view.MotionEvent;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;

public class HorizontalRecyclerViewListener extends RecyclerView.OnScrollListener implements RecyclerView.OnItemTouchListener {
    private static final String LOG_TAG = "HorizontalRecyclerViewListener";
    private RecyclerView.LayoutManager mCellLayoutManager;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private RecyclerView mCurrentRVTouched = null;
    private boolean mIsMoved;
    private RecyclerView mLastTouchedRecyclerView;
    private int mScrollPosition;
    private int mScrollPositionOffset = 0;
    private VerticalRecyclerViewListener mVerticalRecyclerViewListener;
    private int mXPosition;

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public HorizontalRecyclerViewListener(ITableView iTableView) {
        this.mColumnHeaderRecyclerView = iTableView.getColumnHeaderRecyclerView();
        this.mCellLayoutManager = iTableView.getCellRecyclerView().getLayoutManager();
        this.mVerticalRecyclerViewListener = iTableView.getVerticalRecyclerViewListener();
    }

    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        RecyclerView recyclerView2 = this.mCurrentRVTouched;
        if (recyclerView2 != null && recyclerView != recyclerView2) {
            return true;
        }
        if (motionEvent.getAction() == 0) {
            this.mCurrentRVTouched = recyclerView;
            if (recyclerView.getScrollState() == 0) {
                RecyclerView recyclerView3 = this.mLastTouchedRecyclerView;
                if (!(recyclerView3 == null || recyclerView == recyclerView3)) {
                    CellRecyclerView cellRecyclerView = this.mColumnHeaderRecyclerView;
                    if (recyclerView3 == cellRecyclerView) {
                        cellRecyclerView.removeOnScrollListener(this);
                        this.mColumnHeaderRecyclerView.stopScroll();
                        Log.d(LOG_TAG, "Scroll listener  has been removed to mColumnHeaderRecyclerView at last touch control");
                    } else {
                        int index = getIndex(recyclerView3);
                        if (index >= 0 && index < this.mCellLayoutManager.getChildCount() && !((CellRecyclerView) this.mLastTouchedRecyclerView).isHorizontalScrollListenerRemoved()) {
                            ((RecyclerView) this.mCellLayoutManager.getChildAt(index)).removeOnScrollListener(this);
                            String str = LOG_TAG;
                            Log.d(str, "Scroll listener  has been removed to " + this.mLastTouchedRecyclerView.getId() + " CellRecyclerView at last touch control");
                            ((RecyclerView) this.mCellLayoutManager.getChildAt(index)).stopScroll();
                        }
                    }
                }
                this.mXPosition = ((CellRecyclerView) recyclerView).getScrolledX();
                recyclerView.addOnScrollListener(this);
                String str2 = LOG_TAG;
                Log.d(str2, "Scroll listener  has been added to " + recyclerView.getId() + " at action down");
            }
        } else if (motionEvent.getAction() == 2) {
            this.mCurrentRVTouched = recyclerView;
            this.mIsMoved = true;
        } else if (motionEvent.getAction() == 1) {
            this.mCurrentRVTouched = null;
            if (this.mXPosition == ((CellRecyclerView) recyclerView).getScrolledX() && !this.mIsMoved) {
                recyclerView.removeOnScrollListener(this);
                String str3 = LOG_TAG;
                Log.d(str3, "Scroll listener  has been removed to " + recyclerView.getId() + " at action up");
            }
            this.mLastTouchedRecyclerView = recyclerView;
        } else if (motionEvent.getAction() == 3) {
            renewScrollPosition(recyclerView);
            recyclerView.removeOnScrollListener(this);
            String str4 = LOG_TAG;
            Log.d(str4, "Scroll listener  has been removed to " + recyclerView.getId() + " at action cancel");
            this.mIsMoved = false;
            this.mLastTouchedRecyclerView = recyclerView;
            this.mCurrentRVTouched = null;
        }
        return false;
    }

    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        if (recyclerView == this.mColumnHeaderRecyclerView) {
            super.onScrolled(recyclerView, i, i2);
            for (int i3 = 0; i3 < this.mCellLayoutManager.getChildCount(); i3++) {
                ((CellRecyclerView) this.mCellLayoutManager.getChildAt(i3)).scrollBy(i, 0);
            }
            return;
        }
        super.onScrolled(recyclerView, i, i2);
        for (int i4 = 0; i4 < this.mCellLayoutManager.getChildCount(); i4++) {
            CellRecyclerView cellRecyclerView = (CellRecyclerView) this.mCellLayoutManager.getChildAt(i4);
            if (cellRecyclerView != recyclerView) {
                cellRecyclerView.scrollBy(i, 0);
            }
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        super.onScrollStateChanged(recyclerView, i);
        if (i == 0) {
            renewScrollPosition(recyclerView);
            recyclerView.removeOnScrollListener(this);
            String str = LOG_TAG;
            Log.d(str, "Scroll listener has been removed to " + recyclerView.getId() + " at onScrollStateChanged");
            boolean z = false;
            this.mIsMoved = false;
            if (this.mLastTouchedRecyclerView != this.mColumnHeaderRecyclerView) {
                z = true;
            }
            this.mVerticalRecyclerViewListener.removeLastTouchedRecyclerViewScrollListener(z);
        }
    }

    private int getIndex(RecyclerView recyclerView) {
        for (int i = 0; i < this.mCellLayoutManager.getChildCount(); i++) {
            if (this.mCellLayoutManager.getChildAt(i) == recyclerView) {
                return i;
            }
        }
        return -1;
    }

    private void renewScrollPosition(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        this.mScrollPosition = findFirstCompletelyVisibleItemPosition;
        if (findFirstCompletelyVisibleItemPosition == -1) {
            int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            this.mScrollPosition = findFirstVisibleItemPosition;
            if (findFirstVisibleItemPosition != linearLayoutManager.findLastVisibleItemPosition()) {
                this.mScrollPosition++;
            }
        }
        this.mScrollPositionOffset = linearLayoutManager.findViewByPosition(this.mScrollPosition).getLeft();
    }

    public int getScrollPosition() {
        return this.mScrollPosition;
    }

    public int getScrollPositionOffset() {
        return this.mScrollPositionOffset;
    }

    public void setScrollPositionOffset(int i) {
        this.mScrollPositionOffset = i;
    }

    public void setScrollPosition(int i) {
        this.mScrollPosition = i;
    }
}
