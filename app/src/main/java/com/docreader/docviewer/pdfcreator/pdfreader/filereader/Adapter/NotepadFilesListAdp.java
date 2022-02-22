package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad.ActNotepadList;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.NotepadItemModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.List;

public class NotepadFilesListAdp extends RecyclerView.Adapter<NotepadFilesListAdp.MyViewHolder> {
    public ActNotepadList activity;
    public List<NotepadItemModel> fileList;
    public Context mContext;
    public SharedPrefs prefs;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View headerView;
        public LinearLayout backgroundLayout;
        public LinearLayout dataLayout;
        public TextView contentTv;
        public TextView notepadTitleTv;
        public TextView dateTimeTv;
        public ImageView optionMenu;
        public ImageView pinImageView;

        public MyViewHolder(View view) {
            super(view);
            notepadTitleTv = view.findViewById(R.id.notepadTitleTv);
            contentTv = view.findViewById(R.id.contentTv);
            optionMenu = view.findViewById(R.id.optionMenu);
            pinImageView = view.findViewById(R.id.pinImageView);
            dateTimeTv = view.findViewById(R.id.dateTimeTv);
            dataLayout = view.findViewById(R.id.dataLayout);
            headerView = view.findViewById(R.id.headerView);
            backgroundLayout = view.findViewById(R.id.backgroundLayout);
        }
    }

    public NotepadFilesListAdp(Context context, List<NotepadItemModel> list, ActNotepadList activityNotepadList) {
        this.mContext = context;
        this.fileList = list;
        this.activity = activityNotepadList;
        this.prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notepade_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        final NotepadItemModel notepadItemModel = fileList.get(i);
        myViewHolder.notepadTitleTv.setText(notepadItemModel.getTitle());
        myViewHolder.contentTv.setText(notepadItemModel.getContent());
        myViewHolder.dateTimeTv.setText(Utility.geDateTime(Long.parseLong(notepadItemModel.getDate())));

        if (notepadItemModel.getPin() == 0) {
            myViewHolder.pinImageView.setVisibility(View.GONE);
        } else {
            myViewHolder.pinImageView.setVisibility(View.VISIBLE);
        }

        changeBackgroundAndTitleColor(myViewHolder, notepadItemModel.getThem());

        myViewHolder.dataLayout.setOnClickListener(view -> activity.onItemClicked(notepadItemModel, myViewHolder.getAdapterPosition()));

        myViewHolder.optionMenu.setOnClickListener(view -> activity.onOptionClicked(view, notepadItemModel, myViewHolder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void changeBackgroundAndTitleColor(MyViewHolder myViewHolder, int i) {
        if (i != 1010) {
            switch (i) {
                case 101:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color1));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent1));
                    return;
                case 102:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color2));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent2));
                    return;
                case 103:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color3));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent3));
                    return;
                case 104:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color4));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent4));
                    return;
                case 105:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color5));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent5));
                    return;
                case 106:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color6));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent6));
                    return;
                case 107:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color7));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent7));
                    return;
                case 108:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color8));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent8));
                    return;
                case 109:
                    myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color9));
                    myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent9));
                    return;
                default:
            }
        } else {
            myViewHolder.headerView.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color10));
            myViewHolder.backgroundLayout.setBackgroundColor(mContext.getResources().getColor(R.color.notepad_color_transparent10));
        }
    }
}
