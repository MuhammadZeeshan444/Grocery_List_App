package com.example.grocerylistapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText etName, etQuantity, etPrice;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                Toast.makeText(MainActivity.this, "Logout successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }



    private void init() {
        etName = findViewById(R.id.etFullName);
        etQuantity = findViewById(R.id.etQuantity);
        etPrice = findViewById(R.id.etPrice);
        btnLogout = findViewById(R.id.btnLogout);
    }

    public void insertProduct(View v) {

        String name = etName.getText().toString().trim();
        String quantity = etQuantity.getText().toString().trim();
        String price = etPrice.getText().toString().trim();
        if (name.isEmpty()){
            etName.setError("Product Name can'be empty!");
        }
        else if (quantity.isEmpty()){
            etQuantity.setError("Product quantity can'be empty");
        }
        else if (price.isEmpty()){
            etPrice.setError("Product price can't be empty!");
        }
        else {
            HashMap<String, String> data = new HashMap<>();
            data.put("name", name);
            data.put("quantity", quantity);
            data.put("price", price);

            FirebaseDatabase.getInstance().getReference()
                    .child("Products")
                    .push()
                    .setValue(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Data added successfully.", Toast.LENGTH_SHORT).show();
                            etName.setText("");
                            etQuantity.setText("");
                            etPrice.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void viewProductList(View v) {
        startActivity(new Intent(MainActivity.this, ViewProduct.class));
    }


}

