package com.example.budgetbook.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnFxdDaoImpl extends SQLiteOpenHelper implements UnFxdDao {


    private static final String TAG = "UnFxdDaoImpl";
    private static final String UNFIXED = "UNFIXED";
    private static final String DATABASE_NAME = "BUDGET_BOOK";
    Constants constants = new Constants();

    //SQLiteDatabase db;
    public UnFxdDaoImpl(Context context) {

        super(context, UNFIXED, null, 3);

    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createFixedTable = "CREATE TABLE " + constants.UNFXD_TABLE_NAME + " (" + constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + constants.UNFXD_COMP_NAME + " varchar(20)," + constants.UNFXD_COMP_AMT + " float not null," + constants.UNFXD_COMMENT + " varchar(100) DEFAULT null," + constants.UNFXD_PAY_MODE + " varchar(50) not null," + constants.PAY_DATE + " Date not null)";
        db.execSQL(createFixedTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + constants.UNFXD_TABLE_NAME);
        onCreate(db);

    }

    @Override
    public long insertUnfxdCompinDB(FixedVO data) {
        //String Insert_Fxd_Record = "INERT INTO "+ constants.FXD_TABLE_NAME +"( "+ constants.FXD_COMP_NAME +" , "+ constants.FXD_COMP_AMT +" , "+ constants.INCOME+" ) VALUES ( "+ component + " , " +fAmt + " , "+ income + " )";
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(constants.UNFXD_COMP_NAME, data.getComponent());
            contentValues.put(constants.UNFXD_COMP_AMT, data.getUnfxd_amt());
            contentValues.put(constants.UNFXD_COMMENT, data.getComment());
            contentValues.put(constants.UNFXD_PAY_MODE, data.getPayMode());

            contentValues.put(constants.PAY_DATE, formatDate(data.getPayDate(), "dd-MM-yyyy", "yyyy-MM-dd"));

            Log.d(TAG, "inserting data to " + constants.UNFXD_TABLE_NAME + "Data is :: " + data.toString());

            result = db.insert(constants.UNFXD_TABLE_NAME, null, contentValues);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    public ArrayList<FixedVO> getUnfxdTableData() {
        String selectQuery = "SELECT * FROM " + constants.UNFXD_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            while (cur.moveToNext()) {
                FixedVO fx = new FixedVO();
                fx.setID(cur.getInt(cur.getColumnIndex(constants.ID)));
                fx.setComponent(cur.getString(cur.getColumnIndex(constants.UNFXD_COMP_NAME)));
                fx.setUnfxd_amt(cur.getFloat(cur.getColumnIndex(constants.UNFXD_COMP_AMT)));
                fx.setComment(cur.getString(cur.getColumnIndex(constants.UNFXD_COMMENT)));
                fx.setPayMode(cur.getString(cur.getColumnIndex(constants.UNFXD_PAY_MODE)));
                fx.setPayDate(cur.getString(cur.getColumnIndex(constants.PAY_DATE)));
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
    public long deleteUnfxd(int name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            result = db.delete(constants.UNFXD_TABLE_NAME, constants.ID + "=" + name, null);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }


    public ArrayList<FixedVO> getUnFxdTableFilterData(FilterVO data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String where = null;
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            data.setTo(formatDate(data.getTo(), "dd-MM-yyyy", "yyyy-MM-dd"));
            data.setFrom(formatDate(data.getFrom(), "dd-MM-yyyy", "yyyy-MM-dd"));
            String[] colms = {constants.ID, constants.UNFXD_COMP_NAME, constants.UNFXD_COMP_AMT, constants.UNFXD_COMMENT, constants.UNFXD_PAY_MODE, constants.PAY_DATE};
            if ((data.getFreq() != null && !data.getFreq().equals("Select")) && (data.getMode() != null && !data.getMode().equals("Select")) && (data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.UNFXD_PAY_MODE + "='" + data.getMode() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFreq() != null && !data.getFreq().equals("Select")) && (data.getMode() != null && !data.getMode().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.UNFXD_PAY_MODE + "='" + data.getMode() + "'";
            } else if ((data.getMode() != null && !data.getMode().equals("Select")) && (data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.UNFXD_PAY_MODE + "='" + data.getMode() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty()) && (data.getFreq() != null && !data.getFreq().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getMode() != null && !data.getMode().equals("Select"))) {
                where = constants.UNFXD_PAY_MODE + "='" + data.getMode() + "'";
            } else if ((data.getFreq() != null && !data.getFreq().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "'";
            }
            Cursor cur = db.query(constants.UNFXD_TABLE_NAME, colms, where, null, null, null, null);
            while (cur.moveToNext()) {
                FixedVO fx = new FixedVO();
                fx.setID(cur.getInt(cur.getColumnIndex(constants.ID)));
                fx.setComponent(cur.getString(cur.getColumnIndex(constants.UNFXD_COMP_NAME)));
                fx.setUnfxd_amt(cur.getFloat(cur.getColumnIndex(constants.UNFXD_COMP_AMT)));
                fx.setComment(cur.getString(cur.getColumnIndex(constants.UNFXD_COMMENT)));
                fx.setPayMode(cur.getString(cur.getColumnIndex(constants.UNFXD_PAY_MODE)));
                fx.setPayDate(cur.getString(cur.getColumnIndex(constants.PAY_DATE)));
                fx_list.add(fx);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return fx_list;
    }
}
