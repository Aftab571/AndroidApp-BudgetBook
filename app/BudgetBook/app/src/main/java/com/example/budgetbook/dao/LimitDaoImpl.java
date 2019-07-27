package com.example.budgetbook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.model.FixedVO;

import java.util.ArrayList;

public class LimitDaoImpl extends SQLiteOpenHelper implements LimitDao {

    private static final String TAG = "LimitDaoImpl";
    private static final String LIMIT = "LIMIT";
    private static final String DATABASE_NAME = "BUDGET_BOOK";
    Constants constants = new Constants();

    //SQLiteDatabase db;
    public LimitDaoImpl(Context context) {

        super(context, LIMIT, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createFixedTable = "CREATE TABLE " + constants.LIM_TABLE_NAME + " (" + constants.LIM_COMP_NAME + " varchar(20) PRIMARY KEY," + constants.LIM_COMP_AMT + " float not null)";
        db.execSQL(createFixedTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + constants.LIM_TABLE_NAME);
        onCreate(db);

    }

    @Override
    public long insertLimCompinDB(String comp, float fAmt) {
        //String Insert_Fxd_Record = "INERT INTO "+ constants.FXD_TABLE_NAME +"( "+ constants.FXD_COMP_NAME +" , "+ constants.FXD_COMP_AMT +" , "+ constants.INCOME+" ) VALUES ( "+ component + " , " +fAmt + " , "+ income + " )";

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = 0;
        try {
            contentValues.put(constants.LIM_COMP_NAME, comp);
            contentValues.put(constants.LIM_COMP_AMT, fAmt);
            Log.d(TAG, "inserting data to " + constants.LIM_TABLE_NAME + "Data is :: Fixed Amount : " + fAmt + ", Component is : " + comp);

            result = db.insert(constants.LIM_TABLE_NAME, null, contentValues);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }

    public ArrayList<FixedVO> getLimTableData() {
        String selectQuery = "SELECT * FROM " + constants.LIM_TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            while (cur.moveToNext()) {
                FixedVO fx = new FixedVO();
                fx.setComponent(cur.getString(cur.getColumnIndex(constants.LIM_COMP_NAME)));
                fx.setLim_amt(cur.getFloat(cur.getColumnIndex(constants.LIM_COMP_AMT)));
                fx_list.add(fx);
            }
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return fx_list;
    }

    @Override
    public long deleteLim(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            result = db.delete(constants.LIM_TABLE_NAME, constants.LIM_COMP_NAME + "='" + name + "'", null);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }
}
