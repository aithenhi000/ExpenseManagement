package com.khanh.expensemanagement.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.khanh.expensemanagement.fragment.GeneralContentFragment;
import com.prolificinteractive.materialcalendarview.CalendarDay;

public class MonthFragmentAdapter extends FragmentStateAdapter {

    private final CalendarDay currentDate; // Sử dụng CalendarDay
    private int totalMonths; // Tổng số tháng bạn muốn hiển thị

    public MonthFragmentAdapter(@NonNull FragmentActivity fragmentActivity, int totalMonths) {
        super(fragmentActivity);
        this.totalMonths = totalMonths;
        this.currentDate = CalendarDay.today(); // Dùng CalendarDay thay vì Calendar
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        CalendarDay date = currentDate;
        date = CalendarDay.from(date.getYear(), position, 1);

        return GeneralContentFragment.newInstance(date);
    }

    @Override
    public int getItemCount() {
        return totalMonths;
    }
}


