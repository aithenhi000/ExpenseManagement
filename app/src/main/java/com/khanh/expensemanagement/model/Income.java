package com.khanh.expensemanagement.model;

public class Income {
    private String id;
    private String note;
    private Long amount;
    private String category;
    private String date;

    public Income() {
    }

    public Income(Long amount, String category, String date, String id, String note) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.id = id;
        this.note = note;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
