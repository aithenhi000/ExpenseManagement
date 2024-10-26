package com.example.expensemanagement.model;

public class Category {
    private final int id;
    private final String name;
    private final String type;
    private final int icon_ID;

    public Category(int id, String name, String type, int icon_ID) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.icon_ID = icon_ID;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getIcon_ID() {
        return icon_ID;
    }
}