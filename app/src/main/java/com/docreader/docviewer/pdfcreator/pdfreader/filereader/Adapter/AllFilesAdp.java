package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.FilesList;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.AnimUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FavoriteFilesModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Singleton;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.List;

public class AllFilesAdp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public boolean isLongPress = false;
    public final Context mContext;
    public List<Object> mRecyclerViewItems;
    public FavoriteFilesModel favoriteFilesModel;
    public FilesList activity;
    public SharedPrefs prefs;

    public AllFilesAdp(Context context, List<Object> list, FilesList activityFilesList) {
        this.mContext = context;
        this.mRecyclerViewItems = list;
        this.activity = activityFilesList;
        this.prefs = new SharedPrefs(context);
        this.favoriteFilesModel = Singleton.getInstance().getFavoriteFilesModel(context);
    }

    public static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dataLayout;
        public RelativeLayout allFilesLayout, codeFilesLayout, pdfFilesLayout, pptFilesLayout;
        public RelativeLayout rtfFilesLayout, selectionRl, txtFilesLayout, xlxFilesLayout;
        public RelativeLayout docFilesLayout, ebookFilesLayout;
        public TextView codeFilesText, fileNameTv, fileSizeTv;
        public ImageView imageSelect, item_file_image, optionFavorite, optionMenu;

        MenuItemViewHolder(View view) {
            super(view);
            fileNameTv = view.findViewById(R.id.fileNameTv);
            fileSizeTv = view.findViewById(R.id.fileSizeTv);
            optionMenu = view.findViewById(R.id.optionMenu);
            imageSelect = view.findViewById(R.id.imageSelect);
            optionFavorite = view.findViewById(R.id.optionFavorite);
            dataLayout = view.findViewById(R.id.dataLayout);
            item_file_image = view.findViewById(R.id.item_file_image);
            selectionRl = view.findViewById(R.id.selectionRl);
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
        return new MenuItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_files, viewGroup, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MenuItemViewHolder menuItemViewHolder = (MenuItemViewHolder) viewHolder;
        final FileModel fileModel = (FileModel) mRecyclerViewItems.get(i);
        menuItemViewHolder.fileNameTv.setText(fileModel.getName());
        TextView textView = menuItemViewHolder.fileSizeTv;
        textView.setText(Html.fromHtml(Utility.getLastModifyDate(fileModel.getModifiedDate()) + "  " + fileModel.getSize()));
        menuItemViewHolder.imageSelect.setVisibility(View.GONE);
        menuItemViewHolder.selectionRl.setBackgroundResource(0);
        if (fileModel.isSelected()) {
            menuItemViewHolder.selectionRl.setBackgroundResource(R.drawable.tooltip_indicator_rounded_line_unselected);
            menuItemViewHolder.imageSelect.setVisibility(View.VISIBLE);
        }
        if (fileModel.isFavorite()) {
            menuItemViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite_select);
        } else {
            menuItemViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite);
        }
//        if (this.prefs.isMarqueeEffectEnable()) {
//            menuItemViewHolder.fileNameTv.setEllipsize(this.prefs.isMarqueeEffectEnable() ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.MIDDLE);
//            AnimUtils.marqueeAfterDelay(2000, menuItemViewHolder.fileNameTv);
//        }
        showFileICon(menuItemViewHolder, fileModel);

        menuItemViewHolder.dataLayout.setOnClickListener(view -> {
            if (isLongPress) {
                fileModel.setSelected(!fileModel.isSelected());
                notifyDataSetChanged();
                return;
            }
            activity.onItemClicked(fileModel, i);
        });

        menuItemViewHolder.dataLayout.setOnLongClickListener(view -> {
            if (!isLongPress) {
                isLongPress = true;
                Utility.vibratePhone(mContext);
                menuItemViewHolder.selectionRl.setBackgroundResource(R.drawable.tooltip_indicator_rounded_line_unselected);
                menuItemViewHolder.imageSelect.setVisibility(View.VISIBLE);
                fileModel.setSelected(!fileModel.isSelected());
                activity.changeToolBarControl();
                notifyDataSetChanged();
            }
            return false;
        });

        menuItemViewHolder.optionMenu.setOnClickListener(view -> activity.onOptionClicked(fileModel, i));

        menuItemViewHolder.optionFavorite.setOnClickListener(view -> {
            if (fileModel.isFavorite()) {
                fileModel.setFavorite(false);
                menuItemViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite);
                favoriteFilesModel.favoriteFilesList.remove(fileModel.getPath());
                prefs.setFavoriteFilesData(new Gson().toJson(favoriteFilesModel));
                return;
            }
            fileModel.setFavorite(true);
            menuItemViewHolder.optionFavorite.setImageResource(R.drawable.ic_favorite_select);
            favoriteFilesModel.favoriteFilesList.remove(fileModel.getPath());
            favoriteFilesModel.favoriteFilesList.add(fileModel.getPath());
            prefs.setFavoriteFilesData(new Gson().toJson(favoriteFilesModel));
        });
    }

    private void showFileICon(MenuItemViewHolder menuItemViewHolder, FileModel fileModel) {
        int fileType = fileModel.getFileType();
        menuItemViewHolder.allFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.pdfFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.xlxFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.pptFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.txtFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.docFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.codeFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.rtfFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.ebookFilesLayout.setVisibility(View.GONE);
        menuItemViewHolder.item_file_image.setVisibility(View.GONE);

        if (fileType == 0) {
            menuItemViewHolder.docFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 1) {
            menuItemViewHolder.xlxFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 2) {
            menuItemViewHolder.pptFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 3) {
            menuItemViewHolder.pdfFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 4) {
            menuItemViewHolder.txtFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 6) {
            String path = fileModel.getPath();
            String substring = path.substring(path.lastIndexOf(".") + 1);
            menuItemViewHolder.codeFilesText.setText(substring);
            menuItemViewHolder.codeFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 100) {
            menuItemViewHolder.allFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType == 10) {
            menuItemViewHolder.txtFilesLayout.setVisibility(View.VISIBLE);
        } else if (fileType != 11) {
            switch (fileType) {
                case 13:
                    menuItemViewHolder.rtfFilesLayout.setVisibility(View.VISIBLE);
                    return;
                case 14:
                    menuItemViewHolder.optionFavorite.setVisibility(View.GONE);
                    menuItemViewHolder.ebookFilesLayout.setVisibility(View.VISIBLE);
                    return;
                case 15:
                    menuItemViewHolder.item_file_image.setVisibility(View.VISIBLE);
                    return;
                default:
            }
        } else {
            menuItemViewHolder.allFilesLayout.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateAdapter() {
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void reSetAllSelection() {
        for (int i = 0; i < mRecyclerViewItems.size(); i++) {
            if (mRecyclerViewItems.get(i) instanceof FileModel) {
                ((FileModel) mRecyclerViewItems.get(i)).setSelected(false);
            }
        }
        notifyDataSetChanged();
        isLongPress = false;
    }
}