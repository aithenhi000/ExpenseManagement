package com.khanh.expensemanagement.model;

public class User {
    private String name;
    private String email;

    public User() {
        // Constructor rỗng để Firebase có thể sử dụng
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, Object o) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
