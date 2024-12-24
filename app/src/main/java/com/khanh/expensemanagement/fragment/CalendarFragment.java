package com.khanh.expensemanagement.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.khanh.expensemanagement.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

public class CalendarFragment extends Fragment {
    int option = 1;
    List<Object[]> incomeExpenseDays;
    private MaterialCalendarView calendarView;
    private CalendarDay dateCurrent;
    private TextView txtStatus;

    private int selectedMonth, selectedYear;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        txtStatus = view.findViewById(R.id.txtStatus);
        dateCurrent = CalendarDay.today();
        selectedMonth = dateCurrent.getMonth() + 1;
        selectedYear = dateCurrent.getYear();
        txtStatus.setOnClickListener(v -> {
            showMonthYearPickerDialog();
        });
        txtStatus.setText((dateCurrent.getMonth() + 1) + "/" + dateCurrent.getYear());


        handleEventClickCalendar();
        return view;
    }

    private void handleEventClickCalendar() {
        calendarView.setOnMonthChangedListener((widget, date) -> {
            selectedMonth = date.getMonth() + 1;
            selectedYear = date.getYear();
            txtStatus.setText((date.getMonth() + 1) + "/" + date.getYear());

        });
        final CalendarDay[] selectedDate = {null};
        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            if (selectedDate[0] != null && selectedDate[0].equals(date)) {
                // Bỏ chọn ngày và reset biến
                option = 1;
                widget.setDateSelected(date, false);
                selectedDate[0] = null; // Reset lại biến
            } else {
                // Chọn ngày mới và lưu vào biến
                option = 0;
                widget.setDateSelected(date, true);
                selectedDate[0] = date; // Cập nhật ngày đã chọn
            }


        });
    }


    private void showMonthYearPickerDialog() {
        // Lấy tham chiếu đến LayoutInflater và dialog view
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_month_year_picker, null);

        // Khởi tạo NumberPicker cho tháng và năm trong dialog
        NumberPicker monthPicker = dialogView.findViewById(R.id.numberPickerMonth);
        NumberPicker yearPicker = dialogView.findViewById(R.id.numberPickerYear);

        // Cấu hình NumberPicker cho tháng (1-12)
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(selectedMonth); // Mặc định là tháng hiện tại

        yearPicker.setMinValue(2000); // Năm bắt đầu
        yearPicker.setMaxValue(3000); // Năm kết thúc
        yearPicker.setValue(selectedYear); // Mặc định là năm hiện tại

        // Tạo Button để quay về tháng hiện tại
        Button currentMonthButton = dialogView.findViewById(R.id.buttonCurrentMonth);
        currentMonthButton.setOnClickListener(v -> {
            // Cập nhật lại giá trị của monthPicker và yearPicker về tháng và năm hiện tại
            monthPicker.setValue(dateCurrent.getMonth() + 1); // Tháng hiện tại
            yearPicker.setValue(dateCurrent.getYear()); // Năm hiện tại
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn Tháng/Năm");
        builder.setView(dialogView);

        // Khi người dùng nhấn nút "OK", cập nhật EditText và TextView
        builder.setPositiveButton("OK", (dialog, which) -> {
            selectedMonth = monthPicker.getValue();
            selectedYear = yearPicker.getValue();
            String date = String.format("%02d/%d", selectedMonth, selectedYear);
            CalendarDay calendarDay = CalendarDay.from(selectedYear, selectedMonth - 1, 1);
            // Hiển thị trong EditText và TextView
            txtStatus.setText(date);
            calendarView.setCurrentDate(calendarDay);
            txtStatus.setFocusable(false);
        });

        // Khi người dùng nhấn nút "Cancel", không làm gì
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Hiển thị dialog
        builder.show();
    }


}