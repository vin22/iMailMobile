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
import com.application.imail.model.listcontact;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = LoginActivity.this;
    User user;
    SessionManager userConfig;
    UserService userService;
    DomainService domainService;
    List<Domain> itemsdomain;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutUsername;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextUsername;
    private TextInputEditText textInputEditTextPassword;

    MaterialSpinner domain;
    private AppCompatButton appCompatButtonLogin;
    TextView registernow;
    ProgressDialog progress;
    InputValidation inputValidation;
    listcontact listcontact;
    List<listcontact> itemscontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //menyembunyikan action bar
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
        getDomain();
        //jika sudah memiliki session login maka akan langsung masuk HomeUserActivity
        if (SessionManager.with(this).isuserlogin()) {
            Intent masuk=new Intent(this,InboxActivity.class);
            startActivity(masuk);
        }
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutUsername = (TextInputLayout) findViewById(R.id.textInputLayoutUsername);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextUsername = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        domain = findViewById(R.id.domain);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        registernow=findViewById(R.id.registernow);
    }
//    /**
//     * This method is to initialize listeners
//     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        registernow.setOnClickListener(this);
        userConfig = new SessionManager(this);
        userService = APIUtils.getUserService();
        domainService = APIUtils.getDomainService();
        inputValidation = new InputValidation(this);
    }
//    /**
//     * This method is to initialize objects to be used
//     */
    private void initObjects() {
        progress = new ProgressDialog(activity);
        user=new User();
    }

    public void getDomain(){
        Call<List<Domain>> call = domainService.getdomain();
        call.enqueue(new Callback<List<Domain>>() {
            @Override
            public void onResponse(Call<List<Domain>> call, Response<List<Domain>> response) {
                if(response.isSuccessful()){
                    Log.e("User","Masuk1");

                    String status=response.body().get(0).getStatus();
                    String statusmessage=response.body().get(0).getMessage();
                    Log.e("User",status);
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
                            Log.e("Domain",itemsdomain.get(i).getDomainname() );
                        }
                        domain.setItems(itemsdomains);

                    } else {
                        Toast.makeText(LoginActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        Log.e("USER ACTIVITY ERROR", statusmessage);

                    }
                }
                else{
                    Log.e("USER ACTIVITY ERROR", "Response failed");
                    Toast.makeText(LoginActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Domain>> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(LoginActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });
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
                if (!inputValidation.isInputEditTextFilled(textInputEditTextUsername, textInputLayoutUsername, getString(R.string.error_message_username))) {
                    return;
                }
                if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword,getString(R.string.error_message_passwordkosong))) {
                    return;
                }
//                if(textInputEditTextUsername.getText().toString().equals("") && textInputEditTextPassword.getText().toString().equals("")){
//                    emptyInputEditText();
//                }
                else {
                    Log.e("POST",textInputEditTextUsername.getText().toString());
                    Log.e("POST",textInputEditTextPassword.getText().toString());
                    Call<User> call = userService.login(textInputEditTextUsername.getText().toString()+domain.getText().toString(), textInputEditTextPassword.getText().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                SessionManager sessionManager = SessionManager.with(LoginActivity.this);
                                String status=response.body().getStatus();
    //                            int statuscode = response.code();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
//                                    Toast.makeText(LoginActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
//                                    itemscontact = new ArrayList<listcontact>();
//                                    listcontact = new listcontact();
//                                    listcontact.setAddressbookid("1");
//                                    listcontact.setUserid("1");
//                                    listcontact.setName(getResources().getString(R.string.user_pertama));
//                                    listcontact.setEmail(getResources().getString(R.string.user_email_pertama));
//                                    listcontact.setPhone("");
//                                    listcontact.setBirth_date("1997-01-01");
//                                    listcontact.setGender("Laki-laki");
//                                    listcontact.setSaved(true);
//                                    listcontact.setSuggestion(true);
//                                    listcontact.setDelete(false);
//                                    itemscontact.add(listcontact);
//
//                                    listcontact = new listcontact();
//                                    listcontact.setAddressbookid("2");
//                                    listcontact.setUserid("2");
//                                    listcontact.setName(getResources().getString(R.string.user_kedua));
//                                    listcontact.setEmail(getResources().getString(R.string.user_email_kedua));
//                                    listcontact.setPhone("");
//                                    listcontact.setBirth_date("1997-01-01");
//                                    listcontact.setGender("Laki-laki");
//                                    listcontact.setSaved(true);
//                                    listcontact.setSuggestion(true);
//                                    listcontact.setDelete(false);
//                                    itemscontact.add(listcontact);
//
//                                    itemscontact = new ArrayList<listcontact>();
//                                    listcontact = new listcontact();
//                                    listcontact.setAddressbookid("3");
//                                    listcontact.setUserid("3");
//                                    listcontact.setName("Danny");
//                                    listcontact.setEmail("danny@email.com");
//                                    listcontact.setPhone("");
//                                    listcontact.setBirth_date("1997-01-01");
//                                    listcontact.setGender("Laki-laki");
//                                    listcontact.setSaved(true);
//                                    listcontact.setSuggestion(true);
//                                    listcontact.setDelete(false);
//                                    itemscontact.add(listcontact);
//
//                                    itemscontact = new ArrayList<listcontact>();
//                                    listcontact = new listcontact();
//                                    listcontact.setAddressbookid("4");
//                                    listcontact.setUserid("4");
//                                    listcontact.setName("Steven");
//                                    listcontact.setEmail("steven@email.com");
//                                    listcontact.setPhone("");
//                                    listcontact.setBirth_date("1997-01-01");
//                                    listcontact.setGender("Laki-laki");
//                                    listcontact.setSaved(true);
//                                    listcontact.setSuggestion(true);
//                                    listcontact.setDelete(false);
//                                    itemscontact.add(listcontact);
//
//                                    listcontact = new listcontact();
//                                    listcontact.setAddressbookid("5");
//                                    listcontact.setUserid("5");
//                                    listcontact.setName("Tester");
//                                    listcontact.setEmail("tester@email.com");
//                                    listcontact.setPhone("");
//                                    listcontact.setBirth_date("1997-01-01");
//                                    listcontact.setGender("Laki-laki");
//                                    listcontact.setSaved(true);
//                                    listcontact.setSuggestion(true);
//                                    listcontact.setDelete(false);
//                                    itemscontact.add(listcontact);
//
//                                    user.setListcontacts(itemscontact);

                                    user.setUserID(response.body().getUserID());
                                    user.setEmail(response.body().getEmail());
    //                                user.setUsername(response.body().getName());
                                    user.setName(response.body().getName());
                                    user.setPassword(textInputEditTextPassword.getText().toString());
                                    sessionManager.createsession(user);
    //                                user.setPassword("vincent");
    //                                userConfig.writeNorekening(response.body().getNorekening());
    //                                userConfig.writeNama(response.body().getNama());
    //                                userConfig.writeSaldo(response.body().getSaldo());
                                    startActivity(new Intent(LoginActivity.this, InboxActivity.class));
                                    finish();
                                    emptyInputEditText();

                                } else {
                                    Toast.makeText(LoginActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                                else{
                                Toast.makeText(LoginActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("USER ACTIVITY ERROR", t.getMessage());
                            Toast.makeText(LoginActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
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
            case R.id.registernow:
                Intent intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                emptyInputEditText();
                break;
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
        textInputEditTextUsername.setText(null);
        textInputEditTextPassword.setText(null);
    }
}