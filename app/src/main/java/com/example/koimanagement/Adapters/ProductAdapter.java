package com.example.koimanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.koimanagement.Activities.ProductDetailActivity;
import com.example.koimanagement.Models.Product;
import com.example.koimanagement.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private final List<Product> productList;

    public ProductAdapter(Context ctx, List<Product> productList) {
        this.context = ctx;
        this.productList = productList;
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
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN")); // Định dạng theo ngôn ngữ và vùng miền Việt Nam
        holder.txtPrice.setText(numberFormat.format(product.getPrice()) + " VND");
        holder.txtStockQuantity.setText(String.valueOf(product.getStockQuantity()));



        String imageUrl = product.getImage();
        Log.d("ProductAdapter", "Loading image from URL: " + imageUrl);

        // Load image with Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.replace)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("ProductAdapter", "Image load failed", e);
                        return false; // true nếu bạn đã xử lý lỗi
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false; // không xử lý gì
                    }
                })
                .into(holder.productImg);
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
        private final TextView txtName, txtPrice, txtStockQuantity;
        private final ImageView productImg;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textName);
            txtPrice = itemView.findViewById(R.id.textPrice);
            txtStockQuantity = itemView.findViewById(R.id.textStockQuantity);
            productImg = itemView.findViewById(R.id.imageIcon);
        }
    }
}
