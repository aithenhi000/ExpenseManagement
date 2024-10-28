package com.example.expensemanagement.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.R;
import com.example.expensemanagement.model.SharedViewModel;
import com.example.expensemanagement.model.TransactionSummary;

import java.util.List;
import java.util.Map;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private final List<Object[]> transactions;
    private final Context context;

    public TransactionAdapter(Context context, List<Object[]> transactions) {
        this.transactions=transactions;
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
        SharedViewModel viewModel = new ViewModelProvider((FragmentActivity) context).get(SharedViewModel.class);
        Map<String, Integer> viewModelDrawableMap = viewModel.getDrawableMap();

        Object[] transaction = (Object[]) transactions.get(position); // Lấy giao dịch từ danh sách
        Integer drawableId = viewModelDrawableMap.get(transaction[3]);
        // Lấy ngày từ mảng
        String currentDate = (String) transaction[2]; // Chỉnh sửa chỉ số thành 2

        // Kiểm tra xem có cần hiển thị thời gian không
        if (position > 0 && currentDate.equals(((Object[]) transactions.get(position - 1))[2])) {
            holder.Time.setVisibility(View.GONE);
        } else {
            holder.Time.setVisibility(View.VISIBLE);
            holder.Time.setText(currentDate); // Hiển thị ngày
        }

        // Cập nhật hình ảnh biểu tượng và thông tin danh mục
        holder.IconImageView.setImageResource(drawableId); // Lấy id biểu tượng
        holder.CategoryTextView.setText((String) transaction[1]); // Lấy tên danh mục
        holder.amountTextView.setText(String.format("%s$", String.valueOf(transaction[0]))); // Lấy số tiền
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
