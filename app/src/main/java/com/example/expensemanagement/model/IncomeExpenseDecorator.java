package com.example.expensemanagement.model;

// IncomeExpenseDecorator.java
import android.graphics.Color;


import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import java.util.Map;

public class IncomeExpenseDecorator implements DayViewDecorator {
    private final Map<CalendarDay, String> incomeExpenseDays;

    public IncomeExpenseDecorator(Map<CalendarDay, String> incomeExpenseDays) {
        this.incomeExpenseDays = incomeExpenseDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return incomeExpenseDays.containsKey(day); // Đánh dấu các ngày có trong danh sách
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Thêm dấu chấm màu hoặc dấu hiệu dưới ngày
        view.addSpan(new DotSpan(5, Color.BLUE)); // dấu chấm màu xanh dương để biểu thị ngày có thu chi
        // Bạn có thể sử dụng màu sắc hoặc thêm hình dạng đơn giản
    }
}

