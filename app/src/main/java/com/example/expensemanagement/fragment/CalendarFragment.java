package com.example.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.expensemanagement.R;
import com.example.expensemanagement.model.IncomeExpenseDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;
    private CalendarDay date;
    private TransactionViewFragment transactionViewFragment;
    private TextView txtStatus;
    Map<CalendarDay, String> incomeExpenseDays;
    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
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
        txtStatus=view.findViewById(R.id.txtStatus);


// Tạo một danh sách các ngày có thu chi
        incomeExpenseDays = new HashMap<>();
        incomeExpenseDays.put(CalendarDay.from(2024, 10, 5), "+3000 -1500");
        incomeExpenseDays.put(CalendarDay.from(2024, 10, 10), "+2000 -500");

// Đính các dấu hiệu cho các ngày có thu chi
        calendarView.addDecorators(new IncomeExpenseDecorator(incomeExpenseDays));

        showListByMonth();
        return view;
    }



    int flag=1;
    private void showListByMonth() {
        if(date==null){
            CalendarDay calendarDay=CalendarDay.today();
            updateTransactionData(calendarDay, flag);
            showStatus(calendarDay);
        }
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                updateTransactionData(date, flag);
                showStatus(date);
            }

        });
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                flag=0;
                updateTransactionData(date, flag);
                showStatus(date);
                String incomeExpenseInfo = incomeExpenseDays.get(date);
                if (incomeExpenseInfo != null) {
                    Toast.makeText(getContext(), "Thu/Chi: " + incomeExpenseInfo, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    private void updateTransactionData(CalendarDay date, int option) {
        if (transactionViewFragment == null) {
            transactionViewFragment = TransactionViewFragment.newInstance(date, option);
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.rvTransaction, transactionViewFragment)
                    .commit();
        } else {
            // Kiểm tra xem transactionViewFragment có còn gắn kết không
            if (transactionViewFragment.isAdded()) {
                transactionViewFragment.updateData(date, option);
            } else {
                // Nếu không còn gắn kết, bạn có thể cần tạo một Fragment mới
                transactionViewFragment = TransactionViewFragment.newInstance(date, option);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.rvTransaction, transactionViewFragment)
                        .commit();
            }
        }
    }

    private void showStatus(CalendarDay date){

        if(flag==1){
            txtStatus.setText("Giao dịch Tháng "+(date.getMonth()+1));
        }else {
            txtStatus.setText("Giao dịch Ngày "+date.getDay()+"/"+date.getMonth()+"/"+date.getYear());
            flag=1;
        }

    }

}