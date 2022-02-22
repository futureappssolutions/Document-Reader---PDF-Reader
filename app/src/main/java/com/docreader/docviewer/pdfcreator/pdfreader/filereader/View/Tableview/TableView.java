package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.AbstractTableAdapter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.filter.Filter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ColumnSortHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ColumnWidthHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.FilterHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.PreferencesHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.ScrollHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.SelectionHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.VisibilityHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.CellLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.layoutmanager.ColumnHeaderLayoutManager;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.ITableViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.TableViewLayoutChangeListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick.ColumnHeaderRecyclerViewItemClickListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick.RowHeaderRecyclerViewItemClickListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.scroll.VerticalRecyclerViewListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.preference.SavedState;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.sort.SortState;

public class TableView extends FrameLayout implements ITableView {
    private boolean mAllowClickInsideCell = false;
    private boolean mAllowClickInsideColumnHeader = false;
    private boolean mAllowClickInsideRowHeader = false;
    private CellLayoutManager mCellLayoutManager;
    protected CellRecyclerView mCellRecyclerView;
    private int mColumnHeaderHeight;
    private ColumnHeaderLayoutManager mColumnHeaderLayoutManager;
    protected CellRecyclerView mColumnHeaderRecyclerView;
    private ColumnSortHandler mColumnSortHandler;
    private ColumnWidthHandler mColumnWidthHandler;
    private FilterHandler mFilterHandler;
    private boolean mHasFixedWidth;
    private DividerItemDecoration mHorizontalItemDecoration;
    private HorizontalRecyclerViewListener mHorizontalRecyclerViewListener;
    private boolean mIgnoreSelectionColors;
    private boolean mIsSortable;
    private PreferencesHandler mPreferencesHandler;
    private LinearLayoutManager mRowHeaderLayoutManager;
    protected CellRecyclerView mRowHeaderRecyclerView;
    private int mRowHeaderWidth;
    private ScrollHandler mScrollHandler;
    private int mSelectedColor;
    private SelectionHandler mSelectionHandler;
    private int mSeparatorColor = -1;
    private int mShadowColor;
    private boolean mShowHorizontalSeparators = true;
    private boolean mShowVerticalSeparators = true;
    protected AbstractTableAdapter mTableAdapter;
    private ITableViewListener mTableViewListener;
    private int mUnSelectedColor;
    private DividerItemDecoration mVerticalItemDecoration;
    private VerticalRecyclerViewListener mVerticalRecyclerListener;
    private VisibilityHandler mVisibilityHandler;

    public TableView(Context context) {
        super(context);
        initialDefaultValues((AttributeSet) null);
        initialize();
    }

