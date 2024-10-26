package com.example.expensemanagement.adapter;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.expensemanagement.fragment.IncomeFragment;
import com.example.expensemanagement.fragment.TransactionFormFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IncomeFragment(); // Fragment cho Thu
            case 1:
                return new TransactionFormFragment(); // Fragment cho Chi
            default:
                Log.d("TAG", "getItem: " + "null");
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2; // Số lượng tab
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Thu nhập";
            case 1:
                return "Chi tiêu";
            default:
                return null;
        }
    }
}

