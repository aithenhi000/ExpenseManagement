package com.khanh.expensemanagement.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {
    public static String formatCurrency(Long number) {
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedNumber = numberFormat.format(number);
        return numberFormat.format(number) + " đ";
    }

    public static boolean isValidEmail(String email) {
        String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static String getDateNow() {
        CalendarDay calendarDay = CalendarDay.today();
        return formatDateDDMMYYYY(calendarDay);
    }

    public static void handleDatePickerDialog(TextView textView, Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        textView.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view1, year1, month1, dayOfMonth) -> {
                        CalendarDay calendarDay = CalendarDay.from(year1, month1, dayOfMonth);
                        textView.setText(formatDateDDMMYYYY(calendarDay));
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    public static String formatDate(String date) {
        String[] parts = date.split("/");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }

    private static String formatDateDDMMYYYY(CalendarDay calendarDay) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
        return dateFormat.format(calendarDay.getDate());
    }

    public static String convert_us_to_vn_date(String inputDate) {
        // Định dạng đầu vào
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        // Định dạng đầu ra
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));

        try {
            // Chuyển đổi chuỗi đầu vào thành Date
            Date date = inputFormat.parse(inputDate);
            // Trả về chuỗi theo định dạng đầu ra
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
