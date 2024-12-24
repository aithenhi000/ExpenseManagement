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
import com.khanh.expensemanagement.model.Category;
import com.khanh.expensemanagement.model.Expense;
import com.khanh.expensemanagement.model.Utils;
import com.khanh.expensemanagement.repository.ExpenseDAO;
import com.khanh.expensemanagement.repository.IncomeDAO;

import java.util.ArrayList;
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
        } else {
            btnSaveExpense.setText("Nhập khoản thu");
        }
        List<Category> categories = new ArrayList<>();
        if (transactionType.equals("expense")) {
            categories.add(new Category("Ăn uống", R.drawable.ic_food));
            categories.add(new Category("Di chuyển", R.drawable.ic_dichuyen));
        } else {
            categories.add(new Category("Trợ cấp", R.drawable.ic_trocap));
            categories.add(new Category("Lương", R.drawable.ic_salary));
        }


        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        gridViewCategories.setAdapter(categoryAdapter);
        gridViewCategories.setOnItemClickListener((parent, view1, position, id) -> {
            selectedCategory = categories.get(position).getName();
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
        long amount = Long.parseLong(amountText);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Expense expense = new Expense(note, amount, selectedCategory, date);
        if (transactionType.equals("expense")) {
            ExpenseDAO expenseDAO = new ExpenseDAO();
            expenseDAO.add(expense, userId, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Khoản chi đã được lưu và tổng chi đã được cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi lưu khoản chi", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            IncomeDAO incomeDAO = new IncomeDAO();
            incomeDAO.add(expense, userId, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Khoản Thu đã được lưu và tổng Thu đã được cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi lưu khoản Thu", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }

    }
}







