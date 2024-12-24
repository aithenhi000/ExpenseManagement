package com.khanh.expensemanagement.model;

public class TransactionSummary {
    private final double amount;
    private final String categoryName;
    private final String date;
    private final int icon_id;


    public TransactionSummary(double amount, String categoryName, String date, int icon_id) {
        this.amount = amount;
        this.categoryName = categoryName;
        this.date = date;
        this.icon_id = icon_id;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDate() {
        return date;
    }

    public int getIcon_id() {
        return icon_id;
    }
}
