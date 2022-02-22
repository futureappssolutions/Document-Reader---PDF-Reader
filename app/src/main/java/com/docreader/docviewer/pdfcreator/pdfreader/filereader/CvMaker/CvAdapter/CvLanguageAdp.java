package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Language;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.Holder;

import java.util.ArrayList;


public final class CvLanguageAdp extends RecyclerView.Adapter<Holder> {
    private final ArrayList<Language> arrayList;

    public CvLanguageAdp(ArrayList<Language> arrayList2) {
        arrayList = arrayList2;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_skill, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, int i) {
        Language language = arrayList.get(i);
        holder.getTvName$app_release().setText(language.getName());
        TextView textView = holder.getRateTv$app_release();
        textView.setText(language.getRatting() + "/5");
        holder.getRoundedHorizontalProgressBar$app_release().setProgress(language.getRatting());
    }

    public int getItemCount() {
        return arrayList.size();
    }
}
