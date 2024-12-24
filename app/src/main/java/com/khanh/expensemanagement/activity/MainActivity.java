package com.khanh.expensemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.BottomNavAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    private ImageView login;
    private TextView statusLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        statusLogin = findViewById(R.id.statusLogin);
//        login = findViewById(R.id.login);
        viewPager2 = findViewById(R.id.viewpager2);

        BottomNavAdapter bottomNavAdapter = new BottomNavAdapter(this);
        viewPager2.setAdapter(bottomNavAdapter);
        viewPager2.setCurrentItem(0);
        viewPager2.setUserInputEnabled(false);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        //Xử ly đăng nhập
        //handleLogin();

    }

//    private void handleLogin() {
//        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
//        if (sharedPreferencesManager.isUserLoggedIn()) {
//            String username = sharedPreferencesManager.getUserName();
//            statusLogin.setText("Xin chào " + username);
//        } else {
//            statusLogin.setText("Xin chào! Hãy đăng nhập!");
//        }
//        // Xử lý sự kiện khi người dùng nhấn vào nút đăng nhập
//        login.setOnClickListener(v -> {
//            if (sharedPreferencesManager.isUserLoggedIn()) {
//                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
//                startActivity(intent);
//            } else {
//                navigateToLogin();
//            }
//        });
//    }


    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_general) {
            viewPager2.setCurrentItem(0, true);
            return true;
        } else if (itemId == R.id.navigation_calendar) {
            viewPager2.setCurrentItem(1, true);
            return true;
        } else if (itemId == R.id.navigation_transaction) {
            viewPager2.setCurrentItem(2, true);
            return true;
        } else if (itemId == R.id.navigation_report) {
            viewPager2.setCurrentItem(3, true);
            return true;
        } else if (itemId == R.id.navigation_more) {
            viewPager2.setCurrentItem(4, true);
            return true;
        }

        return false;
    }
}


