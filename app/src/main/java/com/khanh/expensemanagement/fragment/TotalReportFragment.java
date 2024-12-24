package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TotalReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalReportFragment extends Fragment {

    private static final String ARG_CALENDAR_DAY = "calendarDay";
    DatabaseHelper db;
    TextView txt_income, txt_expense, txt_balance;
    private CalendarDay calendarDay;

    public TotalReportFragment() {
    }

    public static TotalReportFragment newInstance(CalendarDay calendarDay) {
        TotalReportFragment fragment = new TotalReportFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CALENDAR_DAY, calendarDay);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            calendarDay = getArguments().getParcelable(ARG_CALENDAR_DAY);
        }
        db = DatabaseHelper.getInstance(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_report, container, false);
        txt_income = view.findViewById(R.id.income);
        txt_expense = view.findViewById(R.id.expense);
        txt_balance = view.findViewById(R.id.balance);
        loadTotal();

        return view;
    }

    public void updateData(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
        loadTotal();
    }

    private void loadTotal() {
        Long income = db.getTotal("Income", calendarDay.getMonth() + 1, calendarDay.getYear());
        txt_income.setText(Utils.formatCurrency(income));
        Long expense = db.getTotal("Expense", calendarDay.getMonth() + 1, calendarDay.getYear());
        txt_expense.setText(Utils.formatCurrency(expense));
        txt_balance.setText(Utils.formatCurrency(income - expense));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTotal();
        Log.d("TAG", "onResume: đang chạy");
    }
}