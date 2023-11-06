package com.example.shovelheroapp.WorkOrder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class WODatabaseHelper extends SQLiteOpenHelper {

    public WODatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static long createWorkOrder(WorkOrder workOrder) {
        // Insert a new WorkOrder into the database
        return 0;
    }

    public static List<WorkOrder> getAllWorkOrders() {
        // Retrieve all WorkOrders from the database
        return null;
    }

    public int updateWorkOrder(WorkOrder workOrder) {
        // Update an existing WorkOrder in the database
        return 0;
    }

    public void deleteWorkOrder(long workOrderId) {
        // Delete a WorkOrder from the database
    }


}
