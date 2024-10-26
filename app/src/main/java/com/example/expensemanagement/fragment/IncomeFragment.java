package com.example.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanagement.model.Category;
import com.example.expensemanagement.databases.DatabaseHelper;
import com.example.expensemanagement.R;
import com.example.expensemanagement.model.Transaction;
import com.example.expensemanagement.adapter.TransactionAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class IncomeFragment extends Fragment {
    private TextView tvDate;
    private TextInputEditText editNote, editMoney;
    private RecyclerView rvTransaction, rvCategory;
    private List<Category> categoryList;
    private Category selectedCategory;
    private DatabaseHelper dbHelper;
    private List<Transaction> transactionList;
    private TransactionAdapter transactionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);
//
        return view;
    }

}
