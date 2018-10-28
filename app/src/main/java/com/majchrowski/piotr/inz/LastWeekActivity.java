package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class LastWeekActivity extends AppCompatActivity {
    DatabaseHelper myHelper;
    ArrayList<DaySum> daysums;
    ArrayList<String> dates;
    Calendar calendar;
    SimpleDateFormat sd;
    BarChart chart;
    Toolbar toolBar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_week);
         chart= findViewById(R.id.chart);
        sd = new SimpleDateFormat("yyyy-MM-dd");


        getOutCome();
        List<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(getIncome());
        dataSets.add(getOutCome());

        BarData data = new BarData(dataSets);
        data.setBarWidth(0.9f); // set custom bar width
        chart.setData(data);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.animateY(1000);
        chart.setDescription(null);
        chart.moveViewTo(0,0,null);
        chart.invalidate(); // refresh


        toolBar = (Toolbar) findViewById(R.id.toolbarWeek);

        toolBar.setTitle("Summary for last week");
        setSupportActionBar(toolBar);

        DrawerUtil.getDrawer(this,toolBar);





    }

    private BarDataSet getOutCome() {
        calendar = Calendar.getInstance();
        dates = new ArrayList<>();

        List<BarEntry> entries = new ArrayList<>();
        daysums = new ArrayList<DaySum>();


        for (int i=0;i<7;i++){

            daysums.add(getSumFromDay((sd.format(calendar.getTime())), false));
            calendar.add(Calendar.DAY_OF_YEAR,-1);

        }

        Collections.reverse(daysums);

        for(int i = 0; i<daysums.size(); i++) {
            entries.add(new BarEntry(i, (float) daysums.get(i).value));
            dates.add(daysums.get(i).date.substring(5));
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.get((int)value);
            }

        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        BarDataSet set = new BarDataSet(entries, "Outcomes");
        set.setColor(Color.RED);
        return set;



    }

    private BarDataSet getIncome() {
        calendar = Calendar.getInstance();
        dates = new ArrayList<>();

        List<BarEntry> entries = new ArrayList<>();
        daysums = new ArrayList<DaySum>();


        for (int i=0;i<7;i++){

            daysums.add(getSumFromDay((sd.format(calendar.getTime())), true));
            calendar.add(Calendar.DAY_OF_YEAR,-1);

        }

        Collections.reverse(daysums);

        for(int i = 0; i<daysums.size(); i++) {
            entries.add(new BarEntry(i, (float) daysums.get(i).value));
            dates.add(daysums.get(i).date.substring(5));
        }

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dates.get((int)value);
            }

        };

        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);
        BarDataSet set = new BarDataSet(entries, "Incomes");
        set.setColor(Color.GREEN);
        return set;


    }

    private DaySum getSumFromDay(String data, boolean inOrOut) {

        myHelper = new DatabaseHelper(this);
        myHelper.open();

        DaySum result = null;
        String date = data;
        Log.d("ASD", data);
        Cursor c;

        if (inOrOut) {
            c = myHelper.getSumIncomeOfSingleDay(data);
        }//function to retrieve all values from a table- written in MyDb.java file
        else
        {
            c = myHelper.getSumOutcomeOfSingleDay(data);
        }
        if (c.moveToFirst())
        {

            do
            {
            double value = c.getDouble(0);

            try
            {
                result = new DaySum(date, value);

                
            }
            catch (Exception e) {

            }
            } while (c.moveToNext());

        }

        c.close();

        myHelper.close();
        return result;
    }



    public class DaySum{
        public String date;
        public double value;
        public DaySum(String date, double value){
            this.date=date;
            this.value=value;

        }


    }
}
