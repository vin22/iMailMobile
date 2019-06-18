package com.application.imail.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.R;
import com.application.imail.RegisterActivity;
import com.application.imail.config.SessionManager;
import com.application.imail.model.User;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.UserService;
import com.application.imail.utils.InputValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AkunAlternatifActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = AkunAlternatifActivity.this;
    User user;
    SessionManager userConfig;
    UserService userService;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutAkunAlternatif;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextAkunAlternatif;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonAdd;
    ProgressDialog progress;
    InputValidation inputValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_alternatif);
        initViews();
        initListeners();
        initObjects();
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutAkunAlternatif = (TextInputLayout) findViewById(R.id.textInputLayoutAkunAlternatif);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextAkunAlternatif = (TextInputEditText) findViewById(R.id.textInputEditTextAkunAlternatif);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonAdd = (AppCompatButton) findViewById(R.id.appCompatButtonAdd);
    }
//    /**
//     * This method is to initialize listeners
//     */
    private void initListeners() {
        appCompatButtonAdd.setOnClickListener(this);
        userConfig = new SessionManager(this);
        userService = APIUtils.getUserService();
        inputValidation = new InputValidation(this);
    }
//    /**
//     * This method is to initialize objects to be used
//     */
    private void initObjects() {
        progress = new ProgressDialog(activity);
        user=new User();
    }
    /**
     * This implemented method is to listen the click on view
     *
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //jika klik button login akan melakukan proses validasi
            case R.id.appCompatButtonLogin:
                if (!inputValidation.isInputEditTextFilled(textInputEditTextAkunAlternatif, textInputLayoutAkunAlternatif, getString(R.string.error_message_akunalternatif))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_passwordkosong))) {
                    return;
                }
//                if(textInputEditTextUsername.getText().toString().equals("") && textInputEditTextPassword.getText().toString().equals("")){
//                    emptyInputEditText();
//                }
                else {
                    SessionManager sessionManager = SessionManager.with(AkunAlternatifActivity.this);
                    Call<User> call = userService.addakunalternatif(sessionManager.getuserloggedin().getEmail(), textInputEditTextPassword.getText().toString(),textInputEditTextAkunAlternatif.getText().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
    //                            int statuscode = response.code();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(AkunAlternatifActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    finish();
                                    emptyInputEditText();

                                } else {
                                    Toast.makeText(AkunAlternatifActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                                else{
                                Toast.makeText(AkunAlternatifActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("USER ACTIVITY ERROR", t.getMessage());
                        }
                    });
//                    if (textInputEditTextUsername.getText().toString().equals("vincent@email.com") || textInputEditTextUsername.getText().toString().equals("vin_22") && textInputEditTextPassword.getText().toString().equals("vincent")) {
//                        Log.e("Login", "Masuk");
//                        SessionManager sessionManager = SessionManager.with(this);
//                        if (!SessionManager.with(this).isuserlogin()) {
//                            user.setEmail("vincent@email.com");
//                            user.setUsername("vin22");
//                            user.setName("Vincent");
//                            user.setPassword("vincent");
//                            sessionManager.createsession(user);
//                        }
//                        Intent intent = new Intent(LoginActivity.this, InboxActivity.class);
//                        startActivity(intent);
//
//                    } else if (textInputEditTextUsername.getText().toString().equals("jefri@email.com") || textInputEditTextUsername.getText().toString().equals("jef_21") && textInputEditTextPassword.getText().toString().equals("jefri")) {
//                        Log.e("Login", "Masuk");
//                        SessionManager sessionManager = SessionManager.with(this);
//                        if (!SessionManager.with(this).isuserlogin()) {
//                            user.setEmail("jefri@email.com");
//                            user.setUsername("jef_21");
//                            user.setName("Jefri");
//                            user.setPassword("jefri");
//                            sessionManager.createsession(user);
//                        }
//                        Intent intent = new Intent(LoginActivity.this, InboxActivity.class);
//                        startActivity(intent);
//                        emptyInputEditText();
//                    }
//                verifyFromSQLite();
                    break;
                }
        }
    }
//    /**
//     * This method is to validate the input text fields and verify login credentials from SQLite
//     */
//    private void verifyFromSQLite(){
//        textInputLayoutUsername.setErrorEnabled(false);
//        textInputLayoutPassword.setErrorEnabled(false);
//        User user = new User();
//        boolean _isnorekexist = false;
//
//        for (int i=0;i<User.users.size();i++) {
//            if (User.users.get(i).username.equals(textInputEditTextUsername.getText().toString())) {
//                _isnorekexist = true;
//                user = User.users.get(i);
//                break;
//            }
//        }
//        if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_username))) {
//            return;
//        }
//        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_password))) {
//            return;
//        }
//        if (!inputValidation.isInputEditTextNoRekExist(textInputEditTextUsername, textInputLayoutUsername,_isnorekexist,getString(R.string.error_username_not_exists))){
//            return;
//        }
//        if (databaseHelper.checkUsername(textInputEditTextUsername.getText().toString().trim()
//                , textInputEditTextPassword.getText().toString().trim())) {
//
//            SessionManager sessionManager = SessionManager.with(this);
//            sessionManager.createsession(user);
//            progress = new ProgressDialog(this);
//            progress.setMessage("Loading...");
//            progress.show();
//            Thread _thread = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(2000);
//                        progress.dismiss();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            masuk();
//                        }
//                    });
//
//                }
//            };
//            _thread.start();
//        }
//        else {
//            // Snack Bar to show success message that record is wrong
//            Snackbar.make(nestedScrollView, getString(R.string.error_valid_username_pin), Snackbar.LENGTH_LONG).show();
//        }
//    }
//    /**
//     * This method is to empty all input edit text and go to HomeUserActivity
//     */
//    public void masuk(){
//        Intent accountsIntent = new Intent(this, HomeUserActivity.class);
//        emptyInputEditText();
//        startActivity(accountsIntent);
//    }
//    /**
//     * This method is to empty all input edit text
//     */
    private void emptyInputEditText() {
        textInputEditTextAkunAlternatif.setText(null);
        textInputEditTextPassword.setText(null);
    }
}