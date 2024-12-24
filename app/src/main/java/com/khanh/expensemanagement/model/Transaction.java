package com.khanh.expensemanagement.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Transaction implements Parcelable {
    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    private int id;
    private Long amount;
    private int user_id;
    private int category_id;
    private String note;
    private String date;


    public Transaction(Long amount, int userId, int categoryId, String date, String note) {
        super();
        this.amount = amount;
        this.user_id = userId;
        this.category_id = categoryId;
        this.note = note;
        this.date = date;
    }

    public Transaction(int id, int user_id, Long amount, int category_id, String date, String note) {
        this.amount = amount;
        this.category_id = category_id;
        this.date = date;
        this.id = id;
        this.note = note;
        this.user_id = user_id;
    }

    protected Transaction(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readLong();
        }
        user_id = in.readInt();
        category_id = in.readInt();
        note = in.readString();
        date = in.readString();
    }

    public int getUser_id() {
        return user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public String getNote() {
        return note;
    }

    public String getDate() {
        return date;
    }

    public int getCategory_id() {
        return category_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(amount);
        }
        parcel.writeInt(user_id);
        parcel.writeInt(category_id);
        parcel.writeString(note);
        parcel.writeString(date);
    }
}
