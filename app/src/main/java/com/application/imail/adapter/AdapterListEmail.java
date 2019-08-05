package com.application.imail.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.imail.R;
import com.application.imail.config.SessionManager;
import com.application.imail.model.Message;
import com.application.imail.model.listemail;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.MessageService;
import com.application.imail.user.ReadMessageActivity;
import com.application.imail.utils.FlipAnimator;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListEmail extends RecyclerView.Adapter<AdapterListEmail.OriginalViewHolder> implements Filterable {
    private MessageAdapterListener listener;
    private List<Message> items = new ArrayList<>();
    private List<Message> itemsfilter = new ArrayList<>();
    MessageService messageService = APIUtils.getMessageService();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    ProgressDialog pd;

    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    public interface OnItemClickListener {
        void onItemClick(View view, Message obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListEmail(Context context, List<Message> items, MessageAdapterListener listener) {
        this.listener = listener;
        this.items = items;
        ctx = context;
        this.itemsfilter = items;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public CircularImageView image, imageback;
        public ImageButton select, starred, delete;
        public TextView subject, message, nama, date;
        public View lyt_parent;
        public MaterialRippleLayout layout;

        public OriginalViewHolder(View v) {
            super(v);
            subject = v.findViewById(R.id.subject);
            message = v.findViewById(R.id.message);
            date = v.findViewById(R.id.date);
            image =  v.findViewById(R.id.image);
            imageback =  v.findViewById(R.id.imageback);
            select =  v.findViewById(R.id.select);
            starred =  v.findViewById(R.id.starred);
            delete =  v.findViewById(R.id.delete);
            nama = (TextView) v.findViewById(R.id.nama);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            layout = v.findViewById(R.id.layout_email);
            v.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }

    @Override
    public OriginalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OriginalViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_email, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final OriginalViewHolder view, final int position) {
        final Message p = itemsfilter.get(position);
//        if (holder instanceof OriginalViewHolder) {
//            final OriginalViewHolder view = (OriginalViewHolder) holder;
            SimpleDateFormat formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat format=new SimpleDateFormat("dd MMM yy");


            Glide.with(ctx).load(R.drawable.ic_person).into(view.image);
            Glide.with(ctx).load(R.drawable.ic_check_circle).into(view.imageback);
            // change the row state to activated
            view.itemView.setActivated(selectedItems.get(position, false));

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMessageRowClicked(position);
                }
            });

            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onRowLongClicked(position);
                    view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                    return true;
                }
            });

            applyIconAnimation(view,position);
//            view.lyt_parent.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    view.image.setVisibility(View.GONE);
//                    view.select.setVisibility(View.VISIBLE);
//                    return false;
//                }
//            });
//            view.image.setVisibility(p.isChecked() ? View.VISIBLE : View.GONE);
//            view.select.setVisibility(!p.isChecked() ? View.VISIBLE : View.GONE);
//            textView.setText(employee.getName());
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    employee.setChecked(!employee.isChecked());
//                    imageView.setVisibility(employee.isChecked() ? View.VISIBLE : View.GONE);
//                }
//            });

