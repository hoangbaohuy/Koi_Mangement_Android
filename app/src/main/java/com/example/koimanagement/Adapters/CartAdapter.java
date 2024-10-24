package com.example.koimanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.koimanagement.Fragments.CartFragment;
import com.example.koimanagement.IService.ICartService;
import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private ICartService apiService; // API service to call API methods
    private CartFragment cartFragment; // Tham chiếu đến CartFragment

    public CartAdapter(Context context, List<CartItem> cartItems, ICartService apiService, CartFragment cartFragment) {
        this.context = context;
        this.cartItems = cartItems;
        this.apiService = apiService;
        this.cartFragment = cartFragment;// Initialize API service
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
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Định dạng theo ngôn ngữ và vùng miền Việt Nam
        holder.txtPrice.setText(numberFormat.format(item.getPrice()) + " VND");
        holder.txtOrder.setText(String.valueOf(item.getQuantity()));

        // Load product image using Glide
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.replace)
                .error(R.drawable.replace)
                .into(holder.imageProduct);

        // Handle decrement button click
        holder.imageMinus.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                item.setQuantity(currentQuantity - 1);  // Update quantity
                holder.txtOrder.setText(String.valueOf(item.getQuantity()));
                notifyItemChanged(position); // Cập nhật giao diện
                cartFragment.calculateTotalPrice();
                updateQuantityInApi(item.getCartId(), item.getQuantity(), position);
            }
        });

        // Handle increment button click
        holder.imageAdd.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1); // Update quantity
            holder.txtOrder.setText(String.valueOf(item.getQuantity()));
            notifyItemChanged(position); // Cập nhật giao diện
            cartFragment.calculateTotalPrice();
            updateQuantityInApi(item.getCartId(), item.getQuantity(), position);
        });
       /* holder.imageAdd.setOnClickListener(v -> {
            increaseQuantity(position);
        });

        // Sự kiện cho nút giảm số lượng
        holder.imageMinus.setOnClickListener(v -> {
            decreaseQuantity(position);
        });*/
        // Handle delete button click
        holder.imageTrash.setOnClickListener(v -> {
            removeCartItem(item.getCartId(), position); // Call API to delete item
        });
    }
    /*public void increaseQuantity(int position) {
        CartItem item = cartItems.get(position);
        item.setQuantity(item.getQuantity() + 1); // Tăng số lượng
        notifyItemChanged(position); // Cập nhật giao diện
        cartFragment.calculateTotalPrice();
        holder.txtOrder.setText(String.valueOf(item.getQuantity()));
        updateQuantityInApi(item.getCartId(), item.getQuantity(), position);// Tính lại tổng giá
    }

    // Phương thức giảm số lượng
    public void decreaseQuantity(int position) {
        CartItem item = cartItems.get(position);
        if (item.getQuantity() > 0) {
            item.setQuantity(item.getQuantity() - 1); // Giảm số lượng
            notifyItemChanged(position); // Cập nhật giao diện
            cartFragment.calculateTotalPrice();
            holder.txtOrder.setText(String.valueOf(item.getQuantity()));
            updateQuantityInApi(item.getCartId(), item.getQuantity(), position);// Tính lại tổng giá
        }
    }*/
    private void updateQuantityInApi(int cartId, int quantity, int position) {
        Call<Boolean> call = apiService.updateCartItemQuantity(cartId, quantity);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    // Successfully updated quantity
                } else {
                    Toast.makeText(context, "Failed to update quantity", Toast.LENGTH_SHORT).show();
                    // Revert quantity change if API call fails
                    cartItems.get(position).setQuantity(quantity > 1 ? quantity - 1 : 1);
                    notifyItemChanged(position);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Revert quantity change on error
                cartItems.get(position).setQuantity(quantity > 1 ? quantity - 1 : 1);
                notifyItemChanged(position);
            }
        });
    }

    private void removeCartItem(int cartId, int position) {
        Call<Boolean> call = apiService.removeCartItem(cartId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null && response.body()) {
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cartItems.size());
                } else {
                    Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
