package com.application.imail.user;

import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.application.imail.model.Message;
import com.application.imail.model.listcontact;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.ContactService;
import com.application.imail.remote.MessageService;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.richeditor.RichEditor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForwardActivity extends AppCompatActivity {
    MessageService messageService;
    CoordinatorLayout parent_view;
    MaterialSpinner spinnerfrom;
    TextView to, bcc, cc, fwd, lastmessage, message;
    ChipsInputLayout chips_to, chips_bcc, chips_cc ;
    SessionManager sessionManager;
    ContactService contactService;
    List<listcontact> itemscontact;
    List<listcontact> itemscont;

    private RichEditor mEditor;
    ProgressDialog pd;
    boolean isinput=false;
    TextView mPreview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        initToolbar();
        initComponent();
        messageService= APIUtils.getMessageService();
        contactService= APIUtils.getContactService();
        sessionManager=SessionManager.with(this);
        setEmail();
        initListener();
        getContacts();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forward");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ForwardActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ForwardActivity.this.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initComponent() {
        parent_view=findViewById(R.id.parent_view);
        fwd=findViewById(R.id.edittext_fwd);
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
        mPreview = (TextView) findViewById(R.id.preview);

        mEditor.setPlaceholder("Message");
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setEditorFontSize(20);
        mEditor.setFontSize(20);

    }

    private void initListener(){
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override public void onTextChange(String text) {
                isinput=true;
                mPreview.setText(text);
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


        fwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isinput=true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chips_to.clearFocus();
        chips_bcc.clearFocus();
        chips_cc.clearFocus();

        chips_to.setOnChipsInputTextChangedListener(new ChipsInputLayout.OnChipsInputTextChangedListener() {
            @Override
            public void onChipsInputTextChanged(CharSequence charSequence) {
                isinput=true;
            }
        });

        chips_bcc.setOnChipsInputTextChangedListener(new ChipsInputLayout.OnChipsInputTextChangedListener() {
            @Override
            public void onChipsInputTextChanged(CharSequence charSequence) {
                isinput=true;
            }
        });

        chips_cc.setOnChipsInputTextChangedListener(new ChipsInputLayout.OnChipsInputTextChangedListener() {
            @Override
            public void onChipsInputTextChanged(CharSequence charSequence) {
                isinput=true;
            }
        });
    }

    public void setEmail(){
        spinnerfrom.setItems(sessionManager.getuserloggedin().getEmail());
//        chips_to.setSelectedChipList();
        if(itemscont==null){
            itemscont=new ArrayList<>();
        }
        else{
            itemscont.clear();
        }
        listcontact lc=new listcontact();
        lc.setEmail(getIntent().getStringExtra("to"));
        itemscont.add(lc);

        fwd.setText(getIntent().getStringExtra("subject"));
        mEditor.setHtml(getIntent().getStringExtra("message"));
        mPreview.setText(getIntent().getStringExtra("message"));
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
            addDraft();
            finish();
        }
        else if(id==R.id.action_attachment){
            return true;
        }
        else if(id==R.id.action_sent){
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        addDraft();
        finish();
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
                        chips_bcc.setFilterableChipList(itemscontact);
                        chips_cc.setFilterableChipList(itemscontact);
                    } else {
//                        Toast.makeText(ForwardActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ForwardActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<listcontact>> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ForwardActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addDraft(){
        if(isinput){
            String receiver="", cc="", bcc="";
            boolean select=false;
            for (int i = 0; i < chips_to.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_to.getSelectedChips()));
                if (!select) {
                    receiver += String.valueOf(chips_to.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", receiver);
                    select=true;
                }
                else if (select) {
                    receiver += ","+String.valueOf(chips_to.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", receiver);
                }
            }
            select=false;
            for (int i = 0; i < chips_bcc.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_bcc.getSelectedChips()));
                if (!select) {
                    bcc += String.valueOf(chips_bcc.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", bcc);
                    select=true;
                }
                else if (select) {
                    bcc += ","+String.valueOf(chips_bcc.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", bcc);
                }
            }
            select=false;
            for (int i = 0; i < chips_cc.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_cc.getSelectedChips()));
                if (!select) {
                    cc += String.valueOf(chips_cc.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", cc);
                    select=true;
                }
                else if (select) {
                    cc += ","+String.valueOf(chips_cc.getSelectedChipByPosition(i).getTitle());
                    Log.e("Masuk", cc);
                }
            }

            Call<Message> call = messageService.adddraft(sessionManager.getuserloggedin().getUserID(),spinnerfrom.getText().toString(),receiver, "","",fwd.getText().toString(),
                    mPreview.getText().toString(),cc,bcc,"");
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if(response.isSuccessful()){
                        String status=response.body().getStatus();
                        String statusmessage=response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ForwardActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForwardActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(ForwardActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.e("USER ACTIVITY ERROR", t.getMessage());
                    Toast.makeText(ForwardActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void sendEmail(){
        if(pd!=null){
            pd.setTitle("Please Wait");
            pd.setMessage("Forwarding your message");
            pd.show();
        }
        else{
            pd=new ProgressDialog(ForwardActivity.this);
            pd.setTitle("Please Wait");
            pd.setMessage("Forwarding your message");
            pd.show();
        }
        String receiver="", cc="", bcc="";
        boolean select=false;
        boolean emailvalid=true;
        for (int i = 0; i < chips_to.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_to.getSelectedChips()));
            if(chips_to.getSelectedChipByPosition(i).getTitle().equals(sessionManager.getuserloggedin().getEmail())){
                emailvalid=false;
                break;
            }
            if (!select) {
                receiver += String.valueOf(chips_to.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", receiver);
                select=true;
            }
            else if (select) {
                receiver += ","+String.valueOf(chips_to.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", receiver);
            }
        }
        select=false;
        for (int i = 0; i < chips_bcc.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_bcc.getSelectedChips()));
            if (!select) {
                bcc += String.valueOf(chips_bcc.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", bcc);
                select=true;
            }
            else if (select) {
                bcc += ","+String.valueOf(chips_bcc.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", bcc);
            }
        }
        select=false;
        for (int i = 0; i < chips_cc.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_cc.getSelectedChips()));
            if (!select) {
                cc += String.valueOf(chips_cc.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", cc);
                select=true;
            }
            else if (select) {
                cc += ","+String.valueOf(chips_cc.getSelectedChipByPosition(i).getTitle());
                Log.e("Masuk", cc);
            }
        }
        if(!emailvalid){
            Toast.makeText(ForwardActivity.this, "Email yang dituju merupakan email Anda", Toast.LENGTH_SHORT).show();
            chips_to.clearSelectedChips();
            chips_bcc.clearSelectedChips();
            chips_cc.clearSelectedChips();
            fwd.setText("");
            message.setText("");
            mEditor.setHtml(null);
            getContacts();
            isinput = false;
            fwd.setText(getIntent().getStringExtra("subject"));
            mEditor.setHtml(getIntent().getStringExtra("message"));
            mPreview.setText(getIntent().getStringExtra("message"));
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
        else if(receiver.equals("")){
            Toast.makeText(ForwardActivity.this, "Enter Receiver Email", Toast.LENGTH_SHORT).show();
            chips_to.clearSelectedChips();
            chips_bcc.clearSelectedChips();
            chips_cc.clearSelectedChips();
            fwd.setText("");
            message.setText("");
            mEditor.setHtml(null);
            getContacts();
            isinput = false;
            fwd.setText(getIntent().getStringExtra("subject"));
            mEditor.setHtml(getIntent().getStringExtra("message"));
            mPreview.setText(getIntent().getStringExtra("message"));
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
//        else if(subject.equals("") && mEditor.getHtml().equals("")){
//            Toast.makeText(ForwardActivity.this, "Enter Subject or Message", Toast.LENGTH_SHORT).show();
//            chips_to.clearSelectedChips();
//            chips_bcc.clearSelectedChips();
//            chips_cc.clearSelectedChips();
//            subject.setText("");
//            message.setText("");
//            mEditor.setHtml(null);
//            mPreview.setText("");
//            getContacts();
//            isinput = false;
//            if (pd.isShowing()) {
//                pd.dismiss();
//            }
//        }
        else {
//            Call<Message> call = messageService.send(sessionManager.getuserloggedin().getUserID(), spinnerfrom.getText().toString(), sessionManager.getuserloggedin().getName(), receiver, subject.getText().toString(),
////                    message.getText().toString(), cc, bcc);
            if(receiver.contains("com.tylersuehr.chips")){
                Toast.makeText(ForwardActivity.this, "Harap di enter email tujuan", Toast.LENGTH_SHORT).show();
            }
            else {
                Call<Message> call = messageService.send(sessionManager.getuserloggedin().getUserID(), spinnerfrom.getText().toString(), sessionManager.getuserloggedin().getName(), receiver, fwd.getText().toString(),
                        mPreview.getText().toString(), cc, bcc);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if (response.isSuccessful()) {
                            String status = response.body().getStatus();
                            String statusmessage = response.body().getMessage();
                            if (status.equals("true")) {
                                Toast.makeText(ForwardActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                chips_to.clearSelectedChips();
                                chips_bcc.clearSelectedChips();
                                chips_cc.clearSelectedChips();
                                fwd.setText("");
                                message.setText("");
                                mEditor.setHtml(null);
                                getContacts();
                                isinput = false;
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            } else {
                                Toast.makeText(ForwardActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                                chips_to.clearSelectedChips();
                                chips_bcc.clearSelectedChips();
                                chips_cc.clearSelectedChips();
                                fwd.setText("");
                                message.setText("");
                                mEditor.setHtml(null);
                                getContacts();
                                isinput = false;
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                        } else {
                            if (pd.isShowing()) {
                                pd.dismiss();
                            }
                            Toast.makeText(ForwardActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                        Toast.makeText(ForwardActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

}
