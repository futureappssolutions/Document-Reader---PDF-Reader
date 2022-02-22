package com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;

public class ActSecurityQuestionAnswer extends BaseActivity {
    private EditText answerEt;
    private SharedPrefs prefs;
    private TextView textCounterTv;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_security_answer);
        changeBackGroundColor(100);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.toolBarTitle)).setText(getResources().getString(R.string.verification));

        TextView questionTv = findViewById(R.id.questionTv);
        answerEt = findViewById(R.id.answerEt);
        textCounterTv = findViewById(R.id.textCounterTv);

        prefs = new SharedPrefs(this);

        answerEt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @SuppressLint("SetTextI18n")
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                textCounterTv.setText(answerEt.length() + "/10");
            }
        });

        questionTv.setText(getResources().getStringArray(R.array.security_question_arrays)[prefs.getSelectedSecurityQuestion()]);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.security_question_menu, menu);
        menu.findItem(R.id.nav_save).setTitle(getResources().getString(R.string.ok));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {
            onBackPressed();
        } else if (itemId == R.id.nav_save) {
            if (answerEt.getText().toString().trim().length() <= 0) {
                Utility.Toast(this, getResources().getString(R.string.enteryouAnswerFirst));
            } else if (answerEt.getText().toString().trim().toLowerCase().equals(prefs.getSecurityQuestionAnswer())) {
                Intent intent = new Intent(this, ActPassword.class);
                intent.putExtra("flag", ActPassword.SET_NEW_PASSWORD);
                startActivity(intent);
                finish();
            } else {
                Utility.Toast(this, getResources().getString(R.string.answerDoesNotMatched));
            }
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
