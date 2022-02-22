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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.SearchFile;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FavoriteFilesModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class SearchableFilesListAdp extends RecyclerView.Adapter<SearchableFilesListAdp.MyViewHolder> implements Filterable {
    public SearchFile activity;
    public FavoriteFilesModel favoriteFilesModel;
    public List<Object> fileFilteredList;
    public List<Object> fileList;
    public Context mContext;
    public SharedPrefs prefs;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dataLayout;
        public TextView codeFilesText;
        public TextView fileNameTv;
        public TextView fileSizeTv;
        public ImageView optionFavorite;
        public ImageView optionMenu;
        public RelativeLayout pdfFilesLayout;
        public RelativeLayout pptFilesLayout;
        public RelativeLayout rtfFilesLayout;
        public RelativeLayout txtFilesLayout;
        public RelativeLayout xlxFilesLayout;
        public RelativeLayout docFilesLayout;
        public RelativeLayout csvFilesLayout;
        public RelativeLayout allFilesLayout;
        public RelativeLayout codeFilesLayout;
        
        public MyViewHolder(View view) {
            super(view);
            fileNameTv = view.findViewById(R.id.fileNameTv);
            fileSizeTv = view.findViewById(R.id.fileSizeTv);
            optionMenu = view.findViewById(R.id.optionMenu);
            optionFavorite = view.findViewById(R.id.optionFavorite);
            dataLayout = view.findViewById(R.id.dataLayout);
            allFilesLayout = view.findViewById(R.id.allFilesLayout);
            xlxFilesLayout = view.findViewById(R.id.xlxFilesLayout);
            pdfFilesLayout = view.findViewById(R.id.pdfFilesLayout);
            docFilesLayout = view.findViewById(R.id.docFilesLayout);
            pptFilesLayout = view.findViewById(R.id.pptFilesLayout);
            txtFilesLayout = view.findViewById(R.id.txtFilesLayout);
            codeFilesLayout = view.findViewById(R.id.codeFilesLayout);
            csvFilesLayout = view.findViewById(R.id.csvFilesLayout);
            rtfFilesLayout = view.findViewById(R.id.rtfFilesLayout);
            codeFilesText = view.findViewById(R.id.codeFilesText);
        }
    }

    public SearchableFilesListAdp(Context context, List<Object> list, SearchFile activitySearchFile) {
        mContext = context;
        fileList = list;
        activity = activitySearchFile;
        fileFilteredList = list;
        prefs = new SharedPrefs(context);
        favoriteFilesModel = Singleton.getInstance().getFavoriteFilesModel(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_files, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int i) {
        final FileModel fileModel = (FileModel) fileFilteredList.get(i);
        myViewHolder.fileNameTv.setText(fileModel.getName());
        myViewHolder.fileSizeTv.setText(fileModel.getSize());
        showFileICon(myViewHolder, fileModel);

        if (fileModel.isFavorite()) {
            myViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite_select);
        } else {
            myViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite);
        }

        myViewHolder.dataLayout.setOnClickListener(view -> activity.onItemClicked(fileModel, i));

        myViewHolder.optionMenu.setOnClickListener(view -> activity.onOptionClicked(fileModel, i));

        myViewHolder.optionFavorite.setOnClickListener(view -> {
            if (fileModel.isFavorite()) {
                fileModel.setFavorite(false);
                myViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite);
                favoriteFilesModel.favoriteFilesList.remove(fileModel.getPath());
                prefs.setFavoriteFilesData(new Gson().toJson(favoriteFilesModel));
                return;
            }
            fileModel.setFavorite(true);
            myViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite_select);
            favoriteFilesModel.favoriteFilesList.remove(fileModel.getPath());
            favoriteFilesModel.favoriteFilesList.add(fileModel.getPath());
            prefs.setFavoriteFilesData(new Gson().toJson(favoriteFilesModel));
        });
    }

    @Override
    public int getItemCount() {
        return fileFilteredList.size();
    }

    private void showFileICon(MyViewHolder myViewHolder, FileModel fileModel) {
        int fileType = fileModel.getFileType();
        myViewHolder.allFilesLayout.setVisibility(View.GONE);
        myViewHolder.pdfFilesLayout.setVisibility(View.GONE);
        myViewHolder.xlxFilesLayout.setVisibility(View.GONE);
        myViewHolder.pptFilesLayout.setVisibility(View.GONE);
        myViewHolder.txtFilesLayout.setVisibility(View.GONE);
        myViewHolder.docFilesLayout.setVisibility(View.GONE);
        myViewHolder.codeFilesLayout.setVisibility(View.GONE);
        myViewHolder.csvFilesLayout.setVisibility(View.GONE);
        myViewHolder.rtfFilesLayout.setVisibility(View.GONE);
        if (fileType == 0) {
            myViewHolder.docFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 1) {
            myViewHolder.xlxFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 2) {
            myViewHolder.pptFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 3) {
            myViewHolder.pdfFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 4) {
            myViewHolder.txtFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 6) {
            String path = fileModel.getPath();
            String substring = path.substring(path.lastIndexOf(".") + 1);
            if (substring != null) {
                myViewHolder.codeFilesText.setText(substring);
            }
            myViewHolder.codeFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 13) {
            myViewHolder.rtfFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 100) {
            myViewHolder.allFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 10) {
            myViewHolder.csvFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 11) {
            myViewHolder.allFilesLayout.setVisibility(View.VISIBLE);
        }
    }

    public Filter getFilter() {
        return new Filter() {

            public Filter.FilterResults performFiltering(CharSequence charSequence) {
                String upperCase = charSequence.toString().toUpperCase();
                if (upperCase.isEmpty()) {
                    fileFilteredList = fileList;
                } else {
                    ArrayList<Object> arrayList = new ArrayList<>();
                    for (Object fileModel : fileList) {
                        if (fileModel.toString().toUpperCase().contains(upperCase)) {
                            arrayList.add(fileModel);
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
                fileFilteredList = (ArrayList) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
