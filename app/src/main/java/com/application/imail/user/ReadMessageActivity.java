package com.application.imail.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
import com.application.imail.config.SessionManager;
import com.application.imail.model.Message;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.MessageService;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    SessionManager sessionManager;
    boolean action=false;
    boolean isStarred=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        initToolbar();
        initComponent();
        initListener();
        sessionManager=SessionManager.with(ReadMessageActivity.this);
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
        subject.setText("Subject : "+email.get(0));
        if(email.get(1).equals("true")) {
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
            isStarred=true;
        }
        else{
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
            isStarred=false;
        }

        label.setText(email.get(2));
        if(email.get(2).equals("Inbox")){
            reply.setVisibility(View.VISIBLE);
            forward.setVisibility(View.VISIBLE);
        }
        else{
            reply.setVisibility(View.GONE);
            forward.setVisibility(View.GONE);
        }
        from.setText(email.get(3));
        to.setText(email.get(4));
        SimpleDateFormat formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        SimpleDateFormat format=new SimpleDateFormat("dd MMM yy");
        try {
            date.setText(format.format(formatapi.parse(email.get(5))));
//            if(email.get(2).equals("Inbox")){
//                formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                date.setText(format.format(formatapi.parse(email.get(5))));
//            }
//            else {
//                date.setText(format.format(formatapi.parse(email.get(5))));
//            }
        }catch (ParseException e){
            try {
                formatapi = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                date.setText(format.format(formatapi.parse(email.get(5))));
            }
            catch(ParseException e1){
                e1.printStackTrace();
            }
            e.printStackTrace();

        }
        message.setText(Html.fromHtml(email.get(6)));

        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReadMessageActivity.this,ReplyActivity.class);
                intent.putExtra("to",email.get(8));
                intent.putExtra("subject",email.get(0));
                intent.putExtra("message",email.get(6));
                startActivity(intent);
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ReadMessageActivity.this,ForwardActivity.class);
                intent.putExtra("to",email.get(8));
                intent.putExtra("subject",email.get(0));
                intent.putExtra("message",email.get(6));
                startActivity(intent);
            }
        });
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
        if(email.get(2).equals("Starred")){
            menu.findItem(R.id.action_trash).setVisible(false);
        }
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
            AlertDialog.Builder dialog=new AlertDialog.Builder(ReadMessageActivity.this);
            dialog.setTitle("Move message to folder");
            if(email.get(2).equals("Inbox")){
                String[] items1={"Draft"};
                dialog.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveMessages();
                        dialogInterface.dismiss();
                    }
                });
            }
            else if(email.get(2).equals("Draft") || email.get(2).equals("Sent")) {
                String[] items1 = {"Inbox"};
                dialog.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moveMessages();
                        dialogInterface.dismiss();
                    }
                });
            }
            else if(email.get(2).equals("Starred")){

            }
            else if(email.get(2).equals("Spam")){
                String[] items1={"Inbox","Draft"};
                dialog.setItems(items1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0) {
                            moveMessages();
                            dialogInterface.dismiss();
                        }
                        else{
                            moveSpamtoDraft();
                            dialogInterface.dismiss();
                        }
                    }
                });
            }
            dialog.show();
            return true;
        }
        else if(id==R.id.action_filter) {
            // delete all the selected messages
            if (email.get(2).equals("Inbox")) {
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(ReadMessageActivity.this);
                dialog.setTitle("Mark message as spam");
                dialog.setMessage("Are you sure to mark message as spam?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        markMessagesasspam();
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            } else if (email.get(2).equals("Spam")) {
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(ReadMessageActivity.this);
                dialog.setTitle("Mark spam as non spam");
                dialog.setMessage("Are you sure to mark spam as non spam?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        markSpamasnonspam();
                        dialogInterface.dismiss();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
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
    private void moveMessages() {
        if(email.get(2).equals("Inbox")) {
            Call<Message> call = messageService.moveinbox(Integer.parseInt(email.get(7)));
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus();
                        String statusmessage = response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
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
        else if(email.get(2).equals("Sent")){
            Call<Message> call = messageService.movesent(Integer.parseInt(email.get(7)));
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus();
                        String statusmessage = response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
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
        else if(email.get(2).equals("Draft")){
            Call<Message> call = messageService.movedraft(Integer.parseInt(email.get(7)));
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus();
                        String statusmessage = response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
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
        else if(email.get(2).equals("Spam")){
            Call<Message> call = messageService.movetoinbox(Integer.parseInt(email.get(7)));
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        String status = response.body().getStatus();
                        String statusmessage = response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    } else {
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

    private void moveSpamtoDraft() {
        Call<Message> call = messageService.movetodraft(Integer.parseInt(email.get(7)));
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String statusmessage = response.body().getMessage();
                    if (status.equals("true")) {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    private void markMessagesasspam() {
        Call<Message> call = messageService.markasspam(Integer.parseInt(email.get(7)), sessionManager.getuserloggedin().getUserID());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String statusmessage = response.body().getMessage();
                    if (status.equals("true")) {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    private void markSpamasnonspam() {
        Call<Message> call = messageService.markasnonspam(Integer.parseInt(email.get(7)), sessionManager.getuserloggedin().getUserID());
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String statusmessage = response.body().getMessage();
                    if (status.equals("true")) {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ReadMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                } else {
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

    public void delete(){
        if(email.get(2).equals("Trash")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Trash").setMessage("Are you sure to delete this email permanently?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
        else if(email.get(2).equals("Spam")){
            AlertDialog.Builder dialogs=new AlertDialog.Builder(ReadMessageActivity.this).setTitle("Delete Email in Spam").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    Call<Message> call = messageService.deletespam(Integer.parseInt(email.get(7)));
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
