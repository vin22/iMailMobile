package com.application.imail.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.LoginActivity;
import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.application.imail.model.User;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.UserService;
import com.application.imail.utils.InputValidation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private final AppCompatActivity activity = ChangePasswordActivity.this;
    User user;
    SessionManager userConfig;
    UserService userService;
    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutOldPassword, textInputLayoutNewPassword, textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextOldPassword, textInputEditTextNewPassword, textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonChange;
    ProgressDialog progress;
    InputValidation inputValidation;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initToolbar();
        initViews();
        initListeners();
        initObjects();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ChangePasswordActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ChangePasswordActivity.this.getResources().getColor(R.color.colorPrimary));
        }
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutOldPassword = (TextInputLayout) findViewById(R.id.textInputLayoutOldPassword);
        textInputLayoutNewPassword = (TextInputLayout) findViewById(R.id.textInputLayoutNewPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextOldPassword = (TextInputEditText) findViewById(R.id.textInputEditTextOldPassword);
        textInputEditTextNewPassword = (TextInputEditText) findViewById(R.id.textInputEditTextNewPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonChange = (AppCompatButton) findViewById(R.id.appCompatButtonChange);
    }
//    /**
//     * This method is to initialize listeners
//     */
    private void initListeners() {
        appCompatButtonChange.setOnClickListener(this);
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
            case R.id.appCompatButtonChange:
                if (!inputValidation.isInputEditTextFilled(textInputEditTextOldPassword, textInputLayoutOldPassword, getString(R.string.error_message_oldpasswordkosong))) {
                    return;
                }

                if (!inputValidation.isInputEditTextLengthPassword(textInputEditTextOldPassword, textInputLayoutOldPassword,getString(R.string.error_message_oldpassword_length))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidNumberPassword(textInputEditTextOldPassword, textInputLayoutOldPassword,getString(R.string.error_message_oldpassword_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidLowerPassword(textInputEditTextOldPassword, textInputLayoutOldPassword,getString(R.string.error_message_oldpassword_lower_case))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidUpperPassword(textInputEditTextOldPassword, textInputLayoutOldPassword,getString(R.string.error_message_oldpassword_upper_case))) {
                    return;
                }

                if (!inputValidation.isInputEditTextFilled(textInputEditTextNewPassword, textInputLayoutNewPassword, getString(R.string.error_message_newpasswordkosong))) {
                    return;
                }

                if (!inputValidation.isInputEditTextLengthPassword(textInputEditTextNewPassword, textInputLayoutNewPassword,getString(R.string.error_message_newpassword_length))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidNumberPassword(textInputEditTextNewPassword, textInputLayoutNewPassword,getString(R.string.error_message_newpassword_number))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidLowerPassword(textInputEditTextNewPassword, textInputLayoutNewPassword,getString(R.string.error_message_newpassword_lower_case))) {
                    return;
                }
                if (!inputValidation.isInputEditTextisValidUpperPassword(textInputEditTextNewPassword, textInputLayoutNewPassword,getString(R.string.error_message_newpassword_upper_case))) {
                    return;
                }

                if (!inputValidation.isInputEditTextFilled(textInputEditTextConfirmPassword, textInputLayoutConfirmPassword,getString(R.string.error_message_confirmpasswordkosong))) {
                    return;
                }
                if (!inputValidation.isInputEditTextSamePassword(textInputEditTextNewPassword, textInputEditTextConfirmPassword, textInputLayoutNewPassword,getString(R.string.error_message_samepassword))) {
                    return;
                }
//                if(textInputEditTextEmail.getText().toString().equals("") && textInputEditTextPassword.getText().toString().equals("")){
//                    emptyInputEditText();
//                }
                else {
                    if(pd!=null){
                        pd.setTitle("Please Wait");
                        pd.setMessage("Changing your password");
                        pd.show();
                    }
                    else{
                        pd=new ProgressDialog(ChangePasswordActivity.this);
                        pd.setTitle("Please Wait");
                        pd.setMessage("Changing your password");
                        pd.show();
                    }
                    SessionManager sessionManager = SessionManager.with(ChangePasswordActivity.this);
                    Call<User> call = userService.changepassword(sessionManager.getuserloggedin().getEmail(), textInputEditTextOldPassword.getText().toString(),textInputEditTextNewPassword.getText().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
//                            SessionManager sessionManager = SessionManager.with(RegisterActivity.this);
                            if(response.isSuccessful()){
//                                int statuscode = response.code();
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    emptyInputEditText();
                                    Toast.makeText(ChangePasswordActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ChangePasswordActivity.this, statusmessage, Toast.LENGTH_SHORT).show();

                                }
                            }
                            else{
                                if(pd.isShowing()){
                                    pd.dismiss();
                                }
                                Toast.makeText(ChangePasswordActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("USER ACTIVITY ERROR", t.getMessage());
                            if(pd.isShowing()){
                                pd.dismiss();
                            }
                            Toast.makeText(ChangePasswordActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        textInputEditTextOldPassword.setText(null);
        textInputEditTextNewPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}