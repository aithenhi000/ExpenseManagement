package com.example.expensemanagement.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.model.Category;
import com.example.expensemanagement.R;
import com.example.expensemanagement.model.SharedViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    private int selectedPosition = 0; // Biến để theo dõi vị trí đã chọn
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedViewModel viewModel = new ViewModelProvider((FragmentActivity) context).get(SharedViewModel.class);
        Map<String, Integer> drawableMap = viewModel.getDrawableMap();
        Category category = categories.get(position);
        Integer drawableId = drawableMap.get(category.getIconName());
        holder.icon.setImageResource(drawableId);
        holder.text.setText(category.getName());

        // Cập nhật trạng thái background dựa trên vị trí đã chọn
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.border_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.border_normal);
        }

        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition(); // Lấy vị trí hiện tại của item
            if (currentPosition == RecyclerView.NO_POSITION) {
                return; // Kiểm tra nếu vị trí không hợp lệ, thoát sớm
            }
            // Cập nhật selectedPosition và thông báo cho Adapter để vẽ lại
            selectedPosition = currentPosition;
            notifyDataSetChanged(); // Vẽ lại toàn bộ danh sách để cập nhật trạng thái
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(categories.get(currentPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            text = itemView.findViewById(R.id.text);
        }
    }
}
