package com.khanh.expensemanagement.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.SharedPreferencesManager;
import com.khanh.expensemanagement.model.User;

public class UserInfoActivity extends BaseActivity {

    private DatabaseHelper db;
    private SharedPreferencesManager sharedPreferencesManager;
    private TextView usernameTextView, passwordTextView, emailTextView;
    private Button editPasswordButton, logoutButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Hiển thi title và Back button
        displayTitle("Thông tin người dùng");
        // Khởi tạo SharedPreferences để lấy thông tin người dùng
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);

        // Liên kết các view
        usernameTextView = findViewById(R.id.usernameTextView);
        passwordTextView = findViewById(R.id.passwordTextView);
        editPasswordButton = findViewById(R.id.editPasswordButton);
        logoutButton = findViewById(R.id.logoutButton);
        emailTextView = findViewById(R.id.emailTextView);
        db = DatabaseHelper.getInstance(this);
        // Hiển thị tên đăng nhập và mật khẩu từ SharedPreferences
        String username = sharedPreferencesManager.getUserName();
        String password = sharedPreferencesManager.getUserPassword(username);
        user = db.getUserByUsername(username);
        usernameTextView.setText("Tên đăng nhập: " + username);
        emailTextView.setText("Email: " + user.getEmail());
        passwordTextView.setText("Mật khẩu: " + user.getPassword());
        // Xử lý sự kiện chỉnh sửa mật khẩu
        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditPasswordDialog();
            }
        });

        // Xử lý sự kiện đăng xuất
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void showEditPasswordDialog() {
        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setHint("Nhập mật khẩu mới");

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa mật khẩu")
                .setView(newPasswordInput)
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPassword = newPasswordInput.getText().toString();
                        if (!newPassword.isEmpty()) {
                            db.updatePassword(user.getUserId(), newPassword);
                            passwordTextView.setText("Mật khẩu: " + newPassword);
                            Toast.makeText(UserInfoActivity.this, "Mật khẩu đã được cập nhật!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(UserInfoActivity.this, "Mật khẩu không được để trống!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Xử lý đăng xuất
    private void logout() {
        // Xóa thông tin đăng nhập khỏi SharedPreferences
        sharedPreferencesManager.logout();

        // Chuyển về MainActivity sau khi đăng xuất
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }

}
