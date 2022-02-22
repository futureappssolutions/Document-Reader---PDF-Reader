package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.RoundedHorizontalProgressBar;

public final class Holder extends RecyclerView.ViewHolder {
    private final RoundedHorizontalProgressBar roundedHorizontalProgressBar;
    private final TextView rateTv;
    private final LinearLayout rattingLayout;
    private final TextView tvName;

    public Holder(View view) {
        super(view);
        tvName = view.findViewById(R.id.tvName);
        if (tvName != null) {
            rateTv = view.findViewById(R.id.rateTv);
            if (rateTv != null) {
                rattingLayout = view.findViewById(R.id.rattingLayout);
                if (rattingLayout != null) {
                    roundedHorizontalProgressBar = view.findViewById(R.id.progress_bar);
                    if (roundedHorizontalProgressBar != null) {
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type com.docreader.docviewer.pdfcreator.pdfreader.filereader.view.RoundedHorizontalProgressBar");
                }
                throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
    }

    public final TextView getTvName$app_release() {
        return this.tvName;
    }

    public final TextView getRateTv$app_release() {
        return this.rateTv;
    }

    public final LinearLayout getRattingLayout$app_release() {
        return this.rattingLayout;
    }


    public final RoundedHorizontalProgressBar getRoundedHorizontalProgressBar$app_release() {
        return this.roundedHorizontalProgressBar;
    }
}
