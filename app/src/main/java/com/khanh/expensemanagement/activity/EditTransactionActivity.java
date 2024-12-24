package com.khanh.expensemanagement.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.khanh.expensemanagement.R;
import com.khanh.expensemanagement.databases.DatabaseHelper;
import com.khanh.expensemanagement.model.Transaction;
import com.khanh.expensemanagement.model.Utils;

import java.util.List;

public class EditTransactionActivity extends BaseActivity {
    ImageView exit;
    private EditText editTextAmount, tvDate, tvNote;
    private Button buttonSave, btnDelete;
    private DatabaseHelper dbHelper;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        buttonSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        displayTitle("Chỉnh sửa giao dịch");
        // Khởi tạo DatabaseHelper
        dbHelper = DatabaseHelper.getInstance(this);
        spinner = findViewById(R.id.spinner);
        // Lấy thông tin transaction từ Intent
        Intent intent = getIntent();
        Transaction transaction = intent.getParcelableExtra("TRANSACTION");

        // Khởi tạo Views
        editTextAmount = findViewById(R.id.editMoney);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.editNote);

        // Điền thông tin vào các EditText
        editTextAmount.setText(String.valueOf(transaction.getAmount()));
        tvDate.setText(Utils.convert_us_to_vn_date(transaction.getDate()));
        tvNote.setText(transaction.getNote());
        Utils.handleDatePickerDialog(tvDate, this);

        List<String> itemList = dbHelper.getNamesByCategoryType(transaction.getCategory_id());

        // Thiết lập Adapter cho Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final int[] categorySelect = {-1};
        // Xử lý sự kiện chọn item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = itemList.get(position);
                categorySelect[0] = dbHelper.getIdByCategoryName(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Khi không chọn gì cả
            }
        });


        buttonSave.setOnClickListener(v -> {
            dbHelper.updateTransaction(
                    transaction.getId(),
                    Long.parseLong(editTextAmount.getText().toString()),
                    categorySelect[0],
                    Utils.formatDate(tvDate.getText().toString()),
                    tvNote.getText().toString());
            updateDB();

        });


        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa giao dịch này không?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Gọi hàm xóa nếu người dùng chọn "Yes"
                dbHelper.deleteTransaction(transaction.getId());
                Toast.makeText(getApplicationContext(), "Đã xóa giao dịch", Toast.LENGTH_SHORT).show();
                updateDB();
            });

            // Nút "No" để hủy hành động
            builder.setNegativeButton("No", (dialog, which) -> {
                // Đóng dialog nếu người dùng chọn "No"
                dialog.dismiss();
            });
            builder.show();
        });

    }

    private void updateDB() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
