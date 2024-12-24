package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.CategoryAdapter;
import com.khanh.expensemanagement.enums.Category;
import com.khanh.expensemanagement.model.Transaction;
import com.khanh.expensemanagement.model.Utils;
import com.khanh.expensemanagement.repository.TransactionDAO;

import java.util.List;

public class TransactionFormFragment extends Fragment {
    private static final String ARG_TRANSACTION_TYPE = "transaction_type";
    private EditText etAmount, etNote;
    private TextView tiSelectedDate, tvCategory;
    private Button btnSaveExpense;
    private GridView gridViewCategories;
    private String selectedCategory;
    private String transactionType;

    public TransactionFormFragment() {
    }

    public static TransactionFormFragment newInstance(String transactionType) {
        TransactionFormFragment fragment = new TransactionFormFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTION_TYPE, transactionType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionType = getArguments().getString(ARG_TRANSACTION_TYPE);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_form, container, false);
        tiSelectedDate = view.findViewById(R.id.tiSelectedDate);
        etNote = view.findViewById(R.id.etNote);
        etAmount = view.findViewById(R.id.etAmount);
        gridViewCategories = view.findViewById(R.id.gridViewCategories);
        btnSaveExpense = view.findViewById(R.id.btnSaveExpense);
        tvCategory = view.findViewById(R.id.tvCategory);

        if (transactionType.equals("expense")) {
            btnSaveExpense.setText("Nhập khoản chi");
            tvCategory.setText("Ăn uống");
        } else {
            btnSaveExpense.setText("Nhập khoản thu");
            tvCategory.setText("Lương");

        }
        List<Category> categories = Category.getExpenseCategories(transactionType);

        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        gridViewCategories.setAdapter(categoryAdapter);
        gridViewCategories.setOnItemClickListener((parent, view1, position, id) -> {
            selectedCategory = categories.get(position).getDisplayName();
            tvCategory.setText(selectedCategory);
        });

        tiSelectedDate.setText(Utils.getDateNow());
        Utils.handleDatePickerDialog(tiSelectedDate, getContext());

        btnSaveExpense.setOnClickListener(view1 -> {
            save();
        });

        return view;
    }

    private void save() {
        String amountText = etAmount.getText().toString();
        String note = etNote.getText().toString();
        String date = tiSelectedDate.getText().toString();

        if (amountText.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        Long amount = Long.parseLong(amountText);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Transaction transaction = new Transaction(note, amount, selectedCategory, date, transactionType);
        TransactionDAO expenseDAO = new TransactionDAO();
        expenseDAO.add(transaction, userId, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Khoản chi đã được lưu và tổng chi đã được cập nhật", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Lỗi khi lưu khoản chi", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}







