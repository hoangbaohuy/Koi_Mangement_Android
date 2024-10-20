package com.example.koimanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Add Glide dependency for image loading
import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems; // List of CartItem
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Set product name and price
        holder.txtProductName.setText(item.getProductName());
        holder.txtPrice.setText(item.getPrice() + " VND");
        holder.txtOrder.setText(String.valueOf(item.getStockQuantity())); // Display available stock quantity

        // Load product image using Glide
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.replace) // Placeholder image
                .error(R.drawable.replace) // Error image
                .into(holder.imageProduct);

        // Handle minus button click
        holder.imageMinus.setOnClickListener(v -> {
            int currentQuantity = item.getStockQuantity();
            if (currentQuantity > 1) { // Prevent quantity from going below 1
                item.setStockQuantity(currentQuantity - 1);
                holder.txtOrder.setText(String.valueOf(item.getStockQuantity())); // Update displayed quantity
                notifyItemChanged(position); // Notify adapter of the change
            }
        });

        // Handle add button click
        holder.imageAdd.setOnClickListener(v -> {
            item.setStockQuantity(item.getStockQuantity() + 1); // Increase quantity
            holder.txtOrder.setText(String.valueOf(item.getStockQuantity())); // Update displayed quantity
            notifyItemChanged(position); // Notify adapter of the change
        });

        // Handle trash button click
        holder.imageTrash.setOnClickListener(v -> {
            cartItems.remove(position); // Remove item from list
            notifyItemRemoved(position); // Notify adapter of the removed item
            notifyItemRangeChanged(position, cartItems.size()); // Update the item range
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct;
        TextView txtProductName;
        TextView txtPrice;
        TextView txtOrder;
        ImageView imageMinus;
        ImageView imageAdd;
        ImageView imageTrash;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtOrder = itemView.findViewById(R.id.txtOrder);
            imageMinus = itemView.findViewById(R.id.imageMinus);
            imageAdd = itemView.findViewById(R.id.imageAdd);
            imageTrash = itemView.findViewById(R.id.imageTrash);
        }
    }
}
