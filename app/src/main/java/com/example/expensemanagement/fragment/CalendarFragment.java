package com.example.expensemanagement.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.databases.DatabaseHelper;
import com.example.expensemanagement.R;
import com.example.expensemanagement.model.TransactionSummary;
import com.example.expensemanagement.adapter.TransactionAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
    private MaterialCalendarView calendarView;
    private CalendarDay date;
    private TransactionViewFragment transactionViewFragment;
    private TextView txtStatus;
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

            }
        });
    }
    private void updateTransactionData(CalendarDay date, int option) {
        if (transactionViewFragment == null) {
            transactionViewFragment = TransactionViewFragment.newInstance(date, option);
            getChildFragmentManager().beginTransaction().replace(R.id.rvTransaction, transactionViewFragment).commit();
        } else {
            // Cập nhật dữ liệu trong fragment hiện tại
            transactionViewFragment.updateData(date, option); // Phương thức này cần được định nghĩa trong TransactionViewFragment
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