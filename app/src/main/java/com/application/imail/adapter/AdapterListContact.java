package com.application.imail.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
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
import com.application.imail.config.SessionManager;
import com.application.imail.model.listcontact;
import com.application.imail.model.listemail;
import com.application.imail.remote.APIUtils;
import com.application.imail.remote.ContactService;
import com.application.imail.remote.UserService;
import com.application.imail.user.InboxActivity;
import com.application.imail.utils.InputValidation;
import com.balysv.materialripple.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterListContact extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<listcontact> items = new ArrayList<>();
    private List<listcontact> itemsfilter = new ArrayList<>();
    ContactService contactService = APIUtils.getContactService();
    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    InputValidation inputValidation = new InputValidation(ctx);
    ProgressDialog pd;
    public interface OnItemClickListener {
        void onItemClick(View view, listemail obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListContact(Context context, List<listcontact> items) {
        this.items = items;
        ctx = context;
        this.itemsfilter = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageButton edit, delete;
        public TextView nama, email;
        public View lyt_parent;
        public MaterialRippleLayout layout;

        public OriginalViewHolder(View v) {
            super(v);
            email = v.findViewById(R.id.email);
            image =  v.findViewById(R.id.image);
            edit =  v.findViewById(R.id.edit);
            delete =  v.findViewById(R.id.delete);
            nama = (TextView) v.findViewById(R.id.nama);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
            layout = v.findViewById(R.id.layout_contact);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final listcontact p = itemsfilter.get(position);
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;
            view.nama.setText(p.getName());
            view.email.setText(p.getEmail());
            view.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog=new Dialog(ctx);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_edit_contact);
                    dialog.show();

                    final TextInputLayout textInputLayoutName = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutName);
                    final TextInputLayout textInputLayoutPhone = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutPhoneNumber);
                    final TextInputEditText textInputEditTextName = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextName);
                    final TextInputEditText textInputEditTextPhone = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextPhoneNumber);
                    final AppCompatButton appCompatButtonEdit = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonEdit);
                    final AppCompatButton appCompatButtonCancel = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonCancel);
                    textInputEditTextName.setText(p.getName());
                    textInputEditTextPhone.setText(p.getPhone());
                    appCompatButtonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, ctx.getString(R.string.error_message_name))) {
                                return;
                            }
                            if (!inputValidation.isInputEditTextFilled(textInputEditTextPhone, textInputLayoutPhone,ctx.getString(R.string.error_message_phone))) {
                                return;
                            }
                            if (!inputValidation.isInputEditTextLengthName(textInputEditTextName, textInputLayoutName, ctx.getString(R.string.error_message_name_length))) {
                                return;
                            }
                            else{
                                if(pd!=null){
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Edit your contact");
                                    pd.show();
                                }
                                else{
                                    pd=new ProgressDialog(ctx);
                                    pd.setTitle("Please Wait");
                                    pd.setMessage("Edit your contact");
                                    pd.show();
                                }
                                final SessionManager sessionManager = SessionManager.with(ctx);
                                Call<listcontact> call = contactService.editcontact(sessionManager.getuserloggedin().getUserID(),p.getEmail(),textInputEditTextName.getText().toString(),textInputEditTextPhone.getText().toString());
                                call.enqueue(new Callback<listcontact>() {
                                    @Override
                                    public void onResponse(Call<listcontact> call, Response<listcontact> response) {
                                        if(response.isSuccessful()){
                                            String status=response.body().getStatus();
                                            String statusmessage=response.body().getMessage();
                                            if (status.equals("true")) {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                Call<List<listcontact>> callget = contactService.getcontact(sessionManager.getuserloggedin().getUserID());
                                                callget.enqueue(new Callback<List<listcontact>>() {
                                                    @Override
                                                    public void onResponse(Call<List<listcontact>> call, Response<List<listcontact>> response) {
                                                        if(response.isSuccessful()){
                                                            String status=response.body().get(0).getStatus();
                                                            String statusmessage=response.body().get(0).getMessage();
                                                            if (status.equals("true")) {
                                                                items = response.body();
                                                                itemsfilter = response.body();
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
                                                    public void onFailure(Call<List<listcontact>> call, Throwable t) {
                                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                        if(pd.isShowing()){
                                                            pd.dismiss();
                                                        }
                                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                                if(pd.isShowing()){
                                                    pd.dismiss();
                                                }
                                                dialog.dismiss();
                                            }
                                        }
                                        else{
                                            Toast.makeText(ctx, "Response failed", Toast.LENGTH_SHORT).show();
                                            if(pd.isShowing()){
                                                pd.dismiss();
                                            }
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<listcontact> call, Throwable t) {
                                        Log.e("USER ACTIVITY ERROR", t.getMessage());
                                        if(pd.isShowing()){
                                            pd.dismiss();
                                        }
                                        Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                    appCompatButtonCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });
            view.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogs=new AlertDialog.Builder(ctx).setTitle("Delete Contact").setMessage("Are you sure to delete this contact?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            final SessionManager sessionManager = SessionManager.with(ctx);
                            Call<listcontact> call = contactService.deletecontact(sessionManager.getuserloggedin().getUserID(),p.getEmail());
                            call.enqueue(new Callback<listcontact>() {
                                @Override
                                public void onResponse(Call<listcontact> call, Response<listcontact> response) {
                                    if(pd!=null){
                                        pd.setTitle("Please Wait");
                                        pd.setMessage("Delete your contact");
                                        pd.show();
                                    }
                                    else{
                                        pd=new ProgressDialog(ctx);
                                        pd.setTitle("Please Wait");
                                        pd.setMessage("Delete your contact");
                                        pd.show();
                                    }
                                    if(response.isSuccessful()){
                                        String status=response.body().getStatus();
                                        String statusmessage=response.body().getMessage();
                                        if (status.equals("true")) {
                                            Toast.makeText(ctx, statusmessage, Toast.LENGTH_SHORT).show();
                                            Call<List<listcontact>> callget = contactService.getcontact(sessionManager.getuserloggedin().getUserID());
                                            callget.enqueue(new Callback<List<listcontact>>() {
                                                @Override
                                                public void onResponse(Call<List<listcontact>> call, Response<List<listcontact>> response) {
                                                    if(response.isSuccessful()){
                                                        String status=response.body().get(0).getStatus();
                                                        String statusmessage=response.body().get(0).getMessage();
                                                        if (status.equals("true")) {
                                                            items = response.body();
                                                            itemsfilter = response.body();
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
                                                public void onFailure(Call<List<listcontact>> call, Throwable t) {
                                                    Log.e("USER ACTIVITY ERROR", t.getMessage());
                                                    if(pd.isShowing()){
                                                        pd.dismiss();
                                                    }
                                                    Toast.makeText(ctx, "Response failure", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dialog.dismiss();
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
                                public void onFailure(Call<listcontact> call, Throwable t) {
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
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsfilter.size();
    }

    public void setItems(List<listcontact> filteredGalaxies)
    {
        this.itemsfilter=filteredGalaxies;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint.length() > 0) {
                    //CHANGE TO UPPER
                    constraint = constraint.toString().toUpperCase();

                    //HOLD FILTERS WE FIND
                    List<listcontact> foundFilters = new ArrayList<>();

                    String galaxy;

                    //ITERATE CURRENT LIST
                    for (int i = 0; i < items.size(); i++) {

                        galaxy = items.get(i).getName();

                        //SEARCH
                        if (galaxy.toUpperCase().contains(constraint)) {
                            //ADD IF FOUND
                            foundFilters.add(items.get(i));
                        }
                        else if(items.get(i).getEmail().toUpperCase().contains(constraint)){
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
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                setItems((List<listcontact>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
}