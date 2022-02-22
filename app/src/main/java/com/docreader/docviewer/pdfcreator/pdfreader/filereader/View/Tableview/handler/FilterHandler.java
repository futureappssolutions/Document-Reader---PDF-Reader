package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.AdapterDataSetChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.RowHeaderRecyclerViewAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.Filter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.FilterChangedListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.FilterItem;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.FilterType;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.IFilterableModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilterHandler<T extends IFilterableModel> {
    private AdapterDataSetChangedListener adapterDataSetChangedListener = new AdapterDataSetChangedListener() {
        public void onRowHeaderItemsChanged(List list) {
            List unused = FilterHandler.this.originalRowDataStore = new ArrayList(list);
        }

        public void onCellItemsChanged(List list) {
            List unused = FilterHandler.this.originalCellDataStore = new ArrayList(list);
        }
    };
    private List<FilterChangedListener<T>> filterChangedListeners;
    private CellRecyclerViewAdapter<List<T>> mCellRecyclerViewAdapter;
    private RowHeaderRecyclerViewAdapter<T> mRowHeaderRecyclerViewAdapter;
    
    public List<List<T>> originalCellDataStore;
    
    public List<T> originalRowDataStore;

    public FilterHandler(ITableView iTableView) {
        iTableView.getAdapter().addAdapterDataSetChangedListener(this.adapterDataSetChangedListener);
        this.mCellRecyclerViewAdapter = (CellRecyclerViewAdapter) iTableView.getCellRecyclerView().getAdapter();
        this.mRowHeaderRecyclerViewAdapter = (RowHeaderRecyclerViewAdapter) iTableView.getRowHeaderRecyclerView().getAdapter();
    }

    public void filter(Filter filter) {
        if (this.originalCellDataStore != null && this.originalRowDataStore != null) {
            ArrayList<List> arrayList = new ArrayList<>(this.originalCellDataStore);
            ArrayList arrayList2 = new ArrayList(this.originalRowDataStore);
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList();
            if (filter.getFilterItems().isEmpty()) {
                arrayList3 = new ArrayList(this.originalCellDataStore);
                arrayList4 = new ArrayList(this.originalRowDataStore);
                dispatchFilterClearedToListeners(this.originalCellDataStore, this.originalRowDataStore);
            } else {
                int i = 0;
                while (i < filter.getFilterItems().size()) {
                    FilterItem filterItem = filter.getFilterItems().get(i);
                    if (filterItem.getFilterType().equals(FilterType.ALL)) {
                        for (List list : arrayList) {
                            Iterator it = list.iterator();
                            while (true) {
                                if (it.hasNext()) {
                                    if (((IFilterableModel) it.next()).getFilterableKeyword().toLowerCase().contains(filterItem.getFilter().toLowerCase())) {
                                        arrayList3.add(list);
                                        arrayList4.add((IFilterableModel) arrayList2.get(arrayList3.indexOf(list)));
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                    } else {
                        for (List list2 : arrayList) {
                            if (((IFilterableModel) list2.get(filterItem.getColumn())).getFilterableKeyword().toLowerCase().contains(filterItem.getFilter().toLowerCase())) {
                                arrayList3.add(list2);
                                arrayList4.add((IFilterableModel) arrayList2.get(arrayList3.indexOf(list2)));
                            }
                        }
                    }
                    i++;
                    if (i < filter.getFilterItems().size()) {
                        arrayList = new ArrayList<>(arrayList3);
                        arrayList2 = new ArrayList(arrayList4);
                        arrayList3.clear();
                        arrayList4.clear();
                    }
                }
            }
            this.mRowHeaderRecyclerViewAdapter.setItems(arrayList4, true);
            this.mCellRecyclerViewAdapter.setItems(arrayList3, true);
            dispatchFilterChangedToListeners(arrayList3, arrayList4);
        }
    }

    private void dispatchFilterChangedToListeners(List<List<T>> list, List<T> list2) {
        List<FilterChangedListener<T>> list3 = this.filterChangedListeners;
        if (list3 != null) {
            for (FilterChangedListener<T> onFilterChanged : list3) {
                onFilterChanged.onFilterChanged(list, list2);
            }
        }
    }

    private void dispatchFilterClearedToListeners(List<List<T>> list, List<T> list2) {
        List<FilterChangedListener<T>> list3 = this.filterChangedListeners;
        if (list3 != null) {
            for (FilterChangedListener<T> onFilterCleared : list3) {
                onFilterCleared.onFilterCleared(list, list2);
            }
        }
    }

    public void addFilterChangedListener(FilterChangedListener<T> filterChangedListener) {
        if (this.filterChangedListeners == null) {
            this.filterChangedListeners = new ArrayList();
        }
        this.filterChangedListeners.add(filterChangedListener);
    }
}
