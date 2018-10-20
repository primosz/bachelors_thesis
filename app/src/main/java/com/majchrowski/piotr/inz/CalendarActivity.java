package com.majchrowski.piotr.inz;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    static CompactCalendarView calendar;
    FrameLayout frameLayout;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        frameLayout = findViewById(R.id.frameLayout);




        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        calendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
       calendar.setUseThreeLetterAbbreviation(true);




        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String stringDate = dateFormat.format(dateClicked);
                Context context = getApplicationContext();
                Toast.makeText(context, stringDate, Toast.LENGTH_SHORT).show();
                DailyFragment fragment = new DailyFragment();
                Bundle args = new Bundle();
                args.putString("date", stringDate);
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(dateFormat.format(firstDayOfNewMonth));

            }
        });



    }

    public void AddEvent(Event event){
        CompactCalendarView calendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);
        calendar.addEvent(event);

    }


}
