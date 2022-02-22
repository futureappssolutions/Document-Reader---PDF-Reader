package com.docreader.docviewer.pdfcreator.pdfreader.filereader.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad.ActPassword;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.NotePad.ActSecurityQuestionAnswer;
import com.docreader.docviewer.pdfcreator.pdfreader.filereader.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PasscodeView extends FrameLayout implements View.OnClickListener {
    public int correctStatusColor;
    public int normalStatusColor;
    public int wrongStatusColor;
    public int numberTextColor;
    public int passcodeLength;
    public int passcodeType;
    public boolean secondInput;
    public String correctInputTip;
    public String wrongInputTip;
    public String firstInputTip;
    public String localPasscode;
    public String secondInputTip;
    public String wrongLengthTip;
    public TextView tv_input_tip;
    public TextView number0;
    public TextView number1;
    public TextView number2;
    public TextView number3;
    public TextView number4;
    public TextView number5;
    public TextView number6;
    public TextView number7;
    public TextView number8;
    public TextView number9;
    public ImageView numberB;
    public ImageView numberOK;
    public View cursor;
    public ImageView iv_lock;
    public ImageView iv_ok;
    public ViewGroup layout_psd;
    public PasscodeViewListener listener;

    public PasscodeView(Context context) {
        this(context, null);
    }

    @SuppressLint("ResourceType")
    public PasscodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        localPasscode = "";
        firstInputTip = "Enter a passcode of 4 digits";
        secondInputTip = "Re-enter new passcode";
        wrongLengthTip = "Enter a passcode of 4 digits";
        wrongInputTip = "Passcode do not match";
        correctInputTip = "Passcode is correct";
        passcodeLength = 4;
        correctStatusColor = -10369696;
        wrongStatusColor = -901035;
        normalStatusColor = -1;
        numberTextColor = -9145228;
        passcodeType = 0;
        inflate(getContext(), R.layout.layout_passcode_view, this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PasscodeView);
        try {
            passcodeType = obtainStyledAttributes.getInt(6, passcodeType);
            passcodeLength = obtainStyledAttributes.getInt(5, passcodeLength);
            normalStatusColor = obtainStyledAttributes.getColor(3, normalStatusColor);
            wrongStatusColor = obtainStyledAttributes.getColor(10, wrongStatusColor);
            correctStatusColor = obtainStyledAttributes.getColor(1, correctStatusColor);
            numberTextColor = obtainStyledAttributes.getColor(4, numberTextColor);
            firstInputTip = obtainStyledAttributes.getString(2);
            secondInputTip = obtainStyledAttributes.getString(7);
            wrongLengthTip = obtainStyledAttributes.getString(9);
            wrongInputTip = obtainStyledAttributes.getString(8);
            correctInputTip = obtainStyledAttributes.getString(0);
            obtainStyledAttributes.recycle();
            String str = firstInputTip;
            firstInputTip = str == null ? context.getString(R.string.enterFourDigitPassword) : str;
            String str2 = secondInputTip;
            secondInputTip = str2 == null ? context.getString(R.string.reEnterNewPassword) : str2;
            String str3 = wrongLengthTip;
            wrongLengthTip = str3 == null ? firstInputTip : str3;
            String str4 = wrongInputTip;
            wrongInputTip = str4 == null ? context.getString(R.string.passwordDoNotMatch) : str4;
            String str5 = correctInputTip;
            correctInputTip = str5 == null ? context.getString(R.string.passwordMatch) : str5;
            init();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private void init() {
        layout_psd = findViewById(R.id.layout_psd);
        tv_input_tip = findViewById(R.id.tv_input_tip);
        cursor = findViewById(R.id.cursor);
        iv_lock = findViewById(R.id.iv_lock);
        iv_ok = findViewById(R.id.iv_ok);
        tv_input_tip.setText(firstInputTip);
        number0 = findViewById(R.id.number0);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        numberOK = findViewById(R.id.numberOK);
        numberB = findViewById(R.id.numberB);
        number0.setOnClickListener(this);
        number1.setOnClickListener(this);
        number2.setOnClickListener(this);
        number3.setOnClickListener(this);
        number4.setOnClickListener(this);
        number5.setOnClickListener(this);
        number6.setOnClickListener(this);
        number7.setOnClickListener(this);
        number8.setOnClickListener(this);
        number9.setOnClickListener(this);

        numberB.setOnClickListener(view -> deleteChar());

        numberB.setOnLongClickListener(view -> {
            deleteAllChars();
            return true;
        });

        numberOK.setOnClickListener(view -> next());

        tintImageView(numberB, numberTextColor);
        tintImageView(numberOK, numberTextColor);
        tintImageView(iv_ok, correctStatusColor);

        number0.setTag(0);
        number1.setTag(1);
        number2.setTag(2);
        number3.setTag(3);
        number4.setTag(4);
        number5.setTag(5);
        number6.setTag(6);
        number7.setTag(7);
        number8.setTag(8);
        number9.setTag(9);

        number0.setTextColor(numberTextColor);
        number1.setTextColor(numberTextColor);
        number2.setTextColor(numberTextColor);
        number3.setTextColor(numberTextColor);
        number4.setTextColor(numberTextColor);
        number5.setTextColor(numberTextColor);
        number6.setTextColor(numberTextColor);
        number7.setTextColor(numberTextColor);
        number8.setTextColor(numberTextColor);
        number9.setTextColor(numberTextColor);
    }

    public void onClick(View view) {
        addChar((Integer) view.getTag());
    }

    public String getLocalPasscode() {
        return localPasscode;
    }

    public PasscodeView setLocalPasscode(String str) {
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt < '0' || charAt > '9') {
                throw new RuntimeException("must be number digit");
            }
        }
        localPasscode = str;
        passcodeType = 1;
        return this;
    }

    public PasscodeViewListener getListener() {
        return listener;
    }

    public PasscodeView setListener(PasscodeViewListener passcodeViewListener) {
        listener = passcodeViewListener;
        return this;
    }

    public String getFirstInputTip() {
        return firstInputTip;
    }

    public PasscodeView setFirstInputTip(String str) {
        firstInputTip = str;
        TextView textView = tv_input_tip;
        if (textView != null) {
            textView.setText(str);
        }
        return this;
    }

    public String getSecondInputTip() {
        return secondInputTip;
    }

    public PasscodeView setSecondInputTip(String str) {
        secondInputTip = str;
        TextView textView = tv_input_tip;
        if (textView != null) {
            textView.setText(str);
        }
        return this;
    }

    public String getWrongLengthTip() {
        return wrongLengthTip;
    }

    public PasscodeView setWrongLengthTip(String str) {
        wrongLengthTip = str;
        return this;
    }

    public String getWrongInputTip() {
        return wrongInputTip;
    }

    public PasscodeView setWrongInputTip(String str) {
        wrongInputTip = str;
        return this;
    }

    public String getCorrectInputTip() {
        return correctInputTip;
    }

    public PasscodeView setCorrectInputTip(String str) {
        correctInputTip = str;
        return this;
    }

    public int getPasscodeLength() {
        return passcodeLength;
    }

    public PasscodeView setPasscodeLength(int i) {
        passcodeLength = i;
        return this;
    }

    public int getCorrectStatusColor() {
        return correctStatusColor;
    }

    public PasscodeView setCorrectStatusColor(int i) {
        correctStatusColor = i;
        return this;
    }

    public int getWrongStatusColor() {
        return wrongStatusColor;
    }

    public PasscodeView setWrongStatusColor(int i) {
        wrongStatusColor = i;
        return this;
    }

    public int getNormalStatusColor() {
        return normalStatusColor;
    }

    public PasscodeView setNormalStatusColor(int i) {
        normalStatusColor = i;
        return this;
    }

    public int getNumberTextColor() {
        return numberTextColor;
    }

    public PasscodeView setNumberTextColor(int i) {
        numberTextColor = i;
        return this;
    }

    public int getPasscodeType() {
        return passcodeType;
    }

    public PasscodeView setPasscodeType(int i) {
        passcodeType = i;
        return this;
    }

    public boolean equals(String str) {
        return localPasscode.equals(str);
    }

    public void next() {
        if (passcodeType != 1 || !TextUtils.isEmpty(localPasscode)) {
            String passcodeFromView = getPasscodeFromView();
            if (passcodeFromView.length() != passcodeLength) {
                tv_input_tip.setText(wrongLengthTip);
                runTipTextAnimation();
            } else if (passcodeType == 0 && !secondInput) {
                tv_input_tip.setText(secondInputTip);
                localPasscode = passcodeFromView;
                clearChar();
                secondInput = true;
            } else if (equals(passcodeFromView)) {
                runOkAnimation();
            } else {
                runWrongAnimation();
            }
        } else {
            throw new RuntimeException("must set localPasscode when type is TYPE_CHECK_PASSCODE");
        }
    }

    private void addChar(int i) {
        if (layout_psd.getChildCount() < passcodeLength) {
            CircleView circleView = new CircleView(getContext());
            int dpToPx = dpToPx(8.0f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx, dpToPx);
            layoutParams.setMargins(dpToPx, 0, dpToPx, 0);
            circleView.setLayoutParams(layoutParams);
            circleView.setColor(normalStatusColor);
            circleView.setTag(i);
            layout_psd.addView(circleView);
            if (layout_psd.getChildCount() == 4) {
                next();
            }
        }
    }

    @SuppressLint("WrongConstant")
    private int dpToPx(float f) {
        return (int) TypedValue.applyDimension(1, f, getResources().getDisplayMetrics());
    }

    private void tintImageView(ImageView imageView, int i) {
        imageView.getDrawable().mutate().setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
    }

    private void clearChar() {
        layout_psd.removeAllViews();
    }

    public void deleteChar() {
        int childCount = layout_psd.getChildCount();
        if (childCount > 0) {
            layout_psd.removeViewAt(childCount - 1);
        }
    }

    public void deleteAllChars() {
        if (layout_psd.getChildCount() > 0) {
            layout_psd.removeAllViews();
        }
    }

    public void runTipTextAnimation() {
        shakeAnimator(tv_input_tip).start();
    }

    public void runWrongAnimation() {
        cursor.setTranslationX(0.0f);
        cursor.setVisibility(View.VISIBLE);
        cursor.animate().translationX((float) layout_psd.getWidth()).setDuration(600).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                cursor.setVisibility(View.INVISIBLE);
                tv_input_tip.setText(wrongInputTip);
                PasscodeView passcodeView = PasscodeView.this;
                passcodeView.setPSDViewBackgroundResource(passcodeView.wrongStatusColor);
                PasscodeView passcodeView2 = PasscodeView.this;
                Animator access$800 = passcodeView2.shakeAnimator(passcodeView2.layout_psd);
                access$800.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        setPSDViewBackgroundResource(normalStatusColor);
                        deleteAllChars();
                        if (secondInput && listener != null) {
                            listener.onFail(getPasscodeFromView());
                        }
                    }
                });
                access$800.start();
            }
        }).start();
    }

    public Animator shakeAnimator(View view) {
        return ObjectAnimator.ofFloat(view, "translationX", 0.0f, 25.0f, -25.0f, 25.0f, -25.0f, 15.0f, -15.0f, 6.0f, -6.0f, 0.0f).setDuration(500);
    }

    public void setPSDViewBackgroundResource(int i) {
        int childCount = layout_psd.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            ((CircleView) layout_psd.getChildAt(i2)).setColor(i);
        }
    }

    public void runOkAnimation() {
        cursor.setTranslationX(0.0f);
        cursor.setVisibility(View.VISIBLE);
        cursor.animate().setDuration(600).translationX((float) layout_psd.getWidth()).setListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                cursor.setVisibility(View.INVISIBLE);
                PasscodeView passcodeView = PasscodeView.this;
                passcodeView.setPSDViewBackgroundResource(passcodeView.correctStatusColor);
                tv_input_tip.setText(correctInputTip);
                iv_lock.animate().alpha(0.0f).scaleX(0.0f).scaleY(0.0f).setDuration(200).start();
                iv_ok.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        super.onAnimationEnd(animator);
                        if (listener != null) {
                            listener.onSuccess(getPasscodeFromView());
                        }
                    }
                }).start();
            }
        }).start();
    }

    public String getPasscodeFromView() {
        StringBuilder sb = new StringBuilder();
        int childCount = layout_psd.getChildCount();
        for (int i = 0; i < childCount; i++) {
            sb.append(((Integer) layout_psd.getChildAt(i).getTag()).intValue());
        }
        return sb.toString();
    }

    public void enableForgetPassword(final ActPassword activityPassword) {
        findViewById(R.id.tv_forget_password).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_forget_password).setOnClickListener(view -> {
            activityPassword.startActivity(new Intent(activityPassword, ActSecurityQuestionAnswer.class));
            activityPassword.finish();
        });
    }


    @Retention(RetentionPolicy.SOURCE)
    public @interface PasscodeViewType {
        int TYPE_CHECK_PASSCODE = 1;
        int TYPE_SET_PASSCODE = 0;
    }

    public interface PasscodeViewListener {
        void onFail(String str);

        void onSuccess(String str);
    }
}
