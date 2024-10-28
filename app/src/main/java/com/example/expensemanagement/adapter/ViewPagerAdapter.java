package com.example.expensemanagement.adapter;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expensemanagement.fragment.TransactionFormFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return TransactionFormFragment.newInstance("Income"); // Fragment cho Thu
            case 1:
                return TransactionFormFragment.newInstance("Expense"); // Fragment cho Chi
            default:
                return new Fragment(); // Trả về một Fragment rỗng nếu có lỗi
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Số lượng tab
    }
}

