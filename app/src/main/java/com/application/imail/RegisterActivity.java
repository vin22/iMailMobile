package com.application.imail;

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

import com.application.imail.config.SessionManager;
import com.application.imail.model.Domain;
import com.application.imail.model.User;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.DomainService;
import com.application.imail.remote.UserService;
import com.application.imail.user.InboxActivity;
import com.application.imail.utils.InputValidation;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = RegisterActivity.this;
    User user;
    SessionManager userConfig;
    UserService userService;
    DomainService domainService;
    List<Domain> itemsdomain;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutFullName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword, textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextFullName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword, textInputEditTextConfirmPassword;
    MaterialSpinner domain;
    private AppCompatButton appCompatButtonRegister;
    ProgressDialog progress;
    TextView loginnow;
    InputValidation inputValidation;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //menyembunyikan action bar
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
        getDomain();
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutFullName = (TextInputLayout) findViewById(R.id.textInputLayoutFullName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextFullName = (TextInputEditText) findViewById(R.id.textInputEditTextFullName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        domain = findViewById(R.id.domain);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        loginnow=findViewById(R.id.loginnow);
    }
//    /**
//     * This method is to initialize listeners
//     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        loginnow.setOnClickListener(this);
        userConfig = new SessionManager(this);
        userService = APIUtils.getUserService();
        inputValidation = new InputValidation(this);
        domainService = APIUtils.getDomainService();
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

    public void getDomain(){
        Call<List<Domain>> call = domainService.getdomain();
        call.enqueue(new Callback<List<Domain>>() {
            @Override
            public void onResponse(Call<List<Domain>> call, Response<List<Domain>> response) {
                if(response.isSuccessful()){
                    Log.e("User","Masuk1");
                    String status=response.body().get(0).getStatus();
                    String statusmessage=response.body().get(0).getMessage();
                    if (status.equals("true")) {
                        if(itemsdomain!=null){
                            itemsdomain.clear();
                        }
                        else{
                            itemsdomain=new ArrayList<>();
                        }
                        itemsdomain = response.body();
                        List<String>itemsdomains=new ArrayList<>();
                        for (int i=0;i<itemsdomain.size();i++){
                            itemsdomains.add("@"+itemsdomain.get(i).getDomainname());
                        }
                        domain.setItems(itemsdomains);

                    } else {
                        Toast.makeText(RegisterActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        Log.e("USER ACTIVITY ERROR", statusmessage);

                    }
                }
                else{
                    Log.e("USER ACTIVITY ERROR", "Response failed");
                    Toast.makeText(RegisterActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Domain>> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Response failure", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginnow:
                finish();
                emptyInputEditText();
//                Intent intent=new Intent(this,LoginActivity.class);
//                startActivity(intent);
                break;
            //jika klik button login akan melakukan proses validasi
            case R.id.appCompatButtonRegister:
                if (!inputValidation.isInputEditTextFilled(textInputEditTextFullName, textInputLayoutFullName, getString(R.string.error_message_name))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_username))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_passwordkosong))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextConfirmPassword, textInputLayoutConfirmPassword,getString(R.string.error_message_confirmpasswordkosong))) {
                    return;
                }

                if (!inputValidation.isInputEditTextLengthName(textInputEditTextFullName, textInputLayoutFullName, getString(R.string.error_message_name_length))) {
                    return;
                }

                if (!inputValidation.isInputEditTextLengthEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_username_length))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFirstAlphabetEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_username_first_alphabet))) {
                    return;
                }
                if (!inputValidation.isInputEditTextValidEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_valid_username))) {
                    return;
                }
//                if (!inputValidation.isInputEditTextNumberEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_username_number))) {
//                    return;
//                }

                if (!inputValidation.isInputEditTextLengthPassword(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_password_length))) {
                    return;
                }
                if (!inputValidation.isInputEditTextValidPassword(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_valid_password))) {
                    return;
                }
//                if (!inputValidation.isInputEditTextisValidNumberPassword(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_password_number))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextisValidLowerPassword(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_password_lower_case))) {
//                    return;
//                }
//                if (!inputValidation.isInputEditTextisValidUpperPassword(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_password_upper_case))) {
//                    return;
//                }

                if (!inputValidation.isInputEditTextSamePassword(textInputEditTextPassword, textInputEditTextConfirmPassword, textInputLayoutPassword,getString(R.string.error_message_samepassword))) {
                    return;
                }
//                if(textInputEditTextEmail.getText().toString().equals("") && textInputEditTextPassword.getText().toString().equals("")){
//                    emptyInputEditText();
//                }
                else {
                    if(pd!=null){
                        pd.setTitle("Please Wait");
                        pd.setMessage("Register your email");
                        pd.show();
                    }
                    else{
                        pd=new ProgressDialog(RegisterActivity.this);
                        pd.setTitle("Please Wait");
                        pd.setMessage("Register your email");
                        pd.show();
                    }
                    Call<User> call = userService.register(textInputEditTextEmail.getText().toString()+domain.getText().toString(), textInputEditTextPassword.getText().toString(), textInputEditTextFullName.getText().toString(), itemsdomain.get(domain.getSelectedIndex()).getDomainid());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
//                            SessionManager sessionManager = SessionManager.with(RegisterActivity.this);
                            if(response.isSuccessful()) {
//                                int statuscode = response.code();
                                String status=response.body().getStatus();
//                            Log.e("User",response.body().toString());
                                String statusmessage = response.body().getMessage();
                                if (status.equals("true")) {
                                    emptyInputEditText();
                                    if(pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    Toast.makeText(RegisterActivity.this, statusmessage, Toast.LENGTH_SHORT).show();

//                                user.setEmail(response.body().getEmail());
//                                user.setUsername(response.body().getName());
//                                user.setName(response.body().getName());
//                                sessionManager.createsession(user);
//                                user.setPassword("vincent");
//                                userConfig.writeNorekening(response.body().getNorekening());
//                                userConfig.writeNama(response.body().getNama());
//                                userConfig.writeSaldo(response.body().getSaldo());
//                                startActivity(new Intent(RegisterActivity.this, InboxActivity.class));
//                                finish();

                                } else {
                                    if(pd.isShowing()){
                                        pd.dismiss();
                                    }
                                    Toast.makeText(RegisterActivity.this, statusmessage, Toast.LENGTH_SHORT).show();


                                }
                            }
                            else{
                                if(pd.isShowing()){
                                    pd.dismiss();
                                }
                                Toast.makeText(RegisterActivity.this, "Response failed", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("USER ACTIVITY ERROR", t.getMessage());
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                            Toast.makeText(RegisterActivity.this, "Response failure", Toast.LENGTH_SHORT).show();

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
        textInputEditTextFullName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}