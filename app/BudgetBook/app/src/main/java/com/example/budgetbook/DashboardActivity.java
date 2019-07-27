package com.example.budgetbook;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.LimitDao;
import com.example.budgetbook.dao.LimitDaoImpl;
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.LimitService;
import com.example.budgetbook.service.LimitServiceImpl;
import com.example.budgetbook.service.UnFxdService;
import com.example.budgetbook.service.UnFxdServiceImpl;
import com.example.budgetbook.utility.CommonUtility;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    FileProvider fileProvider = new FileProvider();
    private int pin;
    private float income;
    private Context mContext;
    private static final String TAG = "DashboardActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    public CommonUtility utility = new CommonUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_dashboard);
       Intent i = getIntent();
       pin=i.getIntExtra("pin",0);
       income= i.getFloatExtra("income",0f);
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        setNotifications();

    }

    private void setNotifications (){

        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        int month =cal.get(Calendar.MONTH);
        int year =cal.get(Calendar.YEAR);
        String start_date = "01-"+(month<10?"0"+(month+1):(month+1))+"-"+year;
        Date today = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();

        String end_date = sdf.format(lastDayOfMonth);
        UnFxdDao undao = new UnFxdDaoImpl(mContext);
        FilterVO vo = new FilterVO();
        vo.setFrom(start_date);
        vo.setTo(end_date);
        ArrayList<FixedVO> undata=((UnFxdDaoImpl) undao).getUnFxdTableFilterData(vo);
        LimitDao lmdao = new LimitDaoImpl(mContext);
        ArrayList<FixedVO> lmList=lmdao.getLimTableData();
        float food_amt = 0f;
        float Super_amt = 0f;
        float Medical_amt = 0f;
        float Travel_amt = 0f;
        float Entertainment_amt = 0f;
        float Education_amt = 0f;
        float Shopping_amt = 0f;
        float Sports_amt = 0f;
        float Others_amt = 0f;


        for (FixedVO data : undata) {
            if (data.getComponent().equals("Food")) {
                food_amt = food_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Super market")) {
                Super_amt = Super_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Medical")) {
                Medical_amt = Medical_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Travel")) {
                Travel_amt = Travel_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Entertainment")) {
                Entertainment_amt = Entertainment_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Education")) {
                Education_amt = Education_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Shopping")) {
                Shopping_amt = Shopping_amt + data.getUnfxd_amt();
            } else if (data.getComponent().equals("Sports & Fitness")) {
                Sports_amt = Sports_amt + data.getUnfxd_amt();
            } else {
                Others_amt = Others_amt + data.getUnfxd_amt();
            }
        }

        StringBuilder sb75 = new StringBuilder().append("Your Unfixed expenditure for : ");
        StringBuilder sb100 = new StringBuilder().append("Your Unfixed Expenditure for : ");
        int counterFor75=0;
        int counterFor100=0;

        for (FixedVO data: lmList) {
            switch (data.getComponent()){
                case "Food":
                    if(food_amt>0.75*data.getLim_amt() && food_amt<=data.getLim_amt()){
                        sb75.append("Food,");
                        counterFor75++;
                    }
                    else if(food_amt>data.getLim_amt()){
                        sb100.append("Food,");
                        counterFor100++;
                    }
                    break;
                case "Super market":
                    if(Super_amt>0.75*data.getLim_amt() && Super_amt<=data.getLim_amt()){
                        sb75.append("Super Market,");
                        counterFor75++;
                    }
                    else if(Super_amt>data.getLim_amt()){
                        sb100.append("Super Market,");
                        counterFor100++;
                    }
                    break;
                case "Medical":
                    if(Medical_amt>0.75*data.getLim_amt() && Medical_amt<=data.getLim_amt()){
                        sb75.append("Medical,");
                        counterFor75++;
                    }
                    else if(Medical_amt>data.getLim_amt()){
                        sb100.append("Medical,");
                        counterFor100++;
                    }
                    break;
                case "Travel":
                    if(Travel_amt>0.75*data.getLim_amt() && Travel_amt<=data.getLim_amt()){
                        sb75.append("Travel,");
                        counterFor75++;
                    }
                    else if(Travel_amt>data.getLim_amt()){
                        sb100.append("Travel,");
                        counterFor100++;
                    }
                    break;
                case "Entertainment":
                    if(Entertainment_amt>0.75*data.getLim_amt() && Entertainment_amt<=data.getLim_amt()){
                        sb75.append("Entertainment,");
                        counterFor75++;
                    }
                    else if(Entertainment_amt>data.getLim_amt()){
                        sb100.append("Entertainment,");
                        counterFor100++;
                    }
                    break;
                case "Education":
                    if(Education_amt>0.75*data.getLim_amt() && Education_amt<=data.getLim_amt()){
                        sb75.append("Education,");
                        counterFor75++;
                    }
                    else if(Education_amt>data.getLim_amt()){
                        sb100.append("Education,");
                        counterFor100++;
                    }
                    break;
                case "Shopping":
                    if(Shopping_amt>0.75*data.getLim_amt() && Shopping_amt<=data.getLim_amt()){
                        sb75.append("Shopping,");
                        counterFor75++;
                    }
                    else if(Shopping_amt>data.getLim_amt()){
                        sb100.append("Shopping,");
                        counterFor100++;
                    }
                    break;
                case "Sports & Fitness":
                    if(Sports_amt>0.75*data.getLim_amt() && Sports_amt<=data.getLim_amt()){
                        sb75.append("Sports,");
                        counterFor75++;
                    }
                    else if(Sports_amt>data.getLim_amt()){
                        sb100.append("Sports,");
                        counterFor100++;
                    }
                    break;
                case "Others":
                    if(Others_amt>0.75*data.getLim_amt() && Others_amt<=data.getLim_amt()){
                        sb75.append("Others category,");
                        counterFor75++;
                    }
                    else if(Others_amt>data.getLim_amt()){
                        sb100.append("Others category,");
                        counterFor100++;
                    }
                    break;
            }
        }
        sb75.append(" has exceeded 75% of your set expenses limits for this month.");
        sb100.append(" has exceeded 100% of your set expenses limits for this month.");
        if(counterFor75>0) {
            Notification.Builder builder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                builder = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Alert")
                        .setStyle(new Notification.BigTextStyle().bigText(sb75))
                        .setContentText(sb75);
            }

            Intent notifIntent = new Intent(this, DashboardActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                String channelId = "_budget_book_alert1";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(
                            channelId,
                            "Budget Book",
                            manager.IMPORTANCE_DEFAULT);

                    manager.createNotificationChannel(channel);
                    builder.setChannelId(channelId);
                }

                manager.notify(0, builder.build());
            }
        }
        if(counterFor100>0) {
            Notification.Builder builder100 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                builder100 = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.appicon)
                        .setContentTitle("Warning")
                        .setStyle(new Notification.BigTextStyle().bigText(sb100))
                        .setContentText(sb100);;
            }


            Intent notifIntent = new Intent(this, DashboardActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder100.setContentIntent(contentIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                String channelId = "_budget_book_alert2";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Budget Book",
                        manager.IMPORTANCE_DEFAULT);

                    manager.createNotificationChannel(channel);
                    builder100.setChannelId(channelId);
                }

                manager.notify(1, builder100.build());
            }
        }
    }

    public void update_fxd(View v){
        Intent i = new Intent(DashboardActivity.this, FixedActivity.class);
        i.putExtra("fromDash","Y");
        startActivity(i);

    }
    public void update_lim(View v){
        Intent i = new Intent(DashboardActivity.this, LimitsActivity.class);
        i.putExtra("fromDash","Y");
        startActivity(i);

    }
    public void add_unfxd_exp(View v){
        Intent i = new Intent(DashboardActivity.this, UnfixedActivity.class);
        i.putExtra("fromDash","Y");
        startActivity(i);
    }

    public void visulaizeFxd(View v){
        Intent i = new Intent(DashboardActivity.this, Visualize_Fixed.class);
        startActivity(i);
    }
    public void visulaizeUnFxd(View v){
        Intent i = new Intent(DashboardActivity.this, Visualize_Unfxd.class);
        startActivity(i);
    }

    public void nav_income(View v){
        Intent i = new Intent(DashboardActivity.this, IncomeActivity.class);
        i.putExtra("fromDash","Y");
        startActivity(i);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {

        menu.clear();
        getMenuInflater().inflate(R.menu.activity_dashboardxyz, menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        try {
            switch (item.getItemId()) {
                case R.id.down_pdf:


                    this.createPdfWrapper(false);

                    return true;
                case R.id.email_pdf:
                    emailpdf();
                    return true;
                case R.id.exit_app:
                    exitApp();
                    return true;
                case R.id.reset_pin:
                    resetPin();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void resetPin(){
        Intent i = new Intent(DashboardActivity.this, ResetActivity.class);
        startActivity(i);
    }

    public void exitApp(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        }

        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
            System.exit(0);
        }
        else{
            finish();
            }
    }

    public void emailpdf (){
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents/budgetbook");
        pdfFile = new File(docsFolder.getAbsolutePath(),"budget.pdf");
        if(pdfFile==null){
            try {
                createPdfWrapper(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        else{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "" });
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "test " + "check");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "test");
            Uri uri= FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".FilterActivity", pdfFile);
            ///Uri uri = Uri.fromFile(pdfFile);
            //String uri = Environment.getExternalStorageDirectory() + "/Documents/budgetbook/budgetbook.pdf";
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri.toString()));
            startActivity(shareIntent);

        }

    }


    public void createPdfWrapper(boolean isEmail) throws FileNotFoundException,DocumentException{

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
        }else {
            createPdf(isEmail);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper(false);
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

    private void createPdf(boolean isEmail) throws FileNotFoundException, DocumentException {



        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents/budgetbook");

        try {
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i(TAG, "Created a new directory for PDF");
            }

            pdfFile = new File(docsFolder.getAbsolutePath(),"budget.pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            if(!isEmail) {
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
                document.add(new Paragraph("\n"));
                document.add(createPdfTable2());
                document.add(new Paragraph("\n"));
                document.add(createPdfTable3());
                document.close();
                previewPdf();
            }
            else{
                emailpdf();
            }
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

            Uri uri= FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".FilterActivity", pdfFile);
            ///Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");

            startActivity(intent);
        }else{
            Toast.makeText(this,"Download a PDF Viewer to see the generated PDF",Toast.LENGTH_SHORT).show();
        }
    }

    public PdfPTable createPdfTable2(){

        PdfPTable table = new PdfPTable(6);
        PdfPCell cell;
        try{
            FixedCompDaoHandler dao = new DataBaseHandlerImpl(mContext);
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            Date lastDayOfMonth = calendar.getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String end_date = sdf.format(lastDayOfMonth);
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            ArrayList<FixedVO> fxdData = ((DataBaseHandlerImpl) dao).getFxdTableFilterData(vo);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Fixed Expenditure Summary : "+start_date+" - "+ end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(6);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{ 25, 80,80,72,72,80 });
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("COMPONENT");
            table.addCell("EXPENDITURE");
            table.addCell("PAYMENT DATE");
            table.addCell("PAY MODE");
            table.addCell("FREQUENCY");
            int counter=0;
            for(FixedVO data : fxdData){
                counter++;
                table.addCell(""+counter);
                table.addCell(data.getComponent());
                table.addCell(Float.toString(data.getFxd_amt()));
                table.addCell(data.getPayDate());
                table.addCell(data.getPayMode());
                table.addCell(data.getFreq());
            }
            table.addCell("");
            table.addCell("");
            table.addCell("Total : "+Float.toString(utility.getTotal(fxdData,"FXD")));
            table.addCell("");
            table.addCell("");
            table.addCell("");

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }

        return table;
    }

    public PdfPTable createPdfTable1(){

        PdfPTable table = new PdfPTable(3);
        PdfPCell cell;
        try{
            FixedCompDaoHandler dao = new DataBaseHandlerImpl(mContext);
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            Date lastDayOfMonth = calendar.getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String end_date = sdf.format(lastDayOfMonth);
            ArrayList<FixedVO> income = ((DataBaseHandlerImpl) dao).getIncomeFilter(mContext,start_date,end_date);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Income Summary : "+start_date+" - "+ end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(3);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{ 30, 80,80 });
            cell.setColspan(3);
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("INCOME");
            table.addCell("DATE");
            int counter=0;
            for(FixedVO data : income){
                counter++;
                table.addCell(""+counter);
                table.addCell(Float.toString(data.getIncome()));
                table.addCell(data.getiDate());
            }

            table.addCell("");
            table.addCell("Total : "+Float.toString(utility.getTotal(income,"INC")));
            table.addCell("");

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }


        return table;
    }
    public PdfPTable createPdfTable3(){

        PdfPTable table = new PdfPTable(6);
        PdfPCell cell;
        try{
            UnFxdDao dao = new UnFxdDaoImpl(mContext);
            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            Date lastDayOfMonth = calendar.getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String end_date = sdf.format(lastDayOfMonth);
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            ArrayList<FixedVO> unfxdData = ((UnFxdDaoImpl) dao).getUnFxdTableFilterData(vo);
            FontSelector selector = new FontSelector();
            Font f2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
            f2.setColor(BaseColor.BLACK);
            selector.addFont(f2);
            Phrase ph = selector.process("Un Fixed Expenditure Summary : "+start_date+" - "+ end_date);
            cell = new PdfPCell(new Phrase(ph));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setColspan(6);
            table.setWidthPercentage(100);
            table.setTotalWidth(new float[]{ 25, 80,80,72,72,80 });

            cell.setColspan(6);
            table.addCell(cell);

            table.addCell("S.NO");
            table.addCell("COMPONENT");
            table.addCell("EXPENDITURE");
            table.addCell("PAYMENT DATE");
            table.addCell("PAY MODE");
            table.addCell("COMMENT");
            int counter=0;
            for(FixedVO data : unfxdData){
                counter++;
                table.addCell(""+counter);
                table.addCell(data.getComponent());
                table.addCell(Float.toString(data.getUnfxd_amt()));
                table.addCell(data.getPayDate());
                table.addCell(data.getPayMode());
                table.addCell(data.getComment());
            }
            table.addCell("");
            table.addCell("");
            table.addCell("Total : "+Float.toString(utility.getTotal(unfxdData,"UNFXD")));
            table.addCell("");
            table.addCell("");
            table.addCell("");

        }catch (Exception e){
            Log.e(TAG,e.toString());
        }

        return table;
    }

}
