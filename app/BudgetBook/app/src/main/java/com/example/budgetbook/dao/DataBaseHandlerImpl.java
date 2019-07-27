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

public class DataBaseHandlerImpl extends SQLiteOpenHelper implements FixedCompDaoHandler {

    private static final String TAG = "DataBaseHandlerImpl";
    private static final String FIXED_EXP_TABLE = "FIXED_EXP_TABLE";
    private static final String DATABASE_NAME = "BUDGET_BOOK";
    private static final String PIN_TABLE = "PIN_TAB";
    Constants constants = new Constants();

    //SQLiteDatabase db;
    public DataBaseHandlerImpl(Context context) {

        super(context, FIXED_EXP_TABLE, null, 10);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createIncTable = "CREATE TABLE " + constants.INC_TABLE_NAME + "(" + constants.INCOME + " float not null," + constants.IDATE + " Date PRIMARY KEY)";
        String createFixedTable = "CREATE TABLE " + constants.FXD_TABLE_NAME + "(" + constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + constants.FXD_COMP_NAME + " varchar(20) not null," + constants.FXD_COMP_AMT + " float not null," + constants.PAY_MODE + " varchar(20) not null," + constants.PAY_FREQ + " varchar(20) not null," + constants.PAY_DATE + " Date not null)";
        db.execSQL(createIncTable);
        db.execSQL(createFixedTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + constants.INC_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + constants.FXD_TABLE_NAME);
        onCreate(db);

    }

