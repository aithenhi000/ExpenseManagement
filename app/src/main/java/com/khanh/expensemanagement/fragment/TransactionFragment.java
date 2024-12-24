package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.ViewPaperFormAdapter;

import java.util.Arrays;
import java.util.List;

public class TransactionFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_input_form_container, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.vpTransaction);
        ViewPaperFormAdapter adapter = new ViewPaperFormAdapter(this);
        viewPager.setAdapter(adapter);
        List<String> tabTitles = Arrays.asList("Chi tiêu", "Thu nhập");
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Kiểm tra vị trí để thiết lập tiêu đề tab từ danh sách
                    if (position < tabTitles.size()) {
                        tab.setText(tabTitles.get(position));
                    }
                }).attach();


        return view;
    }

}
