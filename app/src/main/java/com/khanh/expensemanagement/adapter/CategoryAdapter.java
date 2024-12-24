package com.khanh.expensemanagement.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        }

        Category category = categories.get(position);

        ImageView ivCategoryImage = convertView.findViewById(R.id.ivCategoryImage);
        TextView tvCategoryName = convertView.findViewById(R.id.tvCategoryName);

        ivCategoryImage.setImageResource(category.getImageResourceId());
        tvCategoryName.setText(category.getName());

        return convertView;
    }
}

