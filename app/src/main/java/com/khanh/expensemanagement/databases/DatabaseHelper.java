package com.khanh.expensemanagement.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.khanh.expensemanagement.model.Category;
import com.khanh.expensemanagement.model.CategoryTotal;
import com.khanh.expensemanagement.model.SharedPreferencesManager;
import com.khanh.expensemanagement.model.Transaction;
import com.khanh.expensemanagement.model.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "user_name";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    // Tên cơ sở dữ liệu
    private static final String DATABASE_NAME = "Transaction.db";
    private static final int DATABASE_VERSION = 1;
    // Tên bảng và các cột cho "Tiền chi"
    private static final String TABLE_TRANSACTION = "Transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_USER_ID = "user_id";
    private static final String COLUMN_TRANSACTION_AMOUNT = "amount";
    private static final String COLUMN_TRANSACTION_CATEGORY = "category_id";
    private static final String COLUMN_TRANSACTION_DATE = "created_at";
    private static final String COLUMN_TRANSACTION_NOTE = "note";
    private static final String TABLE_CATEGORY = "Categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    private static final String COLUMN_CATEGORY_TYPE = "category_type";
    // Lệnh SQL để tạo bảng Users
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USERNAME + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE);";

    // Câu lệnh SQL để tạo bảng Tiền chi
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACTION + " ("
            + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TRANSACTION_USER_ID + " INTEGER, "
            + COLUMN_TRANSACTION_AMOUNT + " REAL, "
            + COLUMN_TRANSACTION_CATEGORY + " TEXT, "
            + COLUMN_TRANSACTION_DATE + " TEXT, "
            + COLUMN_TRANSACTION_NOTE + " TEXT)";
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " ("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CATEGORY_NAME + " TEXT, "
            + COLUMN_CATEGORY_TYPE + " TEXT) ";
    private static DatabaseHelper instance;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public int get_ID_user() {
        SharedPreferencesManager spf = SharedPreferencesManager.getInstance(context);
        return spf.getUserName() == null ? 0 : getUserByUsername(spf.getUserName()).getUserId();
    }

    public ArrayList<CategoryTotal> getTransactionByCategoryTotal(int month, int year, String type) {
        ArrayList<CategoryTotal> categoryTotals = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String totalQuery = "SELECT SUM(t.amount) AS total_amount " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_type = ? AND t.created_at LIKE ? AND t.user_id = ?";
        long totalAmount = 0;
        try (Cursor totalCursor = db.rawQuery(totalQuery, new String[]{type, year + "-" + format_1num_to_2num(month) + "%", String.valueOf(get_ID_user())})) {
            if (totalCursor != null && totalCursor.moveToFirst()) {
                totalAmount = totalCursor.getLong(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (totalAmount == 0) {
            return categoryTotals;
        }
        String categoryQuery = "SELECT category_name FROM Categories WHERE category_type=?";

        try (Cursor categoryCursor = db.rawQuery(categoryQuery, new String[]{type})) {
            if (categoryCursor != null && categoryCursor.moveToFirst()) {
                do {
                    categoryNames.add(categoryCursor.getString(0));
                } while (categoryCursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Truy vấn tổng tiền theo tên danh mục
        String query = "SELECT SUM(t.amount) AS total_amount, " +
                "c.category_name, c.category_type " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_name = ? AND t.created_at LIKE ? AND t.user_id=?" +
                "GROUP BY c.category_name, c.category_type " +
                "ORDER BY total_amount ASC ";

        // Duyệt qua từng tên danh mục và lấy tổng tiền
        for (String categoryName : categoryNames) {
            String[] args = {categoryName, year + "-" + format_1num_to_2num(month) + "%", String.valueOf(get_ID_user())};
            try (Cursor cursor = db.rawQuery(query, args)) {
                // Kiểm tra nếu cursor không null và có dữ liệu
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        long categoryAmount = cursor.getLong(0);
                        float percentage = Math.round(((float) categoryAmount / totalAmount) * 100 * 100) / 100.0f;
                        CategoryTotal categoryTotal = new CategoryTotal(
                                categoryAmount,  // total_amount
                                cursor.getString(1), // category_name
                                cursor.getString(2), // category_type
                                percentage
                        );
                        categoryTotals.add(categoryTotal);
                    } while (cursor.moveToNext()); // Di chuyển con trỏ đến bản ghi tiếp theo
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return categoryTotals;
    }

    public void insertDefaultCategories(SQLiteDatabase db) {
        addCategory(db, "Ăn uống", "Expense");
        addCategory(db, "Mua sắm", "Expense");
        addCategory(db, "Phương tiện", "Expense");
        addCategory(db, "Lương", "Income");
        addCategory(db, "Kinh doanh", "Income");
        addCategory(db, "Tiền lãi", "Income");
        addCategory(db, "Hóa đơn", "Expense");
        addCategory(db, "Giải trí", "Expense");
        addCategory(db, "Bạn bè", "Income");
        addCategory(db, "Thưởng", "Income");
        addCategory(db, "Trợ cấp", "Income");
    }

    private void addCategory(SQLiteDatabase db, String categoryName, String categoryType) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_CATEGORY_TYPE, categoryType);
        db.insert(TABLE_CATEGORY, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(CREATE_TABLE_TRANSACTION);
        db.execSQL(CREATE_TABLE_CATEGORY);
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại bảng mới
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    public void registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user.getUsername());
        values.put("password", user.getPassword());  // Lưu mật khẩu (có thể mã hóa nếu cần)
        values.put("email", user.getEmail());
        db.insert("Users", null, values);
        db.close();
    }

    public List<Category> loadCategories(String transactionType) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Categories WHERE category_type=?", new String[]{transactionType});
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
                categoryList.add(category);
            } while (cursor.moveToNext());
            cursor.close();
        }


        db.close();

        return categoryList;
    }

    public Category getCategoryForID(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); // Mở cơ sở dữ liệu ở chế độ đọc
        Cursor cursor = null;
        Category category = null;
        try {
            String query = "SELECT * FROM Categories WHERE id = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor != null && cursor.moveToFirst()) {
                category = new Category(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                );
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            if (cursor != null) {
                cursor.close(); // Đóng con trỏ để giải phóng tài nguyên
            }
        }

        return category; // Trả về đối tượng Category hoặc null
    }

    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(COLUMN_TRANSACTION_USER_ID, transaction.getUser_id());
        values.put(COLUMN_TRANSACTION_CATEGORY, transaction.getCategory_id());
        values.put(COLUMN_TRANSACTION_DATE, transaction.getDate());
        values.put(COLUMN_TRANSACTION_NOTE, transaction.getNote());
        return db.insert(TABLE_TRANSACTION, null, values);
    }

    private List<Transaction> getTransactionsByDate(String datePattern) {
        String query = "SELECT * FROM Transactions WHERE created_at LIKE ? AND user_id=" + get_ID_user() + " ORDER BY created_at DESC, id DESC";
        List<Transaction> transactionList = new ArrayList<>();
        String[] args = {datePattern};

        try (SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor = db.rawQuery(query, args)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    transactionList.add(new Transaction(
                            cursor.getInt(0),
                            cursor.getInt(1),
                            cursor.getLong(2),
                            cursor.getInt(3),
                            cursor.getString(4),
                            cursor.getString(5)
                    ));
                }
            }
        } catch (Exception e) {
            Log.e("TAG", "getTransactionsByDate: " + e);
        }

        return transactionList;
    }

    public List<Transaction> getTransactionsForDay(int day, int month, int year) {
        String date = year + "-" + format_1num_to_2num(month) + "-" + format_1num_to_2num(day);
        return getTransactionsByDate(date);
    }

    public List<Transaction> getTransactionsForMonth(int month, int year) {
        String datePattern = year + "-" + format_1num_to_2num(month) + "%";
        return getTransactionsByDate(datePattern);
    }


    public String format_1num_to_2num(int num) {
        return (num < 10 ? "0" : "") + num;
    }

    public Long getTotal(String type, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(t.amount) AS total " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE c.category_type=? AND t.created_at LIKE ? AND t.user_id=?";
        String[] args = {type, year + "-" + format_1num_to_2num(month) + "%", String.valueOf(get_ID_user())};
        Long totalExpense = null;
        Cursor cursor = db.rawQuery(query, args);
        if (cursor != null && cursor.moveToFirst()) {
            totalExpense = cursor.getLong(0);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        if (totalExpense == null) {
            return totalExpense = 0L;
        }
        return totalExpense;
    }

    public List<Object[]> getDateListForType(String LoaiTru, String type) {
        String query = "SELECT t.created_at, c.category_type " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE t.user_id = " + get_ID_user() +
                " GROUP BY t.created_at " +
                " HAVING" +
                " SUM(CASE WHEN c.category_type = ? THEN 1 ELSE 0 END) = 0 AND" +
                " SUM(CASE WHEN c.category_type = ?  THEN 1 ELSE 0 END) > 0;";
        Log.d("TAG", "getDateListForType: " + query);
        SQLiteDatabase db = this.getReadableDatabase();
        List<Object[]> calendarDays = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, new String[]{LoaiTru, type});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Object[] oj = new Object[]{
                        convertStringToCalendarDay(cursor.getString(0)),
                        cursor.getString(1)
                };
                calendarDays.add(oj);
            } while (cursor.moveToNext());
        } else {
            Log.d("TAG", "getDateForType: Cursor empty!!!!!");
        }
        if (cursor != null) {
            cursor.close();
        }

        return calendarDays;
    }

    public List<Object[]> getDateBoth(String type1, String type2) {
        String query = "SELECT t.created_at, c.category_type " +
                "FROM Transactions t " +
                "JOIN Categories c ON t.category_id = c.id " +
                "WHERE t.user_id = " + get_ID_user() +
                " GROUP BY t.created_at " +
                "HAVING" +
                " SUM(CASE WHEN c.category_type = ? THEN 1 ELSE 0 END) > 0 AND" +
                " SUM(CASE WHEN c.category_type = ?  THEN 1 ELSE 0 END) > 0;";

        SQLiteDatabase db = this.getReadableDatabase();
        List<Object[]> calendarDays = new ArrayList<>();
        Cursor cursor = db.rawQuery(query, new String[]{type1, type2});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Object[] oj = new Object[]{
                        convertStringToCalendarDay(cursor.getString(0)),

                        cursor.getString(1)
                };
                Log.d("TAG", "getDateForType: " + oj[1]);
                calendarDays.add(oj);
            } while (cursor.moveToNext());
        } else {
            Log.d("TAG", "getDateForType: Cursor empty!!!!!");
        }
        if (cursor != null) {
            cursor.close();
        }

        return calendarDays;
    }

    public CalendarDay convertStringToCalendarDay(String date) {
        String[] parts = date.split("-");
        return CalendarDay.from(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) - 1, Integer.parseInt(parts[2]));
    }

    public void updateTransaction(int id, Long amount, int category_id, String date, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("category_id", category_id);
        values.put("created_at", date);
        values.put("note", note);
        // Cập nhật trans, action dựa trên ID
        db.update("Transactions", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public Integer getIdByCategoryName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM " + TABLE_CATEGORY + " WHERE category_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{categoryName});

        Integer id = null; // Khởi tạo id với giá trị null

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0); // Lấy id từ cursor
            }
            cursor.close(); // Đóng cursor để giải phóng tài nguyên
        }

        return id; // Trả về id hoặc null nếu không tìm thấy
    }

    public List<String> getNamesByCategoryType(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> namesList = new ArrayList<>();

        // Truy vấn để lấy `type` của category dựa trên `id` đầu vào
        String type = null;
        Cursor cursorType = db.rawQuery("SELECT category_type FROM " + TABLE_CATEGORY + " WHERE id = ?", new String[]{String.valueOf(categoryId)});

        if (cursorType != null) {
            if (cursorType.moveToFirst()) {
                type = cursorType.getString(0); // Lấy `type` từ kết quả truy vấn
            }
            cursorType.close(); // Đóng cursor
        }

        // Nếu `type` không null, tiếp tục truy vấn danh sách các `name` cùng `type`
        if (type != null) {
            Cursor cursorNames = db.rawQuery("SELECT category_name FROM " + TABLE_CATEGORY + " WHERE category_type = ?", new String[]{type});

            if (cursorNames != null) {
                while (cursorNames.moveToNext()) {
                    String name = cursorNames.getString(0); // Lấy `name` từ cột đầu tiên
                    namesList.add(name); // Thêm `name` vào danh sách
                }
                cursorNames.close(); // Đóng cursor để giải phóng tài nguyên
            }
        }

        return namesList;
    }

    public void deleteTransaction(int transactionId) {
        SQLiteDatabase db = this.getWritableDatabase(); // Mở cơ sở dữ liệu ở chế độ ghi

        try {
            // Thực hiện câu lệnh xóa
            int result = db.delete("transactions", "id = ?", new String[]{String.valueOf(transactionId)});

            // Kiểm tra xem có giao dịch nào bị xóa không
            if (result > 0) {
                Log.d("Database", "Transaction deleted successfully");
            } else {
                Log.d("Database", "No transaction found with the given ID");
            }
        } catch (Exception e) {
            Log.e("Database", "Error deleting transaction", e);
        } finally {
            db.close(); // Đóng kết nối với cơ sở dữ liệu
        }
    }

    public User getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn cơ sở dữ liệu tìm người dùng theo tên đăng nhập
        Cursor cursor = db.query(
                "Users",                  // Tên bảng
                null,                      // Chọn tất cả các cột
                "user_name = ?",            // Điều kiện WHERE
                new String[]{username},    // Tham số tên người dùng
                null,                      // Không nhóm kết quả
                null,                      // Không sắp xếp theo điều kiện nào
                null                       // Không giới hạn số lượng kết quả
        );

        if (cursor != null && cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            int passwordColumnIndex = cursor.getColumnIndex("password");
            int emailColumnIndex = cursor.getColumnIndex("email");
            // Lấy thông tin người dùng từ kết quả truy vấn
            int id = cursor.getInt(idColumnIndex);
            String password = cursor.getString(passwordColumnIndex);
            String email = cursor.getString(emailColumnIndex);

            // Đóng con trỏ sau khi lấy dữ liệu
            cursor.close();

            // Trả về đối tượng User với thông tin đã lấy từ cơ sở dữ liệu
            return new User(id, username, password, email);
        }

        // Trường hợp không tìm thấy người dùng, trả về null
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public boolean deleteTransactionsByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "user_id = ?";
        String[] whereArgs = {String.valueOf(userId)};

        int rowsDeleted = db.delete("transactions", whereClause, whereArgs);
        db.close();

        return rowsDeleted > 0;
    }

    public boolean updatePassword(int userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Chuẩn bị giá trị cập nhật
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);  // "password" là tên cột trong bảng của bạn

        // Điều kiện WHERE
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(userId)};

        // Cập nhật cơ sở dữ liệu
        int rowsUpdated = db.update("Users", contentValues, whereClause, whereArgs);

        db.close();

        return rowsUpdated > 0;  // Nếu cập nhật thành công, trả về true
    }


}
