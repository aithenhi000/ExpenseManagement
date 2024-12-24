package com.khanh.expensemanagement.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.model.CategoryTotal;
import com.khanh.expensemanagement.model.Utils;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class GeneralContentFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private ArrayList<CategoryTotal> categoryTotals;
    private LinearLayout lnIncome, lnExpense;
    private PieDataSet pieDataSet;
    private PieData pieData;
    private PieChart pieChart;
    private CalendarDay currentDate;
    private TextView tvIncomeTotal, tvStatusIncome, tvExpenseTotal, tvStatusExpense, balance, tvIncome, tvExpense, des1, des2;
    private String type = "Income"; //mặc định cho view là income = true
    private RecyclerView recyclerView;
    private CalendarDay currentMonth;

    public GeneralContentFragment() {
        // require a empty public constructor
    }

    public static GeneralContentFragment newInstance(CalendarDay date) {
        GeneralContentFragment fragment = new GeneralContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentDate = getArguments().getParcelable(ARG_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general_content, container, false);
        tvIncomeTotal = view.findViewById(R.id.tvIncomeTotal);
        tvIncome = view.findViewById(R.id.tvIncome);
        tvStatusIncome = view.findViewById(R.id.tvStatusIncome);
        tvExpenseTotal = view.findViewById(R.id.tvExpenseTotal);
        tvExpense = view.findViewById(R.id.tvExpense);
        recyclerView = view.findViewById(R.id.rvTransaction);
        tvStatusExpense = view.findViewById(R.id.tvStatusExpense);
        balance = view.findViewById(R.id.balance);
        pieChart = view.findViewById(R.id.pieChart);

        lnIncome = view.findViewById(R.id.lnIncome);
        lnExpense = view.findViewById(R.id.lnExpense);
        des1 = view.findViewById(R.id.des1);
        des2 = view.findViewById(R.id.des2);

        displayTransactionByCategory();
        lnIncome.setSelected(true);
        lnExpense.setSelected(false);
        displayChart(type);
        setColorTextView(type);

        lnIncome.setOnClickListener(v -> updateUI("Income", 0));
        lnExpense.setOnClickListener(v -> updateUI("Expense", 1));

        return view;
    }

    private void updateUI(String newType, int viewPagerPosition) {
        if (!type.equals(newType)) {
            type = newType;
            lnIncome.setSelected(type.equals("Income"));
            lnExpense.setSelected(type.equals("Expense"));
            displayTransactionByCategory();
            // Cập nhật màu sắc và biểu đồ
            setColorTextView(type);
            displayChart(type);

        }
    }

    private void setColorTextView(String type) {
        int selectedColor = Color.BLACK;
        int deselectedColor = Color.WHITE;

        if (type.equals("Income")) {
            des1.setTextColor(selectedColor);
            tvIncomeTotal.setTextColor(selectedColor);
            tvIncome.setTextColor(selectedColor);
            des2.setTextColor(deselectedColor);
            tvExpenseTotal.setTextColor(deselectedColor);
            tvExpense.setTextColor(deselectedColor);
        } else {
            des1.setTextColor(deselectedColor);
            tvIncomeTotal.setTextColor(deselectedColor);
            tvIncome.setTextColor(deselectedColor);
            des2.setTextColor(selectedColor);
            tvExpenseTotal.setTextColor(selectedColor);
            tvExpense.setTextColor(selectedColor);
        }
    }


    private void displayChart(String type) {
        if (categoryTotals.isEmpty()) {
            pieChart.clear();
            pieChart.setNoDataText("Không có dữ liệu");
            pieChart.setNoDataTextColor(Color.GRAY);
            pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
            return;
        }

        // Tạo danh sách PieEntry để lưu các phần của biểu đồ
        List<PieEntry> pieEntries = new ArrayList<>();
        float remaining_percentage = 0;

        for (CategoryTotal categoryTotal : categoryTotals) {
            float percentage = categoryTotal.getPercentage();
            if (percentage > 10) {
                String categoryName = categoryTotal.getCategory_name();  // Lấy tên danh mục
                pieEntries.add(new PieEntry(percentage, categoryName));
            } else {
                remaining_percentage += percentage;
            }

        }
        pieEntries.add(new PieEntry(remaining_percentage, "Còn lại"));

        // Thiết lập PieDataSet
        pieDataSet = new PieDataSet(pieEntries, "");
        if (type.equals("Expense")) {
            pieDataSet.setColors(
                    Color.parseColor("#FFB3BA"),
                    Color.parseColor("#FFDFBA"),
                    Color.parseColor("#FFFFBA"),
                    Color.parseColor("#BAFFC9"),
                    Color.parseColor("#BAE1FF"),
                    Color.parseColor("#E6B3FF")
            );
        } else {
            pieDataSet.setColors(
                    Color.parseColor("#B3E5FC"),
                    Color.parseColor("#B2EBF2"),
                    Color.parseColor("#C8E6C9"),
                    Color.parseColor("#D1C4E9"),
                    Color.parseColor("#BBDEFB"),
                    Color.parseColor("#E0F7FA")
            );
        }

        // Thiết lập thuộc tính của PieDataSet
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setSliceSpace(10f);

        // Tạo PieData và thiết lập cho PieChart
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Cập nhật biểu đồ

        // Thiết lập các thuộc tính bổ sung cho biểu đồ
        pieChart.setUsePercentValues(true);
        pieData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f%%", value);
            }
        });
        pieChart.getDescription().setEnabled(false);
    }


    private void updateStatusText(TextView textView, Long currentTotal, Long previousTotal, int increaseColor, int decreaseColor) {
        long difference = currentTotal - previousTotal;
        if (difference > 0) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), increaseColor));
            textView.setText("Tăng " + Utils.formatCurrency(difference));
        } else if (difference < 0) {
            textView.setTextColor(ContextCompat.getColor(requireContext(), decreaseColor));
            textView.setText("Giảm " + Utils.formatCurrency(Math.abs(difference)));
        } else {
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.strong_blue));
            textView.setText("Không thay đổi");
        }
    }

    private void displayTransactionByCategory() {
        Log.d("TAG", "onResume: displayTransactionByCategory");
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }


    public void updateContent() {
        displayTransactionByCategory();
        displayChart(type);
        Log.d("TAG", "onResume: 123");

    }
}
