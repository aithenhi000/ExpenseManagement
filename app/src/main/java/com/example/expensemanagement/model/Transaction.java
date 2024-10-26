package com.example.expensemanagement.model;


public class Transaction {
    private int id;
    private final double amount;
    private final int category_id;
    private final String note;
    private final String date;

    public Transaction(int id, double amount, int categoryId, String date, String note) {
        super();
        this.id = id;
        this.amount = amount;
        this.category_id = categoryId;
        this.note = note;
        this.date = date;

    }

    public Transaction(double amount, int categoryId, String date, String note) {
        super();
        this.amount = amount;
        this.category_id = categoryId;
        this.note = note;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public int getCategory_id() {
        return category_id;
    }
}
