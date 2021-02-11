package com.example.grocerylistapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product,ProductAdapter.ProductViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position, @NonNull Product model) {
        holder.tvName.setText(model.getName());
        holder.tvQuantity.setText(model.getQuantity());
        holder.tvPrice.setText(model.getPrice());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPlus dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.updatedialog))
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setExpanded(true)
                        .create();
                dialog.show();

                View view  = dialog.getHolderView();
                Button btnUpdate;
                final EditText etName, etQuantity, etPrice;
                etName = v.findViewById(R.id.etFullName);
                etQuantity = v.findViewById(R.id.etQuantity);
                etPrice = v.findViewById(R.id.etPrice);
                btnUpdate = v.findViewById(R.id.btnUpdate);



                etName.setText(model.getName());
                etQuantity.setText(model.getQuantity());
                etPrice.setText(model.getPrice());

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("name", etName.getText().toString().trim());
                        updatedData.put("quantity",etQuantity .getText().toString().trim());
                        updatedData.put("price", etPrice.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference()
                                .child("Products")
                                .child(getRef(position).getKey())
                                .updateChildren(updatedData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Data has been updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.dismiss();

                    }
                });
            }
        });
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("Products")
                        .child(getRef(position).getKey())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(context, "value removed successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.productitem, parent, false);
        return new ProductViewHolder(view);
    }

    public  class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvQuantity, tvPrice;
        ImageView ivEdit, ivDelete;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
           tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivDelete = itemView.findViewById(R.id.ivDelete);
            ivEdit = itemView.findViewById(R.id.ivEdit);

        }
    }
}
