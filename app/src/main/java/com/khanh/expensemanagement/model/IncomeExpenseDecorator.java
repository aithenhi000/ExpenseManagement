package com.khanh.expensemanagement.model;

// IncomeExpenseDecorator.java

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.List;

public class IncomeExpenseDecorator implements DayViewDecorator {
    private final List<Object[]> incomeExpenseDays;

    public IncomeExpenseDecorator(List<Object[]> incomeExpenseDays) {
        this.incomeExpenseDays = incomeExpenseDays;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        for (Object[] obj : incomeExpenseDays) {
            CalendarDay incomeExpenseDay = (CalendarDay) obj[0]; // Giả sử obj[0] là CalendarDay
            if (day.equals(incomeExpenseDay)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        for (Object[] obj : incomeExpenseDays) {
            CalendarDay incomeExpenseDay = (CalendarDay) obj[0]; // Giả sử obj[0] là CalendarDay
            String type = (String) obj[1]; // Giả sử obj[1] là loại giao dịch (Expense hoặc Income)
            if ("Expense".equals(type)) {
                view.addSpan(new DotSpan(7, Color.RED)); // Dấu chấm màu đỏ cho Expense
            } else if ("Income".equals(type)) {
                view.addSpan(new DotSpan(7, Color.BLUE)); // Dấu chấm màu xanh cho Income
            }

        }
    }
}

