package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.TransactionAdapter;
import com.khanh.expensemanagement.model.Category;
import com.khanh.expensemanagement.model.Transaction;

import java.util.List;

public class IncomeFragment extends Fragment {
    private TextView tvDate;
    private TextInputEditText editNote, editMoney;
    private RecyclerView rvTransaction, rvCategory;
    private List<Category> categoryList;
    private Category selectedCategory;
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
