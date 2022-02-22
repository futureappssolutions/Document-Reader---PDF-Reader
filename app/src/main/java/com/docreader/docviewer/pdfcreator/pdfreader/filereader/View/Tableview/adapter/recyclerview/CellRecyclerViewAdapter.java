package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ScrollHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.SelectionHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick.CellRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CellRecyclerViewAdapter<C> extends AbstractRecyclerViewAdapter<C> {
    private final RecyclerView.RecycledViewPool mRecycledViewPool;
    private int mRecyclerViewId = 0;
    private ITableView mTableView;

    public CellRecyclerViewAdapter(Context context, List<C> list, ITableView iTableView) {
        super(context, list);
        this.mTableView = iTableView;
        this.mRecycledViewPool = new RecyclerView.RecycledViewPool();
    }

    public AbstractViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CellRecyclerView cellRecyclerView = new CellRecyclerView(this.mContext);
        cellRecyclerView.setRecycledViewPool(this.mRecycledViewPool);
        if (this.mTableView.isShowHorizontalSeparators()) {
            cellRecyclerView.addItemDecoration(this.mTableView.getHorizontalItemDecoration());
        }
        cellRecyclerView.setHasFixedSize(this.mTableView.hasFixedWidth());
        cellRecyclerView.addOnItemTouchListener(this.mTableView.getHorizontalRecyclerViewListener());
        if (this.mTableView.isAllowClickInsideCell()) {
            cellRecyclerView.addOnItemTouchListener(new CellRecyclerViewItemClickListener(cellRecyclerView, this.mTableView));
        }
        cellRecyclerView.setLayoutManager(new ColumnLayoutManager(this.mContext, this.mTableView));
        cellRecyclerView.setAdapter(new CellRowRecyclerViewAdapter(this.mContext, this.mTableView));
        cellRecyclerView.setId(this.mRecyclerViewId);
        this.mRecyclerViewId++;
        return new CellRowViewHolder(cellRecyclerView);
    }

    public void onBindViewHolder(AbstractViewHolder abstractViewHolder, int i) {
        CellRowRecyclerViewAdapter cellRowRecyclerViewAdapter = (CellRowRecyclerViewAdapter) ((CellRowViewHolder) abstractViewHolder).recyclerView.getAdapter();
        cellRowRecyclerViewAdapter.setYPosition(i);
        cellRowRecyclerViewAdapter.setItems((List) this.mItemList.get(i));
    }

    public void onViewAttachedToWindow(AbstractViewHolder abstractViewHolder) {
        super.onViewAttachedToWindow(abstractViewHolder);
        CellRowViewHolder cellRowViewHolder = (CellRowViewHolder) abstractViewHolder;
        ScrollHandler scrollHandler = this.mTableView.getScrollHandler();
        ((ColumnLayoutManager) cellRowViewHolder.recyclerView.getLayoutManager()).scrollToPositionWithOffset(scrollHandler.getColumnPosition(), scrollHandler.getColumnPositionOffset());
        SelectionHandler selectionHandler = this.mTableView.getSelectionHandler();
        if (selectionHandler.isAnyColumnSelected()) {
            AbstractViewHolder abstractViewHolder2 = (AbstractViewHolder) cellRowViewHolder.recyclerView.findViewHolderForAdapterPosition(selectionHandler.getSelectedColumnPosition());
            if (abstractViewHolder2 != null) {
                if (!this.mTableView.isIgnoreSelectionColors()) {
                    abstractViewHolder2.setBackgroundColor(this.mTableView.getSelectedColor());
                }
                abstractViewHolder2.setSelected(AbstractViewHolder.SelectionState.SELECTED);
            }
        } else if (selectionHandler.isRowSelected(abstractViewHolder.getAdapterPosition())) {
            selectionHandler.changeSelectionOfRecyclerView(cellRowViewHolder.recyclerView, AbstractViewHolder.SelectionState.SELECTED, this.mTableView.getSelectedColor());
        }
    }

    public void onViewDetachedFromWindow(AbstractViewHolder abstractViewHolder) {
        super.onViewDetachedFromWindow(abstractViewHolder);
        this.mTableView.getSelectionHandler().changeSelectionOfRecyclerView(((CellRowViewHolder) abstractViewHolder).recyclerView, AbstractViewHolder.SelectionState.UNSELECTED, this.mTableView.getUnSelectedColor());
    }

    public void onViewRecycled(AbstractViewHolder abstractViewHolder) {
        super.onViewRecycled(abstractViewHolder);
        ((CellRowViewHolder) abstractViewHolder).recyclerView.clearScrolledX();
    }

    static class CellRowViewHolder extends AbstractViewHolder {
        final CellRecyclerView recyclerView;

        CellRowViewHolder(View view) {
            super(view);
            this.recyclerView = (CellRecyclerView) view;
        }
    }

    public void notifyCellDataSetChanged() {
        RecyclerView.Adapter adapter;
        CellRecyclerView[] visibleCellRowRecyclerViews = this.mTableView.getCellLayoutManager().getVisibleCellRowRecyclerViews();
        if (visibleCellRowRecyclerViews.length > 0) {
            for (CellRecyclerView cellRecyclerView : visibleCellRowRecyclerViews) {
                if (!(cellRecyclerView == null || (adapter = cellRecyclerView.getAdapter()) == null)) {
                    adapter.notifyDataSetChanged();
                }
            }
            return;
        }
        notifyDataSetChanged();
    }

    public List<C> getColumnItems(int i) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.mItemList.size(); i2++) {
            List list = (List) this.mItemList.get(i2);
            if (list.size() > i) {
                arrayList.add(list.get(i));
            }
        }
        return arrayList;
    }

    public void removeColumnItems(int i) {
        AbstractRecyclerViewAdapter abstractRecyclerViewAdapter;
        for (CellRecyclerView cellRecyclerView : this.mTableView.getCellLayoutManager().getVisibleCellRowRecyclerViews()) {
            if (!(cellRecyclerView == null || (abstractRecyclerViewAdapter = (AbstractRecyclerViewAdapter) cellRecyclerView.getAdapter()) == null)) {
                abstractRecyclerViewAdapter.deleteItem(i);
            }
        }
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.mItemList.size(); i2++) {
            ArrayList arrayList2 = new ArrayList((List) this.mItemList.get(i2));
            if (arrayList2.size() > i) {
                arrayList2.remove(i);
            }
            arrayList.add(arrayList2);
        }
        setItems(arrayList, false);
    }

    public void addColumnItems(int i, List<C> list) {
        if (list.size() == this.mItemList.size() && !list.contains((Object) null)) {
            CellLayoutManager cellLayoutManager = this.mTableView.getCellLayoutManager();
            for (int findFirstVisibleItemPosition = cellLayoutManager.findFirstVisibleItemPosition(); findFirstVisibleItemPosition < cellLayoutManager.findLastVisibleItemPosition() + 1; findFirstVisibleItemPosition++) {
                ((AbstractRecyclerViewAdapter) ((RecyclerView) cellLayoutManager.findViewByPosition(findFirstVisibleItemPosition)).getAdapter()).addItem(i, list.get(findFirstVisibleItemPosition));
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.mItemList.size(); i2++) {
                ArrayList arrayList2 = new ArrayList((List) this.mItemList.get(i2));
                if (arrayList2.size() > i) {
                    arrayList2.add(i, list.get(i2));
                }
                arrayList.add(arrayList2);
            }
            setItems(arrayList, false);
        }
    }
}
