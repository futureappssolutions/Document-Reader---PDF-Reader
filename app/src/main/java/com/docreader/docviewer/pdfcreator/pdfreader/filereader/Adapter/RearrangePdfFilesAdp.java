package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.FileModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.DynamicGridView.BaseDynamicGridAdapter;

import java.util.List;

public class RearrangePdfFilesAdp extends BaseDynamicGridAdapter {
    public RearrangePdfFilesAdp(Context context, List<?> list, int i) {
        super(context, list, i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        CheeseViewHolder cheeseViewHolder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_pdf_file_selection, null);
            cheeseViewHolder = new CheeseViewHolder(view);
            view.setTag(cheeseViewHolder);
        } else {
            cheeseViewHolder = (CheeseViewHolder) view.getTag();
        }
        cheeseViewHolder.build((FileModel) getItems().get(i));
        return view;
    }

    private static class CheeseViewHolder {
        public RelativeLayout baseBackLayout, dataLayout;
        public TextView codeFilesText, fileNameTv, fileSizeTv;
        public ImageView imageSelect;

        private CheeseViewHolder(View view) {
            this.fileNameTv = view.findViewById(R.id.fileNameTv);
            this.fileSizeTv = view.findViewById(R.id.fileSizeTv);
            this.dataLayout = view.findViewById(R.id.dataLayout);
            this.codeFilesText = view.findViewById(R.id.codeFilesText);
            this.imageSelect = view.findViewById(R.id.imageSelect);
            this.baseBackLayout = view.findViewById(R.id.baseBackLayout);
        }


        public void build(FileModel fileModel) {
            this.fileNameTv.setText(fileModel.getName());
            this.fileSizeTv.setText(fileModel.getSize());
            if (fileModel.isSelected()) {
                this.imageSelect.setVisibility(View.VISIBLE);
                this.baseBackLayout.setBackgroundColor(Color.parseColor("#291565C0"));
                return;
            }
            this.imageSelect.setVisibility(View.GONE);
            this.baseBackLayout.setBackgroundColor(0);
        }
    }
}
