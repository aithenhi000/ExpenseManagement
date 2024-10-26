package com.example.expensemanagement.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.expensemanagement.R;
import com.example.expensemanagement.model.Category;
import com.example.expensemanagement.model.Transaction;
import com.example.expensemanagement.model.TransactionSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "Transaction.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột cho "Tiền chi"
    private static final String TABLE_TRANSACTION = "Transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_CATEGORY = "category_id";
    private static final String COLUMN_TRANSACTION_DATE = "created_at";
    private static final String COLUMN_TRANSACTION_NOTE = "note";

    private static final String TABLE_CATEGORY = "Categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final String COLUMN_CATEGORY_TYPE = "category_type";
    private static final String COLUMN_CATEGORY_ICON_ID = "icon_id";

    // Câu lệnh SQL để tạo bảng Tiền chi
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " ("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TRANSACTION_AMOUNT + " REAL, "
            + COLUMN_TRANSACTION_CATEGORY + " TEXT, "
            + COLUMN_TRANSACTION_DATE + " TEXT, "
            + COLUMN_TRANSACTION_NOTE + " TEXT)";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " ("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME + " TEXT, "
            + COLUMN_CATEGORY_TYPE + " TEXT, "
            + COLUMN_CATEGORY_ICON_ID + " INTEGER)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        addCategory(db, "Food", "Expense", R.drawable.ic_food);
        addCategory(db, "Shopping", "Expense", R.drawable.ic_shopping);
        addCategory(db, "Vehicle", "Expense", R.drawable.ic_vehicle);
        addCategory(db, "Salary", "Income", R.drawable.ic_salary);
        addCategory(db, "Invest", "Income", R.drawable.ic_invest);
        addCategory(db, "Bank", "Income", R.drawable.ic_bank);
    }

    private void addCategory(SQLiteDatabase db, String categoryName, String categoryType, Integer categoryIconID) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_CATEGORY_TYPE, categoryType);
        values.put(COLUMN_CATEGORY_ICON_ID, categoryIconID);
        db.insert(TABLE_CATEGORY, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSACTION);
        db.execSQL(CREATE_TABLE_CATEGORY);
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        onCreate(db);
    }

    public List<Category> loadCategories() {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories", null);
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    // Phương thức thêm khoản tiền chi
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TRANSACTION_CATEGORY, transaction.getCategory_id());
        values.put(COLUMN_TRANSACTION_DATE, transaction.getDate());
        values.put(COLUMN_TRANSACTION_NOTE, transaction.getNote());
        return db.insert(TABLE_TRANSACTION, null, values);
    }

    public Transaction getTransaction(int TransactionID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TRANSACTION, null, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(TransactionID)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return new Transaction(cursor.getInt(0), cursor.getDouble(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
    }

    public List<TransactionSummary> getTransactionsForMonth(int month, int year) {
        String query = "SELECT t.amount, c.category_name, t.created_at, c.icon_id " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE created_at LIKE ?" +
                "ORDER BY t.created_at DESC";
        List<TransactionSummary> TransactionList = new ArrayList<>();
        String monthStr = (month < 10 ? "0" : "") + month;
        String[] args = {year + "-" + monthStr + "%"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, args);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TransactionSummary transaction = new TransactionSummary(
                        cursor.getDouble(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)

                );
                TransactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        return TransactionList; // Trả về danh sách Transaction
    }


    public List<TransactionSummary> getTransactionsByCategory(String[] categoryName) {
        String query = "SELECT t.amount, c.category_name, t.created_at, c.icon_id " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_type = ?";

        List<TransactionSummary> transactionSummaries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, categoryName);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TransactionSummary summary = new TransactionSummary(

                        cursor.getDouble(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)

                );
                Log.d("tag", "summary" + cursor.getDouble(0));
                transactionSummaries.add(summary); // Thêm vào danh sách
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return transactionSummaries;
    }


    // Xóa một khoản tiền chi
    public int deleteTransaction(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TRANSACTION, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Sửa một khoản tiền chi
    public int updateTransaction(long id, double amount, String category, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, amount);
        values.put(COLUMN_TRANSACTION_CATEGORY, category);
        values.put(COLUMN_TRANSACTION_DATE, date);
        values.put(COLUMN_TRANSACTION_NOTE, note);

        return db.update(TABLE_TRANSACTION, values, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Map<String, Double> calculateAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c.category_name, SUM(t.amount) AS total_expense " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_type = 'Expense' " +
                "GROUP BY c.category_name";

        Cursor cursor = db.rawQuery(query, null);

        Map<String, Double> expensesMap = new HashMap<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0);
                double totalExpense = cursor.getDouble(1);
                expensesMap.put(categoryName, totalExpense);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return expensesMap; // Trả về HashMap chứa tổng chi
    }

    public double getTotalExpense() {
        double totalExpense = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(t.amount) AS total_expense " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_type = 'Expense'";

        Cursor cursor = db.rawQuery(query, null);

        // Kiểm tra nếu con trỏ không rỗng và có dữ liệu
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                totalExpense = cursor.getDouble(0); // Lấy giá trị tổng từ cột đầu tiên
            }
            cursor.close(); // Đóng con trỏ
        }

        return totalExpense;
    }


}
