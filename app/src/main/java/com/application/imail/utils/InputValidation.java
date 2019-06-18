package com.application.imail.utils;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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