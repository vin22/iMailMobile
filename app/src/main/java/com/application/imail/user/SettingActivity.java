package com.application.imail.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.application.imail.LoginActivity;
import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.application.imail.model.User;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity{

    View lyt_manageaccount, lyt_changepassword,lyt_logout;
    UserService userService;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initToolbar();
        initViews();
//        initListeners();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Window window = SettingActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(SettingActivity.this.getResources().getColor(R.color.colorPrimary));
    }

    private void initViews() {
        sessionManager=SessionManager.with(SettingActivity.this);
        userService= APIUtils.getUserService();
        lyt_manageaccount = findViewById(R.id.lyt_manageaccount);
        lyt_manageaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(SettingActivity.this);
                dialog.setTitle("Manage Account Alternative");
                String[] items={"Add Account Alternative", "Remove Account Alternative"};
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent=new Intent(SettingActivity.this, AkunAlternatifActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                        else{
                            Call<User> call = userService.getakunalternatif(sessionManager.getuserloggedin().getUserID());
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.isSuccessful()){
                                        Log.e("User","Masuk1");
                                        String status=response.body().getStatus();
                                        String statusmessage=response.body().getMessage();
                                        if (status.equals("true")) {
                                            AlertDialog.Builder dialogs=new AlertDialog.Builder(SettingActivity.this);
                                            dialogs.setTitle("Remove Account Alternative");
                                            dialogs.setMessage("Are you sure to remove your account alternative?");
                                            dialogs.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Call<User> call1 = userService.removeakunalternatif(sessionManager.getuserloggedin().getUserID());
                                                    call1.enqueue(new Callback<User>() {
                                                        @Override
                                                        public void onResponse(Call<User> call, Response<User> response) {
                                                            if(response.isSuccessful()){
                                                                Log.e("User","Masuk1");
                                                                String status=response.body().getStatus();
                                                                String statusmessage=response.body().getMessage();
                                                                if (status.equals("true")) {
                                                                    Toast.makeText(SettingActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(SettingActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(SettingActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<User> call, Throwable t) {
                                                            Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                            Toast.makeText(SettingActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialogs.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            dialogs.show();
                                        } else {
                                            Toast.makeText(SettingActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(SettingActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.e("USER ACTIVITY ERROR", t.getMessage());
                                    Toast.makeText(SettingActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                                }
                            });



//                            final Dialog dialogs=new Dialog(SettingActivity.this);
//                            dialogs.setCancelable(true);
//                            dialogs.setContentView(R.layout.dialog_remove_account_alternative);
//                            dialogs.show();
//
//                            final AppCompatButton recyclerview = (AppCompatButton) dialogs.findViewById(R.id.recyclerView);
//                            final AppCompatButton appCompatButtonClose = (AppCompatButton) dialogs.findViewById(R.id.appCompatButtonClose);
//
//                            appCompatButtonClose.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialogs.dismiss();
//                                }
//                            });
//                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        lyt_changepassword = findViewById(R.id.lyt_changepassword);
        lyt_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        lyt_logout = findViewById(R.id.lyt_logout);
        lyt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
                Log.e("Setting","Masuk1");
            }
        });
    }

//    private void initListeners() {
//        lyt_logout.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        Log.e("Setting","Masuk");
//        if(v.getId()==R.id.lyt_logout) {
////            super.onBackPressed();
//            Log.e("Setting","Masuk1");
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void Logout(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Apakah Anda yakin untuk logout dari iMail?");
        dialog.setCancelable(false);
        //final Integer nama=username.getText().toString().length();
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(SettingActivity.this, LoginActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SessionManager sessionManager = SessionManager.with(SettingActivity.this);
                sessionManager.clearsession();
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}
