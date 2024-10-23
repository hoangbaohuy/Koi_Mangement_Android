package com.example.koimanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koimanagement.Models.Response.OrderResponse;
import com.example.koimanagement.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderResponse> orders;
    private Context context;

    public OrderAdapter(List<OrderResponse> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderResponse order = orders.get(position);

        // Set Order ID, Order Date, Total Price, Address, and Phone
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvOrderDate.setText("Order Date: " + order.getOrderDate());
        holder.tvTotalPrice.setText("Total Price: $" + order.getTotalPrice());
        holder.tvAddress.setText("Address: " + order.getAddress());
        holder.tvPhone.setText("Phone: " + order.getPhone());

        // Clear the products list first (in case of recycling views)
        holder.layoutProducts.removeAllViews();

        // Dynamically add product items (name and image) to the LinearLayout
        for (OrderResponse.OrderItemResponse item : order.getOrderItems()) {
            // Inflate each product item layout
            View productItemView = LayoutInflater.from(context)
                    .inflate(R.layout.product_item2, holder.layoutProducts, false);  // Sử dụng layout product_item.xml

            TextView tvProductName = productItemView.findViewById(R.id.tvProductName);
            ImageView ivProductImage = productItemView.findViewById(R.id.ivProductImage);

            // Set the product name
            tvProductName.setText(item.getProductName());

            // Load the product image using Glide
            Glide.with(context)
                    .load(item.getImage())
                    .into(ivProductImage);

            // Add this product view to the products layout
            holder.layoutProducts.addView(productItemView);
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvOrderDate, tvAddress, tvPhone;
        LinearLayout layoutProducts; // To display dynamic list of products

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            layoutProducts = itemView.findViewById(R.id.layoutProducts); // Initialize the LinearLayout for products
        }
    }
}
