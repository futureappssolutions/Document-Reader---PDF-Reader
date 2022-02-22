package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter.CvResumeEventAdp;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.ResumeEvent;

import java.util.Objects;

public abstract class ResumeEventFragment<T extends ResumeEvent> extends ResumeFragment implements CvResumeEventAdp.ResumeEventOnClickListener {
    public static final int REQUEST_ADD = 1;
    public static final int REQUEST_EDIT = 2;
    public RecyclerView recyclerView;
    public LinearLayout llAddEducation;

    public abstract void addClicked();

    public abstract void delete(int i);

    public abstract CvResumeEventAdp<T> getAdapter(View view);

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_resume_event, viewGroup, false);

        recyclerView = inflate.findViewById(R.id.recycler_view);
        llAddEducation = inflate.findViewById(R.id.llAddEducation);
        @SuppressLint("ResourceType") View findViewById = inflate.findViewById(16908292);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(getAdapter(findViewById));

        llAddEducation.setOnClickListener(v -> addClicked());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 12) {
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder2) {
                return false;
            }

            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                delete((Integer) viewHolder.itemView.getTag());
                notifyDataChanged();
            }
        }).attachToRecyclerView(recyclerView);
        return inflate;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyDataChanged() {
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }
}
