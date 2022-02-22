package com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.CompositeFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.FileFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.HiddenFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.filter.PatternFilter;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.FilePicker.ui.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class MaterialFilePicker {
    private Activity mActivity;
    private Boolean mCloseable = true;
    private String mCurrentPath;
    private Boolean mDirectoriesFilter = false;
    private Pattern mFileFilter;
    private Class<? extends FilePickerActivity> mFilePickerClass = FilePickerActivity.class;
    private Fragment mFragment;
    private Integer mRequestCode;
    private String mRootPath;
    private Boolean mShowHidden = false;
    private androidx.fragment.app.Fragment mSupportFragment;
    private CharSequence mTitle;

    public MaterialFilePicker withActivity(Activity activity) {
        if (mSupportFragment == null && mFragment == null) {
            mActivity = activity;
            return this;
        }
        throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
    }

    public MaterialFilePicker withFragment(Fragment fragment) {
        if (mSupportFragment == null && mActivity == null) {
            mFragment = fragment;
            return this;
        }
        throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
    }

    public MaterialFilePicker withSupportFragment(androidx.fragment.app.Fragment fragment) {
        if (mActivity == null && mFragment == null) {
            mSupportFragment = fragment;
            return this;
        }
        throw new RuntimeException("You must pass either Activity, Fragment or SupportFragment");
    }

    public MaterialFilePicker withRequestCode(int i) {
        mRequestCode = i;
        return this;
    }

    public MaterialFilePicker withFilter(Pattern pattern) {
        mFileFilter = pattern;
        return this;
    }

    public MaterialFilePicker withFilterDirectories(boolean z) {
        mDirectoriesFilter = z;
        return this;
    }

    public MaterialFilePicker withRootPath(String str) {
        mRootPath = str;
        return this;
    }

    public MaterialFilePicker withPath(String str) {
        mCurrentPath = str;
        return this;
    }

    public MaterialFilePicker withHiddenFiles(boolean z) {
        mShowHidden = z;
        return this;
    }

    public MaterialFilePicker withCloseMenu(boolean z) {
        mCloseable = z;
        return this;
    }

    public MaterialFilePicker withTitle(CharSequence charSequence) {
        mTitle = charSequence;
        return this;
    }

    public MaterialFilePicker withCustomActivity(Class<? extends FilePickerActivity> cls) {
        mFilePickerClass = cls;
        return this;
    }

    public void start() {
        if (mActivity == null && mFragment == null && mSupportFragment == null) {
            throw new RuntimeException("You must pass Activity/Fragment by calling withActivity/withFragment/withSupportFragment method");
        } else if (mRequestCode != null) {
            Intent intent = getIntent();
            Activity activity = mActivity;
            if (activity != null) {
                activity.startActivityForResult(intent, mRequestCode);
                return;
            }
            Fragment fragment = mFragment;
            if (fragment != null) {
                fragment.startActivityForResult(intent, mRequestCode);
            } else {
                mSupportFragment.startActivityForResult(intent, mRequestCode);
            }
        } else {
            throw new RuntimeException("You must pass request code by calling withRequestCode method");
        }
    }

    private CompositeFilter getFilter() {
        ArrayList<FileFilter> arrayList = new ArrayList<>();
        if (!mShowHidden) {
            arrayList.add(new HiddenFilter());
        }
        Pattern pattern = mFileFilter;
        if (pattern != null) {
            arrayList.add(new PatternFilter(pattern, mDirectoriesFilter));
        }
        return new CompositeFilter(arrayList);
    }

    private Intent getIntent() {
        CompositeFilter filter = getFilter();
        Context context = mActivity;
        if (context == null) {
            Fragment fragment = mFragment;
            if (fragment != null) {
                context = fragment.getActivity();
            } else {
                androidx.fragment.app.Fragment fragment2 = mSupportFragment;
                context = fragment2 != null ? fragment2.getActivity() : null;
            }
        }
        Intent intent = new Intent(context, mFilePickerClass);
        intent.putExtra(FilePickerActivity.ARG_FILTER, filter);
        intent.putExtra(FilePickerActivity.ARG_CLOSEABLE, mCloseable);
        if (mRootPath != null) {
            intent.putExtra(FilePickerActivity.ARG_START_FILE, new File(mRootPath));
        }
        if (mCurrentPath != null) {
            intent.putExtra(FilePickerActivity.ARG_CURRENT_FILE, new File(mCurrentPath));
        }
        CharSequence charSequence = mTitle;
        if (charSequence != null) {
            intent.putExtra(FilePickerActivity.ARG_TITLE, charSequence);
        }
        return intent;
    }
}
