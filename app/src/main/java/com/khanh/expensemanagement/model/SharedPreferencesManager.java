package com.khanh.expensemanagement.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UserPrefs";
    private static SharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    // Constructor ở chế độ private để áp dụng Singleton pattern
    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Sử dụng Singleton để đảm bảo có một instance duy nhất
    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }


    // Kiểm tra xem người dùng đã tồn tại chưa
    public boolean contains(String username) {
        return sharedPreferences.contains(username);
    }

    // Lấy mật khẩu người dùng theo tên đăng nhập
    public String getUserPassword(String username) {
        return sharedPreferences.getString(username, null);
    }

    // Lấy tên đăng nhập hiện tại (nếu cần)
    public String getUserName() {
        return sharedPreferences.getString("loggedInUser", null); // Trả về tên người dùng đã đăng nhập
    }

    // Kiểm tra xem người dùng đã đăng nhập chưa
    public boolean isUserLoggedIn() {
        return sharedPreferences.contains("loggedInUser");
    }

    // Lưu trạng thái đăng nhập của người dùng hiện tại
    public void setLoggedInUser(String username) {
        editor.putString("loggedInUser", username); // Lưu tên người dùng đã đăng nhập vào SharedPreferences
        editor.apply();
    }

    // Xóa thông tin người dùng khi đăng xuất
    public void logout() {
        editor.remove("loggedInUser");  // Chỉ xóa thông tin người dùng hiện tại
        editor.apply();
    }


    // Lấy thông tin trạng thái đăng nhập từ SharedPreferences
    public String getLoginStatus(Context context) {
        String loggedInUser = sharedPreferences.getString("loggedInUser", null);
        if (loggedInUser != null) {
            // Nếu có user đăng nhập, bạn có thể lấy thông tin từ DB hoặc thực hiện thao tác tiếp theo
            return loggedInUser;
        } else {
            return null;
        }
    }
}
