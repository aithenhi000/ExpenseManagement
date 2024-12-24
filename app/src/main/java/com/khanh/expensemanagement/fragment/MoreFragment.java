package com.khanh.expensemanagement.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.backup.BackupHelper;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.SharedPreferencesManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_FILE = 101;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView tvBackup, tvdeleteTrans;
    private BackupHelper backupHelper;

    public MoreFragment() {
        // Required empty public constructor
    }

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        tvBackup = view.findViewById(R.id.backup);
        tvdeleteTrans = view.findViewById(R.id.deleteTrans);
        DatabaseHelper db = DatabaseHelper.getInstance(getContext());
        SharedPreferencesManager spf = SharedPreferencesManager.getInstance(getContext());

        tvdeleteTrans.setOnClickListener(v -> {
            if (!spf.isUserLoggedIn()) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Chưa đăng nhập")
                        .setMessage("Các giao dịch tạm thời (chưa có người dùng đăng nhập) sẽ được xóa. Bạn có chắc không?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            db.deleteTransactionsByUserId(0);
                        })
                        .setNegativeButton("Không", null)
                        .show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("Chưa đăng nhập")
                        .setMessage("Bạn có muốn xóa dữ liệu tất các giao dịch của tài khoản: " + spf.getUserName())
                        .setPositiveButton("Có", (dialog, which) -> {
                            db.deleteTransactionsByUserId(db.getUserByUsername(spf.getUserName()).getUserId());
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        backupHelper = new BackupHelper(getContext());
        tvBackup.setOnClickListener(v -> {
            backupHelper.backupDatabase();
        });


        return view;
    }
}