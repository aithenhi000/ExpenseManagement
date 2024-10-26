package com.example.expensemanagement.main;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensemanagement.R;
import com.example.expensemanagement.fragment.CalendarFragment;
import com.example.expensemanagement.fragment.ReportFragment;
import com.example.expensemanagement.fragment.TransactionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity

        implements BottomNavigationView.OnItemSelectedListener {
    //Tạo thanh chuyển hướng giữa các fragment
    BottomNavigationView bottomNavigationView;

    TransactionFragment HomeFragment = new TransactionFragment();
    ReportFragment reportFlagment = new ReportFragment();
    CalendarFragment calendarFragment = new CalendarFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Thiết lập Fragment mặc định khi khởi động
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, HomeFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, HomeFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        } else if (itemId == R.id.navigation_chart) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, reportFlagment)
                    .commit();
            return true;
        } else if (itemId == R.id.navigation_more) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, calendarFragment)
                    .commit();
            return true;
        }
        return false;
    }


}

