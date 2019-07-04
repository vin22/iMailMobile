package com.application.imail.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.R;
import com.application.imail.model.Message;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.MessageService;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReadMessageActivity extends AppCompatActivity {
    CoordinatorLayout parent_view;
    ImageButton starred;
    TextView subject, label, message, from, to, date;
    AppCompatButton reply, forward;
    MessageService messageService;
    ArrayList<String>email;
    boolean action=false;
    boolean isStarred=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        initToolbar();
        initComponent();
        initListener();
        email=getIntent().getStringArrayListExtra("email");
        setEmail();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ReadMessageActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ReadMessageActivity.this.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initComponent() {
        parent_view=findViewById(R.id.parent_view);
        subject=findViewById(R.id.subject);
        message=findViewById(R.id.message);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        date=findViewById(R.id.date);
        starred=findViewById(R.id.starred);
        label=findViewById(R.id.labels);
        reply=findViewById(R.id.reply);
        forward=findViewById(R.id.forward);
        messageService = APIUtils.getMessageService();
    }

    private void initListener(){
        starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Starred(Integer.parseInt(email.get(7)));
                Log.e("image","Masuk");
                if(isStarred){
                    isStarred=false;
                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
                }
                else{
                    isStarred=true;
                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
                }
            }
        });
    }

    public void setEmail(){
        subject.setText(email.get(0));
        if(email.get(1).equals("true")) {
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
            isStarred=true;
        }
        else{
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
            isStarred=false;
        }

        label.setText(email.get(2));
        from.setText(email.get(3));
        to.setText(email.get(4));
        SimpleDateFormat formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat format=new SimpleDateFormat("dd MMM yy");
        try {
            date.setText(format.format(formatapi.parse(email.get(5))));
        }catch (ParseException e){
            e.printStackTrace();
        }
        message.setText(email.get(6));
//        starred.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(email.get(1).equals("false")) {
//                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
//                }
//                else{
//                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_message, menu);
//        MenuItem item = menu.findItem(R.id.action_search);

//        searchView.setMenuItem(item);
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                //Do some magic
//                if(adapter!=null) {
//                    adapter.getFilter().filter(query);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                //Do some magic
//                if(adapter!=null){
//                    adapter.getFilter().filter(query);
//                }
//                return false;
//            }
//        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            if(action) {
                Intent data = new Intent();
                setResult(RESULT_OK, data.putExtra("folder", email.get(2)));
                finish();
            }
            else{
                finish();
            }
        }
        else if(id==R.id.action_move){
            return true;
        }
        else if(id==R.id.action_trash){
            delete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(action) {
            Intent data = new Intent();
            setResult(RESULT_OK, data.putExtra("folder", email.get(2)));
            finish();
        }
        else{
            finish();
        }
    }

    public void delete(){
        if(email.get(2).equals("Trash")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Trash").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.deletetrash(Integer.parseInt(email.get(7)));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    action=true;
                                } else {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }
        else if(email.get(2).equals("Draft")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Draft").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.deletedraft(Integer.parseInt(email.get(7)));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    action=true;
                                } else {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }
        else if(email.get(2).equals("Inbox")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Inbox").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.deleteinbox(Integer.parseInt(email.get(7)));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    action=true;
                                } else {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }
        else if(email.get(2).equals("Sent")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Sent").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.deletesent(Integer.parseInt(email.get(7)));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    action=true;
                                } else {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }
        //belum
        else if(email.get(2).equals("Starred")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Starred").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.starred(Integer.parseInt(email.get(2)));
                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.isSuccessful()){
                                String status=response.body().getStatus();
                                String statusmessage=response.body().getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }
                            else{
                                Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    dialog.dismiss();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogs.show();
        }
    }

    public void Starred(int messageid){
        Call<Message> call = messageService.starred(messageid);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    String status=response.body().getStatus();
                    String statusmessage=response.body().getMessage();
                    if (status.equals("true")) {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        action=true;
                    } else {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ReadMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ReadMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
