package com.myazji.cloud1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterRecycleVIew extends RecyclerView.Adapter<AdapterRecycleVIew.Viewholder> {

    private Context context;
    private ArrayList<Model> list;
    FirebaseFirestore db;
    ProgressDialog dialog;

    public AdapterRecycleVIew(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        Model model = list.get(position);
        holder.name.setText(model.getName());
        holder.address.setText(model.getAddress());
        holder.phone.setText(model.getPhone());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String id = model.getId();
                Log.d("LogedId", "onClick: "+id);
                db.collection("Users")
                        .document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Delete SuccessFully!!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone;
        TextView address;
        TextView delete;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameItem);
            phone = itemView.findViewById(R.id.phoneItem);
            address = itemView.findViewById(R.id.addressItem);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
