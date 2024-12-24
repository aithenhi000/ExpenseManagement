package com.khanh.expensemanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.khanh.expensemanagement.fragment.CalendarFragment;
import com.khanh.expensemanagement.fragment.GeneralFragment;
import com.khanh.expensemanagement.fragment.MoreFragment;
import com.khanh.expensemanagement.fragment.TransactionFragment;

public class BottomNavAdapter extends FragmentStateAdapter {
    public BottomNavAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GeneralFragment();
            case 1:
                return new CalendarFragment();
            case 2:
                return new TransactionFragment();
            case 3:
                return new MoreFragment();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
