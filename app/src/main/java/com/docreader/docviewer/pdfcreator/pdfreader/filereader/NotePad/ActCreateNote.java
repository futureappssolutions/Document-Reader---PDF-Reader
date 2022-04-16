package com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Ads.GoogleAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.CvMaker.CvActivity.ScreenCVEdit;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Database.DatabaseHelper;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.GetSet.NotepadItemModel;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Objects;

public class ActCreateNote extends BaseActivity implements View.OnClickListener {
    public boolean isChangingMade = false;
    public int colorThem = 100;
    public int pin = 0;
    public LinearLayout backgroundLayout;
    public BottomSheetDialog bottomSheetDialog;
    public TextView editedDateTime;
    public String f798id = "0";
    public ImageView pinImageView;
    public SharedPrefs prefs;
    public DatabaseHelper dbhelper;
    public MenuItem removeMenuItem;
    public MenuItem pinMenuItem;
    public EditText titleEditText;
    public EditText contentEditText;
    public NotepadItemModel note;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_create_note);

        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        prefs = new SharedPrefs(this);

        LinearLayout ll_banner = findViewById(R.id.ll_banner);
        GoogleAds.showBannerAds(ActCreateNote.this, ll_banner);

        titleEditText = findViewById(R.id.TitleEditText);
        contentEditText = findViewById(R.id.ContentEditText);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        editedDateTime = findViewById(R.id.editedDateTime);
        pinImageView = findViewById(R.id.pinImageView);

        titleEditText.setTypeface(Typeface.MONOSPACE);
        contentEditText.setTypeface(Typeface.MONOSPACE);

        dbhelper = new DatabaseHelper(getApplicationContext());

        f798id = getIntent().getStringExtra(ScreenCVEdit.FIELD_ID);
        if (!f798id.equals("0") && (note = dbhelper.getNote(f798id)) != null) {
            titleEditText.setText(note.getTitle());
            contentEditText.setText(note.getContent());
            pin = note.getPin();
            if (note.getPin() != 0) {
                pinImageView.setVisibility(View.VISIBLE);
            }

            editedDateTime.setText(Html.fromHtml(getResources().getString(R.string.edited) + Utility.geDateTime(Long.parseLong(note.getDate()))));
            colorThem = note.getThem();
        }

        changeBackgroundAndThemColor();

        titleEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                isChangingMade = true;
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                isChangingMade = true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_notepad, menu);
        pinMenuItem = menu.findItem(R.id.ic_pin);
        removeMenuItem = menu.findItem(R.id.ic_remove);
        if (pin == 0) {
            pinMenuItem.setTitle(getResources().getString(R.string.pin));
        } else {
            pinMenuItem.setTitle(getResources().getString(R.string.unPin));
        }
        if (!f798id.equals("0")) {
            removeMenuItem.setVisible(true);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
            case R.id.ic_change_color:
                changeBgColor();
                break;
            case R.id.ic_pin:
                updatePin();
                break;
            case R.id.ic_remove:
                new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.remove)).setMessage(getResources().getString(R.string.areYouSureToRemove)).setPositiveButton(getResources().getString(R.string.remove), (dialogInterface, i) -> {
                    dbhelper.removeNote(f798id);
                    dialogInterface.dismiss();
                    finish();
                }).setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
                break;
            case R.id.ic_save:
                saveFileToDataBase();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void updatePin() {
        if (pin == 0) {
            pin = 1;
            pinImageView.setVisibility(View.VISIBLE);
            pinMenuItem.setTitle(getResources().getString(R.string.unPin));
        } else {
            pin = 0;
            pinImageView.setVisibility(View.GONE);
            pinMenuItem.setTitle(getResources().getString(R.string.pin));
        }
        updateFile();
    }

    @SuppressLint("ResourceType")
    private void changeBgColor() {
        bottomSheetDialog = new BottomSheetDialog(ActCreateNote.this);
        @SuppressLint("InflateParams") View inflate = getLayoutInflater().inflate(R.layout.bottomsheet_notepad_color, null);
        bottomSheetDialog.setContentView(inflate);

        inflate.findViewById(R.id.image1).setOnClickListener(this);
        inflate.findViewById(R.id.image2).setOnClickListener(this);
        inflate.findViewById(R.id.image3).setOnClickListener(this);
        inflate.findViewById(R.id.image4).setOnClickListener(this);
        inflate.findViewById(R.id.image5).setOnClickListener(this);
        inflate.findViewById(R.id.image6).setOnClickListener(this);
        inflate.findViewById(R.id.image7).setOnClickListener(this);
        inflate.findViewById(R.id.image8).setOnClickListener(this);
        inflate.findViewById(R.id.image9).setOnClickListener(this);
        inflate.findViewById(R.id.image10).setOnClickListener(this);
        inflate.findViewById(R.id.btnCancel).setOnClickListener(this);

        bottomSheetDialog.show();
    }

    public void onBackPressed() {
        if (!isChangingMade) {
            finish();
        } else if (titleEditText.getText().toString().trim().length() == 0 && contentEditText.getText().toString().trim().length() == 0) {
            finish();
        } else {
            saveAlertDialog();
        }
    }


    public void saveAlertDialog() {
        new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.save)).setMessage(getResources().getString(R.string.doYouWantToSave)).setPositiveButton(getResources().getString(R.string.save), (dialogInterface, i) -> {
            saveFileToDataBase();
            dialogInterface.dismiss();
        }).setNegativeButton(getResources().getString(R.string.dontSave), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            finish();
        }).setNeutralButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss()).create().show();
    }


    public void saveFileToDataBase() {
        if (titleEditText.getText().toString().trim().length() == 0 || contentEditText.getText().toString().trim().length() == 0) {
            if (titleEditText.getText().toString().trim().length() == 0) {
                Utility.Toast(this, getResources().getString(R.string.enterTitleFirst));
            } else {
                Utility.Toast(this, getResources().getString(R.string.enterContentFirst));
            }
        } else if (f798id.equals("0")) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            dbhelper = databaseHelper;
            databaseHelper.addNote(titleEditText.getText().toString(), contentEditText.getText().toString(), colorThem, pin);
            Utility.Toast(this, getResources().getString(R.string.saved));
            finish();
        } else {
            dbhelper.updateNote(f798id, titleEditText.getText().toString(), contentEditText.getText().toString(), colorThem, pin);
            Utility.Toast(this, getResources().getString(R.string.saved));
            finish();
        }
    }


    public void updateFile() {
        if (!(titleEditText.getText().toString().trim().length() == 0 && contentEditText.getText().toString().trim().length() == 0) && !f798id.equals("0")) {
            dbhelper.updateNote(f798id, titleEditText.getText().toString(), contentEditText.getText().toString(), colorThem, pin);
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.btnCancel) {
            switch (id) {
                case R.id.image1:
                    colorThem = 101;
                    break;
                case R.id.image10:
                    colorThem = 1010;
                    break;
                case R.id.image2:
                    colorThem = 102;
                    break;
                case R.id.image3:
                    colorThem = 103;
                    break;
                case R.id.image4:
                    colorThem = 104;
                    break;
                case R.id.image5:
                    colorThem = 105;
                    break;
                case R.id.image6:
                    colorThem = 106;
                    break;
                case R.id.image7:
                    colorThem = 107;
                    break;
                case R.id.image8:
                    colorThem = 108;
                    break;
                case R.id.image9:
                    colorThem = 109;
                    break;
            }
        } else {
            bottomSheetDialog.dismiss();
        }
        changeBackgroundAndThemColor();

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
    }


    public void changeBackgroundAndThemColor() {
        changeBackGroundColor(colorThem);
        int i = colorThem;
        if (i != 1010) {
            switch (i) {
                case 101:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent1));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent1));
                    break;
                case 102:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent2));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent2));
                    break;
                case 103:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent3));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent3));
                    break;
                case 104:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent4));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent4));
                    break;
                case 105:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent5));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent5));
                    break;
                case 106:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent6));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent6));
                    break;
                case 107:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent7));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent7));
                    break;
                case 108:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent8));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent8));
                    break;
                case 109:
                    contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent9));
                    backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent9));
                    break;
            }
        } else {
            contentEditText.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent10));
            backgroundLayout.setBackgroundColor(getResources().getColor(R.color.notepad_color_transparent10));
        }
        updateFile();
    }
}