//            view.select.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(p.isChecked()){
//                        p.setChecked(false);
//                    }
//                    else{
//                        p.setChecked(true);
//                    }
//                }
//            });

            if(!p.isRead()){
                view.nama.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                view.subject.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            else{
                view.nama.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                view.subject.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
            if(p.getFolder().equals("Draft")) {
                try{
                    if(p.getSendername().equals("null")){
                        view.nama.setText("(Draft)");
                    }
                    else if(p.getSendername().equals("")){
                        view.nama.setText("(Draft)");
                    }
                    else{
                        view.nama.setText(p.getSendername()+"(Draft)");
                    }
                }catch (Exception e){
                    view.nama.setText("(Draft)");
                    e.printStackTrace();
                }
            }
            else{
                view.nama.setText(p.getSendername());
            }
            try{
                if(p.getSubject().equals("null")){
                    view.subject.setText("[no Subject]");
                }
                else if(p.getSubject().equals("")){
                    view.subject.setText("[no Subject]");
                }
                else{
                    view.subject.setText(p.getSubject());
                }
            }catch (Exception e){
                view.subject.setText("[no Subject]");
                e.printStackTrace();
            }

//            catch (Exception e){
//                view.subject.setText("[no Subject]");
//                e.printStackTrace();
//                Log.e("Date","Masuk");
//            }
            try {
//                if(p.getFolder().equals("Inbox")){
//                    formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                }
                Calendar cal=Calendar.getInstance();
                if((cal.getTime().getTime()-formatapi.parse(p.getDate().substring(0,19)).getTime())<=(1000*60*60)){
                    view.date.setText(String.valueOf((cal.getTime().getTime()-formatapi.parse(p.getDate().substring(0,19)).getTime())/(1000*60*60)+1)+" hour ago");
                }
                else if((cal.getTime().getTime()-formatapi.parse(p.getDate().substring(0,19)).getTime())<(1000*60*60*24)){
                    view.date.setText(String.valueOf((cal.getTime().getTime()-formatapi.parse(p.getDate().substring(0,19)).getTime())/(1000*60*60)+1)+" hours ago");
                }
                else{
                    view.date.setText(format.format(formatapi.parse(p.getDate().substring(0,19))));
                }
            }
            catch (ParseException e){
                e.printStackTrace();
                Log.e("Date",p.getDate());
            }
//            if(p.getBody().toString().equals("null")){
//                view.message.setText("[no Message]");
//            }
//            else if(p.getBody().equals("")){
//
//            }
            try {
                if (p.getBody().equals("null")) {
                    view.message.setText("[no Message]");
                } else if (p.getBody().equals("")) {
                    view.message.setText("[no Message]");
                } else {
                    view.message.setText(Html.fromHtml(p.getBody()));
                }
            }
            catch (Exception e){
                view.message.setText("[no Message]");
                e.printStackTrace();
                Log.e("Date","Masuk");
            }
            if(p.isTrash()){
                view.starred.setVisibility(View.GONE);
            }
            if(p.getFolder().equals("Starred")){
                view.delete.setVisibility(View.GONE);
            }
            if(p.isStarred()){
                Glide.with(ctx).load(R.drawable.ic_star).into(view.starred);
                Log.e("image","Star");
            }
            else{
                Glide.with(ctx).load(R.drawable.ic_star_border).into(view.starred);
                Log.e("image","Star_border");
            }
            view.starred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Starred(p.getMessageID(), p.getFolder());
                    Log.e("image","Masuk");
                    if(!p.isStarred()){
                        p.setStarred(true);
                        Glide.with(ctx).load(R.drawable.ic_star).into(view.starred);
                    }
                    else{
                        p.setStarred(false);
                        Glide.with(ctx).load(R.drawable.ic_star_border).into(view.starred);
                    }
                }
            });
            view.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(p.getFolder().equals("Inbox")){
                        AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Email in Inbox").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Inbox");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Inbox");
                                    pd.show();
                                }
                                Call<Message> call = messageService.deleteinbox(p.getMessageID());
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                SessionManager sessionManager = SessionManager.with(ctx);
                                                Call<List<Message>> call1 = messageService.read(sessionManager.getuserloggedin().getUserID(),sessionManager.getuserloggedin().getEmail(), sessionManager.getuserloggedin().getPassword());
                                                call1.enqueue(new Callback<List<Message>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                                        if(response.isSuccessful()){
                                                            Log.e("User","Masuk1");
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {
                                                                items= response.body();
                                                                itemsfilter = response.body();
                                                                for(int i=0;i<items.size();i++){
                                                                    items.get(i).setFolder("Inbox");
                                                                    itemsfilter.get(i).setFolder("Inbox");
                                                                }
                                                                Collections.sort(items);
                                                                Collections.sort(itemsfilter);
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }

                                                            } else {
                                                                items.clear();
                                                                itemsfilter.clear();
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            if(pd.isShowing()){
                                                                pd.dismiss();
                                                            }
                                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Message>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
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
                    else if(p.getFolder().equals("Spam")){
                        AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Email in Spam").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Spam");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Spam");
                                    pd.show();
                                }
                                Call<Message> call = messageService.deletespam(p.getMessageID());
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                SessionManager sessionManager = SessionManager.with(ctx);
                                                Call<List<Message>> call1 = messageService.readspam(sessionManager.getuserloggedin().getUserID(),sessionManager.getuserloggedin().getEmail(), sessionManager.getuserloggedin().getPassword());
                                                call1.enqueue(new Callback<List<Message>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                                        if(response.isSuccessful()){
                                                            Log.e("User","Masuk1");
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {
                                                                items= response.body();
                                                                itemsfilter = response.body();
                                                                for(int i=0;i<items.size();i++){
                                                                    items.get(i).setFolder("Spam");
                                                                    itemsfilter.get(i).setFolder("Spam");
                                                                }
                                                                Collections.sort(items);
                                                                Collections.sort(itemsfilter);
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }

                                                            } else {
                                                                items.clear();
                                                                itemsfilter.clear();
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            if(pd.isShowing()){
                                                                pd.dismiss();
                                                            }
                                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Message>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
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
                    else if(p.getFolder().equals("Sent")){
                        AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Email in Sent").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Sent");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Sent");
                                    pd.show();
                                }
                                Call<Message> call = messageService.deletesent(p.getMessageID());
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                SessionManager sessionManager = SessionManager.with(ctx);
                                                Call<List<Message>> call1 = messageService.getsent(sessionManager.getuserloggedin().getUserID());
                                                call1.enqueue(new Callback<List<Message>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                                        if(response.isSuccessful()){
                                                            Log.e("User","Masuk1");
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {
                                                                items = response.body();
                                                                itemsfilter = response.body();
                                                                for(int i=0;i<items.size();i++){
                                                                    items.get(i).setFolder("Sent");
                                                                    itemsfilter.get(i).setFolder("Sent");
                                                                }
                                                                Collections.sort(items);
                                                                Collections.sort(itemsfilter);
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }

                                                            } else {
                                                                items.clear();
                                                                itemsfilter.clear();
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            if(pd.isShowing()){
                                                                pd.dismiss();
                                                            }
                                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Message>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            } else {
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
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
//                    else if(p.getFolder().equals("Starred")){
//
//                    }
                    else if(p.getFolder().equals("Draft")){
                        AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Email in Draft").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Draft");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Draft");
                                    pd.show();
                                }
                                Call<Message> call = messageService.deletedraft(p.getMessageID());
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                SessionManager sessionManager = SessionManager.with(ctx);
                                                Call<List<Message>> call1 = messageService.getdraft(sessionManager.getuserloggedin().getUserID());
                                                call1.enqueue(new Callback<List<Message>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                                        if(response.isSuccessful()){
                                                            Log.e("User","Masuk1");
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {
                                                                items = response.body();
                                                                itemsfilter = response.body();
                                                                for(int i=0;i<items.size();i++) {
                                                                    items.get(i).setFolder("Draft");
                                                                    itemsfilter.get(i).setFolder("Draft");
                                                                }
                                                                Collections.sort(items);
                                                                Collections.sort(itemsfilter);
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }

                                                            } else {
                                                                items.clear();
                                                                itemsfilter.clear();
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            if(pd.isShowing()){
                                                                pd.dismiss();
                                                            }
                                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Message>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
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
                    else if(p.getFolder().equals("Trash")){
                        AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Email in Trash").setMessage("Are you sure to delete this email?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Trash");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Delete email in Trash");
                                    pd.show();
                                }
                                Call<Message> call = messageService.deletetrash(p.getMessageID());
                                call.enqueue(new Callback<Message>() {
                                    @Override
                                    public void onResponse(Call<Message> call, Response<Message> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                SessionManager sessionManager = SessionManager.with(ctx);
                                                Call<List<Message>> call1 = messageService.gettrash(sessionManager.getuserloggedin().getUserID());
                                                call1.enqueue(new Callback<List<Message>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                                        if(response.isSuccessful()){
                                                            Log.e("User","Masuk1");
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {

                                                                items = response.body();
                                                                itemsfilter = response.body();
                                                                for(int i=0;i<items.size();i++){
                                                                    items.get(i).setFolder("Trash");
                                                                    itemsfilter.get(i).setFolder("Trash");
                                                                }
                                                                Collections.sort(items);
                                                                Collections.sort(itemsfilter);
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                            } else {
                                                                items.clear();
                                                                itemsfilter.clear();
                                                                notifyDataSetChanged();
                                                                if(pd.isShowing()){
                                                                    pd.dismiss();
                                                                }
                                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        else{
                                                            if(pd.isShowing()){
                                                                pd.dismiss();
                                                            }
                                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Message>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            } else {
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Message> call, Throwable t) {
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
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
            });

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String>itemsemail=new ArrayList<>();
                    itemsemail.add(view.subject.getText().toString());
                    itemsemail.add(String.valueOf(items.get(position).isStarred()));
                    itemsemail.add(items.get(position).getFolder());
                    itemsemail.add(view.nama.getText().toString()+"-"+p.getSender());
                    try{
                        if(items.get(position).getReceiver().equals("null")){
                            itemsemail.add("");
                        }
                        else if(items.get(position).getReceiver().equals("")){
                            itemsemail.add("");
                        }
                        else{
                            itemsemail.add(items.get(position).getReceiver());
                        }
                    }catch (Exception e){
                        itemsemail.add("");
                        e.printStackTrace();
                    }

                    itemsemail.add(items.get(position).getDate());
                    try {
                        if (p.getBody().equals("null")) {
                            itemsemail.add("[no Message]");
                        } else if (p.getBody().equals("")) {
                            itemsemail.add("[no Message]");
                        } else {
                            itemsemail.add(p.getBody());
                        }
                    }
                    catch (Exception e){
                        itemsemail.add("[no Message]");
                        e.printStackTrace();
                    }
//                    itemsemail.add(view.message.getText().toString());
                    itemsemail.add(String.valueOf(items.get(position).getMessageID()));
                    itemsemail.add(String.valueOf(items.get(position).getSender()));
                    for(int i=0;i<itemsemail.size();i++){
                        Log.e("Items",itemsemail.get(i));
                    }
                    Intent intent=new Intent(ctx, ReadMessageActivity.class);
                    intent.putStringArrayListExtra("email",itemsemail);

                    ((Activity)ctx).startActivityForResult(intent, 200);

                    //make isRead=true
                    Call<Message> call1 = messageService.readinbox(p.getMessageID());
                    call1.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.isSuccessful()) {
                                Log.e("User", "Masuk1");
                                String status = response.body().getStatus();
                                String statusmessage = response.body().getMessage();
                                if (status.equals("true")) {

//                                    items = response.body();
//                                    itemsfilter = response.body();
//                                    for (int i = 0; i < items.size(); i++) {
//                                        items.get(i).setFolder("Trash");
//                                        itemsfilter.get(i).setFolder("Trash");
//                                    }
//                                    notifyDataSetChanged();
//                                    if (pd.isShowing()) {
//                                        pd.dismiss();
//                                    }
                                } else {
//                                    if (pd.isShowing()) {
//                                        pd.dismiss();
//                                    }
//                                    Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Log.e("USER ACTIVITY ERROR", t.getMessage());
//                            if (pd.isShowing()) {
//                                pd.dismiss();
//                            }
                            Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
//        }
    }

    @Override
    public int getItemCount() {
        return itemsfilter.size();
    }

    public void setItems(List<Message> filteredGalaxies)
    {
        this.itemsfilter=filteredGalaxies;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                Filter.FilterResults filterResults = new Filter.FilterResults();

                if (constraint.length() > 0) {
                    //CHANGE TO UPPER
                    constraint = constraint.toString().toUpperCase();

                    //HOLD FILTERS WE FIND
                    List<Message> foundFilters = new ArrayList<>();

                    String galaxy;

                    //ITERATE CURRENT LIST
                    for (int i = 0; i < items.size(); i++) {

                        galaxy = items.get(i).getSendername();

                        //SEARCH
                        if (galaxy.toUpperCase().contains(constraint)) {
                            //ADD IF FOUND
                            foundFilters.add(items.get(i));
                        }
                        else if(items.get(i).getSubject().toUpperCase().contains(constraint)){
                            //ADD IF FOUND
                            foundFilters.add(items.get(i));
                        }
                        else if(items.get(i).getBody().toUpperCase().contains(constraint)){
                            //ADD IF FOUND
                            foundFilters.add(items.get(i));
                        }
                    }

                    //SET RESULTS TO FILTER LIST
                    filterResults.count = foundFilters.size();
                    filterResults.values = foundFilters;
                } else {
                    //NO ITEM FOUND.LIST REMAINS INTACT
                    filterResults.count = items.size();
                    filterResults.values = items;
                }

                //RETURN RESULTS
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
                setItems((List<Message>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public void Starred(int messageid, final String folder){
        Call<Message> call = messageService.starred(messageid);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    String status=response.body().getStatus();
                    String statusmessage=response.body().getMessage();
                    if (status.equals("true")) {
                        if(folder.equals("Starred")) {
                            SessionManager sessionManager = SessionManager.with(ctx);
                            Call<List<Message>> call1 = messageService.getstarred(sessionManager.getuserloggedin().getUserID());
                            call1.enqueue(new Callback<List<Message>>() {
                                @Override
                                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                                    if (response.isSuccessful()) {
                                        Log.e("User", "Masuk1");
                                        String status = response.body().get(0).getStatus();
                                        String statusmessage = response.body().get(0).getMessage();
                                        if (status.equals("true")) {
                                            items = response.body();
                                            itemsfilter = response.body();
                                            for (int i = 0; i < items.size(); i++) {
                                                items.get(i).setFolder("Starred");
                                                itemsfilter.get(i).setFolder("Starred");
                                            }
                                            Collections.sort(items);
                                            Collections.sort(itemsfilter);
                                            notifyDataSetChanged();

                                        } else {
                                            items.clear();
                                            itemsfilter.clear();
                                            notifyDataSetChanged();
                                            Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();

                                        }
                                    } else {
                                        Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Message>> call, Throwable t) {
                                    Log.e("USER ACTIVITY ERROR", t.getMessage());
                                    Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e("USER ACTIVITY ERROR", t.getMessage());
                Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public long getItemId(int position) {
        return itemsfilter.get(position).getMessageID();
    }

    private void applyIconAnimation(final OriginalViewHolder holder, final int position) {
        if (selectedItems.get(position, false)) {
            holder.image.setVisibility(View.GONE);
            resetIconYAxis(holder.imageback);
            holder.imageback.setVisibility(View.VISIBLE);
//            holder.imageback.setAlpha(1);
            Glide.with(ctx).load(R.drawable.ic_check_circle).into(holder.imageback);
            if (currentSelectedIndex == position) {
//                holder.image.setVisibility(View.VISIBLE);
//                holder.select.setVisibility(View.GONE);
                FlipAnimator.flipView(ctx, holder.imageback, holder.image, true);
                resetCurrentIndex();
            }
        } else {
            holder.imageback.setVisibility(View.GONE);
            resetIconYAxis(holder.image);
            holder.image.setVisibility(View.VISIBLE);
            Glide.with(ctx).load(R.drawable.ic_person).into(holder.image);
//            holder.image.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
//                holder.image.setVisibility(View.GONE);
//                holder.select.setVisibility(View.VISIBLE);
                FlipAnimator.flipView(ctx, holder.imageback, holder.image, false);
                resetCurrentIndex();
            }
        }
    }

    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }

    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Message> getSelectedItems() {
        List<Message> items =
                new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(itemsfilter.get(selectedItems.keyAt(i)));
        }
        return items;
    }

//    public List<Integer> getSelectedItems() {
//        List<Integer> items =
//                new ArrayList<>(selectedItems.size());
//        for (int i = 0; i < selectedItems.size(); i++) {
//            items.add(selectedItems.keyAt(i));
//        }
//        return items;
//    }

    public void removeData(int position) {
        itemsfilter.remove(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface MessageAdapterListener {
        void onIconClicked(int position);

        void onMessageRowClicked(int position);

        void onRowLongClicked(int position);
    }
}