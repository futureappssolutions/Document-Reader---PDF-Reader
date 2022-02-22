package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.util.TableViewUtils;

public class ColumnHeaderLayoutManager extends LinearLayoutManager {
    private SparseIntArray mCachedWidthList = new SparseIntArray();
    private ITableView mTableView;

    public ColumnHeaderLayoutManager(Context context, ITableView iTableView) {
        super(context);
        this.mTableView = iTableView;
        setOrientation(0);
    }

    public void measureChildWithMargins(View view, int i, int i2) {
        super.measureChildWithMargins(view, i, i2);
        if (!this.mTableView.hasFixedWidth()) {
            measureChild(view, i, i2);
        }
    }

    public void measureChild(View view, int i, int i2) {
        if (this.mTableView.hasFixedWidth()) {
            super.measureChild(view, i, i2);
            return;
        }
        int cacheWidth = getCacheWidth(getPosition(view));
        if (cacheWidth != -1) {
            TableViewUtils.setWidth(view, cacheWidth);
        } else {
            super.measureChild(view, i, i2);
        }
    }

    public void setCacheWidth(int i, int i2) {
        this.mCachedWidthList.put(i, i2);
    }

    public int getCacheWidth(int i) {
        return this.mCachedWidthList.get(i, -1);
    }

    public int getFirstItemLeft() {
        return findViewByPosition(findFirstVisibleItemPosition()).getLeft();
    }

    public void removeCachedWidth(int i) {
        this.mCachedWidthList.removeAt(i);
    }

    public void clearCachedWidths() {
        this.mCachedWidthList.clear();
    }

    public void customRequestLayout() {
        int firstItemLeft = getFirstItemLeft();
        for (int findFirstVisibleItemPosition = findFirstVisibleItemPosition(); findFirstVisibleItemPosition < findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            int cacheWidth = getCacheWidth(findFirstVisibleItemPosition) + firstItemLeft;
            View findViewByPosition = findViewByPosition(findFirstVisibleItemPosition);
            findViewByPosition.setLeft(firstItemLeft);
            findViewByPosition.setRight(cacheWidth);
            layoutDecoratedWithMargins(findViewByPosition, findViewByPosition.getLeft(), findViewByPosition.getTop(), findViewByPosition.getRight(), findViewByPosition.getBottom());
            firstItemLeft = cacheWidth + 1;
        }
    }

    public AbstractViewHolder[] getVisibleViewHolders() {
        AbstractViewHolder[] abstractViewHolderArr = new AbstractViewHolder[((findLastVisibleItemPosition() - findFirstVisibleItemPosition()) + 1)];
        int i = 0;
        for (int findFirstVisibleItemPosition = findFirstVisibleItemPosition(); findFirstVisibleItemPosition < findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
            abstractViewHolderArr[i] = (AbstractViewHolder) this.mTableView.getColumnHeaderRecyclerView().findViewHolderForAdapterPosition(findFirstVisibleItemPosition);
            i++;
        }
        return abstractViewHolderArr;
    }

    public AbstractViewHolder getViewHolder(int i) {
        return (AbstractViewHolder) this.mTableView.getColumnHeaderRecyclerView().findViewHolderForAdapterPosition(i);
    }
}
