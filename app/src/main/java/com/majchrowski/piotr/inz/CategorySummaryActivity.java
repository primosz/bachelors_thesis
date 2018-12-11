package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CategorySummaryActivity extends AppCompatActivity {
    Toolbar toolBar;
    TextView  tvmonth,  tvNoData;
    DatabaseHelper myHelper;
    static int current =0;
    PieChart pieChart;
    final int[] colors={Color.YELLOW, Color.DKGRAY, Color.GREEN, Color.RED, Color.MAGENTA, Color.LTGRAY, Color.CYAN, Color.BLACK};
    Calendar cal;
    SimpleDateFormat month_date;
    double incomeValue;
    double outcomeValue;
    List<PieEntry> entries;
    PieDataSet set;
    PieData data;
    static int monthnum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_summary);

        tvmonth = findViewById(R.id.tvMonth);
        pieChart = findViewById(R.id.pieChart);
        tvNoData = findViewById(R.id.tvNoDataCat);
        month_date = new SimpleDateFormat("MM-yyyy", new Locale("pl"));
        cal=Calendar.getInstance();
        tvNoData.setVisibility(View.GONE);

        myHelper = new DatabaseHelper(this);
        myHelper.open();


        toolBar = (Toolbar) findViewById(R.id.toolbarCategorySummary);
        toolBar.setTitle(R.string.category_summary);
        setSupportActionBar(toolBar);

        DrawerUtil.getDrawer(this,toolBar);

        showData(current);
    }

    private void showData(int current) {

        ArrayList<CategorySum> mArrayList = new ArrayList<>();
        Cursor mCursor = myHelper.getSumOfCategories(current);
        Cursor oCursor = myHelper.getSumIncomeOfMonth(current);
        if (oCursor.moveToFirst())
        {
            tvmonth.setText(oCursor.getString(1)+"-"+oCursor.getString(2));
        }

        oCursor.close();
        if (mCursor.getCount()==0) tvNoData.setVisibility(View.VISIBLE);
        else tvNoData.setVisibility(View.GONE);

        if (!(mCursor==null && mCursor.getCount()==0)) {
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
                // The Cursor is now set to the right position
                mArrayList.add(new CategorySum(mCursor.getString(0), mCursor.getDouble(1), mCursor.getString(2)));
            }
            entries = new ArrayList<>();
            for(CategorySum x : mArrayList) {
                entries.add(new PieEntry(-1*(float) x.value, x.category));
            }
            set = new PieDataSet(entries, "");
            set.setColors(colors);
            set.setValueTextSize(14);
            set.setValueTextColor(Color.GRAY);
            data = new PieData(set);
            Legend legend = pieChart.getLegend();
            legend.setFormSize(26);
            legend.setTextSize(22);

            legend.setWordWrapEnabled(true);


            pieChart.setDescription(null);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setHoleRadius(30);
            pieChart.setNoDataText(getString(R.string.no_data_forthismonth));
            pieChart.setEntryLabelTextSize(0);
            pieChart.setEntryLabelColor(Color.GRAY);
            pieChart.setData(data);
            pieChart.animateY(1000);
            pieChart.invalidate(); // refresh
            pieChart.setVisibility(View.VISIBLE);
            pieChart.setFitsSystemWindows(true);

        }

    }


    public void prevMonthCat(View view) {
        current-=1;
        showData(current);
    }

    public void nextMonthCat(View view) {
        current+=1;
        showData(current);
    }

    class CategorySum{
        public String category;
        public double value;
        public String month;
        CategorySum(String category, double value, String month){
            this.category = category;
            this.value = value;
            this.month = month;

        }
    }
}