    @Override
    public long insertFxdCompinDB(FixedVO data) {
        //String Insert_Fxd_Record = "INERT INTO "+ constants.FXD_TABLE_NAME +"( "+ constants.FXD_COMP_NAME +" , "+ constants.FXD_COMP_AMT +" , "+ constants.INCOME+" ) VALUES ( "+ component + " , " +fAmt + " , "+ income + " )";
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(constants.FXD_COMP_NAME, data.getComponent());
            contentValues.put(constants.FXD_COMP_AMT, data.getFxd_amt());
            contentValues.put(constants.PAY_FREQ, data.getFreq());
            contentValues.put(constants.PAY_DATE, formatDate(data.getPayDate(), "dd-MM-yyyy", "yyyy-MM-dd"));
            contentValues.put(constants.PAY_MODE, data.getPayMode());


            Log.d(TAG, "inserting data to " + constants.FXD_TABLE_NAME + "Data is :: Fixed Amount : " + data.getFxd_amt() + ", Component is : " + data.getComponent());

            result = db.insert(constants.FXD_TABLE_NAME, null, contentValues);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<FixedVO> getFxdTableData() {
        String selectQuery = "SELECT * FROM " + constants.FXD_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            while (cur.moveToNext()) {
                FixedVO fx = new FixedVO();
                fx.setID(cur.getInt(cur.getColumnIndex(constants.ID)));
                fx.setComponent(cur.getString(cur.getColumnIndex(constants.FXD_COMP_NAME)));
                fx.setFxd_amt(cur.getInt(cur.getColumnIndex(constants.FXD_COMP_AMT)));
                fx.setPayDate(cur.getString(cur.getColumnIndex(constants.PAY_DATE)));
                fx.setFreq(cur.getString(cur.getColumnIndex(constants.PAY_FREQ)));
                fx.setPayMode(cur.getString(cur.getColumnIndex(constants.PAY_MODE)));
                //fx.setIncome(cur.getInt(cur.getColumnIndex(constants.INCOME)));
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
    public long deleteFxd(int name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            result = db.delete(constants.FXD_TABLE_NAME, constants.ID + "=" + name, null);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }

    @Override
    public long deleteInc(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        try {
            result = db.delete(constants.INC_TABLE_NAME, constants.IDATE + "='" + date + "'", null);
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }

    @Override
    public long addIncome(Context mContext, float income, String iDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = 0;
        try {
            contentValues.put(constants.INCOME, income);
            contentValues.put(constants.IDATE, formatDate(iDate, "dd-MM-yyyy", "yyyy-MM-dd"));

            Log.d(TAG, "inserting data to " + constants.INC_TABLE_NAME + "Data is :: Income : " + income);
            result = db.insert(constants.INC_TABLE_NAME, null, contentValues);
            //long result = db.update(PIN_TABLE,contentValues,constants.PIN+"="+pin,null);
            db.close();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return result;
    }

    @Override
    public ArrayList<FixedVO> getIncome(Context mContext) {
        String selectQuery = "SELECT * FROM " + constants.INC_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            while (cur.moveToNext()) {
                FixedVO vo = new FixedVO();
                vo.setIncome(cur.getFloat(cur.getColumnIndex(constants.INCOME)));
                vo.setiDate(cur.getString(cur.getColumnIndex(constants.IDATE)));
                fx_list.add(vo);

            }
        } catch (Exception e) {
            Log.e("error is: ", e.toString());
        } finally {
            db.close();
        }
        return fx_list;
    }

    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }


    public ArrayList<FixedVO> getIncomeFilter(Context mContext, String from_dt, String to_dt) {
        //String selectQuery = "SELECT * FROM "+ constants.INC_TABLE_NAME;
        String from = null;
        String to = null;
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            from = formatDate(from_dt, "dd-MM-yyyy", "yyyy-MM-dd");
            to = formatDate(to_dt, "dd-MM-yyyy", "yyyy-MM-dd");

            String where = constants.IDATE + ">=date('" + from + "') AND " + constants.IDATE + "<=date('" + to + "')";

            Cursor cur = db.query(constants.INC_TABLE_NAME, new String[]{constants.INCOME, constants.IDATE}, where, null, null, null, null, null);

            while (cur.moveToNext()) {
                FixedVO vo = new FixedVO();
                vo.setIncome(cur.getFloat(cur.getColumnIndex(constants.INCOME)));
                vo.setiDate(cur.getString(cur.getColumnIndex(constants.IDATE)));
                fx_list.add(vo);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return fx_list;
    }

    public ArrayList<FixedVO> getFxdTableFilterData(FilterVO data) {
        //String selectQuery = "SELECT * FROM "+ constants.FXD_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<FixedVO> fx_list = new ArrayList<>();
        try {
            data.setTo(formatDate(data.getTo(), "dd-MM-yyyy", "yyyy-MM-dd"));
            data.setFrom(formatDate(data.getFrom(), "dd-MM-yyyy", "yyyy-MM-dd"));

            String where = null;
            String[] colms = {constants.ID, constants.FXD_COMP_NAME, constants.FXD_COMP_AMT, constants.PAY_DATE, constants.PAY_MODE, constants.PAY_FREQ};
            if ((data.getFreq() != null && !data.getFreq().equals("Select")) && (data.getMode() != null && !data.getMode().equals("Select")) && (data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.PAY_MODE + "='" + data.getMode() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFreq() != null && !data.getFreq().equals("Select")) && (data.getMode() != null && !data.getMode().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.PAY_MODE + "='" + data.getMode() + "'";
            } else if ((data.getMode() != null && !data.getMode().equals("Select")) && (data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.PAY_MODE + "='" + data.getMode() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty()) && (data.getFreq() != null && !data.getFreq().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "' AND " + constants.PAY_DATE + ">=date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getFrom() != null && !data.getFrom().isEmpty()) && (data.getTo() != null && !data.getTo().isEmpty())) {
                where = constants.PAY_DATE + ">= date('" + data.getFrom() + "') AND " + constants.PAY_DATE + "<=date('" + data.getTo() + "')";
            } else if ((data.getMode() != null && !data.getMode().equals("Select"))) {
                where = constants.PAY_MODE + "='" + data.getMode() + "'";
            } else if ((data.getFreq() != null && !data.getFreq().equals("Select"))) {
                where = constants.PAY_FREQ + "='" + data.getFreq() + "'";
            }
            Cursor cur = db.query(constants.FXD_TABLE_NAME, colms, where, null, null, null, null);

            while (cur.moveToNext()) {
                FixedVO fx = new FixedVO();
                fx.setID(cur.getInt(cur.getColumnIndex(constants.ID)));
                fx.setComponent(cur.getString(cur.getColumnIndex(constants.FXD_COMP_NAME)));
                fx.setFxd_amt(cur.getInt(cur.getColumnIndex(constants.FXD_COMP_AMT)));
                fx.setPayDate(cur.getString(cur.getColumnIndex(constants.PAY_DATE)));
                fx.setFreq(cur.getString(cur.getColumnIndex(constants.PAY_FREQ)));
                fx.setPayMode(cur.getString(cur.getColumnIndex(constants.PAY_MODE)));
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
