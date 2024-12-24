package com.khanh.expensemanagement.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.khanh.expensemanagement.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryIconMapper {
    private static final Map<String, Integer> categoryIconMap = new HashMap<>();

    static {
        // Ánh xạ tên danh mục với `drawable id` của icon
        categoryIconMap.put("Ăn uống", R.drawable.ic_anuong);
        categoryIconMap.put("Mua sắm", R.drawable.ic_muasam);
        categoryIconMap.put("Phương tiện", R.drawable.ic_dichuyen);
        categoryIconMap.put("Lương", R.drawable.ic_luong);
        categoryIconMap.put("Kinh doanh", R.drawable.ic_kinhdoanh);
        categoryIconMap.put("Tiền lãi", R.drawable.ic_tienlai);
        categoryIconMap.put("Hóa đơn", R.drawable.ic_hoadon);
        categoryIconMap.put("Giải trí", R.drawable.ic_giaitri);
        categoryIconMap.put("Bạn bè", R.drawable.ic_banbe);
        categoryIconMap.put("Thưởng", R.drawable.ic_thuong);
        categoryIconMap.put("Trợ cấp", R.drawable.ic_trocap);
    }

    // Phương thức để lấy icon từ tên danh mục
    public static Drawable getIconByCategory(Context context, String categoryName) {
        Integer iconId = categoryIconMap.get(categoryName);
        if (iconId != null) {
            return ContextCompat.getDrawable(context, iconId);
        }
        // Nếu không tìm thấy icon, trả về icon mặc định hoặc null
        return ContextCompat.getDrawable(context, R.drawable.ic_launcher_foreground);
    }

    public static List<String> getCategoryNames() {
        return new ArrayList<>(categoryIconMap.keySet());
    }
}
