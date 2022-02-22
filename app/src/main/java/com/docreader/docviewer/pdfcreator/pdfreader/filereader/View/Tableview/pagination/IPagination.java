package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.pagination;

public interface IPagination {
    int getCurrentPage();

    int getItemsPerPage();

    int getPageCount();

    void goToPage(int i);

    boolean isPaginated();

    void nextPage();

    void previousPage();

    void removeOnTableViewPageTurnedListener();

    void setItemsPerPage(int i);

    void setOnTableViewPageTurnedListener(Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener);
}
