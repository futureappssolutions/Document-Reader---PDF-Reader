package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnLanguageChooseItemClick;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.AppLanguage;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdp extends RecyclerView.Adapter<LanguageAdp.MyViewHolder> implements Filterable {
    public Context mContext;
    public List<AppLanguage> fileFilteredList;
    public List<AppLanguage> fileList;
    public OnLanguageChooseItemClick onRecyclerViewItemClick;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dataLayout;
        public TextView languageName;
        public TextView localLanguageName;
        public ImageView selectIv;

        public MyViewHolder(View view) {
            super(view);
            languageName = view.findViewById(R.id.languageName);
            localLanguageName = view.findViewById(R.id.localLanguageName);
            selectIv = view.findViewById(R.id.selectIv);
            dataLayout = view.findViewById(R.id.dataLayout);
        }
    }

    public LanguageAdp(Context context, List<AppLanguage> list) {
        mContext = context;
        fileList = list;
        fileFilteredList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.choos_language_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final AppLanguage appLanguage = fileFilteredList.get(i);
        myViewHolder.languageName.setText(appLanguage.getName());
        myViewHolder.localLanguageName.setText(appLanguage.getLocalLanguageName());

        if (appLanguage.isSelected()) {
            myViewHolder.selectIv.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.selectIv.setVisibility(View.INVISIBLE);
        }
        myViewHolder.dataLayout.setOnClickListener(view -> onRecyclerViewItemClick.onItemClick(appLanguage, i));
    }

    @Override
    public int getItemCount() {
        return fileFilteredList.size();
    }

    public Filter getFilter() {
        return new Filter() {

            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                String upperCase = charSequence.toString().toUpperCase();
                if (upperCase.isEmpty()) {
                    fileFilteredList = fileList;
                } else {
                    ArrayList<AppLanguage> arrayList = new ArrayList<>();
                    for (AppLanguage appLanguage : fileList) {
                        if (appLanguage.getName().toUpperCase().contains(upperCase)) {
                            arrayList.add(appLanguage);
                        }
                    }
                    fileFilteredList = arrayList;
                }
                Filter.FilterResults filterResults = new Filter.FilterResults();
                filterResults.values = fileFilteredList;
                return filterResults;
            }


            @SuppressLint("NotifyDataSetChanged")
            public void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                fileFilteredList = (List<AppLanguage>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setOnRecyclerViewItemClick(OnLanguageChooseItemClick onLanguageChooseItemClick) {
        onRecyclerViewItemClick = onLanguageChooseItemClick;
    }
}
