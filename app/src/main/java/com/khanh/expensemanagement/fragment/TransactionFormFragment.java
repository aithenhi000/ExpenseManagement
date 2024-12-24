package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.CategoryAdapter;
import com.khanh.expensemanagement.adapter.TransactionAdapter;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.Category;
import com.khanh.expensemanagement.model.SharedPreferencesManager;
import com.khanh.expensemanagement.model.Transaction;
import com.khanh.expensemanagement.model.TransactionSummary;
import com.khanh.expensemanagement.model.Utils;
import com.khanh.expensemanagement.viewmodel.TransactionSharedViewModel;

import java.util.List;
import java.util.Objects;

public class TransactionFormFragment extends Fragment {
    private static final String ARG_TRANSACTION_TYPE = "transaction_type";
    private String transactionType; // "Expense" hoặc "Income"
    private TextView tiDate;
    private TextInputEditText editNote, editMoney;
    private RecyclerView rvTransaction, rvCategory;
    private Category selectedCategory;
    private DatabaseHelper dbHelper;
    private TransactionAdapter transactionAdapter;
    private List<TransactionSummary> transactionSummary;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private List<Category> categories;
    private Button btnAddTransaction;

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
        dbHelper = new DatabaseHelper(requireContext());
        categoryList = dbHelper.loadCategories(transactionType);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_form, container, false);
        tiDate = view.findViewById(R.id.tiDate);
        editNote = view.findViewById(R.id.editNote);
        editMoney = view.findViewById(R.id.editMoney);
        rvCategory = view.findViewById(R.id.rvCategory);
        btnAddTransaction = view.findViewById(R.id.btnAddTransaction);
        setupDefaultTextBehavior(editNote, getString(R.string.default_note));
        setupDefaultTextBehavior(editMoney, getString(R.string.default_amount));

        tiDate.setText(Utils.getDateNow());
        Utils.handleDatePickerDialog(tiDate, getContext());

        createCategoryView();

        if (Objects.equals(transactionType, "Expense")) {
            btnAddTransaction.setText("Nhập khoản chi");

        } else {
            btnAddTransaction.setText("Nhập khoản thu");
        }
        btnAddTransaction.setOnClickListener(view1 -> {
            if (String.valueOf(editMoney.getText()).isEmpty() || String.valueOf(editMoney.getText()).equals(getString(R.string.default_amount))) {
                Toast.makeText(getContext(), "Hãy nhập Số tiền!", Toast.LENGTH_SHORT).show();

            } else {
                SharedPreferencesManager spf = SharedPreferencesManager.getInstance(getContext());
                int user_id = spf.isUserLoggedIn() ? dbHelper.getUserByUsername(spf.getUserName()).getUserId() : 0;
                long expenseId = dbHelper.addTransaction(new Transaction(
                        Long.parseLong(String.valueOf(editMoney.getText())),
                        user_id,
                        selectedCategory.getId(),
                        Utils.formatDate(String.valueOf(tiDate.getText())),
                        String.valueOf(editNote.getText())));
                if (expenseId != -1) {
                    TransactionSharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(TransactionSharedViewModel.class);
                    sharedViewModel.notifyTransactionAdded();
                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }


        });

        return view;
    }


    private void setupDefaultTextBehavior(TextInputEditText editText, String text) {
        editText.setText(text); // Đặt text mặc định ban đầu

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Khi nhấn vào ô, nếu văn bản hiện tại là văn bản mặc định, xóa nó
                if (String.valueOf(editText.getText()).equals(text)) {
                    editText.setText("");
                }
            } else {
                // Khi không còn focus, kiểm tra xem ô có trống không
                if (String.valueOf(editText.getText()).isEmpty()) {
                    editText.setText(text); // Đặt lại văn bản mặc định nếu không có văn bản
                }
            }
        });

        // Đảm bảo khi nhấn vào ô, văn bản mặc định sẽ không được tính
        editText.setOnClickListener(v -> {
            if (String.valueOf(editText.getText()).equals(text)) {
                editText.setText(""); // Xóa văn bản mặc định khi nhấn vào ô
            }
        });
    }

    private void createCategoryView() {

        categoryAdapter = new CategoryAdapter(requireContext(), categoryList);
        rvCategory.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvCategory.setAdapter(categoryAdapter);


        selectedCategory = categoryList.get(categoryAdapter.getSelectedPosition());
        categoryAdapter.setOnItemClickListener(category -> {
            selectedCategory = category;
        });
    }


}
