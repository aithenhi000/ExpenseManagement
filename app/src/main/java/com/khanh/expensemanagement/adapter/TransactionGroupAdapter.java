package com.khanh.expensemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.enums.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ViewHolder> {
    private final Context context;
    private List<Map.Entry<String, Float>> categoryList;

    public TransactionGroupAdapter(Map<String, Float> categoryPercentages, Context context) {
        categoryList = new ArrayList<>(categoryPercentages.entrySet());
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionGroupAdapter.ViewHolder holder, int position) {
        Map.Entry<String, Float> categoryEntry = categoryList.get(position);
        String category = categoryEntry.getKey();
        Float percentage = categoryEntry.getValue();
        holder.IconImageView.setImageResource(Category.fromString(category)); // Lấy id biểu tượng
        holder.CategoryTextView.setText(category);
        holder.percent.setText(String.format("%.2f%%", percentage));

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView IconImageView;
        TextView amountTextView, CategoryTextView, percent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IconImageView = itemView.findViewById(R.id.icon);
            CategoryTextView = itemView.findViewById(R.id.category);
            percent = itemView.findViewById(R.id.percent);
        }
    }
}
