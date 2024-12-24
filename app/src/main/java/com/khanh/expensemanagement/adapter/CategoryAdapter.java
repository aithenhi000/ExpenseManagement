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
import com.khanh.expensemanagement.model.Category;
import com.khanh.expensemanagement.model.CategoryIconMapper;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categories;
    private Context context;
    private int selectedPosition = 0; // Biến để theo dõi vị trí đã chọn
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Category category = categories.get(position);
        String category_name = category.getName();
        holder.icon.setImageDrawable(CategoryIconMapper.getIconByCategory(context, category_name));
        holder.text.setText(category_name);

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_bg);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.unselected_bg);
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

    public interface OnItemClickListener {
        void onItemClick(Category category);
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