    public TableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialDefaultValues(attributeSet);
        initialize();
    }

    public TableView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialDefaultValues((AttributeSet) null);
        initialize();
    }

    @SuppressLint("ResourceType")
    private void initialDefaultValues(AttributeSet attributeSet) {
        this.mRowHeaderWidth = (int) getResources().getDimension(R.dimen.default_row_header_width);
        this.mColumnHeaderHeight = (int) getResources().getDimension(R.dimen.default_column_header_height);
        this.mSelectedColor = ContextCompat.getColor(getContext(), R.color.table_view_default_selected_background_color);
        this.mUnSelectedColor = ContextCompat.getColor(getContext(), R.color.white);
        this.mShadowColor = ContextCompat.getColor(getContext(), R.color.table_view_default_shadow_background_color);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.PeriodicTableView, 0, 0);
            try {
                this.mRowHeaderWidth = (int) obtainStyledAttributes.getDimension(R.styleable.PeriodicTableView_row_header_width, (float) this.mRowHeaderWidth);
                this.mColumnHeaderHeight = (int) obtainStyledAttributes.getDimension(R.styleable.PeriodicTableView_column_header_height, (float) this.mColumnHeaderHeight);
                this.mSelectedColor = obtainStyledAttributes.getColor(5, this.mSelectedColor);
                this.mUnSelectedColor = obtainStyledAttributes.getColor(10, this.mUnSelectedColor);
                this.mShadowColor = obtainStyledAttributes.getColor(7, this.mShadowColor);
                this.mSeparatorColor = obtainStyledAttributes.getColor(6, ContextCompat.getColor(getContext(), R.color.table_view_default_separator_color));
                this.mShowVerticalSeparators = obtainStyledAttributes.getBoolean(9, this.mShowVerticalSeparators);
                this.mShowHorizontalSeparators = obtainStyledAttributes.getBoolean(8, this.mShowHorizontalSeparators);
                this.mAllowClickInsideCell = obtainStyledAttributes.getBoolean(0, this.mAllowClickInsideCell);
                this.mAllowClickInsideRowHeader = obtainStyledAttributes.getBoolean(2, this.mAllowClickInsideRowHeader);
                this.mAllowClickInsideColumnHeader = obtainStyledAttributes.getBoolean(1, this.mAllowClickInsideColumnHeader);
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
    }

    private void initialize() {
        this.mColumnHeaderRecyclerView = createColumnHeaderRecyclerView();
        this.mRowHeaderRecyclerView = createRowHeaderRecyclerView();
        this.mCellRecyclerView = createCellRecyclerView();
        addView(this.mColumnHeaderRecyclerView);
        addView(this.mRowHeaderRecyclerView);
        addView(this.mCellRecyclerView);
        this.mSelectionHandler = new SelectionHandler(this);
        this.mVisibilityHandler = new VisibilityHandler(this);
        this.mScrollHandler = new ScrollHandler(this);
        this.mPreferencesHandler = new PreferencesHandler(this);
        this.mColumnWidthHandler = new ColumnWidthHandler(this);
        initializeListeners();
    }


    public void initializeListeners() {
        VerticalRecyclerViewListener verticalRecyclerViewListener = new VerticalRecyclerViewListener(this);
        this.mVerticalRecyclerListener = verticalRecyclerViewListener;
        this.mRowHeaderRecyclerView.addOnItemTouchListener(verticalRecyclerViewListener);
        this.mCellRecyclerView.addOnItemTouchListener(this.mVerticalRecyclerListener);
        HorizontalRecyclerViewListener horizontalRecyclerViewListener = new HorizontalRecyclerViewListener(this);
        this.mHorizontalRecyclerViewListener = horizontalRecyclerViewListener;
        this.mColumnHeaderRecyclerView.addOnItemTouchListener(horizontalRecyclerViewListener);
        if (this.mAllowClickInsideColumnHeader) {
            this.mColumnHeaderRecyclerView.addOnItemTouchListener(new ColumnHeaderRecyclerViewItemClickListener(this.mColumnHeaderRecyclerView, this));
        }
        if (this.mAllowClickInsideRowHeader) {
            this.mRowHeaderRecyclerView.addOnItemTouchListener(new RowHeaderRecyclerViewItemClickListener(this.mRowHeaderRecyclerView, this));
        }
        TableViewLayoutChangeListener tableViewLayoutChangeListener = new TableViewLayoutChangeListener(this);
        this.mColumnHeaderRecyclerView.addOnLayoutChangeListener(tableViewLayoutChangeListener);
        this.mCellRecyclerView.addOnLayoutChangeListener(tableViewLayoutChangeListener);
    }


    public CellRecyclerView createColumnHeaderRecyclerView() {
        CellRecyclerView cellRecyclerView = new CellRecyclerView(getContext());
        cellRecyclerView.setLayoutManager(getColumnHeaderLayoutManager());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, this.mColumnHeaderHeight);
        layoutParams.leftMargin = this.mRowHeaderWidth;
        cellRecyclerView.setLayoutParams(layoutParams);
        if (isShowHorizontalSeparators()) {
            cellRecyclerView.addItemDecoration(getHorizontalItemDecoration());
        }
        return cellRecyclerView;
    }


    public CellRecyclerView createRowHeaderRecyclerView() {
        CellRecyclerView cellRecyclerView = new CellRecyclerView(getContext());
        cellRecyclerView.setLayoutManager(getRowHeaderLayoutManager());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(this.mRowHeaderWidth, -2);
        layoutParams.topMargin = this.mColumnHeaderHeight;
        cellRecyclerView.setLayoutParams(layoutParams);
        if (isShowVerticalSeparators()) {
            cellRecyclerView.addItemDecoration(getVerticalItemDecoration());
        }
        return cellRecyclerView;
    }


    public CellRecyclerView createCellRecyclerView() {
        CellRecyclerView cellRecyclerView = new CellRecyclerView(getContext());
        cellRecyclerView.setMotionEventSplittingEnabled(false);
        cellRecyclerView.setLayoutManager(getCellLayoutManager());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = this.mRowHeaderWidth;
        layoutParams.topMargin = this.mColumnHeaderHeight;
        cellRecyclerView.setLayoutParams(layoutParams);
        if (isShowVerticalSeparators()) {
            cellRecyclerView.addItemDecoration(getVerticalItemDecoration());
        }
        return cellRecyclerView;
    }

    public <CH, RH, C> void setAdapter(AbstractTableAdapter<CH, RH, C> abstractTableAdapter) {
        if (abstractTableAdapter != null) {
            this.mTableAdapter = abstractTableAdapter;
            abstractTableAdapter.setRowHeaderWidth(this.mRowHeaderWidth);
            this.mTableAdapter.setColumnHeaderHeight(this.mColumnHeaderHeight);
            this.mTableAdapter.setTableView(this);
            this.mColumnHeaderRecyclerView.setAdapter(this.mTableAdapter.getColumnHeaderRecyclerViewAdapter());
            this.mRowHeaderRecyclerView.setAdapter(this.mTableAdapter.getRowHeaderRecyclerViewAdapter());
            this.mCellRecyclerView.setAdapter(this.mTableAdapter.getCellRecyclerViewAdapter());
            this.mColumnSortHandler = new ColumnSortHandler(this);
            this.mFilterHandler = new FilterHandler(this);
        }
    }

    public boolean hasFixedWidth() {
        return this.mHasFixedWidth;
    }

    public void setHasFixedWidth(boolean z) {
        this.mHasFixedWidth = z;
        this.mColumnHeaderRecyclerView.setHasFixedSize(z);
    }

    public boolean isIgnoreSelectionColors() {
        return this.mIgnoreSelectionColors;
    }

    public void setIgnoreSelectionColors(boolean z) {
        this.mIgnoreSelectionColors = z;
    }

    public boolean isShowHorizontalSeparators() {
        return this.mShowHorizontalSeparators;
    }

    public boolean isAllowClickInsideCell() {
        return this.mAllowClickInsideCell;
    }

    public boolean isSortable() {
        return this.mIsSortable;
    }

    public void setShowHorizontalSeparators(boolean z) {
        this.mShowHorizontalSeparators = z;
    }

    public boolean isShowVerticalSeparators() {
        return this.mShowVerticalSeparators;
    }

    public void setShowVerticalSeparators(boolean z) {
        this.mShowVerticalSeparators = z;
    }

    public CellRecyclerView getCellRecyclerView() {
        return this.mCellRecyclerView;
    }

    public CellRecyclerView getColumnHeaderRecyclerView() {
        return this.mColumnHeaderRecyclerView;
    }

    public CellRecyclerView getRowHeaderRecyclerView() {
        return this.mRowHeaderRecyclerView;
    }

    public ColumnHeaderLayoutManager getColumnHeaderLayoutManager() {
        if (this.mColumnHeaderLayoutManager == null) {
            this.mColumnHeaderLayoutManager = new ColumnHeaderLayoutManager(getContext(), this);
        }
        return this.mColumnHeaderLayoutManager;
    }

    public CellLayoutManager getCellLayoutManager() {
        if (this.mCellLayoutManager == null) {
            this.mCellLayoutManager = new CellLayoutManager(getContext(), this);
        }
        return this.mCellLayoutManager;
    }

    public LinearLayoutManager getRowHeaderLayoutManager() {
        if (this.mRowHeaderLayoutManager == null) {
            this.mRowHeaderLayoutManager = new LinearLayoutManager(getContext(), 1, false);
        }
        return this.mRowHeaderLayoutManager;
    }

    public HorizontalRecyclerViewListener getHorizontalRecyclerViewListener() {
        return this.mHorizontalRecyclerViewListener;
    }

    public VerticalRecyclerViewListener getVerticalRecyclerViewListener() {
        return this.mVerticalRecyclerListener;
    }

    public ITableViewListener getTableViewListener() {
        return this.mTableViewListener;
    }

    public void setTableViewListener(ITableViewListener iTableViewListener) {
        this.mTableViewListener = iTableViewListener;
    }

    public void sortColumn(int i, SortState sortState) {
        this.mIsSortable = true;
        this.mColumnSortHandler.sort(i, sortState);
    }

    public void sortRowHeader(SortState sortState) {
        this.mIsSortable = true;
        this.mColumnSortHandler.sortByRowHeader(sortState);
    }

    public void remeasureColumnWidth(int i) {
        getColumnHeaderLayoutManager().removeCachedWidth(i);
        getCellLayoutManager().fitWidthSize(i, false);
    }

    public AbstractTableAdapter getAdapter() {
        return this.mTableAdapter;
    }

    public void filter(Filter filter) {
        this.mFilterHandler.filter(filter);
    }

    public FilterHandler getFilterHandler() {
        return this.mFilterHandler;
    }

    public SortState getSortingStatus(int i) {
        return this.mColumnSortHandler.getSortingStatus(i);
    }

    public SortState getRowHeaderSortingStatus() {
        return this.mColumnSortHandler.getRowHeaderSortingStatus();
    }

    public void scrollToColumnPosition(int i) {
        this.mScrollHandler.scrollToColumnPosition(i);
    }

    public void scrollToColumnPosition(int i, int i2) {
        this.mScrollHandler.scrollToColumnPosition(i, i2);
    }

    public void scrollToRowPosition(int i) {
        this.mScrollHandler.scrollToRowPosition(i);
    }

    public void scrollToRowPosition(int i, int i2) {
        this.mScrollHandler.scrollToRowPosition(i, i2);
    }

    public ScrollHandler getScrollHandler() {
        return this.mScrollHandler;
    }

    public void showRow(int i) {
        this.mVisibilityHandler.showRow(i);
    }

    public void hideRow(int i) {
        this.mVisibilityHandler.hideRow(i);
    }

    public void showAllHiddenRows() {
        this.mVisibilityHandler.showAllHiddenRows();
    }

    public void clearHiddenRowList() {
        this.mVisibilityHandler.clearHideRowList();
    }

    public void showColumn(int i) {
        this.mVisibilityHandler.showColumn(i);
    }

    public void hideColumn(int i) {
        this.mVisibilityHandler.hideColumn(i);
    }

    public boolean isColumnVisible(int i) {
        return this.mVisibilityHandler.isColumnVisible(i);
    }

    public void showAllHiddenColumns() {
        this.mVisibilityHandler.showAllHiddenColumns();
    }

    public void clearHiddenColumnList() {
        this.mVisibilityHandler.clearHideColumnList();
    }

    public boolean isRowVisible(int i) {
        return this.mVisibilityHandler.isRowVisible(i);
    }

    public int getSelectedRow() {
        return this.mSelectionHandler.getSelectedRowPosition();
    }

    public void setSelectedRow(int i) {
        this.mSelectionHandler.setSelectedRowPosition((AbstractViewHolder) getRowHeaderRecyclerView().findViewHolderForAdapterPosition(i), i);
    }

    public int getSelectedColumn() {
        return this.mSelectionHandler.getSelectedColumnPosition();
    }

    public void setSelectedColumn(int i) {
        this.mSelectionHandler.setSelectedColumnPosition((AbstractViewHolder) getColumnHeaderRecyclerView().findViewHolderForAdapterPosition(i), i);
    }

    public void setSelectedCell(int i, int i2) {
        this.mSelectionHandler.setSelectedCellPositions(getCellLayoutManager().getCellViewHolder(i, i2), i, i2);
    }

    public SelectionHandler getSelectionHandler() {
        return this.mSelectionHandler;
    }

    public ColumnSortHandler getColumnSortHandler() {
        return this.mColumnSortHandler;
    }

    public VisibilityHandler getVisibilityHandler() {
        return this.mVisibilityHandler;
    }

    public DividerItemDecoration getHorizontalItemDecoration() {
        if (this.mHorizontalItemDecoration == null) {
            this.mHorizontalItemDecoration = createItemDecoration(0);
        }
        return this.mHorizontalItemDecoration;
    }

    public DividerItemDecoration getVerticalItemDecoration() {
        if (this.mVerticalItemDecoration == null) {
            this.mVerticalItemDecoration = createItemDecoration(1);
        }
        return this.mVerticalItemDecoration;
    }


    public DividerItemDecoration createItemDecoration(int i) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), i);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.cell_line_divider);
        if (drawable == null) {
            return dividerItemDecoration;
        }
        int i2 = this.mSeparatorColor;
        if (i2 != -1) {
            drawable.setColorFilter(i2, PorterDuff.Mode.SRC_ATOP);
        }
        dividerItemDecoration.setDrawable(drawable);
        return dividerItemDecoration;
    }

    public void setSelectedColor(int i) {
        this.mSelectedColor = i;
    }

    public int getSelectedColor() {
        return this.mSelectedColor;
    }

    public void setSeparatorColor(int i) {
        this.mSeparatorColor = i;
    }

    public int getSeparatorColor() {
        return this.mSeparatorColor;
    }

    public void setUnSelectedColor(int i) {
        this.mUnSelectedColor = i;
    }

    public int getUnSelectedColor() {
        return this.mUnSelectedColor;
    }

    public void setShadowColor(int i) {
        this.mShadowColor = i;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public int getRowHeaderWidth() {
        return this.mRowHeaderWidth;
    }

    public void setRowHeaderWidth(int i) {
        this.mRowHeaderWidth = i;
        ViewGroup.LayoutParams layoutParams = this.mRowHeaderRecyclerView.getLayoutParams();
        layoutParams.width = i;
        this.mRowHeaderRecyclerView.setLayoutParams(layoutParams);
        this.mRowHeaderRecyclerView.requestLayout();
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mColumnHeaderRecyclerView.getLayoutParams();
        layoutParams2.leftMargin = i;
        this.mColumnHeaderRecyclerView.setLayoutParams(layoutParams2);
        this.mColumnHeaderRecyclerView.requestLayout();
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) this.mCellRecyclerView.getLayoutParams();
        layoutParams3.leftMargin = i;
        this.mCellRecyclerView.setLayoutParams(layoutParams3);
        this.mCellRecyclerView.requestLayout();
        if (getAdapter() != null) {
            getAdapter().setRowHeaderWidth(i);
        }
    }

    public void setColumnWidth(int i, int i2) {
        this.mColumnWidthHandler.setColumnWidth(i, i2);
    }


    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.preferences = this.mPreferencesHandler.savePreferences();
        return savedState;
    }


    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mPreferencesHandler.loadPreferences(savedState.preferences);
    }
}
