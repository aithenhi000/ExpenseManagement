package com.khanh.expensemanagement.backup;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackupHelper {
    private static final String TAG = "BackupHelper";
    private final Context context;

    public BackupHelper(Context context) {
        this.context = context;
    }

    public void backupDatabase() {
        File dbFile = context.getDatabasePath("Transaction.db"); // Đổi tên database của bạn
        File backupDir = context.getExternalFilesDir("Backup");

        if (backupDir != null && !backupDir.exists()) {
            backupDir.mkdirs();
        }

        File backupFile = new File(backupDir, "backup_" + System.currentTimeMillis() + ".db");

        try (FileInputStream fis = new FileInputStream(dbFile);
             FileOutputStream fos = new FileOutputStream(backupFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

            Log.d(TAG, "Backup successful: " + backupFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Backup failed: " + e.getMessage(), e);
        }
    }
}
