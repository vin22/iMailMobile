package com.application.imail.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidation {
    private Context context;

    /**
     * constructor
     *
     * @param context
     */
    public InputValidation(Context context) {
        this.context = context;
    }
    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }
    /**
     * method to check InputEditText has valid norekening .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextNoRekExist(TextInputEditText textInputEditText, TextInputLayout textInputLayout,boolean isnorekexist, String message) {
        if (!isnorekexist) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    //no rekening pengirim dan penerima tidak boleh sama
    public boolean isInputEditTextOtherNoRek(TextInputEditText textInputEditText, TextInputLayout textInputLayout,boolean other, String message) {
        if (!other) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    //Transfer Minimum 50000
    public boolean isInputEditTextDanaMin(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Integer uang=Integer.parseInt(value);
        if (uang<50000) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    //Transfer Minimum berkelipatan 50000
    public boolean isInputEditTextDanaMinKelipatan(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Integer uang=Integer.parseInt(value);
        if (uang%50000!=0) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    //panjang no hp 10 sampai 12
    public boolean isInputEditTextNoHp(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length()<9) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang PIN 6
    public boolean isInputEditTextPIN(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length()!=6) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang Password 8
    public boolean isInputEditTextLengthPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length()<8) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang Name 2
    public boolean isInputEditTextLengthName(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length()<2) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }

    //panjang Username first alphabet
    public boolean isInputEditTextFirstAlphabetEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().substring(0,1);
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^[a-zA-Z]+$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang Username
    public boolean isInputEditTextValidEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=\\S+$).{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }

    //panjang Username
    public boolean isInputEditTextValidPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }

    //panjang Username
    public boolean isInputEditTextAlphabetEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang Username alphabet and char
    public boolean isInputEditTextNumberEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "(?=.*?[0-9])";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //panjang Email 6
    public boolean isInputEditTextLengthEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        if (value.length()<6) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //Password valid
    public boolean isInputEditTextisValidFirstStringPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //Password valid
    public boolean isInputEditTextisValidNumberPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "(?=.*[0-9])";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }

    //Password valid
    public boolean isInputEditTextisValidUpperPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "(?=.*[A-Z])";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }
    //Password valid
    public boolean isInputEditTextisValidLowerPassword(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        Pattern pattern;
        Matcher matcher;

//        final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[A-Za-z])(?=\\S+$)";
        final String PASSWORD_PATTERN = "(?=.*[a-z])";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        return true;
    }

    //Password lama dan Password baru wajib berbeda
    public boolean isInputEditTextSamePassword(TextInputEditText textInputEditText, TextInputEditText textInputEditText1, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        String value1 = textInputEditText1.getText().toString().trim();
        if (!value.equals(value1)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            hideKeyboardFrom(textInputEditText1);
            return false;
        }
        return true;
    }
    //PIN lama dan PIN baru wajib berbeda
    public boolean isInputEditTextDifferentPIN(TextInputEditText textInputEditText, TextInputEditText textInputEditText1, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
        String value1 = textInputEditText1.getText().toString().trim();
        if (value.equals(value1)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            hideKeyboardFrom(textInputEditText1);
            return false;
        }
        return true;
    }
    //Saldo minimum setelah transaksi wajib 200000
    public boolean isInputEditTextDanaMax(Integer batas, TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        if (batas<200000) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    /**
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}