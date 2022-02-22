package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.OnFolderItemClickListener;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.Folder;

import java.io.File;
import java.util.List;

public class FolderViewAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mContext;
    private final List<Folder> mRecyclerViewItems;
    private OnFolderItemClickListener onFolderItemClickListener;

    public FolderViewAdp(Context context, List<Folder> list) {
        mContext = context;
        mRecyclerViewItems = list;
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView number;

        MenuItemViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.tv_name);
            number = view.findViewById(R.id.tv_number);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MenuItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ef_imagepicker_item_folder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
        final Folder folder = mRecyclerViewItems.get(i);
        if (folder != null) {
            Glide.with(mContext).load(new File(folder.getImages().get(0).getPath())).into(menuItemViewHolder.image);
            menuItemViewHolder.name.setText(folder.getFolderName());
            menuItemViewHolder.number.setText(String.valueOf(folder.getImages().size()));
            menuItemViewHolder.itemView.setOnClickListener(view -> onFolderItemClickListener.onFolderItemClick(folder));
        }
    }

    public void setOnFolderViewItemClick(OnFolderItemClickListener onFolderItemClickListener2) {
        onFolderItemClickListener = onFolderItemClickListener2;
    }
}
