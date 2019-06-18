package com.application.imail.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.imail.LoginActivity;
import com.application.imail.R;
import com.application.imail.adapter.AdapterListContact;
import com.application.imail.adapter.AdapterListEmail;
import com.application.imail.config.SessionManager;
import com.application.imail.model.listcontact;
import com.application.imail.model.listemail;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    AdapterListContact adapter_contact;
    AdapterListEmail adapter_inbox,adapter_spam, adapter_starred, adapter_sent, adapter_trash, adapter_draft;
    RecyclerView recyclerView_inbox, recyclerView_spam, recyclerView_starred, recyclerView_sent, recyclerView_trash, recyclerView_draft, recyclerView_contact;
    SwipeRefreshLayout swipeinbox, swipespam, swipestarred, swipesent, swipetrash, swipedraft, swipecontact;
    CoordinatorLayout parent_view;
    listemail listemail;
    List<listemail> items;
    List<listcontact> itemscontact;
    Toolbar toolbar;
    MaterialSearchView searchView;
    SessionManager sessionManager;
    TextView name, Email;
    LinearLayout linear;
    String folder="Inbox";
    listcontact listcontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        sessionManager = SessionManager.with(this);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = InboxActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(InboxActivity.this.getResources().getColor(R.color.colorPrimary));
        }
        initToolbar(folder);
        parent_view=findViewById(R.id.parent_view);
        swipeinbox=findViewById(R.id.swipeinbox);
        swipespam=findViewById(R.id.swipespam);
        swipestarred=findViewById(R.id.swipestarred);
        swipesent=findViewById(R.id.swipesent);
        swipetrash=findViewById(R.id.swipetrash);
        swipedraft=findViewById(R.id.swipedraft);
        swipecontact=findViewById(R.id.swipecontact);

        recyclerView_inbox=findViewById(R.id.recyclerView_inbox);
        recyclerView_spam=findViewById(R.id.recyclerView_spam);
        recyclerView_starred=findViewById(R.id.recyclerView_starred);
        recyclerView_sent=findViewById(R.id.recyclerView_sent);
        recyclerView_trash=findViewById(R.id.recyclerView_trash);
        recyclerView_draft=findViewById(R.id.recyclerView_draft);
        recyclerView_contact=findViewById(R.id.recyclerView_contact);

        searchView=(MaterialSearchView)findViewById(R.id.searchView);
        setEmailInbox();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(folder.equals("Contact")){

                }
                else {
                    Intent intent = new Intent(InboxActivity.this, ComposeMessageActivity.class);
                    startActivity(intent);
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        linear = (LinearLayout) navigationView.getHeaderView(0);
        name = linear.findViewById(R.id.name);
        Email = linear.findViewById(R.id.email);
        name.setText(sessionManager.getuserloggedin().Name);
        Email.setText(sessionManager.getuserloggedin().Email);

        items=new ArrayList<>();
        swipeinbox.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailInbox();
            }
        });

        recyclerView_inbox.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeinbox.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipespam.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailSpam();
            }
        });

        recyclerView_spam.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipespam.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipestarred.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailStarred();
            }
        });

        recyclerView_starred.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipestarred.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipesent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailSent();
            }
        });

        recyclerView_sent.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipesent.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipetrash.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailTrash();
            }
        });

        recyclerView_trash.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipetrash.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipedraft.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(items!=null){
                    items.clear();
                }
                else{
                    items=new ArrayList<>();
                }
                setEmailDraft();
            }
        });

        recyclerView_draft.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipedraft.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        swipecontact.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(itemscontact!=null){
                    itemscontact.clear();
                }
                else{
                    itemscontact=new ArrayList<>();
                }
                setContact();
            }
        });

        recyclerView_contact.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipecontact.setEnabled(topRowVerticalPosition >= 0);
            }
        });

        ArrayList<String> ar = new ArrayList<String>();
        String s1 ="Google";
        String s2 ="Facebook";
        String s3 ="Instagram";
        String s4 ="Line";
        String s5 ="Whatsapp";
        ar.add(s1);
        ar.add(s2);
        ar.add(s3);
        ar.add(s4);
        ar.add(s5);
        String[] arrays=new String[]{"A","B"};
        String[] arrays1=new String[]{"A","B"};
        for (int i=0;i<arrays.length;i++){
            arrays1[i]=arrays[i];
        }
        String[] stockArr = new String[ar.size()];
        stockArr = ar.toArray(stockArr);
    }

    public void setEmailInbox(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_inbox!=null){
            adapter_inbox.notifyDataSetChanged();
        }
        for(int i=0;i<10;i++){
            if(i%2==0) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama));
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject(getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(false);
                listemail.setFolder("Inbox");
                items.add(listemail);
                Log.e("listemailinbox",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject(getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(false);
                listemail.setFolder("Inbox");
                items.add(listemail);
                Log.e("listemailinbox",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_inbox!=null){
            adapter_inbox.notifyDataSetChanged();
            if(recyclerView_inbox.getLayoutManager()==null) {
                recyclerView_inbox.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_inbox.setHasFixedSize(true);
            }
            recyclerView_inbox.setAdapter(adapter_inbox);
        }
        else{
            adapter_inbox=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_inbox.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_inbox.setHasFixedSize(true);
            recyclerView_inbox.setAdapter(adapter_inbox);
        }
        if(swipeinbox.isRefreshing()){
            swipeinbox.setRefreshing(false);
        }
    }

    public void setEmailSpam(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_spam!=null){
            adapter_spam.notifyDataSetChanged();
        }
        for(int i=0;i<10;i++){
            if(i%2==1) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama));
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject(getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(false);
                listemail.setFolder("Spam");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject(getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(false);
                listemail.setFolder("Spam");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_spam!=null){
            adapter_spam.notifyDataSetChanged();
            if(recyclerView_spam.getLayoutManager()==null) {
                recyclerView_spam.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_spam.setHasFixedSize(true);
            }
            recyclerView_spam.setAdapter(adapter_spam);
        }
        else{
            adapter_spam=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_spam.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_spam.setHasFixedSize(true);
            recyclerView_spam.setAdapter(adapter_spam);
        }
        if(swipespam.isRefreshing()){
            swipespam.setRefreshing(false);
        }
    }

    public void setEmailStarred(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_starred!=null){
            adapter_starred.notifyDataSetChanged();
        }
        for(int i=0;i<5;i++){
            if(i%3==1) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama));
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(true);
                listemail.setFolder("Starred");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(true);
                listemail.setFolder("Starred");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_starred!=null){
            adapter_starred.notifyDataSetChanged();
            if(recyclerView_starred.getLayoutManager()==null) {
                recyclerView_starred.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_starred.setHasFixedSize(true);
            }
            recyclerView_starred.setAdapter(adapter_starred);
        }
        else{
            adapter_starred=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_starred.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_starred.setHasFixedSize(true);
            recyclerView_starred.setAdapter(adapter_starred);
        }
        if(swipestarred.isRefreshing()){
            swipestarred.setRefreshing(false);
        }
    }

    public void setEmailSent(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_sent!=null){
            adapter_sent.notifyDataSetChanged();
        }
        for(int i=0;i<6;i++){
            if(i%2==1) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama));
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(false);
                listemail.setFolder("Sent");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(false);
                listemail.setFolder("Sent");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_sent!=null){
            adapter_sent.notifyDataSetChanged();
            if(recyclerView_sent.getLayoutManager()==null) {
                recyclerView_sent.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_sent.setHasFixedSize(true);
            }
            recyclerView_sent.setAdapter(adapter_sent);
        }
        else{
            adapter_sent=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_sent.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_sent.setHasFixedSize(true);
            recyclerView_sent.setAdapter(adapter_sent);
        }
        if(swipesent.isRefreshing()){
            swipesent.setRefreshing(false);
        }
    }

    public void setEmailTrash(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_trash!=null){
            adapter_trash.notifyDataSetChanged();
        }
        for(int i=0;i<6;i++){
            if(i%2==0) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama));
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(false);
                listemail.setFolder("Trash");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(false);
                listemail.setFolder("Trash");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_trash!=null){
            adapter_trash.notifyDataSetChanged();
            if(recyclerView_trash.getLayoutManager()==null) {
                recyclerView_trash.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_trash.setHasFixedSize(true);
            }
            recyclerView_trash.setAdapter(adapter_trash);
        }
        else{
            adapter_trash=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_trash.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_trash.setHasFixedSize(true);
            recyclerView_trash.setAdapter(adapter_trash);
        }
        if(swipetrash.isRefreshing()){
            swipetrash.setRefreshing(false);
        }
    }

    public void setEmailDraft(){
        if(items!=null){
            items.clear();
        }
        else{
            items=new ArrayList<>();
        }
        if(adapter_draft!=null){
            adapter_draft.notifyDataSetChanged();
        }
        for(int i=0;i<12;i++){
            if(i%2==0) {
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_pertama)+"Draft");
                listemail.setSender(getResources().getString(R.string.user_email_pertama));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_pertama));
                listemail.setSent_date(getResources().getString(R.string.date_pertama));
                listemail.setMessage(getResources().getString(R.string.message_pertama));
                listemail.setStarred(false);
                listemail.setFolder("Draft");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_pertama));
            }
            else{
                listemail = new listemail();
                listemail.setSendername(getResources().getString(R.string.user_kedua));
                listemail.setSender(getResources().getString(R.string.user_email_kedua));
                listemail.setReceiver(sessionManager.getuserloggedin().Email);
                listemail.setSubject("Starred:"+getResources().getString(R.string.subject_kedua));
                listemail.setSent_date(getResources().getString(R.string.date_kedua));
                listemail.setMessage(getResources().getString(R.string.message_kedua));
                listemail.setStarred(false);
                listemail.setFolder("Draft");
                items.add(listemail);
                Log.e("listemailspam",getResources().getString(R.string.user_kedua));
            }
        }
        if(adapter_draft!=null){
            adapter_draft.notifyDataSetChanged();
            if(recyclerView_draft.getLayoutManager()==null) {
                recyclerView_draft.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
                recyclerView_draft.setHasFixedSize(true);
            }
            recyclerView_draft.setAdapter(adapter_draft);
        }
        else{
            adapter_draft=new AdapterListEmail(InboxActivity.this,items);
            recyclerView_draft.setLayoutManager(new LinearLayoutManager(InboxActivity.this));
            recyclerView_draft.setHasFixedSize(true);
            recyclerView_draft.setAdapter(adapter_draft);
        }
        if(swipedraft.isRefreshing()){
            swipedraft.setRefreshing(false);
        }
    }

    public void setContact(){
        if(itemscontact!=null){
            itemscontact.clear();
        }
        else{
            itemscontact=new ArrayList<>();
        }
        if(adapter_contact!=null){
            adapter_contact.notifyDataSetChanged();
        }
        for(int i=0;i<10;i++){
            if(i%2==0) {
                listcontact = new listcontact();
                listcontact.setAddressbookid(String.valueOf(i));
                listcontact.setUserid(String.valueOf(i));
                listcontact.setName(getResources().getString(R.string.user_pertama));
                listcontact.setEmail(getResources().getString(R.string.user_email_pertama));
                listcontact.setPhone("");
                listcontact.setBirth_date("1997-01-01");
                listcontact.setGender("Laki-laki");
                listcontact.setSaved(true);
                listcontact.setSuggestion(false);
                listcontact.setDelete(false);
                itemscontact.add(listcontact);
            }
            else{
                listcontact = new listcontact();
                listcontact.setAddressbookid(String.valueOf(i));
                listcontact.setUserid(String.valueOf(i));
                listcontact.setName(getResources().getString(R.string.user_kedua));
                listcontact.setEmail(getResources().getString(R.string.user_email_kedua));
                listcontact.setPhone("");
                listcontact.setBirth_date("1997-01-01");
                listcontact.setGender("Laki-laki");
                listcontact.setSaved(true);
                listcontact.setSuggestion(false);
                listcontact.setDelete(false);
                itemscontact.add(listcontact);
            }
        }
        if(swipecontact.isRefreshing()){
            swipecontact.setRefreshing(false);
        }
    }

    private void initToolbar(String folder) {
        getSupportActionBar().setTitle(folder);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }
        else{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
//                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//                dialog.setMessage("Apakah Anda yakin untuk logout dari iMail?");
//                dialog.setCancelable(false);
//                //final Integer nama=username.getText().toString().length();
//                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent=new Intent(InboxActivity.this, LoginActivity.class);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        SessionManager sessionManager = SessionManager.with(InboxActivity.this);
//                        sessionManager.clearsession();
//                        startActivity(intent);
//                    }
//                });
//                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                dialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                if(adapter_inbox!=null) {
                    adapter_inbox.getFilter().filter(query);
                }
                if(adapter_spam!=null) {
                    adapter_spam.getFilter().filter(query);
                }
                if(adapter_starred!=null) {
                    adapter_starred.getFilter().filter(query);
                }
                if(adapter_sent!=null) {
                    adapter_sent.getFilter().filter(query);
                }
                if(adapter_trash!=null) {
                    adapter_trash.getFilter().filter(query);
                }
                if(adapter_draft!=null) {
                    adapter_draft.getFilter().filter(query);
                }
                if(adapter_contact!=null) {
                    adapter_contact.getFilter().filter(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //Do some magic
                if(adapter_inbox!=null){
                    adapter_inbox.getFilter().filter(query);
                }
                if(adapter_spam!=null) {
                    adapter_spam.getFilter().filter(query);
                }
                if(adapter_starred!=null) {
                    adapter_starred.getFilter().filter(query);
                }
                if(adapter_sent!=null) {
                    adapter_sent.getFilter().filter(query);
                }
                if(adapter_trash!=null) {
                    adapter_trash.getFilter().filter(query);
                }
                if(adapter_draft!=null) {
                    adapter_draft.getFilter().filter(query);
                }
                if(adapter_contact!=null) {
                    adapter_contact.getFilter().filter(query);
                }
                return false;
            }
        });
        SessionManager sessionManager=SessionManager.with(InboxActivity.this);
        String[] stockArr = new String[sessionManager.getuserloggedin().getListcontacts().size()];
        stockArr = sessionManager.getuserloggedin().getListcontacts().toArray(stockArr);

        searchView.setSuggestions(stockArr);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inbox) {
            // Handle the camera action
            folder="Inbox";
            initToolbar(folder);
            swipeinbox.setVisibility(View.VISIBLE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.GONE);
            setEmailInbox();
        } else if (id == R.id.nav_sent) {
            folder="Sent";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.VISIBLE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.GONE);
            setEmailSent();
        } else if (id == R.id.nav_favorit) {
            folder="Starred";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.VISIBLE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.GONE);
            setEmailStarred();
        } else if (id == R.id.nav_spam) {
            folder="Spam";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.VISIBLE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.GONE);
            setEmailSpam();
        } else if (id == R.id.nav_draft) {
            folder="Draft";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.VISIBLE);
            swipecontact.setVisibility(View.GONE);
            setEmailDraft();
        } else if (id == R.id.nav_trash) {
            folder="Trash";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.VISIBLE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.GONE);
            setEmailTrash();
        } else if (id == R.id.nav_contact) {
            folder="Contact";
            initToolbar(folder);
            swipeinbox.setVisibility(View.GONE);
            swipespam.setVisibility(View.GONE);
            swipestarred.setVisibility(View.GONE);
            swipesent.setVisibility(View.GONE);
            swipetrash.setVisibility(View.GONE);
            swipedraft.setVisibility(View.GONE);
            swipecontact.setVisibility(View.VISIBLE);
            setContact();
        }else if (id == R.id.nav_settings) {
            Intent intent=new Intent(InboxActivity.this,SettingActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
