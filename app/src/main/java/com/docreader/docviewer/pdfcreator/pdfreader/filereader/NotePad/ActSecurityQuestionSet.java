package com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ActSecurityQuestionSet extends BaseActivity {
    private EditText answerEt;
    private SharedPrefs prefs;
    private Spinner question_spinner;
    private TextView textCounterTv;
    private String userPassword = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_security_question);
        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.securityQuestion));

        question_spinner = findViewById(R.id.question_spinner);
        answerEt = findViewById(R.id.answerEt);
        textCounterTv = findViewById(R.id.textCounterTv);

        prefs = new SharedPrefs(this);

        if (getIntent() != null) {
            userPassword = getIntent().getStringExtra("password");
        }

        answerEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TextView textView = textCounterTv;
                textView.setText(answerEt.length() + "/10");
            }
        });

        answerEt.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getAction() != 0 || i != 66) {
                return false;
            }
            setSecurityQuestion();
            return true;
        });
    }

    public void setSecurityQuestion() {
        if (answerEt.getText().toString().trim().length() > 0) {
            prefs.setSecurityQuestionAnswer(answerEt.getText().toString().toLowerCase().trim());
            prefs.setSelectedSecurityQuestion(question_spinner.getSelectedItemPosition());
            prefs.setNotepadPassword(userPassword);
            prefs.setPasswordOnNotepad(true);
            Utility.Toast(this, getResources().getString(R.string.saved));
            finish();
            return;
        }
        Utility.Toast(this, getResources().getString(R.string.enteryouAnswerFirst));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.security_question_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.nav_save) {
            setSecurityQuestion();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
