package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.FileFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.FileTypeUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.utils.ImagePickerFileUtils;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.widget.EmptyRecyclerView;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.io.File;
import java.util.Objects;

public class DirectoryFragment extends Fragment {
    private static final String ARG_FILE = "arg_file_path";
    private DirectoryAdapter mDirectoryAdapter;
    private RecyclerView mDirectoryRecyclerView;
    private FileClickListener mFileClickListener;
    private FileFilter mFilter;
    private View mEmptyView;
    private File mFile;

    interface FileClickListener {
        void onCurrentFileClicked();

        void onFileClicked(File file);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFileClickListener = (FileClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFileClickListener = null;
    }

    static DirectoryFragment getInstance(File file, FileFilter fileFilter) {
        DirectoryFragment directoryFragment = new DirectoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FILE, file);
        bundle.putSerializable("arg_filter", fileFilter);
        directoryFragment.setArguments(bundle);
        return directoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_directory, viewGroup, false);
        mDirectoryRecyclerView = inflate.findViewById(R.id.directory_recycler_view);
        mEmptyView = inflate.findViewById(R.id.directory_empty_view);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initArgs();
        initFilesList();
    }

    private void initFilesList() {
        mDirectoryAdapter = new DirectoryAdapter(getActivity(), ImagePickerFileUtils.getFileList(mFile, mFilter));
        mDirectoryAdapter.setOnItemClickListener(new ThrottleClickListener() {
            @Override
            public void onItemClickThrottled(View view, int i) {
                if (mFileClickListener != null) {
                    mFileClickListener.onFileClicked(mDirectoryAdapter.getModel(i));
                }
            }

   /*         @Override
            public void onOptionClick(View view, File file) {
                final String[] strArr = Singleton.getInstance().isCodeFileFolderView() ? new String[]{getResources().getString(R.string.open), getResources().getString(R.string.share), getResources().getString(R.string.info)} : new String[]{getResources().getString(R.string.open), getResources().getString(R.string.delete), getResources().getString(R.string.share), getResources().getString(R.string.info)};
                new AlertDialog.Builder(getActivity()).setTitle(file.getName()).setItems(strArr, (dialogInterface, i) -> {
                    if (strArr[i].equals(getResources().getString(R.string.open))) {
                        if (mFileClickListener != null) {
                            mFileClickListener.onFileClicked(file);
                        }
                    } else if (strArr[i].equals(getResources().getString(R.string.delete))) {
                        deleteFile(file);
                    } else if (strArr[i].equals(getResources().getString(R.string.share))) {
                        Utility.shareFile(requireActivity(), file.getPath());
                    } else if (strArr[i].equals(getResources().getString(R.string.info))) {
                        fileInfo(file);
                    }
                }).create().show();
            }*/
        });

        mDirectoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDirectoryRecyclerView.setAdapter(mDirectoryAdapter);
      //  mDirectoryRecyclerView.setEmptyView(mEmptyView);
    }

    private void initArgs() {
        Bundle arguments = getArguments();
        Objects.requireNonNull(arguments).getClass();
        if (arguments.containsKey(ARG_FILE)) {
            mFile = (File) getArguments().getSerializable(ARG_FILE);
        }
        mFilter = (FileFilter) getArguments().getSerializable("arg_filter");
    }

    public void deleteFile(final File file) {
        new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.alert)).setMessage(getResources().getString(R.string.areYouSureToDeleteThisFile)).setPositiveButton(getResources().getString(R.string.delete), (dialogInterface, i) -> {
            Utility.deleteDir(file);
            mFileClickListener.onCurrentFileClicked();
            dialogInterface.dismiss();
        }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }

    public void fileInfo(File file) {
        String str;
        if (!file.isDirectory()) {
            str = getResources().getString(R.string.sizeColon) + Utility.getFileSize(file) + "\n\n";
        } else {
            str = "";
        }
        new AlertDialog.Builder(getActivity()).setTitle(file.getName()).setIcon(FileTypeUtils.getFileType(file).getIcon()).setMessage(getResources().getString(R.string.pathColon) + file.getPath() + "\n\n" + getResources().getString(R.string.nameColon) + file.getName() + "\n\n" + str + getResources().getString(R.string.modifyDateColon) + Utility.getFileDateTime(file.lastModified())).setPositiveButton(getResources().getString(R.string.ok), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }
}
