package com.application.imail.user;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.application.imail.model.Message;
import com.application.imail.model.listcontact;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.ContactService;
import com.application.imail.remote.MessageService;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tylersuehr.chips.ChipsInputLayout;
import com.tylersuehr.chips.Chip;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComposeMessageActivity extends AppCompatActivity {
    MessageService messageService;
    CoordinatorLayout parent_view;
    MaterialSpinner spinnerfrom;
    TextView to, bcc, cc, subject, message;
    ChipsInputLayout chips_to, chips_bcc, chips_cc, chips_subject, chips_message;
    boolean isinput=false;
    SessionManager sessionManager;
    ContactService contactService;
    List<listcontact>itemscontact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_message);
        initToolbar();
        initComponent();
        initListener();
        messageService= APIUtils.getMessageService();
        contactService= APIUtils.getContactService();
        setEmail();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.black));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Compose");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ComposeMessageActivity.this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ComposeMessageActivity.this.getResources().getColor(R.color.colorPrimary));
        }
    }

    private void initComponent() {
        parent_view=findViewById(R.id.parent_view);
        subject=findViewById(R.id.edittext_subject);
        message=findViewById(R.id.edittext_message);
        spinnerfrom=findViewById(R.id.spinnerfrom);
//        to=findViewById(R.id.edittext_to);
//        bcc=findViewById(R.id.edittext_bcc);
//        cc=findViewById(R.id.edittext_cc);

        chips_to = (ChipsInputLayout)findViewById(R.id.chips_to);
        chips_bcc = (ChipsInputLayout)findViewById(R.id.chips_bcc);
        chips_cc = (ChipsInputLayout)findViewById(R.id.chips_cc);
//        chips_subject = (ChipsInputLayout)findViewById(R.id.chips_subject);
//        chips_message = (ChipsInputLayout)findViewById(R.id.chips_message);
        // ...Cool logic to acquire chips
//        List<Chip> contactList = new ArrayList<>();
//        contactList.add(new Chip() {
//            @Nullable
//            @Override
//            public Object getId() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public String getTitle() {
//                return "Tes1";
//            }
//
//            @Nullable
//            @Override
//            public String getSubtitle() {
//                return "tes1@email.com";
//            }
//
//            @Nullable
//            @Override
//            public Uri getAvatarUri() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Drawable getAvatarDrawable() {
//                return null;
//            }
//        });
//        contactList.add(new Chip() {
//            @Nullable
//            @Override
//            public Object getId() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public String getTitle() {
//                return "Tes2";
//            }
//
//            @Nullable
//            @Override
//            public String getSubtitle() {
//                return "tes2@email.com";
//            }
//
//            @Nullable
//            @Override
//            public Uri getAvatarUri() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Drawable getAvatarDrawable() {
//                return null;
//            }
//        });
//
//        List<Chip> contactList1 = new ArrayList<>();
//        contactList1.add(new Chip() {
//            @Nullable
//            @Override
//            public Object getId() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public String getTitle() {
//                return "Subjek1";
//            }
//
//            @Nullable
//            @Override
//            public String getSubtitle() {
//                return "subjek1@email.com";
//            }
//
//            @Nullable
//            @Override
//            public Uri getAvatarUri() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Drawable getAvatarDrawable() {
//                return null;
//            }
//        });
//        contactList1.add(new Chip() {
//            @Nullable
//            @Override
//            public Object getId() {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public String getTitle() {
//                return "Subjek2";
//            }
//
//            @Nullable
//            @Override
//            public String getSubtitle() {
//                return "subjek2@email.com";
//            }
//
//            @Nullable
//            @Override
//            public Uri getAvatarUri() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public Drawable getAvatarDrawable() {
//                return null;
//            }
//        });


        message.addTextChangedListener(new TextWatcher() {
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
        subject.addTextChangedListener(new TextWatcher() {
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
//        chips_subject.setFilterableChipList(contactList1);
//        chips_message.setFilterableChipList(contactList1);
//        chips_bcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chips_bcc.requestFocus();
//            }
//        });
//        chips_subject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chips_subject.requestFocus();
//            }
//        });
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



//        chips_message.requestFocus();
//        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
//        ChipsInput chipsInput = (ChipsInput) findViewById(R.id.chips_input);
//
//        // build the ContactChip list
//        List<Chip> contactList = new ArrayList<>();
//        contactList.add(new Chip("tes1","info"));
//        contactList.add(new Chip("tes2","info"));
////        chipsInput.addChip("tes1",  "info");
////        chipsInput.addChip("tes2",  "info");
//        // pass the ContactChip list
//        chipsInput.setFilterableList(contactList);
        getContacts();
    }

    private void initListener(){

    }

    public void setEmail(){
        sessionManager=SessionManager.with(this);
        spinnerfrom.setItems(sessionManager.getuserloggedin().Email);
    }

    public void getContacts(){
        Call<List<listcontact>> call = contactService.getcontact(sessionManager.getuserloggedin().getUserID());
        call.enqueue(new Callback<List<listcontact>>() {
            @Override
            public void onResponse(Call<List<listcontact>> call, Response<List<listcontact>> response) {
                if(response.isSuccessful()){
                    Log.e("User","Masuk1");
                    String status=response.body().get(0).getStatus();
                    String statusmessage=response.body().get(0).getMessage();
                    if (status.equals("true")) {
                        if(itemscontact!=null){
                            itemscontact.clear();
                        }
                        else {
                            itemscontact=new ArrayList<>();
                        }
                        itemscontact = response.body();
                        Log.e("User",String.valueOf(itemscontact));
                        chips_to.clearFilteredChips();
                        chips_bcc.clearFilteredChips();
                        chips_cc.clearFilteredChips();

                        chips_to.setFilterableChipList(itemscontact);
                        chips_bcc.setFilterableChipList(itemscontact);
                        chips_cc.setFilterableChipList(itemscontact);
                    } else {
                        Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(ComposeMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<listcontact>> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ComposeMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });
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

    public void addDraft(){
        if(isinput){
            String receiver="", cc="", bcc="";
            boolean select=false;
            for (int i = 0; i < chips_to.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_to.getSelectedChips()));
                if (!select) {
                    receiver += String.valueOf(chips_to.getSelectedChipByPosition(i));
                    Log.e("Masuk", receiver);
                    select=true;
                }
                else if (select) {
                    receiver += ","+String.valueOf(chips_to.getSelectedChipByPosition(i));
                    Log.e("Masuk", receiver);
                }
            }
            select=false;
            for (int i = 0; i < chips_bcc.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_bcc.getSelectedChips()));
                if (!select) {
                    bcc += String.valueOf(chips_bcc.getSelectedChipByPosition(i));
                    Log.e("Masuk", bcc);
                    select=true;
                }
                else if (select) {
                    bcc += ","+String.valueOf(chips_bcc.getSelectedChipByPosition(i));
                    Log.e("Masuk", bcc);
                }
            }
            select=false;
            for (int i = 0; i < chips_cc.getSelectedChips().size(); i++) {
                Log.e("Masuk", String.valueOf(chips_cc.getSelectedChips()));
                if (!select) {
                    cc += String.valueOf(chips_cc.getSelectedChipByPosition(i));
                    Log.e("Masuk", cc);
                    select=true;
                }
                else if (select) {
                    cc += ","+String.valueOf(chips_cc.getSelectedChipByPosition(i));
                    Log.e("Masuk", cc);
                }
            }

            Call<Message> call = messageService.adddraft(spinnerfrom.getText().toString(),receiver, "","",subject.getText().toString(),
                    message.getText().toString(),cc,bcc,"");
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if(response.isSuccessful()){
                        String status=response.body().getStatus();
                        String statusmessage=response.body().getMessage();
                        if (status.equals("true")) {
                            Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(ComposeMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.e("USER ACTIVITY ERROR", t.getMessage());
                    Toast.makeText(ComposeMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void sendEmail(){
        String receiver="", cc="", bcc="";
        boolean select=false;
        for (int i = 0; i < chips_to.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_to.getSelectedChips()));
            if (!select) {
                receiver += String.valueOf(chips_to.getSelectedChipByPosition(i));
                Log.e("Masuk", receiver);
                select=true;
            }
            else if (select) {
                receiver += ","+String.valueOf(chips_to.getSelectedChipByPosition(i));
                Log.e("Masuk", receiver);
            }
        }
        select=false;
        for (int i = 0; i < chips_bcc.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_bcc.getSelectedChips()));
            if (!select) {
                bcc += String.valueOf(chips_bcc.getSelectedChipByPosition(i));
                Log.e("Masuk", bcc);
                select=true;
            }
            else if (select) {
                bcc += ","+String.valueOf(chips_bcc.getSelectedChipByPosition(i));
                Log.e("Masuk", bcc);
            }
        }
        select=false;
        for (int i = 0; i < chips_cc.getSelectedChips().size(); i++) {
            Log.e("Masuk", String.valueOf(chips_cc.getSelectedChips()));
            if (!select) {
                cc += String.valueOf(chips_cc.getSelectedChipByPosition(i));
                Log.e("Masuk", cc);
                select=true;
            }
            else if (select) {
                cc += ","+String.valueOf(chips_cc.getSelectedChipByPosition(i));
                Log.e("Masuk", cc);
            }
        }

        Call<Message> call = messageService.send(sessionManager.getuserloggedin().getUserID(),spinnerfrom.getText().toString(),sessionManager.getuserloggedin().getName(),receiver,subject.getText().toString(),
                message.getText().toString(),cc,bcc);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    String status=response.body().getStatus();
                    String statusmessage=response.body().getMessage();
                    if (status.equals("true")) {
                        Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                        chips_to.clearSelectedChips();
                        chips_bcc.clearSelectedChips();
                        chips_cc.clearSelectedChips();
                        subject.setText("");
                        message.setText("");
                        getContacts();
                    } else {
                        Toast.makeText(ComposeMessageActivity.this, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ComposeMessageActivity.this, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ComposeMessageActivity.this, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
