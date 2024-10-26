package com.example.expensemanagement.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.model.Category;
import com.example.expensemanagement.databases.DatabaseHelper;
import com.example.expensemanagement.R;
import com.example.expensemanagement.model.Transaction;
import com.example.expensemanagement.model.TransactionSummary;
import com.example.expensemanagement.adapter.CategoryAdapter;
import com.example.expensemanagement.adapter.TransactionAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class TransactionFormFragment extends Fragment {
    private TextView tvDate;
    private TextInputEditText editNote, editMoney;
    private RecyclerView rvTransaction, rvCategory;
    private Category selectedCategory;
    private DatabaseHelper dbHelper;
    private TransactionAdapter transactionAdapter;
    private List<TransactionSummary> transactionSummary;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        tvDate = view.findViewById(R.id.tvDate);
        editNote = view.findViewById(R.id.editNote);
        editMoney = view.findViewById(R.id.editMoney);
        rvCategory = view.findViewById(R.id.rvCategory);
        //s rvTransaction = view.findViewById(R.id.rvTransaction);
        dbHelper = new DatabaseHelper(requireContext());
//        Map<String, Double> expensesMap = new HashMap<>();
//        expensesMap = dbHelper.calculateAllExpenses();
//        expensesMap.forEach((categoryName, totalExpense) -> {
//        });


        showDatePickerDialog();

        createCategoryView();
        //Create Expense RecycleView

//

        Button btnAddExpense = view.findViewById(R.id.btnAddTransaction);
        btnAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExpense();

            }
        });

        return view;
    }

    private void createCategoryView() {
        categoryList = dbHelper.loadCategories();
        categoryAdapter = new CategoryAdapter(requireContext(), categoryList);
        rvCategory.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        rvCategory.setAdapter(categoryAdapter);


        selectedCategory = categoryList.get(categoryAdapter.getSelectedPosition());
        categoryAdapter.setOnItemClickListener(category -> {
            selectedCategory = category;
        });
    }

    private void addExpense() {
        String note = "No note";
        String date = formatDate(tvDate.getText().toString());
        note = Objects.requireNonNull(editNote.getText()).toString();
        String expenseStr = Objects.requireNonNull(editMoney.getText()).toString();
        if (expenseStr.isEmpty()) {
            Toast.makeText(getContext(), "Add Thất Bại, Vui lòng nhập Tiền chi", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("tag", "addExpense: " + selectedCategory.getName());
        double expense = Double.parseDouble(expenseStr);
        long expenseId = dbHelper.addTransaction(new Transaction(expense, selectedCategory.getId(), date, note));

        if (expenseId != -1) {
            //updateExpenseList();
            Toast.makeText(getContext(), "Add THÀNH CÔNG", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Add THẤT BẠI!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        updateDateText(year, month, day);
        // Set click listener for the date TextView
        tvDate.setOnClickListener(v -> {
            // Open DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, year1, month1, dayOfMonth) -> {
                        // Update the date in TextView when selected
                        updateDateText(year1, month1, dayOfMonth);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    private String formatDate(String date) {
        String[] parts = date.split("/");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    private void updateDateText(int year, int month, int day) {
        String selectedDate = String.format("%02d/%02d/%d", day, month + 1, year);
        tvDate.setText(selectedDate);
    }

    private void updateExpenseList() {
        transactionSummary.clear();
        transactionSummary.addAll(dbHelper.getTransactionsByCategory(new String[]{"Expense"}));
        transactionAdapter.notifyDataSetChanged();
        rvTransaction.scrollToPosition(transactionSummary.size() - 1);// Thông báo cho adapter cập nhật dữ liệu
    }

}
