package com.example.expensemanagement.model;

public class Category {
    private int id;
    private String name;
    private String type;
    private String iconName;





    public Category(int id, String name, String type, String iconName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.iconName=iconName;

    }

    public Category(String name, String iconName) {
        this.name = name;
        this.iconName = iconName;
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


    public void setName(String name) {
        this.name = name;
    }
    public String getIconName() {
        return iconName;
    }
}