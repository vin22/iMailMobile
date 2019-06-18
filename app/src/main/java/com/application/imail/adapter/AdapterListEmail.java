package com.application.imail.adapter;

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

import com.application.imail.R;
import com.application.imail.model.listemail;
import com.application.imail.user.ReadMessageActivity;
import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AdapterListEmail extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<listemail> items = new ArrayList<>();
    private List<listemail> itemsfilter = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, listemail obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListEmail(Context context, List<listemail> items) {
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
        final listemail p = itemsfilter.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            view.nama.setText(p.getSendername());
            view.subject.setText(p.getSubject());
            view.date.setText(p.getSent_date());
            view.message.setText(p.getMessage());
            if(p.getFolder().equals("Trash")){
                view.starred.setVisibility(View.GONE);
            }
            if(p.getStarred()){
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
                    Log.e("image","Masuk");
                    if(!p.getStarred()){
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
                public void onClick(View view) {
                    ArrayList<String>itemsemail=new ArrayList<>();
                    itemsemail.add(items.get(position).getSubject());
                    itemsemail.add(String.valueOf(items.get(position).getStarred()));
                    itemsemail.add(items.get(position).getFolder());
                    itemsemail.add(items.get(position).getSendername());
                    itemsemail.add(items.get(position).getReceiver());
                    itemsemail.add(items.get(position).getSent_date());
                    itemsemail.add(items.get(position).getMessage());
                    Intent intent=new Intent(ctx, ReadMessageActivity.class);
                    intent.putStringArrayListExtra("email",itemsemail);
                    ctx.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsfilter.size();
    }

    public void setItems(List<listemail> filteredGalaxies)
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
                    List<listemail> foundFilters = new ArrayList<>();

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
                        else if(items.get(i).getMessage().toUpperCase().contains(constraint)){
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
                setItems((List<listemail>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}