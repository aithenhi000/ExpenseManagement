package com.khanh.expensemanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.BottomNavAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewpager2);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        BottomNavAdapter bottomNavAdapter = new BottomNavAdapter(this);
        viewPager2.setAdapter(bottomNavAdapter);
        viewPager2.setCurrentItem(0);
        viewPager2.setUserInputEnabled(false);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

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
        } else if (itemId == R.id.navigation_more) {
            viewPager2.setCurrentItem(3, true);
            return true;
        }

        return false;
    }


}


