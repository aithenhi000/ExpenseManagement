package com.khanh.expensemanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.khanh.expensemanagement.R;

public class BaseActivity extends AppCompatActivity {
    protected void displayTitle(String title) {
        TitleFragment titleFragment = TitleFragment.newInstance(title);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.titleBar, titleFragment)  // ID của ViewGroup chứa Fragment
                .commit();
    }
}
