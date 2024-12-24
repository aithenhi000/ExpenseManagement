package com.khanh.expensemanagement.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khanh.expensemanagement.model.Expense;

public class IncomeDAO {
    private DatabaseReference usersRef;   // Tham chiếu đến "users"

    public IncomeDAO() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void add(Expense expense, String userId, OnCompleteListener<Void> onCompleteListener) {
        String expenseId = usersRef.child(userId).child("incomes").push().getKey(); // Tạo ID duy nhất cho mỗi khoản chi
        if (expenseId != null) {
            usersRef.child(userId).child("incomes").child(expenseId).setValue(expense)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Cập nhật tổng chi cho người dùng
                            updateTotalExpenses(userId, expense.getAmount(), onCompleteListener);
                        } else {
                            onCompleteListener.onComplete(task);
                        }
                    });
        }
    }

    private void updateTotalExpenses(String userId, Long amount, OnCompleteListener<Void> onCompleteListener) {
        usersRef.child(userId).child("totalIncomes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long currentTotal = snapshot.exists() ? snapshot.getValue(Long.class) : 0L;
                Long newTotal = currentTotal + amount;
                usersRef.child(userId).child("totalIncomes").setValue(newTotal)
                        .addOnCompleteListener(onCompleteListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllExpenses(String userId, ValueEventListener listener) {
    }
}
