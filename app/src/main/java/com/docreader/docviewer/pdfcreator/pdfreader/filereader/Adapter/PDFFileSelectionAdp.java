package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.PDFViewWebViewBase;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.SelectPdfFile;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;

import java.util.List;

public class PDFFileSelectionAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public SelectPdfFile activity;
    public List<Object> mRecyclerViewItems;
    public FileModel selectedFileModel;
    public Context mContext;

    public PDFFileSelectionAdp(Context context, List<Object> list, SelectPdfFile activitySelectPdfFile) {
        mContext = context;
        mRecyclerViewItems = list;
        activity = activitySelectPdfFile;
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout baseBackLayout, dataLayout;
        public TextView codeFilesText, fileNameTv, fileSizeTv;
        public ImageView imageSelect;

        MenuItemViewHolder(View view) {
            super(view);
            fileNameTv = view.findViewById(R.id.fileNameTv);
            fileSizeTv = view.findViewById(R.id.fileSizeTv);
            dataLayout = view.findViewById(R.id.dataLayout);
            codeFilesText = view.findViewById(R.id.codeFilesText);
            imageSelect = view.findViewById(R.id.imageSelect);
            baseBackLayout = view.findViewById(R.id.baseBackLayout);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MenuItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pdf_file_selection, viewGroup, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) != 1) {
            MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
            final FileModel fileModel = (FileModel) mRecyclerViewItems.get(i);
            menuItemViewHolder.fileNameTv.setText(fileModel.getName());
            menuItemViewHolder.fileSizeTv.setText(fileModel.getSize());
            if (fileModel.isSelected()) {
                menuItemViewHolder.imageSelect.setVisibility(View.VISIBLE);
                menuItemViewHolder.baseBackLayout.setBackgroundColor(Color.parseColor("#291565C0"));
            } else {
                menuItemViewHolder.imageSelect.setVisibility(View.GONE);
                menuItemViewHolder.baseBackLayout.setBackgroundColor(0);
            }

            menuItemViewHolder.dataLayout.setOnClickListener(view -> {
                fileModel.setSelected(!fileModel.isSelected());
                activity.onItemClicked(fileModel, i);
                notifyDataSetChanged();
            });

            menuItemViewHolder.dataLayout.setOnLongClickListener(view -> {
                selectedFileModel = fileModel;
                Intent intent = new Intent(mContext, PDFViewWebViewBase.class);
                intent.putExtra("path", fileModel.getPath());
                intent.putExtra("name", fileModel.getName());
                intent.putExtra("fileType", fileModel.getFileType() + "");
                mContext.startActivity(intent);
                return false;
            });
        }
    }
}
