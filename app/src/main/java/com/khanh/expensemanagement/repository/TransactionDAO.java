package com.khanh.expensemanagement.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.khanh.expensemanagement.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionDAO {
    private String userId;
    private DatabaseReference usersRef;   // Tham chiếu đến "users"
    private DatabaseReference expensesRef;

    public TransactionDAO() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void add(Transaction expense, String userId, OnCompleteListener<Void> onCompleteListener) {
        String expenseId = usersRef.child(userId).child("transactions").push().getKey(); // Tạo ID duy nhất cho mỗi khoản chi
        if (expenseId != null) {
            usersRef.child(userId).child("transactions").child(expenseId).setValue(expense)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateTotalExpenses(userId, expense.getAmount(), onCompleteListener);
                        } else {
                            onCompleteListener.onComplete(task);
                        }
                    });
        }
    }

    private void updateTotalExpenses(String userId, Long amount, OnCompleteListener<Void> onCompleteListener) {

        usersRef.child(userId).child("totalExpenses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long currentTotal = snapshot.exists() ? snapshot.getValue(Long.class) : 0L;
                Long newTotal = currentTotal + amount;

                usersRef.child(userId).child("totalExpenses").setValue(newTotal)
                        .addOnCompleteListener(onCompleteListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void calculateTotalByTypeForMonth(String monthYear, final TotalIncomeTypeCallback callback) {
        // Truy vấn tất cả các giao dịch có ngày trong tháng
        Query query = usersRef.child(userId).child("transactions")
                .orderByChild("date")
                .startAt(monthYear + "-01") // Bắt đầu từ ngày 1 của tháng
                .endAt(monthYear + "-31"); // Kết thúc ở ngày 31 của tháng

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalIncome = 0;
                double totalExpense = 0;

                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null) {
                        if (transaction.getType().equals("income")) {
                            totalIncome += transaction.getAmount();
                        } else if (transaction.getType().equals("expense")) {
                            totalExpense += transaction.getAmount();
                        }
                    }
                }
                Map<String, Double> totalByType = new HashMap<>();
                totalByType.put("income", totalIncome);
                totalByType.put("expense", totalExpense);

                callback.onTotalIncomeCalculated(totalByType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error: " + databaseError.getMessage());
            }
        });
    }


    public void findTransactionsBySpecificDate(String date, final TransactionCallback callback) {
        Query query = usersRef.child(userId).child("transactions")
                .orderByChild("date")
                .equalTo(date);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Transaction> transactions = new ArrayList<>();
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }
                callback.onTransactionsFound(transactions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void findTransactionsByMonthYear(String monthYear, final TransactionCallback callback) {
        Log.d("TAG", "findTransactionsByMonthYear: " + monthYear);
        Query query = usersRef.child(userId).child("transactions")
                .orderByChild("date")
                .startAt(monthYear + "-01")
                .endAt(monthYear + "-31");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Transaction> transactions = new ArrayList<>();
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    transactions.add(transaction);
                }
                callback.onTransactionsFound(transactions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void calculateCategoryPercentagesByMonth(String monthYear, String type, final TotalIncomeCallback callback) {
        Query query = usersRef.child(userId).child("transactions")
                .orderByChild("date")
                .startAt(monthYear + "-01")
                .endAt(monthYear + "-31");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Float totalAmount = 0F;
                Map<String, Float> categoryTotals = new HashMap<>();

                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = transactionSnapshot.getValue(Transaction.class);
                    if (transaction != null && transaction.getType().equals(type)) {
                        totalAmount += transaction.getAmount();

                        String category = transaction.getCategory();
                        categoryTotals.put(category, categoryTotals.getOrDefault(category, 0F) + transaction.getAmount());
                    }
                }

                Map<String, Float> categoryPercentages = new HashMap<>();
                if (totalAmount > 0) {
                    for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
                        String category = entry.getKey();
                        Float categoryTotal = entry.getValue();
                        Float percentage = (categoryTotal / totalAmount) * 100;
                        categoryPercentages.put(category, percentage);
                    }
                }
                callback.onCategoryPercentagesCalculated(categoryPercentages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "Error: " + databaseError.getMessage());
            }
        });
    }


    public interface TotalIncomeCallback {


        void onCategoryPercentagesCalculated(Map<String, Float> categoryPercentages);
    }


    public interface TransactionCallback {
        void onTransactionsFound(List<Transaction> transactions);

        void onError(String error);
    }


    public interface TotalIncomeTypeCallback {
        void onTotalIncomeCalculated(Map<String, Double> totalByType);
    }
}
