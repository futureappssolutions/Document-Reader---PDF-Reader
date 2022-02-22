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

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.onRecyclerViewItemClick;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;

import java.io.File;
import java.util.List;

public class PickImageVideoAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    int counter;
    private final Context mContext;
    private final List<ImageModel> mRecyclerViewItems;
    private com.docreader.docviewer.pdfcreator.pdfreader.filereader.Interface.onRecyclerViewItemClick onRecyclerViewItemClick;

    public PickImageVideoAdp(Context context, List<ImageModel> list) {
        mContext = context;
        mRecyclerViewItems = list;
        counter = 0;
    }

    public void reSetCounter() {
        counter = 0;
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout checkedLayout;
        public ImageView iconPlay;
        public ImageView fileIcon;
        public ImageView viewImage;
        public RelativeLayout fileLayout;
        public TextView videoTimeTv;

        MenuItemViewHolder(View view) {
            super(view);
            fileIcon = view.findViewById(R.id.fileIcon);
            fileLayout = view.findViewById(R.id.fileLayout);
            checkedLayout = view.findViewById(R.id.checkedLayout);
            viewImage = view.findViewById(R.id.viewImage);
            videoTimeTv = view.findViewById(R.id.videoTimeTv);
            iconPlay = view.findViewById(R.id.iconPlay);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MenuItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pick_file_row_view, viewGroup, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
        final ImageModel imageModel = mRecyclerViewItems.get(i);
        if (imageModel != null) {
            if (imageModel.getPath().isEmpty()) {
                Glide.with(mContext).load(imageModel.getUri()).into(menuItemViewHolder.fileIcon);
            } else {
                Glide.with(mContext).load(new File(imageModel.getPath())).into(menuItemViewHolder.fileIcon);
            }
            if (imageModel.isSelected()) {
                menuItemViewHolder.checkedLayout.setVisibility(View.VISIBLE);
            } else {
                menuItemViewHolder.checkedLayout.setVisibility(View.GONE);
            }
            if (imageModel.isVideo()) {
                menuItemViewHolder.iconPlay.setVisibility(View.VISIBLE);
            } else {
                menuItemViewHolder.iconPlay.setVisibility(View.GONE);
            }
            menuItemViewHolder.fileLayout.setOnClickListener(view -> {
                boolean z = !imageModel.isSelected();
                imageModel.setSelected(z);
                if (z) {
                    counter++;
                } else {
                    PickImageVideoAdp pickImageVideoAdapter = PickImageVideoAdp.this;
                    pickImageVideoAdapter.counter--;
                }
                onRecyclerViewItemClick.onItemClick(imageModel);
                notifyDataSetChanged();
            });
            menuItemViewHolder.viewImage.setOnClickListener(view -> {
            });
        }
    }

    public void setOnRecyclerViewItemClick(onRecyclerViewItemClick onrecyclerviewitemclick) {
        onRecyclerViewItemClick = onrecyclerviewitemclick;
    }
}
