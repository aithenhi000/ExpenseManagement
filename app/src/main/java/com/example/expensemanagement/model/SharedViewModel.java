package com.example.expensemanagement.model;

import androidx.lifecycle.ViewModel;

import com.example.expensemanagement.R;

import java.util.HashMap;
import java.util.Map;

public class SharedViewModel extends ViewModel {
    private final Map<String, Integer> drawableMap = new HashMap<>();

    public SharedViewModel() {
        drawableMap.put("ic_food", R.drawable.ic_food);
        drawableMap.put("ic_shopping", R.drawable.ic_shopping);
        drawableMap.put("ic_vehicle", R.drawable.ic_vehicle);
        drawableMap.put("ic_salary", R.drawable.ic_salary);
        drawableMap.put("ic_invest", R.drawable.ic_invest);
        drawableMap.put("ic_bank", R.drawable.ic_bank);
        // Thêm các icon khác vào đây
    }

    public Map<String, Integer> getDrawableMap() {
        return drawableMap;
    }
}

