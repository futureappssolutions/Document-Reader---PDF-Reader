package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.TableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.preference.Preferences;

public class PreferencesHandler {
    private ScrollHandler scrollHandler;
    private SelectionHandler selectionHandler;

    public PreferencesHandler(TableView tableView) {
        this.scrollHandler = tableView.getScrollHandler();
        this.selectionHandler = tableView.getSelectionHandler();
    }

    public Preferences savePreferences() {
        Preferences preferences = new Preferences();
        preferences.columnPosition = this.scrollHandler.getColumnPosition();
        preferences.columnPositionOffset = this.scrollHandler.getColumnPositionOffset();
        preferences.rowPosition = this.scrollHandler.getRowPosition();
        preferences.rowPositionOffset = this.scrollHandler.getRowPositionOffset();
        preferences.selectedColumnPosition = this.selectionHandler.getSelectedColumnPosition();
        preferences.selectedRowPosition = this.selectionHandler.getSelectedRowPosition();
        return preferences;
    }

    public void loadPreferences(Preferences preferences) {
        this.scrollHandler.scrollToColumnPosition(preferences.columnPosition, preferences.columnPositionOffset);
        this.scrollHandler.scrollToRowPosition(preferences.rowPosition, preferences.rowPositionOffset);
        this.selectionHandler.setSelectedColumnPosition(preferences.selectedColumnPosition);
        this.selectionHandler.setSelectedRowPosition(preferences.selectedRowPosition);
    }
}
