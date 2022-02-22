package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.util.TableViewUtils;

public class ColumnLayoutManager extends LinearLayoutManager {
    private static final String LOG_TAG = "ColumnLayoutManager";
    private CellLayoutManager mCellLayoutManager;
    private CellRecyclerView mCellRowRecyclerView;
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    private CellRecyclerView mColumnHeaderRecyclerView;
    private int mLastDx = 0;
    private boolean mNeedFitForHorizontalScroll;
    private boolean mNeedFitForVerticalScroll;
    private ITableView mTableView;
    private int mYPosition;

    public ColumnLayoutManager(Context context, ITableView iTableView) {
        super(context);
        this.mTableView = iTableView;
        this.mColumnHeaderRecyclerView = iTableView.getColumnHeaderRecyclerView();
        this.mColumnHeaderLayoutManager = this.mTableView.getColumnHeaderLayoutManager();
        this.mCellLayoutManager = this.mTableView.getCellLayoutManager();
        setOrientation(0);
        setRecycleChildrenOnDetach(true);
    }

    public void onAttachedToWindow(RecyclerView recyclerView) {
        super.onAttachedToWindow(recyclerView);
        this.mCellRowRecyclerView = (CellRecyclerView) recyclerView;
        this.mYPosition = getRowPosition();
    }

    public void measureChildWithMargins(View view, int i, int i2) {
        super.measureChildWithMargins(view, i, i2);
        if (!this.mTableView.hasFixedWidth()) {
            measureChild(view, i, i2);
        }
    }

    public void measureChild(View view, int i, int i2) {
        int position = getPosition(view);
        int cacheWidth = this.mCellLayoutManager.getCacheWidth(this.mYPosition, position);
        int cacheWidth2 = this.mColumnHeaderLayoutManager.getCacheWidth(position);
        if (cacheWidth == -1 || cacheWidth != cacheWidth2) {
            View findViewByPosition = this.mColumnHeaderLayoutManager.findViewByPosition(position);
            if (findViewByPosition != null) {
                fitWidthSize(view, this.mYPosition, position, cacheWidth, cacheWidth2, findViewByPosition);
            } else {
                return;
            }
        } else if (view.getMeasuredWidth() != cacheWidth) {
            TableViewUtils.setWidth(view, cacheWidth);
        }
        if (shouldFitColumns(position, this.mYPosition)) {
            if (this.mLastDx < 0) {
                String str = LOG_TAG;
                Log.e(str, "x: " + position + " y: " + this.mYPosition + " fitWidthSize left side ");
                this.mCellLayoutManager.fitWidthSize(position, true);
            } else {
                this.mCellLayoutManager.fitWidthSize(position, false);
                String str2 = LOG_TAG;
                Log.e(str2, "x: " + position + " y: " + this.mYPosition + " fitWidthSize right side");
            }
            this.mNeedFitForVerticalScroll = false;
        }
        this.mNeedFitForHorizontalScroll = false;
    }

    private void fitWidthSize(View view, int i, int i2, int i3, int i4, View view2) {
        if (i3 == -1) {
            i3 = view.getMeasuredWidth();
        }
        if (i4 == -1) {
            i4 = view2.getMeasuredWidth();
        }
        if (i3 != 0) {
            if (i4 > i3) {
                i3 = i4;
            } else if (i3 > i4) {
                i4 = i3;
            } else {
                int i5 = i4;
                i4 = i3;
                i3 = i5;
            }
            if (i3 != view2.getWidth()) {
                TableViewUtils.setWidth(view2, i3);
                this.mNeedFitForVerticalScroll = true;
                this.mNeedFitForHorizontalScroll = true;
            }
            this.mColumnHeaderLayoutManager.setCacheWidth(i2, i3);
            i3 = i4;
        }
        TableViewUtils.setWidth(view, i3);
        this.mCellLayoutManager.setCacheWidth(i, i2, i3);
    }

    private boolean shouldFitColumns(int i, int i2) {
        if (!this.mNeedFitForHorizontalScroll || this.mCellRowRecyclerView.isScrollOthers() || !this.mCellLayoutManager.shouldFitColumns(i2)) {
            return false;
        }
        int i3 = this.mLastDx;
        if (i3 > 0) {
            if (i == findLastVisibleItemPosition()) {
                return true;
            }
            return false;
        } else if (i3 >= 0 || i != findFirstVisibleItemPosition()) {
            return false;
        } else {
            return true;
        }
    }

    public int scrollHorizontallyBy(int i, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mColumnHeaderRecyclerView.getScrollState() == 0 && this.mCellRowRecyclerView.isScrollOthers()) {
            this.mColumnHeaderRecyclerView.scrollBy(i, 0);
        }
        this.mLastDx = i;
        setInitialPrefetchItemCount(2);
        return super.scrollHorizontallyBy(i, recycler, state);
    }

    private int getRowPosition() {
        return this.mCellLayoutManager.getPosition(this.mCellRowRecyclerView);
    }

    public int getLastDx() {
        return this.mLastDx;
    }

    public boolean isNeedFit() {
        return this.mNeedFitForVerticalScroll;
    }

    public void clearNeedFit() {
        this.mNeedFitForVerticalScroll = false;
    }

    public AbstractViewHolder[] getVisibleViewHolders() {
        AbstractViewHolder[] abstractViewHolderArr = new AbstractViewHolder[((findLastVisibleItemPosition() - findFirstVisibleItemPosition()) + 1)];
        int i = 0;
        for (int findFirstVisibleItemPosition = findFirstVisibleItemPosition(); findFirstVisibleItemPosition < findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            abstractViewHolderArr[i] = (AbstractViewHolder) this.mCellRowRecyclerView.findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
            i++;
        }
        return abstractViewHolderArr;
    }
}
