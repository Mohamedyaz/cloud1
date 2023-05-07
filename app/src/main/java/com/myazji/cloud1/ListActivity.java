package com.myazji.cloud1;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class ListActivity extends AppCompatActivity {

    ArrayList<Model> models;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    AdapterRecycleVIew adapterRecycleVIew;
    ProgressBar bar;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_list);
        models = new ArrayList<>();
        Collections.reverse(models);
        recyclerView = findViewById(R.id.recycler);
        bar = findViewById(R.id.progressBar);
        db = FirebaseFirestore.getInstance();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){

                        Model model = new Model();
                        model.setId(document.getString("id"));
                        model.setName(document.getString("name"));
                        model.setPhone(document.getString("phone"));
                        model.setAddress(document.getString("address"));
                        models.add(model);
                        Collections.reverse(models);
                        adapterRecycleVIew = new AdapterRecycleVIew(ListActivity.this,models);
                        LinearLayoutManager manager = new LinearLayoutManager(ListActivity.this);
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapterRecycleVIew);
                        adapterRecycleVIew.notifyDataSetChanged();
                        bar.setVisibility(View.GONE);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }
}