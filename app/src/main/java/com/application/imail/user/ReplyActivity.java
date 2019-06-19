package com.application.imail.user;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tylersuehr.chips.Chip;
import com.tylersuehr.chips.ChipsInputLayout;

import java.util.ArrayList;
import java.util.List;


public class ReplyActivity extends AppCompatActivity {
    CoordinatorLayout parent_view;
    MaterialSpinner spinnerfrom;
    TextView to, bcc, cc, re, lastmessage, message;
    ChipsInputLayout chips_to, chips_bcc, chips_cc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        initToolbar();
        initComponent();
        initListener();
//        setEmail();
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

        List<Chip> contactList = new ArrayList<>();
        contactList.add(new Chip() {
            @Nullable
            @Override
            public Object getId() {
                return null;
            }

            @NonNull
            @Override
            public String getTitle() {
                return "Tes1";
            }

            @Nullable
            @Override
            public String getSubtitle() {
                return "tes1@email.com";
            }

            @Nullable
            @Override
            public Uri getAvatarUri() {
                return null;
            }

            @Nullable
            @Override
            public Drawable getAvatarDrawable() {
                return null;
            }
        });
        contactList.add(new Chip() {
            @Nullable
            @Override
            public Object getId() {
                return null;
            }

            @NonNull
            @Override
            public String getTitle() {
                return "Tes2";
            }

            @Nullable
            @Override
            public String getSubtitle() {
                return "tes2@email.com";
            }

            @Nullable
            @Override
            public Uri getAvatarUri() {
                return null;
            }

            @Nullable
            @Override
            public Drawable getAvatarDrawable() {
                return null;
            }
        });

        List<Chip> contactList1 = new ArrayList<>();
        contactList1.add(new Chip() {
            @Nullable
            @Override
            public Object getId() {
                return null;
            }

            @NonNull
            @Override
            public String getTitle() {
                return "Subjek1";
            }

            @Nullable
            @Override
            public String getSubtitle() {
                return "subjek1@email.com";
            }

            @Nullable
            @Override
            public Uri getAvatarUri() {
                return null;
            }

            @Nullable
            @Override
            public Drawable getAvatarDrawable() {
                return null;
            }
        });
        contactList1.add(new Chip() {
            @Nullable
            @Override
            public Object getId() {
                return null;
            }

            @NonNull
            @Override
            public String getTitle() {
                return "Subjek2";
            }

            @Nullable
            @Override
            public String getSubtitle() {
                return "subjek2@email.com";
            }

            @Nullable
            @Override
            public Uri getAvatarUri() {
                return null;
            }

            @Nullable
            @Override
            public Drawable getAvatarDrawable() {
                return null;
            }
        });

        chips_to.setFilterableChipList(contactList);
        chips_bcc.setFilterableChipList(contactList);
        chips_cc.setFilterableChipList(contactList);
    }

    private void initListener(){

    }

    public void setEmail(){
        SessionManager sessionManager=SessionManager.with(this);
        spinnerfrom.setItems(sessionManager.getuserloggedin().Email);
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
}
