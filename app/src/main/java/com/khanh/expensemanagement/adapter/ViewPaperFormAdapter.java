package com.khanh.expensemanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.khanh.expensemanagement.fragment.TransactionFormFragment;

public class ViewPaperFormAdapter extends FragmentStateAdapter {


    public ViewPaperFormAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return TransactionFormFragment.newInstance("Expense");
            case 1:
                return TransactionFormFragment.newInstance("Income");
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
