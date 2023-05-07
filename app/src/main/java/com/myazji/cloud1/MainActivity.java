package com.myazji.cloud1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    EditText name;
    EditText phone;
    EditText address;
    Button add;
    Button viewList;

    ProgressDialog dialog;

    @SuppressLint({"SourceLockedOrientationActivity", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        db = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Please Wait ...");
        dialog.setCancelable(false);


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        add = findViewById(R.id.add);
        viewList = findViewById(R.id.viewList);
        viewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ListActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validAll()) {

                } else {
                    dialog.show();
                    addData();
                }
            }
        });
    }

    public void addData() {
        String getName = name.getText().toString().trim();
        String getPhone = phone.getText().toString().trim();
        String getAddress = address.getText().toString().trim();
        String getId = String.valueOf(System.currentTimeMillis());
        Map<String, Object> user = new HashMap<>();
        user.put("id", getId);
        user.put("name", getName);
        user.put("phone", getPhone);
        user.put("address", getAddress);

        db.collection("Users")
                .document(getId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        name.getText().clear();
                        phone.getText().clear();
                        address.getText().clear();
                        dialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

    boolean validAll() {
        return isEmpty(name, "required") &
                isEmpty(phone, "required") &
                isEmpty(address, "required");
    }

    boolean isEmpty(TextView editText, String msg) {
        boolean isDone = true;
        if (editText != null) {
            if (editText.getText().toString().isEmpty()) {
                editText.setError(msg);
                isDone = false;
            }
        }
        return isDone;
    }
}