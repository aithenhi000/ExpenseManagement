package com.khanh.expensemanagement.model;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

public class MultiDecorator implements DayViewDecorator {
    private final List<Object[]> incomeExpenseDays;

    public MultiDecorator(List<Object[]> incomeExpenseDays) {
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
        view.addSpan(new MultipleDotSpan(7, Color.BLUE, Color.RED));
    }
}
