package com.khanh.expensemanagement.model;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

public class MultipleDotSpan implements LineBackgroundSpan {
    private final int radius;
    private final int[] colors;

    public MultipleDotSpan(int radius, int... colors) {
        this.radius = radius;
        this.colors = colors;
    }

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lineNumber) {
        int originalColor = paint.getColor(); // Lưu màu gốc của Paint

        int totalWidth = colors.length * radius * 3; // Tổng chiều rộng của các chấm
        int startX = (left + right) / 2 - totalWidth / 2; // Tính toán vị trí bắt đầu để chấm được canh giữa

        for (int i = 0; i < colors.length; i++) {
            paint.setColor(colors[i]); // Đặt màu chấm hiện tại
            canvas.drawCircle(startX + i * radius * 3, bottom + radius, radius, paint);
        }

        paint.setColor(originalColor); // Khôi phục lại màu gốc của Paint sau khi vẽ xong
    }
}

