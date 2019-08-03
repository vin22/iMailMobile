package com.application.imail.user;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.application.imail.model.listcontact;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.ContactService;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReplyActivity extends AppCompatActivity {
    CoordinatorLayout parent_view;
    MaterialSpinner spinnerfrom;
    TextView to, bcc, cc, re, lastmessage, message;
    ChipsInputLayout chips_to, chips_bcc, chips_cc ;
    SessionManager sessionManager;
    ContactService contactService;
    List<listcontact> itemscontact;
    List<listcontact> itemscont;
    private RichEditor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        initToolbar();
        initComponent();
        initListener();
        contactService= APIUtils.getContactService();
        sessionManager=SessionManager.with(this);
        setEmail();
        getContacts();

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reply");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ReplyActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ReplyActivity.this.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initComponent() {
        parent_view=findViewById(R.id.parent_view);
        re=findViewById(R.id.edittext_re);
        lastmessage=findViewById(R.id.edittext_lastmessage);
        message=findViewById(R.id.edittext_message);
        spinnerfrom=findViewById(R.id.spinnerfrom);
        to=findViewById(R.id.edittext_to);
//        bcc=findViewById(R.id.edittext_bcc);
//        cc=findViewById(R.id.edittext_cc);

        chips_to = (ChipsInputLayout)findViewById(R.id.chips_to);
        chips_bcc = (ChipsInputLayout)findViewById(R.id.chips_bcc);
        chips_cc = (ChipsInputLayout)findViewById(R.id.chips_cc);
        mEditor = (RichEditor) findViewById(R.id.editor);

        mEditor.setPlaceholder("Message");
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setEditorFontSize(20);
        mEditor.setFontSize(20);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {

            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
            }
        });
    }

    private void initListener(){

    }

    public void setEmail(){

        spinnerfrom.setItems(sessionManager.getuserloggedin().getEmail());
//        chips_to.setSelectedChipList();
        itemscont=new ArrayList<>();
        listcontact lc=new listcontact();
        lc.setEmail(getIntent().getStringExtra("to"));
        itemscont.add(lc);

        re.setText(getIntent().getStringExtra("subject"));
        lastmessage.setText(getIntent().getStringExtra("message"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_message, menu);
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
        else if(id==R.id.action_attachment){
            return true;
        }
        else if(id==R.id.action_sent){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getContacts(){
        Call<List<listcontact>> call = contactService.getcontacts(sessionManager.getuserloggedin().getUserID());
        call.enqueue(new Callback<List<listcontact>>() {
            @Override
            public void onResponse(Call<List<listcontact>> call, Response<List<listcontact>> response) {
                if(response.isSuccessful()){
                    Log.e("User","Masuk1");
                    Log.e("User",String.valueOf(sessionManager.getuserloggedin().getUserID()));
                    String status=response.body().get(0).getStatus();
                    String statusmessage=response.body().get(0).getMessage();
                    Log.e("User",status);
                    if (status.equals("true")) {
                        if(itemscontact!=null){
                            itemscontact.clear();
                        }
                        else {
                            itemscontact=new ArrayList<>();
                        }
                        itemscontact = response.body();
                        Log.e("User",String.valueOf(itemscontact.size()));
                        chips_to.clearFilteredChips();
                        chips_bcc.clearFilteredChips();
                        chips_cc.clearFilteredChips();

                        chips_to.setFilterableChipList(itemscontact);
                        chips_to.setSelectedChipList(itemscont);
                        chips_bcc.setFilterableChipList(itemscontact);
                        chips_cc.setFilterableChipList(itemscontact);
                    } else {
//                        Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(ReplyActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<listcontact>> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ReplyActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
