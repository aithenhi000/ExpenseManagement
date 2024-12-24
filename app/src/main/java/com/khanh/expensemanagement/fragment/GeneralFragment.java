package com.khanh.expensemanagement.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.adapter.MonthFragmentAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralFragment extends Fragment {


    private int currentPosition;
    private String mParam1;
    private String mParam2;
    private ViewPager2 viewPager2;
    private MonthFragmentAdapter adapter;
    private TextView txtStatusMonth;
    private ImageView btnpreMonth, btnnextMonth;

    public GeneralFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        viewPager2 = view.findViewById(R.id.viewpager2);
        btnpreMonth = view.findViewById(R.id.premonth);
        btnnextMonth = view.findViewById(R.id.nextmonth);
        txtStatusMonth = view.findViewById(R.id.txtStatus);
        CalendarDay day = CalendarDay.today();

        adapter = new MonthFragmentAdapter(requireActivity(), 12);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(day.getMonth(), false);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                txtStatusMonth.setText("Tổng quan tháng: " + (currentPosition + 1));
            }
        });
        currentPosition = viewPager2.getCurrentItem();


        Log.d("TAG", "onCreateView: " + currentPosition);
        btnpreMonth.setOnClickListener(v -> {
            if (currentPosition > 0) { // Đảm bảo không lùi quá trang đầu tiên
                currentPosition--;
                viewPager2.setCurrentItem(currentPosition, true);
                txtStatusMonth.setText("Tổng quan tháng: " + (currentPosition + 1));

            }
        });

// Xử lý sự kiện cho nút "Tháng sau"
        btnnextMonth.setOnClickListener(v -> {
            if (currentPosition < viewPager2.getAdapter().getItemCount() - 1) { // Đảm bảo không vượt quá số trang
                currentPosition++;
                viewPager2.setCurrentItem(currentPosition, true);
                txtStatusMonth.setText("Tổng quan tháng: " + (currentPosition + 1));
            }
        });

        txtStatusMonth.setText("Tổng quan tháng: " + (currentPosition + 1));

        return view;
    }

}