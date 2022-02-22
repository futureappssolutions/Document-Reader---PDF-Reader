package com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

import java.util.Objects;

public class OCREditText extends BaseActivity {
    private EditText ocrTextView;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_ocr_edit_text);
        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ocrTextView = findViewById(R.id.ocrTextView);

        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.editText));

        if (getIntent() != null) {
            ocrTextView.setText(getIntent().getStringExtra("ocrText"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ocr_edit_text, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.action_copy) {
            copyTextToClipBoard();
        } else if (itemId == R.id.action_share) {
            shareOCRText();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void copyTextToClipBoard() {
        ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("label", ocrTextView.getText().toString()));
        Utility.Toast(this, getResources().getString(R.string.copyTextToClipBoard));
    }

    private void shareOCRText() {
        try {
            String obj = ocrTextView.getText().toString();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.TEXT", obj);
            startActivity(Intent.createChooser(intent, getResources().getString(R.string.shareVia)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
