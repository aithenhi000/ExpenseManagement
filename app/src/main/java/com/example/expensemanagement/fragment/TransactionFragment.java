package com.example.expensemanagement.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.expensemanagement.R;
import com.example.expensemanagement.adapter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TransactionFragment extends Fragment {
    @Override
    //Layout Inflater chuyển đổi (inflate) 1 tài nguyên XML thành 1 View
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.vpTransaction);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
