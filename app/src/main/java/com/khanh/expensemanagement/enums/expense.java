package com.khanh.expensemanagement.enums;


import com.khanh.expensemanagement.R;

public enum expense {
    FOOD("Ăn uống", R.drawable.ic_food),
    TRANSPORT("Di chuyển", R.drawable.ic_dichuyen),
    ENTERTAINMENT("Giải trí", R.drawable.ic_giaitri),
    SHOPPING("Mua sắm", R.drawable.ic_shopping);


    private final String displayName;
    private final int imageResId;

    // Constructor để gán tên hiển thị và ảnh cho mỗi enum constant
    expense(String displayName, int imageResId) {
        this.displayName = displayName;
        this.imageResId = imageResId;
    }

//    // Optional: Thêm phương thức giúp tìm category từ chuỗi
//    public static expense fromString(String text) {
//        for (e category : Category.values()) {
//            if (category.displayName.equalsIgnoreCase(text)) {
//                return category;
//            }
//        }
//        return null; // hoặc ném ra exception nếu không tìm thấy
//    }

    // Getter để lấy tên hiển thị của category
    public String getDisplayName() {
        return displayName;
    }

    // Getter để lấy ảnh đại diện của category
    public int getImageResId() {
        return imageResId;
    }
}

