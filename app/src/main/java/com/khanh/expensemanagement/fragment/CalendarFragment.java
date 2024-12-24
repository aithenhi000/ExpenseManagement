package com.khanh.expensemanagement.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.TransactionAdapter;
import com.khanh.expensemanagement.model.Transaction;
import com.khanh.expensemanagement.repository.TransactionDAO;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.List;

public class CalendarFragment extends Fragment {
    int option = 1;
    List<Object[]> incomeExpenseDays;
    private MaterialCalendarView calendarView;
    private CalendarDay dateCurrent;
    private TextView txtStatus;
    private TransactionDAO transactionDAO;
    private int selectedMonth, selectedYear;
    private RecyclerView recyclerView;

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
        recyclerView = view.findViewById(R.id.rvTransaction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        txtStatus.setText((dateCurrent.getMonth() + 1) + "/" + dateCurrent.getYear());
        transactionDAO = new TransactionDAO();

        handleEventClickCalendar();
        return view;
    }


    private void loadRecyclerView(List<Transaction> transactionList) {
        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactionList);

        recyclerView.setAdapter(transactionAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                int position = rv.getChildAdapterPosition(child);
                Transaction transaction = transactionList.get(position);
                return true;
            }


            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void handleEventClickCalendar() {
        calendarView.setOnMonthChangedListener((widget, date) -> {
            String formattedMonth = (date.getMonth() < 10) ? "0" + (date.getMonth() + 1) : String.valueOf(date.getMonth() + 1);
            String formatDate = date.getYear() + "-" + formattedMonth;
            Log.d("TAG", "handleEventClickCalendar: " + formatDate + "aa " + date.getDay());

            txtStatus.setText((date.getMonth() + 1) + "-" + date.getYear());
            transactionDAO.findTransactionsByMonthYear(formatDate, new TransactionDAO.TransactionCallback() {
                @Override
                public void onTransactionsFound(List<Transaction> transactions) {
                    loadRecyclerView(transactions);
                }

                @Override
                public void onError(String error) {

                }
            });
        });

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            String formattedMonth = (date.getMonth() < 10) ? "0" + (date.getMonth() + 1) : String.valueOf(date.getMonth() + 1);

            String formattedDay = (date.getDay() < 10) ? "0" + date.getDay() : String.valueOf(date.getDay());

            String str = date.getYear() + "-" + formattedMonth + "-" + formattedDay;
            transactionDAO.findTransactionsBySpecificDate(str, new TransactionDAO.TransactionCallback() {
                @Override
                public void onTransactionsFound(List<Transaction> transactions) {
                    loadRecyclerView(transactions);
                }

                @Override
                public void onError(String error) {

                }

            });
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