package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.itemclick;

import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.ITableView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.adapter.recyclerview.CellRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.handler.SelectionHandler;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.Tableview.listener.ITableViewListener;

public abstract class AbstractItemClickListener implements RecyclerView.OnItemTouchListener {
    private ITableViewListener mListener;
    protected CellRecyclerView mRecyclerView;
    protected SelectionHandler mSelectionHandler;
    protected ITableView mTableView;

    public abstract boolean clickAction(RecyclerView recyclerView, MotionEvent motionEvent);


    public abstract boolean doubleClickAction(MotionEvent motionEvent);


    public abstract void longPressAction(MotionEvent motionEvent);

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
    }

    public AbstractItemClickListener(CellRecyclerView cellRecyclerView, ITableView iTableView) {
        this.mRecyclerView = cellRecyclerView;
        this.mTableView = iTableView;
        this.mSelectionHandler = iTableView.getSelectionHandler();
    }

    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        GestureDetector mGestureDetector = new GestureDetector(mRecyclerView.getContext(), new GestureDetector.SimpleOnGestureListener() {
            MotionEvent start;

            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                AbstractItemClickListener abstractItemClickListener = AbstractItemClickListener.this;
                return abstractItemClickListener.clickAction(abstractItemClickListener.mRecyclerView, motionEvent);
            }

            public boolean onDoubleTap(MotionEvent motionEvent) {
                return AbstractItemClickListener.this.doubleClickAction(motionEvent);
            }

            public boolean onDown(MotionEvent motionEvent) {
                this.start = motionEvent;
                return false;
            }

            public void onLongPress(MotionEvent motionEvent) {
                MotionEvent motionEvent2 = this.start;
                if (motionEvent2 != null && Math.abs(motionEvent2.getRawX() - motionEvent.getRawX()) < 20.0f && Math.abs(this.start.getRawY() - motionEvent.getRawY()) < 20.0f) {
                    AbstractItemClickListener.this.longPressAction(motionEvent);
                }
            }
        });
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }


    public ITableViewListener getTableViewListener() {
        if (this.mListener == null) {
            this.mListener = this.mTableView.getTableViewListener();
        }
        return this.mListener;
    }
}
