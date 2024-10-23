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
import com.example.koimanagement.IService.ICartService;
import com.example.koimanagement.Models.CartItem;
import com.example.koimanagement.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;
    private ICartService apiService; // API service để gọi đến các phương thức API

    public CartAdapter(Context context, List<CartItem> cartItems, ICartService apiService) {
        this.context = context;
        this.cartItems = cartItems;
        this.apiService = apiService; // Khởi tạo API service
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

        // Thiết lập tên sản phẩm và giá
        holder.txtProductName.setText(item.getProductName());
        holder.txtPrice.setText(item.getPrice() + " VND");
        holder.txtOrder.setText(String.valueOf(item.getQuantity()));

        // Tải hình ảnh sản phẩm bằng Glide
        Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.replace)
                .error(R.drawable.replace)
                .into(holder.imageProduct);

        // Xử lý sự kiện nhấn nút trừ
        holder.imageMinus.setOnClickListener(v -> {
            int currentQuantity = item.getQuantity();
            if (currentQuantity > 1) {
                item.setQuantity(currentQuantity - 1);  // Sử dụng setQuantity để cập nhật
                holder.txtOrder.setText(String.valueOf(item.getQuantity()));
                notifyItemChanged(position);
            }
        });

        // Xử lý sự kiện nhấn nút cộng
        holder.imageAdd.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1); // Sử dụng setQuantity
            holder.txtOrder.setText(String.valueOf(item.getQuantity()));
            notifyItemChanged(position);
        });

        // Xử lý sự kiện nhấn nút xóa
        holder.imageTrash.setOnClickListener(v -> {
            removeCartItem(item.getCartId(), position); // Gọi API xóa mặt hàng
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
