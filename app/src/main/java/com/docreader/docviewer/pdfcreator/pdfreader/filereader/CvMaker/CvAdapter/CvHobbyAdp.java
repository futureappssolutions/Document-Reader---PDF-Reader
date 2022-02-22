package com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvGetSet.Hobby;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvTemplate.Holder;

import java.util.ArrayList;

public final class CvHobbyAdp extends RecyclerView.Adapter<Holder> {
    private final ArrayList<Hobby> arrayList;

    public CvHobbyAdp(ArrayList<Hobby> arrayList2) {
        arrayList = arrayList2;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Holder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_skill, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        Hobby hobby = arrayList.get(i);
        holder.getRattingLayout$app_release().setVisibility(View.GONE);
        holder.getTvName$app_release().setText(hobby.getName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
