package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnHeaderLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class ColumnSortHelper {
    private static Directive EMPTY_DIRECTIVE = new Directive(-1, SortState.UNSORTED);
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    private List<Directive> mSortingColumns = new ArrayList();

    public ColumnSortHelper(ColumnHeaderLayoutManager columnHeaderLayoutManager) {
        this.mColumnHeaderLayoutManager = columnHeaderLayoutManager;
    }

    private void sortingStatusChanged(int i, SortState sortState) {
        AbstractViewHolder viewHolder = this.mColumnHeaderLayoutManager.getViewHolder(i);
        if (viewHolder == null) {
            return;
        }
        if (viewHolder instanceof AbstractSorterViewHolder) {
            ((AbstractSorterViewHolder) viewHolder).onSortingStatusChanged(sortState);
            return;
        }
        throw new IllegalArgumentException("Column Header ViewHolder must extend AbstractSorterViewHolder");
    }

    public void setSortingStatus(int i, SortState sortState) {
        Directive directive = getDirective(i);
        if (directive != EMPTY_DIRECTIVE) {
            this.mSortingColumns.remove(directive);
        }
        if (sortState != SortState.UNSORTED) {
            this.mSortingColumns.add(new Directive(i, sortState));
        }
        sortingStatusChanged(i, sortState);
    }

    public void clearSortingStatus() {
        this.mSortingColumns.clear();
    }

    public boolean isSorting() {
        return this.mSortingColumns.size() != 0;
    }

    public SortState getSortingStatus(int i) {
        return getDirective(i).direction;
    }

    private Directive getDirective(int i) {
        for (int i2 = 0; i2 < this.mSortingColumns.size(); i2++) {
            Directive directive = this.mSortingColumns.get(i2);
            if (directive.column == i) {
                return directive;
            }
        }
        return EMPTY_DIRECTIVE;
    }

    private static class Directive {
        
        public int column;
        
        public SortState direction;

        Directive(int i, SortState sortState) {
            this.column = i;
            this.direction = sortState;
        }
    }
}
