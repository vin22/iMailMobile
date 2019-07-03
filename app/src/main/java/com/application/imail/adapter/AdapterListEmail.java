package com.application.imail.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.application.imail.model.Message;
import com.application.imail.model.listemail;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.MessageService;
import com.application.imail.user.ReadMessageActivity;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListEmail extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Message> items = new ArrayList<>();
    private List<Message> itemsfilter = new ArrayList<>();
    MessageService messageService = APIUtils.getMessageService();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Message obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListEmail(Context context, List<Message> items) {
        this.items = items;
        ctx = context;
        this.itemsfilter = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageButton starred;
        public TextView subject, message, nama, date;
        public View lyt_parent;
        public MaterialRippleLayout layout;

        public OriginalViewHolder(View v) {
            super(v);
            subject = v.findViewById(R.id.subject);
            message = v.findViewById(R.id.message);
            date = v.findViewById(R.id.date);
            image =  v.findViewById(R.id.image);
            starred =  v.findViewById(R.id.starred);
            nama = (TextView) v.findViewById(R.id.nama);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            layout = v.findViewById(R.id.layout_email);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_email, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Message p = itemsfilter.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            SimpleDateFormat formatapi=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat format=new SimpleDateFormat("dd MMM yy");

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
                view.date.setText(format.format(formatapi.parse(p.getDate())));
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
                    view.message.setText(p.getBody());
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
                    Starred(p.getMessageID());
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

            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String>itemsemail=new ArrayList<>();
                    itemsemail.add(view.subject.getText().toString());
                    itemsemail.add(String.valueOf(items.get(position).isStarred()));
                    itemsemail.add(items.get(position).getFolder());
                    itemsemail.add(view.nama.getText().toString());
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
                    itemsemail.add(view.message.getText().toString());
                    itemsemail.add(String.valueOf(items.get(position).getMessageID()));
                    for(int i=0;i<itemsemail.size();i++){
                        Log.e("Items",itemsemail.get(i));
                    }
                    Intent intent=new Intent(ctx, ReadMessageActivity.class);
                    intent.putStringArrayListExtra("email",itemsemail);

                    ((Activity)ctx).startActivityForResult(intent, 200);
                }
            });
        }
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

    public void Starred(int messageid){
        Call<Message> call = messageService.starred(messageid);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if(response.isSuccessful()){
                    String status=response.body().getStatus();
                    String statusmessage=response.body().getMessage();
                    if (status.equals("true")) {
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
}