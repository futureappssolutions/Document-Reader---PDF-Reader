package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mlsdev.animatedrv.AnimatedRecyclerView;

public class EmptyRecyclerView extends AnimatedRecyclerView {
    private View mEmptyView;

    private final AnimatedRecyclerView.AdapterDataObserver observer = new AnimatedRecyclerView.AdapterDataObserver() {
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public EmptyRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void checkIfEmpty() {
        AnimatedRecyclerView.Adapter adapter = getAdapter();
        if (mEmptyView != null && adapter != null) {
            mEmptyView.setVisibility(adapter.getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    public void setAdapter(AnimatedRecyclerView.Adapter adapter) {
        AnimatedRecyclerView.Adapter adapter2 = getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        checkIfEmpty();
    }
}
