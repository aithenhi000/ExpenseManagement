package com.khanh.expensemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.SharedPreferencesManager;
import com.khanh.expensemanagement.model.User;
import com.khanh.expensemanagement.model.Utils;

public class RegisterActivity extends BaseActivity {
    private EditText etUsername, etPassword, etConfirmPassword, etEmail;
    private Button btnRegister;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        displayTitle("Đăng ký");
        // Sử dụng SharedPreferencesManager để quản lý SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();

            String confirmPassword = etConfirmPassword.getText().toString().trim();
            // Kiểm tra tính hợp lệ của thông tin nhập vào
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirmPassword)) {
                Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            } else if (sharedPreferencesManager.contains(username)) {
                // Kiểm tra xem tên người dùng đã tồn tại
                Toast.makeText(RegisterActivity.this, "Tên người dùng đã tồn tại, vui lòng chọn tên khác", Toast.LENGTH_SHORT).show();
            } else if (!Utils.isValidEmail(email)) {
                Toast.makeText(RegisterActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                User user = new User(username, password, email);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
                databaseHelper.registerUser(user);
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });
    }

    // Chuyển hướng về màn hình đăng nhập sau khi đăng ký thành công
    private void navigateToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

