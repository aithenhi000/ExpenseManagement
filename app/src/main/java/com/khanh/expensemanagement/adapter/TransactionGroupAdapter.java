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
import com.khanh.expensemanagement.model.CategoryIconMapper;
import com.khanh.expensemanagement.model.CategoryTotal;
import com.khanh.expensemanagement.model.Utils;

import java.util.List;

public class TransactionGroupAdapter extends RecyclerView.Adapter<TransactionGroupAdapter.ViewHolder> {
    private final List<CategoryTotal> transactions;
    private final Context context;

    public TransactionGroupAdapter(List<CategoryTotal> transactions, Context context) {
        this.transactions = transactions;
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
        CategoryTotal categoryTotal = transactions.get(position);
        holder.IconImageView.setImageDrawable(CategoryIconMapper.getIconByCategory(context, categoryTotal.getCategory_name())); // Lấy id biểu tượng
        holder.CategoryTextView.setText(categoryTotal.getCategory_name());
        holder.amountTextView.setText(Utils.formatCurrency(categoryTotal.getTotalAmount()));
        holder.percent.setText(categoryTotal.getPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView IconImageView;
        TextView amountTextView, CategoryTextView, percent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IconImageView = itemView.findViewById(R.id.icon);
            amountTextView = itemView.findViewById(R.id.amount);
            CategoryTextView = itemView.findViewById(R.id.category);
            percent = itemView.findViewById(R.id.percent);
        }
    }
}
