package com.khanh.expensemanagement.fragment;

import com.khanh.expensemanagement.model.Transaction;

public interface TransactionClickListener {
    // Triển khai phương thức từ interface
    void onTransactionClick(Transaction transaction);
}
