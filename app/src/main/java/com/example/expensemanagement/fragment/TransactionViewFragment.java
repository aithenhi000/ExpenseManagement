package com.example.expensemanagement.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.expensemanagement.R;
import com.example.expensemanagement.adapter.TransactionAdapter;
import com.example.expensemanagement.databases.DatabaseHelper;
import com.example.expensemanagement.model.TransactionSummary;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DATE="date";
    // TODO: Rename and change types of parameters

    private CalendarDay date;
    private RecyclerView rvTransaction;
    private TransactionAdapter transactionAdapter;
    private DatabaseHelper dbHelper;
    private List<TransactionSummary> transactionSummary;

    public TransactionViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param date Parameter 1.
     * @return A new instance of fragment TransactionViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionViewFragment newInstance(CalendarDay date) {
        TransactionViewFragment fragment = new TransactionViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            date = getArguments().getParcelable(ARG_DATE);
        }
        dbHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_view, container, false);

        rvTransaction = view.findViewById(R.id.rvTransaction);
        loadTransactionData(date);
        // Inflate the layout for this fragment
        return view;
    }

    private void loadTransactionData(CalendarDay date) {
        transactionSummary = dbHelper.getTransactionsForMonth(date.getMonth()+1,date.getYear());
        transactionAdapter = new TransactionAdapter(requireContext(), transactionSummary);
        rvTransaction.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvTransaction.setAdapter(transactionAdapter);
    }

    public void updateData(CalendarDay date) {
        this.date=date;
        loadTransactionData(date);
    }
}