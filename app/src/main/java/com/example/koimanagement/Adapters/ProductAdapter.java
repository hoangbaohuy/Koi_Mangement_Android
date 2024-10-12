package com.example.koimanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.koimanagement.Activities.ProductDetailActivity;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    List<Product> productList;
    int listImages[];

    public ProductAdapter(Context ctx, List<Product> productList, int[] images) {
        this.context = ctx;
        this.productList = productList;
        this.listImages = images;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getProductName());
        holder.txtDescription.setText(product.getProductDescription());
        holder.txtPrice.setText(String.format("$%.2f", product.getPrice()));
        holder.txtStockQuantity.setText(String.valueOf(product.getStockQuantity()));
        holder.productImg.setImageResource(listImages[position]);

        // Set item click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product); // Pass product object to details activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtDescription, txtStockQuantity;
        ImageView productImg;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtPrice = itemView.findViewById(R.id.textPrice);
            txtDescription = itemView.findViewById(R.id.textDescription);
            txtStockQuantity = itemView.findViewById(R.id.textStockQuantity);
            productImg = itemView.findViewById(R.id.imageIcon);
        }
    }
}
