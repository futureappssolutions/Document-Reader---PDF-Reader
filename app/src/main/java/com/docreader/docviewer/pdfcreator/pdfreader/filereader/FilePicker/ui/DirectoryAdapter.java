package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.AnimUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.FileTypeUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.constant.MainConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.util.List;

class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {
    private final Context context;
    private final List<File> mFiles;
    private final SharedPrefs prefs;
    private OnItemClickListener mOnItemClickListener;

    DirectoryAdapter(Context context2, List<File> list) {
        context = context2;
        mFiles = list;
        prefs = new SharedPrefs(context2);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    public DirectoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DirectoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_file, viewGroup, false));
    }

    public void onBindViewHolder(final DirectoryViewHolder directoryViewHolder, int i) {
        String str;
        final File file = mFiles.get(i);
        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(file);
        directoryViewHolder.mFileTitle.setText(file.getName());
        if (!file.isDirectory()) {
            str = "   " + Utility.getFileSize(file);
        } else {
            str = "";
        }
        context.getString(fileType.getDescription());
        directoryViewHolder.mFileSubtitle.setText(Html.fromHtml(Utility.getLastModifyDate(file.lastModified()) + str));
//        if (prefs.isMarqueeEffectEnable()) {
//            directoryViewHolder.mFileTitle.setEllipsize(prefs.isMarqueeEffectEnable() ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.MIDDLE);
//            AnimUtils.marqueeAfterDelay(2000, directoryViewHolder.mFileTitle);
//        }
        showFileICon(directoryViewHolder, MainConstant.getFileType(file.getPath()), file.getPath(), fileType.getDescription(), file.getName());
//        directoryViewHolder.optionMenu.setOnClickListener(view -> mOnItemClickListener.onOptionClick(view, file));

        directoryViewHolder.dataLayout.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, directoryViewHolder.getAdapterPosition()));
    }

    public int getItemCount() {
        return mFiles.size();
    }

    public File getModel(int i) {
        return mFiles.get(i);
    }

    static class DirectoryViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout dataLayout;
        public RelativeLayout codeFilesLayout;
        public RelativeLayout docFilesLayout;
        public RelativeLayout epubLayout;
        public RelativeLayout csvFilesLayout;
        public RelativeLayout pdfFilesLayout;
        public RelativeLayout pptFilesLayout;
        public RelativeLayout rtfFilesLayout;
        public RelativeLayout txtFilesLayout;
        public RelativeLayout xlxFilesLayout;
        public TextView codeFilesText;
        public TextView mFileSubtitle;
        public TextView mFileTitle;
        public ImageView mFileImage;
        public ImageView optionMenu;

        DirectoryViewHolder(View view) {
            super(view);
            dataLayout = view.findViewById(R.id.dataLayout);
            mFileImage = view.findViewById(R.id.item_file_image);
            optionMenu = view.findViewById(R.id.optionMenu);
            mFileTitle = view.findViewById(R.id.item_file_title);
            mFileSubtitle = view.findViewById(R.id.item_file_subtitle);
            codeFilesText = view.findViewById(R.id.codeFilesText);
            xlxFilesLayout = view.findViewById(R.id.xlxFilesLayout);
            pdfFilesLayout = view.findViewById(R.id.pdfFilesLayout);
            docFilesLayout = view.findViewById(R.id.docFilesLayout);
            pptFilesLayout = view.findViewById(R.id.pptFilesLayout);
            txtFilesLayout = view.findViewById(R.id.txtFilesLayout);
            codeFilesLayout = view.findViewById(R.id.codeFilesLayout);
            epubLayout = view.findViewById(R.id.epubLayout);
            rtfFilesLayout = view.findViewById(R.id.rtfFilesLayout);
            csvFilesLayout = view.findViewById(R.id.csvFilesLayout);
        }
    }

    private void showFileICon(DirectoryViewHolder directoryViewHolder, int i, String str, int i2, String str2) {
        directoryViewHolder.pdfFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.xlxFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.pptFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.txtFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.docFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.codeFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.epubLayout.setVisibility(View.GONE);
        directoryViewHolder.rtfFilesLayout.setVisibility(View.GONE);
        directoryViewHolder.csvFilesLayout.setVisibility(View.GONE);
        FileTypeUtils.FileType fileType = FileTypeUtils.getFileType(new File(str));
        directoryViewHolder.mFileImage.setVisibility(View.VISIBLE);
        if (fileType.getIcon() == R.drawable.icon_folder) {
            directoryViewHolder.mFileImage.setImageResource(R.drawable.icon_folder);
        } else if (fileType.getDescription() == R.string.type_image) {
            Glide.with(context).load(new File(str)).into(directoryViewHolder.mFileImage);
        } else if (fileType.getIcon() == R.drawable.icon_zip) {
            directoryViewHolder.mFileImage.setImageResource(R.drawable.icon_zip);
        } else if (fileType.getIcon() == R.drawable.icon_music) {
            directoryViewHolder.mFileImage.setImageResource(R.drawable.icon_music);
        } else if (fileType.getIcon() == R.drawable.ic_video) {
            directoryViewHolder.mFileImage.setImageResource(R.drawable.ic_video);
        } else if (fileType.getIcon() == R.drawable.icon_android) {
            Drawable appIcon = getAppIcon(str);
            if (appIcon != null) {
                directoryViewHolder.mFileImage.setImageDrawable(appIcon);
            } else {
                directoryViewHolder.mFileImage.setImageResource(R.drawable.icon_android);
            }
        } else {
            directoryViewHolder.mFileImage.setVisibility(View.GONE);
            if (i == 10) {
                directoryViewHolder.csvFilesLayout.setVisibility(View.VISIBLE);
            } else if (i == 13) {
                directoryViewHolder.rtfFilesLayout.setVisibility(View.VISIBLE);
            } else if (i != 14) {
                switch (i) {
                    case 0:
                        directoryViewHolder.docFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    case 1:
                        directoryViewHolder.xlxFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    case 2:
                        directoryViewHolder.pptFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    case 3:
                        directoryViewHolder.pdfFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    case 4:
                        directoryViewHolder.txtFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    case 5:
                        directoryViewHolder.mFileImage.setVisibility(View.VISIBLE);
                        return;
                    case 6:
                        String substring = str.substring(str.lastIndexOf(".") + 1);
                        directoryViewHolder.codeFilesText.setText(substring);
                        directoryViewHolder.codeFilesLayout.setVisibility(View.VISIBLE);
                        return;
                    default:
                        directoryViewHolder.mFileImage.setVisibility(View.VISIBLE);
                }
            } else {
                directoryViewHolder.epubLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    public Drawable getAppIcon(String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(str, 0);
            packageArchiveInfo.applicationInfo.sourceDir = str;
            packageArchiveInfo.applicationInfo.publicSourceDir = str;
            return packageArchiveInfo.applicationInfo.loadIcon(packageManager);
        } catch (Exception unused) {
            return null;
        }
    }
}
