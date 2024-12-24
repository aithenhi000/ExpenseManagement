package com.khanh.expensemanagement.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TransactionSharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> transactionAdded = new MutableLiveData<>();

    public void notifyTransactionAdded() {
        transactionAdded.setValue(true);
    }

    // Phương thức để reset giá trị sau khi xử lý
    public void resetTransactionAdded() {
        transactionAdded.setValue(false);
    }

    public LiveData<Boolean> getTransactionAddedNotifier() {
        return transactionAdded;
    }
}