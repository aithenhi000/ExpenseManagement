package com.khanh.expensemanagement.enums;


import com.khanh.expensemanagement.R;

import java.util.ArrayList;
import java.util.List;

public enum Category {
    FOOD("Ăn uống", R.drawable.ic_food, "expense"),
    TRANSPORT("Di chuyển", R.drawable.ic_dichuyen, "expense"),
    ENTERTAINMENT("Giải trí", R.drawable.ic_giaitri, "expense"),
    SHOPPING("Mua sắm", R.drawable.ic_shopping, "expense"),
    FRIEND("Bạn bè", R.drawable.ic_banbe, "income"),
    BUSINESS("Kinh doanh", R.drawable.ic_kinhdoanh, "income"),
    RELATIVES("Người thân", R.drawable.ic_nguoithan, "income"),
    BONUS("Thưởng", R.drawable.ic_thuong, "income"),
    INTEREST("Tiền lãi", R.drawable.ic_tienlai, "income"),
    SUBSIDY("Trợ cấp", R.drawable.ic_trocap, "income");

    private final String displayName;
    private final int imageResId;
    private final String type;

    // Constructor để gán tên hiển thị và ảnh cho mỗi enum constant
    Category(String displayName, int imageResId, String type) {
        this.displayName = displayName;
        this.imageResId = imageResId;
        this.type = type;
    }

    public static List<Category> getExpenseCategories(String type) {
        List<Category> expenseCategories = new ArrayList<>();
        for (Category expense : Category.values()) {
            if (expense.type.equals(type)) {
                expenseCategories.add(expense);
            }
        }
        return expenseCategories;
    }

    // Optional: Thêm phương thức giúp tìm category từ chuỗi
    public static int fromString(String text) {
        for (Category expense : Category.values()) {
            if (expense.displayName.equalsIgnoreCase(text)) {
                return expense.imageResId;
            }
        }
        return 0;
    }

    public String getType() {
        return type;
    }

    // Getter để lấy tên hiển thị của category
    public String getDisplayName() {
        return displayName;
    }

    // Getter để lấy ảnh đại diện của category
    public int getImageResId() {
        return imageResId;
    }
}

