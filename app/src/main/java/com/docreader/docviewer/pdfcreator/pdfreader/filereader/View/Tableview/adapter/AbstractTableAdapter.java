package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.ColumnHeaderRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableAdapter<CH, RH, C> implements ITableAdapter<CH, RH, C> {
    private List<AdapterDataSetChangedListener<CH, RH, C>> dataSetChangedListeners;
    protected List<List<C>> mCellItems;
    private CellRecyclerViewAdapter mCellRecyclerViewAdapter;
    private int mColumnHeaderHeight;
    protected List<CH> mColumnHeaderItems;
    private ColumnHeaderRecyclerViewAdapter<CH> mColumnHeaderRecyclerViewAdapter;
    private View mCornerView;
    protected List<RH> mRowHeaderItems;
    private RowHeaderRecyclerViewAdapter<RH> mRowHeaderRecyclerViewAdapter;
    private int mRowHeaderWidth;
    private ITableView mTableView;

    public void setTableView(ITableView iTableView) {
        this.mTableView = iTableView;
        initialize();
    }

    private void initialize() {
        Context context = this.mTableView.getContext();
        this.mColumnHeaderRecyclerViewAdapter = new ColumnHeaderRecyclerViewAdapter<>(context, this.mColumnHeaderItems, this);
        this.mRowHeaderRecyclerViewAdapter = new RowHeaderRecyclerViewAdapter<>(context, this.mRowHeaderItems, this);
        this.mCellRecyclerViewAdapter = new CellRecyclerViewAdapter(context, this.mCellItems, this.mTableView);
    }

    public void setColumnHeaderItems(List<CH> list) {
        if (list != null) {
            this.mColumnHeaderItems = list;
            this.mTableView.getColumnHeaderLayoutManager().clearCachedWidths();
            this.mColumnHeaderRecyclerViewAdapter.setItems(this.mColumnHeaderItems);
            dispatchColumnHeaderDataSetChangesToListeners(list);
        }
    }

    public void setRowHeaderItems(List<RH> list) {
        if (list != null) {
            this.mRowHeaderItems = list;
            this.mRowHeaderRecyclerViewAdapter.setItems(list);
            dispatchRowHeaderDataSetChangesToListeners(this.mRowHeaderItems);
        }
    }

    public void setCellItems(List<List<C>> list) {
        if (list != null) {
            this.mCellItems = list;
            this.mTableView.getCellLayoutManager().clearCachedWidths();
            this.mCellRecyclerViewAdapter.setItems(this.mCellItems);
            dispatchCellDataSetChangesToListeners(this.mCellItems);
        }
    }

    public void setAllItems(List<CH> list, List<RH> list2, List<List<C>> list3) {
        ITableView iTableView;
        setColumnHeaderItems(list);
        setRowHeaderItems(list2);
        setCellItems(list3);
        if (list != null && !list.isEmpty() && list2 != null && !list2.isEmpty() && list3 != null && !list3.isEmpty() && (iTableView = this.mTableView) != null && this.mCornerView == null) {
            View onCreateCornerView = onCreateCornerView((ViewGroup) iTableView);
            this.mCornerView = onCreateCornerView;
            this.mTableView.addView(onCreateCornerView, new FrameLayout.LayoutParams(this.mRowHeaderWidth, this.mColumnHeaderHeight));
        } else if (this.mCornerView == null) {
        } else {
            if (list2 == null || list2.isEmpty()) {
                this.mCornerView.setVisibility(View.GONE);
            } else {
                this.mCornerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public View getCornerView() {
        return this.mCornerView;
    }

    public ColumnHeaderRecyclerViewAdapter getColumnHeaderRecyclerViewAdapter() {
        return this.mColumnHeaderRecyclerViewAdapter;
    }

    public RowHeaderRecyclerViewAdapter getRowHeaderRecyclerViewAdapter() {
        return this.mRowHeaderRecyclerViewAdapter;
    }

    public CellRecyclerViewAdapter getCellRecyclerViewAdapter() {
        return this.mCellRecyclerViewAdapter;
    }

    public void setRowHeaderWidth(int i) {
        this.mRowHeaderWidth = i;
        View view = this.mCornerView;
        if (view != null) {
            view.getLayoutParams().width = i;
        }
    }

    public void setColumnHeaderHeight(int i) {
        this.mColumnHeaderHeight = i;
    }

    public CH getColumnHeaderItem(int i) {
        List<CH> list = this.mColumnHeaderItems;
        if (list == null || list.isEmpty() || i < 0 || i >= this.mColumnHeaderItems.size()) {
            return null;
        }
        return this.mColumnHeaderItems.get(i);
    }

    public RH getRowHeaderItem(int i) {
        List<RH> list = this.mRowHeaderItems;
        if (list == null || list.isEmpty() || i < 0 || i >= this.mRowHeaderItems.size()) {
            return null;
        }
        return this.mRowHeaderItems.get(i);
    }

    public C getCellItem(int i, int i2) {
        List<List<C>> list = this.mCellItems;
        if (list == null || list.isEmpty() || i < 0 || i2 >= this.mCellItems.size() || this.mCellItems.get(i2) == null || i2 < 0 || i >= this.mCellItems.get(i2).size()) {
            return null;
        }
        return this.mCellItems.get(i2).get(i);
    }

    public List<C> getCellRowItems(int i) {
        return (List) this.mCellRecyclerViewAdapter.getItem(i);
    }

    public void removeRow(int i) {
        this.mCellRecyclerViewAdapter.deleteItem(i);
        this.mRowHeaderRecyclerViewAdapter.deleteItem(i);
    }

    public void removeRow(int i, boolean z) {
        this.mCellRecyclerViewAdapter.deleteItem(i);
        if (z) {
            i = this.mRowHeaderRecyclerViewAdapter.getItemCount() - 1;
            this.mCellRecyclerViewAdapter.notifyDataSetChanged();
        }
        this.mRowHeaderRecyclerViewAdapter.deleteItem(i);
    }

    public void removeRowRange(int i, int i2) {
        this.mCellRecyclerViewAdapter.deleteItemRange(i, i2);
        this.mRowHeaderRecyclerViewAdapter.deleteItemRange(i, i2);
    }

    public void removeRowRange(int i, int i2, boolean z) {
        this.mCellRecyclerViewAdapter.deleteItemRange(i, i2);
        if (z) {
            i = (this.mRowHeaderRecyclerViewAdapter.getItemCount() - 1) - i2;
            this.mCellRecyclerViewAdapter.notifyDataSetChanged();
        }
        this.mRowHeaderRecyclerViewAdapter.deleteItemRange(i, i2);
    }

    public void addRow(int i, RH rh, List<C> list) {
        this.mCellRecyclerViewAdapter.addItem(i, list);
        this.mRowHeaderRecyclerViewAdapter.addItem(i, rh);
    }

    public void addRowRange(int i, List<RH> list, List<List<C>> list2) {
        this.mRowHeaderRecyclerViewAdapter.addItemRange(i, list);
        this.mCellRecyclerViewAdapter.addItemRange(i, list2);
    }

    public void changeRowHeaderItem(int i, RH rh) {
        this.mRowHeaderRecyclerViewAdapter.changeItem(i, rh);
    }

    public void changeRowHeaderItemRange(int i, List<RH> list) {
        this.mRowHeaderRecyclerViewAdapter.changeItemRange(i, list);
    }

    public void changeCellItem(int i, int i2, C c) {
        List list = (List) this.mCellRecyclerViewAdapter.getItem(i2);
        if (list != null && list.size() > i) {
            list.set(i, c);
            this.mCellRecyclerViewAdapter.changeItem(i2, list);
        }
    }

    public void changeColumnHeader(int i, CH ch) {
        this.mColumnHeaderRecyclerViewAdapter.changeItem(i, ch);
    }

    public void changeColumnHeaderRange(int i, List<CH> list) {
        this.mColumnHeaderRecyclerViewAdapter.changeItemRange(i, list);
    }

    public List<C> getCellColumnItems(int i) {
        return this.mCellRecyclerViewAdapter.getColumnItems(i);
    }

    public void removeColumn(int i) {
        this.mColumnHeaderRecyclerViewAdapter.deleteItem(i);
        this.mCellRecyclerViewAdapter.removeColumnItems(i);
    }

    public void addColumn(int i, CH ch, List<C> list) {
        this.mColumnHeaderRecyclerViewAdapter.addItem(i, ch);
        this.mCellRecyclerViewAdapter.addColumnItems(i, list);
    }

    public final void notifyDataSetChanged() {
        this.mColumnHeaderRecyclerViewAdapter.notifyDataSetChanged();
        this.mRowHeaderRecyclerViewAdapter.notifyDataSetChanged();
        this.mCellRecyclerViewAdapter.notifyCellDataSetChanged();
    }

    public ITableView getTableView() {
        return this.mTableView;
    }

    private void dispatchColumnHeaderDataSetChangesToListeners(List<CH> list) {
        List<AdapterDataSetChangedListener<CH, RH, C>> list2 = this.dataSetChangedListeners;
        if (list2 != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> onColumnHeaderItemsChanged : list2) {
                onColumnHeaderItemsChanged.onColumnHeaderItemsChanged(list);
            }
        }
    }

    private void dispatchRowHeaderDataSetChangesToListeners(List<RH> list) {
        List<AdapterDataSetChangedListener<CH, RH, C>> list2 = this.dataSetChangedListeners;
        if (list2 != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> onRowHeaderItemsChanged : list2) {
                onRowHeaderItemsChanged.onRowHeaderItemsChanged(list);
            }
        }
    }

    private void dispatchCellDataSetChangesToListeners(List<List<C>> list) {
        List<AdapterDataSetChangedListener<CH, RH, C>> list2 = this.dataSetChangedListeners;
        if (list2 != null) {
            for (AdapterDataSetChangedListener<CH, RH, C> onCellItemsChanged : list2) {
                onCellItemsChanged.onCellItemsChanged(list);
            }
        }
    }

    public void addAdapterDataSetChangedListener(AdapterDataSetChangedListener<CH, RH, C> adapterDataSetChangedListener) {
        if (this.dataSetChangedListeners == null) {
            this.dataSetChangedListeners = new ArrayList();
        }
        this.dataSetChangedListeners.add(adapterDataSetChangedListener);
    }
}
