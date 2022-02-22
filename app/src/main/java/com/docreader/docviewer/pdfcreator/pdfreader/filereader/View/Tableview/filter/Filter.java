package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter;

import androidx.annotation.NonNull;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Filter {
    private List<FilterItem> filterItems = new ArrayList();
    private ITableView tableView;

    public Filter(ITableView iTableView) {
        this.tableView = iTableView;
    }

    public void set(String str) {
        set(-1, str);
    }

    public void set(int i, String str) {
        FilterItem filterItem = new FilterItem(i == -1 ? FilterType.ALL : FilterType.COLUMN, i, str);
        if (isAlreadyFiltering(i, filterItem)) {
            if (str.isEmpty()) {
                remove(i, filterItem);
            } else {
                update(i, filterItem);
            }
        } else if (!str.isEmpty()) {
            add(filterItem);
        }
    }

    private void add(FilterItem filterItem) {
        this.filterItems.add(filterItem);
        this.tableView.filter(this);
    }

    private void remove(int i, FilterItem filterItem) {
        Iterator<FilterItem> it = this.filterItems.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            FilterItem next = it.next();
            if (i != -1 || !next.getFilterType().equals(filterItem.getFilterType())) {
                if (next.getColumn() == filterItem.getColumn()) {
                    it.remove();
                    break;
                }
            } else {
                it.remove();
                break;
            }
        }
        this.tableView.filter(this);
    }

    private void update(int i, FilterItem filterItem) {
        Iterator<FilterItem> it = this.filterItems.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            FilterItem next = it.next();
            if (i != -1 || !next.getFilterType().equals(filterItem.getFilterType())) {
                if (next.getColumn() == filterItem.getColumn()) {
                    List<FilterItem> list = this.filterItems;
                    list.set(list.indexOf(next), filterItem);
                    break;
                }
            } else {
                List<FilterItem> list2 = this.filterItems;
                list2.set(list2.indexOf(next), filterItem);
                break;
            }
        }
        this.tableView.filter(this);
    }

    private boolean isAlreadyFiltering(int column, @NonNull FilterItem filterItem) {
        for (FilterItem item : filterItems) {
            if (column == -1 && item.getFilterType().equals(filterItem.getFilterType())) {
                return true;
            } else if (item.getColumn() == filterItem.getColumn()) {
                return true;
            }
        }
        return false;
    }

    public List<FilterItem> getFilterItems() {
        return this.filterItems;
    }
}
