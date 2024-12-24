package com.khanh.expensemanagement.model;

public class CategoryTotal {
    private Long totalAmount;
    private String category_name;
    private String category_type;
    private float percentage;

    public CategoryTotal(Long totalAmount, String category_name, String category_type, float percentage) {
        this.totalAmount = totalAmount;
        this.category_name = category_name;
        this.category_type = category_type;
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage;
    }


    public String getCategory_type() {
        return category_type;
    }

    public String getCategory_name() {
        return category_name;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }
}
