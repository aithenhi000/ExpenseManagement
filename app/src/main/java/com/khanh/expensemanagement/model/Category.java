package com.khanh.expensemanagement.model;

public class Category {
    private String name;
    private int imageResourceId;

    // Constructor
    public Category(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
