package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Summary extends AppCompatActivity {
    Toolbar toolBar;
    TextView tvIncomeValue, tvOutcomeValue, tvmonth, tvSum, tvNoData;
    DatabaseHelper myHelper;
    static int current =0;
    PieChart pieChart;
    final int[] colors={Color.GREEN, Color.RED};
    Calendar cal;
    SimpleDateFormat month_date;
    double incomeValue;
    double outcomeValue;
    List<PieEntry> entries;
    PieDataSet set;
    PieData data;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        tvIncomeValue = findViewById(R.id.tvIncomesValue);
        tvOutcomeValue = findViewById(R.id.tvOutcomesValue);
        tvSum = findViewById(R.id.tvSumValue);
        tvmonth = findViewById(R.id.tvMonth);
        pieChart = findViewById(R.id.pieChart);
        tvNoData = findViewById(R.id.tvNoData);
        month_date = new SimpleDateFormat("MMMM");
        cal=Calendar.getInstance();
        tvNoData.setVisibility(View.GONE);

        myHelper = new DatabaseHelper(this);
        myHelper.open();


        toolBar = (Toolbar) findViewById(R.id.toolbarSummary);
        toolBar.setTitle("Summary for recent months");
        setSupportActionBar(toolBar);

        DrawerUtil.getDrawer(this,toolBar);

        showData(current);
    }

    private void showData(int current) {
        incomeValue = getSumFromMonth(current, true);
        outcomeValue = getSumFromMonth(current, false);

            tvIncomeValue.setText(String.valueOf(incomeValue));
            tvOutcomeValue.setText(String.valueOf(outcomeValue));
            tvSum.setText(String.valueOf(incomeValue + outcomeValue));
        if(!(incomeValue==0 &&outcomeValue==0)) {

            entries = new ArrayList<>();
            entries.add(new PieEntry((float) incomeValue, "Income"));
            entries.add(new PieEntry(-1 * (float) outcomeValue, "Outcome"));
            set = new PieDataSet(entries, "Expenses");
            set.setColors(colors);
            set.setValueTextSize(18);
            set.setValueTextColor(Color.GRAY);

            data = new PieData(set);

            pieChart.setDescription(null);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setNoDataText("No data for this month");

            pieChart.setEntryLabelTextSize(18);
            pieChart.setEntryLabelColor(Color.GRAY);
            pieChart.setData(data);
            pieChart.animateY(1000);
            pieChart.invalidate(); // refresh
            pieChart.setVisibility(View.VISIBLE);
        }
        else
        {   pieChart.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }



    }

    private double getSumFromMonth(int current, boolean inOrOut) {

        myHelper = new DatabaseHelper(this);
        myHelper.open();

        double result = 12.3;

        Cursor c;

        if (inOrOut) {
            c = myHelper.getSumIncomeOfMonth(current);
        }//function to retrieve all values from a table- written in MyDb.java file
        else
        {
            c = myHelper.getSumOutcomeOfMonth(current);
        }
        if (c.moveToFirst())
        {


            int monthnum=Integer.parseInt(c.getString(1));
            cal.set(Calendar.MONTH,monthnum);

            tvmonth.setText(month_date.format(cal.getTime()));

            do
            {


                try
                {
                    result = c.getDouble(0);


                }
                catch (Exception e) {

                }
            } while (c.moveToNext());

        }

        c.close();

        myHelper.close();
        return result;
    }

    public void prevMonth(View view) {
        current-=1;
        showData(current);
    }

    public void nextMonth(View view) {
        current+=1;
        showData(current);
    }
}
