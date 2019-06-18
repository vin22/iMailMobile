package com.application.imail.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.application.imail.R;
import com.bumptech.glide.Glide;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReadMessageActivity extends AppCompatActivity {
    CoordinatorLayout parent_view;
    ImageButton starred;
    TextView subject, label, message, from, to, date;
    AppCompatButton reply, forward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_message);
        initToolbar();
        initComponent();
        initListener();
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
    }

    private void initListener(){

    }

    public void setEmail(){
        final ArrayList<String>email=getIntent().getStringArrayListExtra("email");
        subject.setText(email.get(0));
        if(email.get(1).equals("true")) {
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
        }
        else{
            Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
        }
        label.setText(email.get(2));
        from.setText(email.get(3));
        to.setText(email.get(4));
        date.setText(email.get(5));
        message.setText(email.get(6));
        starred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.get(1).equals("false")) {
                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star).into(starred);
                }
                else{
                    Glide.with(ReadMessageActivity.this).load(R.drawable.ic_star_border).into(starred);
                }
            }
        });
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
            finish();
        }
        else if(id==R.id.action_move){
            return true;
        }
        else if(id==R.id.action_trash){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
