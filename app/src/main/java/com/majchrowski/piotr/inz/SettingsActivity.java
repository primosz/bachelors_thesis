package com.majchrowski.piotr.inz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    Button btnClear, btnSet;
    EditText newCategory;
    TextView setTime;
    private DatabaseHelper myHelper;
    Calendar cal;
    boolean isSet=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnClear = findViewById(R.id.btnClear);
        btnSet = findViewById(R.id.btn_SetNotification);
        newCategory = findViewById(R.id.editText_AddCategory);
        setTime = findViewById(R.id.tvSetTime);

        myHelper = new DatabaseHelper(this);
        myHelper.open();
        setTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTime.setText( selectedHour + ":" + selectedMinute);
                        cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                        cal.set(Calendar.MINUTE, selectedMinute);
                        isSet=true;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle(getString(R.string.select_time));
                mTimePicker.show();


            }
        });

    }
    public void setTime(View view) {
 if(isSet) {
     Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
     intent.putExtra("Notification Key", 1);
     PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
     AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
     alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
     Toast.makeText(SettingsActivity.this, getString(R.string.notif_set) +
             cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
 }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    myHelper.clearDatabase();
                    Toast.makeText(getApplicationContext(), R.string.db_cleared, Toast.LENGTH_SHORT).show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };



    public void ClearDatabase(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.uSure).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();


    }

    public void addCategory(View view) {
        String name = newCategory.getText().toString();
        if(!name.equals(""));
        {
            myHelper.addCategory(name);
            Toast.makeText(getApplicationContext(), getString(R.string.category) +" " + name + " "+ getString(R.string.added), Toast.LENGTH_SHORT).show();
        }
    }


}
