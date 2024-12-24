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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.TransactionGroupAdapter;
import com.khanh.expensemanagement.repository.TransactionDAO;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneralContentFragment extends Fragment {
    private static final String ARG_DATE = "date";
    private LinearLayout lnIncome, lnExpense;
    private PieChart pieChart;
    private CalendarDay currentDate;
    private TextView tvIncomeTotal, tvExpenseTotal, tvIncome, tvExpense;
    private String type = "income"; //mặc định cho view là income = true
    private RecyclerView recyclerView;
    private TransactionDAO transactionDAO;

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
        tvExpenseTotal = view.findViewById(R.id.tvExpenseTotal);
        tvExpense = view.findViewById(R.id.tvExpense);
        recyclerView = view.findViewById(R.id.rvTransaction);
        pieChart = view.findViewById(R.id.pieChart);

        lnIncome = view.findViewById(R.id.lnIncome);
        lnExpense = view.findViewById(R.id.lnExpense);

        transactionDAO = new TransactionDAO();
        lnIncome.setSelected(true);
        lnExpense.setSelected(false);
        displayChart(type);
        setColorTextView(type);

        lnIncome.setOnClickListener(v -> updateUI("income", 0));
        lnExpense.setOnClickListener(v -> updateUI("expense", 1));

        return view;
    }

    private void updateUI(String newType, int viewPagerPosition) {
        if (!type.equals(newType)) {
            type = newType;
            lnIncome.setSelected(type.equals("income"));
            lnExpense.setSelected(type.equals("expense"));
            setColorTextView(type);
            displayChart(type);

        }
    }

    private void setColorTextView(String type) {
        int selectedColor = Color.BLACK;
        int deselectedColor = Color.WHITE;

        if (type.equals("income")) {

            tvIncomeTotal.setTextColor(selectedColor);
            tvIncome.setTextColor(selectedColor);
            tvExpenseTotal.setTextColor(deselectedColor);
            tvExpense.setTextColor(deselectedColor);
        } else {
            tvIncomeTotal.setTextColor(deselectedColor);
            tvIncome.setTextColor(deselectedColor);
            tvExpenseTotal.setTextColor(selectedColor);
            tvExpense.setTextColor(selectedColor);
        }
    }

    public void displayCategoryPercentages(Map<String, Float> categoryPercentages) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TransactionGroupAdapter adapter = new TransactionGroupAdapter(categoryPercentages, getContext());
        recyclerView.setAdapter(adapter);
    }


    private void displayChart(String type) {
        String monthText = currentDate.getMonth() < 10 ? "0" + (currentDate.getMonth() + 1) : String.valueOf(currentDate.getMonth() + 1);
        String date = currentDate.getYear() + "-" + monthText;

        Log.d("TAG", "displayChart: " + date);
        transactionDAO.calculateTotalByTypeForMonth(date, new TransactionDAO.TotalIncomeTypeCallback() {
            @Override
            public void onTotalIncomeCalculated(Map<String, Double> totalByType) {
                tvIncomeTotal.setText(String.valueOf(totalByType.get("income")));
                tvExpenseTotal.setText(String.valueOf(totalByType.get("expense")));
            }
        });

        transactionDAO.calculateCategoryPercentagesByMonth(date, type, new TransactionDAO.TotalIncomeCallback() {

            @Override
            public void onCategoryPercentagesCalculated(Map<String, Float> categoryPercentages) {
                displayCategoryPercentages(categoryPercentages);
                float remaining_percentage = 0;
                List<PieEntry> pieEntries = new ArrayList<>();
                for (Map.Entry<String, Float> entry : categoryPercentages.entrySet()) {

                    String category = entry.getKey();
                    float percentage = entry.getValue();
                    if (percentage > 10) {
                        pieEntries.add(new PieEntry(percentage, category));
                    } else {
                        remaining_percentage += percentage;
                    }
                }

                if (categoryPercentages.isEmpty()) {
                    pieChart.clear();
                    pieChart.setNoDataText("Không có dữ liệu");
                    pieChart.setNoDataTextColor(Color.GRAY);
                    pieChart.setNoDataTextTypeface(Typeface.DEFAULT_BOLD);
                    return;
                }
                pieEntries.add(new PieEntry(remaining_percentage, "Còn lại"));

                PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
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

                // Thiết lập các thuộc tính của PieDataSet
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(12f);
                pieDataSet.setSliceSpace(10f);

                // Tạo PieData và thiết lập cho PieChart
                PieData pieData = new PieData(pieDataSet);
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
        });

    }


}
