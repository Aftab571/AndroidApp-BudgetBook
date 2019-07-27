package com.example.budgetbook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.model.FixedVO;

public class PinDaoImpl extends SQLiteOpenHelper implements PinDao {
    Constants constants = new Constants();
    private static final String PIN_TABLE = "PIN_TAB";
    private static final String TAG = "PinDaoImpl";

    public PinDaoImpl(Context context) {
        super(context, PIN_TABLE, null, 2);
    }

    @Override
    public FixedVO getPin(Context context) {
        String selectQuery = "SELECT * FROM " + PIN_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        FixedVO fv = new FixedVO();
        try {
            Cursor cur = db.rawQuery(selectQuery, null);

            int pin = 0;
            float inc = 0f;
            while (cur.moveToNext()) {
                pin = cur.getInt(cur.getColumnIndex("PIN"));
                inc = cur.getFloat(cur.getColumnIndex("INCOME"));
                fv.setPin(pin);
                fv.setIncome(inc);
                break;
            }
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }

        return fv;
    }

    @Override
    public long insertPin(Context mcontext, int entered_pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = 0;
        try {
            contentValues.put(constants.PIN, entered_pin);
            Log.d(TAG, "inserting data to " + PIN_TABLE + "Data is :: entered_pin is : " + entered_pin);

            result = db.insert(PIN_TABLE, null, contentValues);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }

    @Override
    public long deletePin(Context mcontext, int entered_pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            result = db.delete(PIN_TABLE, constants.PIN + "=" + entered_pin, null);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }

        return result;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPinTable = "CREATE TABLE " + PIN_TABLE + "(" + constants.PIN + " int PRIMARY KEY," + constants.INCOME + " float)";
        db.execSQL(createPinTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PIN_TABLE);
        onCreate(db);
    }
}
