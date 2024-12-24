package com.khanh.expensemanagement.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.SharedPreferencesManager;
import com.khanh.expensemanagement.model.User;

public class LoginActivity extends BaseActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        displayTitle("Đăng nhập");
        // Khởi tạo SharedPreferences
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        // Liên kết các view
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Xử lý sự kiện khi nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> login());

        // Xử lý sự kiện khi nhấn nút đăng ký
        registerButton.setPaintFlags(registerButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        registerButton.setOnClickListener(v -> register());
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);  // Giả sử UserDatabaseHelper là lớp giúp truy vấn DB
        User user = dbHelper.getUserByUsername(username);

        if (user != null) {
            // Kiểm tra mật khẩu
            if (user.getPassword().equals(password)) {
                // Đăng nhập thành công, lưu thông tin đăng nhập vào SharedPreferences
                sharedPreferencesManager.setLoggedInUser(username);

                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                // Chuyển về MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Người dùng chưa được tạo, vui lòng đăng ký!", Toast.LENGTH_SHORT).show();
        }
    }


    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
