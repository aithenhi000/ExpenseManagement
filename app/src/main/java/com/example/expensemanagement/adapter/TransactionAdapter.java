package com.example.expensemanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.R;
import com.example.expensemanagement.model.TransactionSummary;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private final List<TransactionSummary> TS_list;
    private final Context context;

    public TransactionAdapter(Context context, List<TransactionSummary> TS_list) {
        this.TS_list = TS_list;
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
        TransactionSummary TS = TS_list.get(position);

        if (position > 0 && TS_list.get(position - 1).getDate().equals(TS.getDate())) {
            holder.Time.setVisibility(View.GONE);
        } else {
            // Nếu không giống, hiển thị tên danh mục
            holder.Time.setVisibility(View.VISIBLE);
            holder.Time.setText(TS.getDate());
        }
        Log.d("TEST", "onBindViewHolder: Id_icon" + TS.getIcon_id());
        holder.IconImageView.setImageResource(TS.getIcon_id());
        holder.CategoryTextView.setText(TS.getCategoryName());
        holder.amountTextView.setText(String.format("%s$", String.valueOf(TS.getAmount())));

    }

    @Override
    public int getItemCount() {
        return TS_list.size();
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
