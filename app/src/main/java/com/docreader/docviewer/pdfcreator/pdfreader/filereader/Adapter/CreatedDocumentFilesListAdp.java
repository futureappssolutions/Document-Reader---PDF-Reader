package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.CreateNewPdfFile;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.DocumentFiles;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FavoriteFilesModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.List;

public class CreatedDocumentFilesListAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<DocumentFiles> mRecyclerViewItems;
    public CreateNewPdfFile activity;
    public FavoriteFilesModel favoriteFilesModel;
    public Context mContext;
    public SharedPrefs prefs;

    public CreatedDocumentFilesListAdp(Context context, List<DocumentFiles> list, CreateNewPdfFile activityCreateNewPdfFile) {
        mContext = context;
        mRecyclerViewItems = list;
        activity = activityCreateNewPdfFile;
        prefs = new SharedPrefs(context);
        favoriteFilesModel = Singleton.getInstance().getFavoriteFilesModel(context);
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public TextView codeFilesText;
        public TextView fileNameTv;
        public TextView fileSizeTv;
        public ImageView optionMenu;
        public LinearLayout dataLayout;
        public RelativeLayout allFilesLayout;
        public RelativeLayout codeFilesLayout;
        public RelativeLayout docFilesLayout;
        public RelativeLayout ebookFilesLayout;
        public RelativeLayout pdfFilesLayout;
        public RelativeLayout pptFilesLayout;
        public RelativeLayout rtfFilesLayout;
        public RelativeLayout txtFilesLayout;
        public RelativeLayout xlxFilesLayout;

        MenuItemViewHolder(View view) {
            super(view);
            fileNameTv = view.findViewById(R.id.fileNameTv);
            fileSizeTv = view.findViewById(R.id.fileSizeTv);
            optionMenu = view.findViewById(R.id.optionMenu);
            dataLayout = view.findViewById(R.id.dataLayout);
            allFilesLayout = view.findViewById(R.id.allFilesLayout);
            xlxFilesLayout = view.findViewById(R.id.xlxFilesLayout);
            pdfFilesLayout = view.findViewById(R.id.pdfFilesLayout);
            docFilesLayout = view.findViewById(R.id.docFilesLayout);
            pptFilesLayout = view.findViewById(R.id.pptFilesLayout);
            txtFilesLayout = view.findViewById(R.id.txtFilesLayout);
            codeFilesLayout = view.findViewById(R.id.codeFilesLayout);
            rtfFilesLayout = view.findViewById(R.id.rtfFilesLayout);
            ebookFilesLayout = view.findViewById(R.id.ebookFilesLayout);
            codeFilesText = view.findViewById(R.id.codeFilesText);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MenuItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.edit_file_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
        final DocumentFiles documentFiles = mRecyclerViewItems.get(i);
        menuItemViewHolder.fileNameTv.setText(documentFiles.getFileName());
        menuItemViewHolder.fileSizeTv.setText(Utility.getFileDateTime(Long.valueOf(documentFiles.getCreatedAt())));
        menuItemViewHolder.dataLayout.setOnClickListener(view -> activity.onItemClicked(documentFiles, i));
        menuItemViewHolder.optionMenu.setOnClickListener(view -> activity.onOptionClicked(documentFiles, i));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter() {
        notifyDataSetChanged();
    }
}
