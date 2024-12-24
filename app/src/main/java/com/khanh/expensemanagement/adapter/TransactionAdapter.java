package com.khanh.expensemanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.enums.Category;
import com.khanh.expensemanagement.model.Utils;

import java.util.List;
import java.util.Objects;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private final List<com.khanh.expensemanagement.model.Transaction> transactions;
    private final Context context;


    public TransactionAdapter(Context context, List<com.khanh.expensemanagement.model.Transaction> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.khanh.expensemanagement.model.Transaction transaction = transactions.get(position);
        Log.d("TAG", "onBindViewHolder: " + transaction.getCategory());
        String currentDate = transaction.getDate();
        if (position > 0 && currentDate.equals(transactions.get(position - 1).getDate())) {
            holder.Time.setVisibility(View.GONE);
        } else {
            holder.Time.setVisibility(View.VISIBLE);
            holder.Time.setText(currentDate);
        }

        holder.IconImageView.setImageResource(Category.fromString(transaction.getCategory()));// Lấy id biểu tượng
        holder.CategoryTextView.setText(transaction.getCategory()); // Lấy tên danh mục
        holder.amountTextView.setText(Utils.formatCurrency(transaction.getAmount()));
        int color;
        if (Objects.equals(transaction.getType(), "Expense")) {
            color = ContextCompat.getColor(context, R.color.error_red);
        } else {
            color = ContextCompat.getColor(context, R.color.success_green);
        }
        holder.amountTextView.setTextColor(color);
        //holder.itemView.setOnClickListener(v -> listener.onTransactionClick(transaction));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView IconImageView;
        TextView amountTextView;
        TextView CategoryTextView;
        TextView Time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IconImageView = itemView.findViewById(R.id.icon);
            amountTextView = itemView.findViewById(R.id.amount);
            CategoryTextView = itemView.findViewById(R.id.category);
            Time = itemView.findViewById(R.id.time);
        }

    }


}
