package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.ImageModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView.BaseDynamicGridAdapter;

import java.io.File;
import java.util.List;

public class ImageToPdfAdp extends BaseDynamicGridAdapter {
    public ImageToPdfAdp(Context context, List<?> list, int i) {
        super(context, list, i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        CheeseViewHolder cheeseViewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.img_to_pdf_row_view, null);
            cheeseViewHolder = new CheeseViewHolder(view);
            view.setTag(cheeseViewHolder);
        } else {
            cheeseViewHolder = (CheeseViewHolder) view.getTag();
        }
        cheeseViewHolder.build((ImageModel) getItems().get(i));
        return view;
    }

    private class CheeseViewHolder {
        public LinearLayout checkedLayout;
        public RelativeLayout fileLayout;
        public TextView videoTimeTv;
        public ImageView fileIcon;
        public ImageView iconPlay;
        public ImageView viewImage;

        private CheeseViewHolder(View view) {
            fileIcon = view.findViewById(R.id.fileIcon);
            fileLayout = view.findViewById(R.id.fileLayout);
            checkedLayout = view.findViewById(R.id.checkedLayout);
            viewImage = view.findViewById(R.id.viewImage);
            videoTimeTv = view.findViewById(R.id.videoTimeTv);
            iconPlay = view.findViewById(R.id.iconPlay);
        }

        public void build(ImageModel imageModel) {
            if (imageModel != null) {
                if (imageModel.getPath().isEmpty()) {
                    Glide.with(mContext).load(imageModel.getUri()).into(fileIcon);
                } else {
                    Glide.with(mContext).load(new File(imageModel.getPath())).into(fileIcon);
                }
                if (imageModel.isSelected()) {
                    checkedLayout.setVisibility(View.VISIBLE);
                } else {
                    checkedLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}
