package com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad;

import android.content.Intent;
import android.os.Bundle;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Activity.BaseActivity;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.office.res.ResConstant;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.SharedPrefs;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.Utils.Utility;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.View.PasscodeView;

public class ActPassword extends BaseActivity {
    public static String CHANGE_PASSWORD = "changePassword";
    public static String PASSWORD = "Password";
    public static String REMOVE_PASSWORD = "removePassword";
    public static String SET_NEW_PASSWORD = "setNewPassword";
    public String flagValue = "";

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().getDecorView().setSystemUiVisibility(1280);
        getWindow().setStatusBarColor(0);
        setContentView(R.layout.activity_passcode);

        if (getIntent() != null) {
            flagValue = getIntent().getStringExtra("flag");
        }

        final SharedPrefs sharedPrefs = new SharedPrefs(this);

        final PasscodeView passcodeView = findViewById(R.id.passcodeView);

        if (flagValue.equals(SET_NEW_PASSWORD)) {
            passcodeView.setPasscodeType(0);
            passcodeView.setFirstInputTip(getResources().getString(R.string.enterFourDigitPassword));
        } else if (flagValue.equals(REMOVE_PASSWORD)) {
            passcodeView.setPasscodeType(1);
            passcodeView.setLocalPasscode(sharedPrefs.getNotepadPassword());
            passcodeView.setFirstInputTip("To Remove Password, Enter Your Password First");
        } else if (flagValue.equals(CHANGE_PASSWORD)) {
            passcodeView.setPasscodeType(1);
            passcodeView.setLocalPasscode(sharedPrefs.getNotepadPassword());
            passcodeView.setFirstInputTip("To Change Password, Enter Old Password First");
        } else {
            passcodeView.enableForgetPassword(this);
            passcodeView.setPasscodeType(1);
            passcodeView.setFirstInputTip(ResConstant.DIALOG_ENTER_PASSWORD);
            passcodeView.setWrongInputTip("Wrong Password..Try Again");
            passcodeView.setLocalPasscode(sharedPrefs.getNotepadPassword());
        }

        passcodeView.setPasscodeLength(4).setListener(new PasscodeView.PasscodeViewListener() {
            public void onFail(String str) {
            }

            public void onSuccess(String str) {
                if (flagValue.equals(ActPassword.SET_NEW_PASSWORD)) {
                    if (sharedPrefs.getSecurityQuestionAnswer() == null) {
                        Intent intent = new Intent(ActPassword.this, ActSecurityQuestionSet.class);
                        intent.putExtra("password", str);
                        startActivity(intent);
                    } else {
                        sharedPrefs.setNotepadPassword(str);
                        sharedPrefs.setPasswordOnNotepad(true);
                        Utility.Toast(getApplication(), getResources().getString(R.string.passwordHasbeenSet));
                    }
                } else if (flagValue.equals(ActPassword.REMOVE_PASSWORD)) {
                    sharedPrefs.setPasswordOnNotepad(false);
                    sharedPrefs.setNotepadPassword(null);
                    Utility.Toast(getApplication(), "Password Has Been Removed");
                } else if (flagValue.equals(ActPassword.CHANGE_PASSWORD)) {
                    flagValue = ActPassword.SET_NEW_PASSWORD;
                    passcodeView.deleteAllChars();
                    passcodeView.setPasscodeType(0);
                    passcodeView.setFirstInputTip("Enter Your New Password");
                    return;
                } else if (flagValue.equals(ActPassword.PASSWORD)) {
                    startActivity(new Intent(ActPassword.this, ActNotepadList.class));
                }
                finish();
            }
        });
    }
}
