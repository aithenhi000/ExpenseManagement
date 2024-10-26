package com.example.expensemanagement.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.expensemanagement.databases.DatabaseHelper;
import com.example.expensemanagement.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFragment extends Fragment {
    private PieChart pieChart;


    public ReportFragment() {
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_second, container, false);

        List<PieEntry> pieEntries = new ArrayList<>();
        // Khởi tạo PieChart
        pieChart = view.findViewById(R.id.pieChart);
        DatabaseHelper db = new DatabaseHelper(requireContext());
        double total_expense = db.getTotalExpense();
        Map<String, Double> expensesMap = new HashMap<>();
        expensesMap = db.calculateAllExpenses();
        expensesMap.forEach((categoryName, totalExpense) -> {
            pieEntries.add(new PieEntry((float) (totalExpense * 100 / total_expense), categoryName));
        });

        // Tạo PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Categories");
        pieDataSet.setColors(ColorTemplate.createColors(new int[]{Color.parseColor("#8454E5"), Color.parseColor("#B9E5E8"), Color.parseColor("#7F3EAC")})); // Màu sắc cho các phần của biểu đồ

        // Tạo PieData
        PieData pieData = new PieData(pieDataSet);

        // Thiết lập dữ liệu cho biểu đồ
        pieChart.setData(pieData);
        pieChart.invalidate(); // Cập nhật biểu đồ
        return view;
    }

    private void createHistoryList() {

    }
}
