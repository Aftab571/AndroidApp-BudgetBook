package com.example.budgetbook;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.FilterServiceImpl;
import com.example.budgetbook.service.FixedCompService;
import com.example.budgetbook.service.FixedCompServiceImpl;

import com.example.budgetbook.utility.CommonUtility;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfImage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterActivity extends AppCompatActivity implements Serializable {
    FileProvider fileProvider = new FileProvider();
    private static final String TAG = "FilterActivity";
    private Context mContext;
    private Activity mActivity;
    private String src;
    private TextView mDisplayDate_to;
    private DatePickerDialog.OnDateSetListener mDateListener_to;
    private ImageButton mDatePicker_to;

    private TextView mDisplayDate_from;
    private DatePickerDialog.OnDateSetListener mDateListener_from;
    private ImageButton mDatePicker_from;
    public CommonUtility utility = new CommonUtility();
    public ArrayList<FixedVO> searchList;
    public Constants constants = new Constants();
    private Spinner mPayFreq_fil;
    private Spinner mPayMode_fil;
    private ImageButton mVisual;
    private TextView mFreqText;
    public TableLayout stk;
    private ImageButton pdfD;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mContext = getApplicationContext();

        mActivity = FilterActivity.this;
        mFreqText = findViewById(R.id.text_freq);
        Intent i = getIntent();
        src = i.getStringExtra("src");
        mPayFreq_fil = findViewById(R.id.filter_freq);
        mPayMode_fil = findViewById(R.id.filter_payMode);
        mVisual = findViewById(R.id.clckVisual);
        if (src.equals("UNFXD")) {
            mFreqText.setVisibility(View.INVISIBLE);
            mPayFreq_fil.setVisibility(View.INVISIBLE);
        }
        mVisual.setVisibility(View.INVISIBLE);
        mDisplayDate_from = findViewById(R.id.From_date);
        mDatePicker_from = findViewById(R.id.from_date);
        mDatePicker_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        FilterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener_from,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateListener_from = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "date is : " + dayOfMonth + "/" + (month + 1) + "/" + year);
                mDisplayDate_from.setText(dayOfMonth + "-" + "0" + (month + 1) + "-" + year);
            }
        };


        mDisplayDate_to = findViewById(R.id.To_date);
        mDatePicker_to = findViewById(R.id.to_date);
        mDatePicker_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        FilterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener_to,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });


        mDateListener_to = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "date is : " + dayOfMonth + "/" + (month + 1) + "/" + year);
                mDisplayDate_to.setText(dayOfMonth + "-" + "0" + (month + 1) + "-" + year);
            }
        };
        stk = findViewById(R.id.fxd_table);

        pdfD = findViewById(R.id.pdfBtn);
        pdfD.setVisibility(View.INVISIBLE);
        pdfD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void filterData(View v) {
        String frm = mDisplayDate_from.getText().toString();
        String to = mDisplayDate_to.getText().toString();
        String freq = mPayFreq_fil.getSelectedItem().toString();
        String mode = mPayMode_fil.getSelectedItem().toString();
        if ((frm != null && !frm.isEmpty() && to != null && !to.isEmpty())) {

            if ((frm != null && !frm.isEmpty() && to != null && !to.isEmpty()) || (freq != null && !freq.equals("Select")) || (mode != null && !mode.equals("Select"))) {
                String regex = constants.date_reg;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher1 = pattern.matcher(frm);
                Matcher matcher2 = pattern.matcher(to);
                if (matcher1.matches() && matcher2.matches()) {
                    if (utility.isDateValid(frm) && utility.isDateValid(to)) {
                        FilterVO fil = new FilterVO();
                        fil.setFreq(freq);
                        fil.setFrom(frm);
                        fil.setMode(mode);
                        fil.setTo(to);
                        FilterServiceImpl fs = new FilterServiceImpl();
                        searchList = fs.searchData(fil, src, mContext);
                        if (!searchList.isEmpty()) {
                            stk.removeAllViews();
                            createTable(true, null, searchList);
                            mVisual.setVisibility(View.VISIBLE);
                            pdfD.setVisibility(View.VISIBLE);
                        } else {
                            stk.removeAllViews();
                            Toast toast = Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();


                        }
                    } else {
                        Toast toast = Toast.makeText(this, "Date Does not exist in the Calendar", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, -690);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -690);
                    toast.show();
                }
            } else {
                stk.removeAllViews();
                Toast toast = Toast.makeText(this, "Please select atleast one Filter Criteria", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }


        } else {
            stk.removeAllViews();
            Toast toast = Toast.makeText(this, "Please enter Mandatory fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, -690);
            toast.show();
        }

    }

    public void createTable(boolean isAdd, Button next, ArrayList<FixedVO> dataList) {

        FilterServiceImpl srv = new FilterServiceImpl();
        srv.createTable(mContext, stk, isAdd, next, dataList, src);
    }

    public void visualizeData(View v) {
        if (src.equals("FIXED")) {
            Intent i = new Intent(FilterActivity.this, Visualize_Fixed.class);
            i.putExtra("fxdList", searchList);
            i.putExtra("todate", mDisplayDate_to.getText().toString());
            i.putExtra("fromdate", mDisplayDate_from.getText().toString());
            startActivity(i);
        } else {
            Intent i = new Intent(FilterActivity.this, Visualize_Unfxd.class);
            i.putExtra("unfxdList", searchList);
            i.putExtra("todate", mDisplayDate_to.getText().toString());
            i.putExtra("fromdate", mDisplayDate_from.getText().toString());
            startActivity(i);
        }

    }

    public void cancelNav(View v) {
        if (src.equals("FIXED")) {
            Intent i = new Intent(FilterActivity.this, Visualize_Fixed.class);
            startActivity(i);
        } else {
            Intent i = new Intent(FilterActivity.this, Visualize_Unfxd.class);
            startActivity(i);
        }
    }


    public void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {


        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents/budgetbook");

        try {
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i(TAG, "Created a new directory for PDF");
            }

            pdfFile = new File(docsFolder.getAbsolutePath(), "budget.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();
            //String imageFile = "../res/drawable/budgetbook.png";

            //  Image image = Image.getInstance(imageFile);

            // document.add(image);

            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 20);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
//            Image image = Image.getInstance("./java/budgetbook.png");
//            PdfGState gs1 = new PdfGState();
//            gs1.setFillOpacity(0.5f);
//           document.add(image);
            // transparency
            Phrase ph = selector.process("Budget Book");
            Phrase phq = selector.process("Summary of Income, Fixed & Unfixed expenditure");
            Paragraph p1 = new Paragraph(ph);
            p1.setAlignment(Element.ALIGN_CENTER);
            Paragraph p2 = new Paragraph(phq);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);
            document.add(new Paragraph("\n"));
            document.add(p2);
            document.add(new Paragraph("\n"));
            document.add(createPdfTable1());
            if (src.equals("FIXED")) {
                document.add(new Paragraph("\n"));
                document.add(createPdfTable2());
            }
            if (src.equals("UNFXD")) {
                document.add(new Paragraph("\n"));
                document.add(createPdfTable3());
            }
            document.close();
            previewPdf();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void previewPdf() {

        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Uri uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".FilterActivity", pdfFile);
            ///Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        } else {
            Toast.makeText(this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }

    public PdfPTable createPdfTable2() {

        PdfPTable table = new PdfPTable(6);
        PdfPCell cell;
        try {
            FixedCompDaoHandler dao = new DataBaseHandlerImpl(mContext);
//            Calendar cal = Calendar.getInstance();
//            int month = cal.get(Calendar.MONTH);
//            int year = cal.get(Calendar.YEAR);
//            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
//            Date today = new Date();
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(today);
//
//            calendar.add(Calendar.MONTH, 1);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            calendar.add(Calendar.DATE, -1);
//            Date lastDayOfMonth = calendar.getTime();
//            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            String end_date = sdf.format(lastDayOfMonth);
            String start_date = mDisplayDate_from.getText().toString();
            String end_date = mDisplayDate_to.getText().toString();
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            ArrayList<FixedVO> fxdData = ((DataBaseHandlerImpl) dao).getFxdTableFilterData(vo);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Fixed Expenditure Summary : " + start_date + " - " + end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(6);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{25, 80, 80, 72, 72, 80});
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("COMPONENT");
            table.addCell("EXPENDITURE");
            table.addCell("PAYMENT DATE");
            table.addCell("PAY MODE");
            table.addCell("FREQUENCY");
            int counter = 0;
            for (FixedVO data : fxdData) {
                counter++;
                table.addCell("" + counter);
                table.addCell(data.getComponent());
                table.addCell(Float.toString(data.getFxd_amt()));
                table.addCell(data.getPayDate());
                table.addCell(data.getPayMode());
                table.addCell(data.getFreq());
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return table;
    }

    public PdfPTable createPdfTable1() {

        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        try {
            FixedCompDaoHandler dao = new DataBaseHandlerImpl(mContext);
//            Calendar cal = Calendar.getInstance();
//            int month = cal.get(Calendar.MONTH);
//            int year = cal.get(Calendar.YEAR);
//            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
//            Date today = new Date();
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(today);
//
//            calendar.add(Calendar.MONTH, 1);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            calendar.add(Calendar.DATE, -1);
//            Date lastDayOfMonth = calendar.getTime();
//            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            String end_date = sdf.format(lastDayOfMonth);
            String start_date = mDisplayDate_from.getText().toString();
            String end_date = mDisplayDate_to.getText().toString();
            ArrayList<FixedVO> income = ((DataBaseHandlerImpl) dao).getIncomeFilter(mContext, start_date, end_date);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Income Summary : " + start_date + " - " + end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(3);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{30, 80, 80});
            cell.setColspan(3);
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("INCOME");
            table.addCell("DATE");
            int counter = 0;
            for (FixedVO data : income) {
                counter++;
                table.addCell("" + counter);
                table.addCell(Float.toString(data.getIncome()));
                table.addCell(data.getiDate());
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        return table;
    }

    public PdfPTable createPdfTable3() {

        PdfPTable table = new PdfPTable(6);
        PdfPCell cell;
        try {
            UnFxdDao dao = new UnFxdDaoImpl(mContext);
//            Calendar cal = Calendar.getInstance();
//            int month = cal.get(Calendar.MONTH);
//            int year = cal.get(Calendar.YEAR);
//            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
//            Date today = new Date();
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(today);
//
//            calendar.add(Calendar.MONTH, 1);
//            calendar.set(Calendar.DAY_OF_MONTH, 1);
//            calendar.add(Calendar.DATE, -1);
//            Date lastDayOfMonth = calendar.getTime();
//            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            String end_date = sdf.format(lastDayOfMonth);
            String start_date = mDisplayDate_from.getText().toString();
            String end_date = mDisplayDate_to.getText().toString();
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            ArrayList<FixedVO> unfxdData = ((UnFxdDaoImpl) dao).getUnFxdTableFilterData(vo);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Un Fixed Expenditure Summary : " + start_date + " - " + end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(6);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{25, 80, 80, 72, 72, 80});

            cell.setColspan(6);
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("COMPONENT");
            table.addCell("EXPENDITURE");
            table.addCell("PAYMENT DATE");
            table.addCell("PAY MODE");
            table.addCell("COMMENT");
            int counter = 0;
            for (FixedVO data : unfxdData) {
                counter++;
                table.addCell("" + counter);
                table.addCell(data.getComponent());
                table.addCell(Float.toString(data.getUnfxd_amt()));
                table.addCell(data.getPayDate());
                table.addCell(data.getPayMode());
                table.addCell(data.getComment());
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return table;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}

