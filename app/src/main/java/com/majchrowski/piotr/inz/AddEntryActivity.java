package com.majchrowski.piotr.inz;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameEditText, valueEditText;
    private  TextView dateTextView;
    private String dateString;
    private Spinner categorySpinner;
    private List<Category> categoryList;
    private DatabaseHelper myHelper;
    int selectedCategory;
    String currentDateString;
    ToggleButton typeButton;
    SimpleDateFormat sdf;
    Calendar c;


   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        valueEditText = (EditText) findViewById(R.id.value_edittext);
        typeButton = (ToggleButton) findViewById(R.id.typeButton);
        dateTextView = (TextView) findViewById(R.id.date_textview);


        Date cal = (Date) Calendar.getInstance().getTime();
        currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        dateTextView.setText(currentDateString);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");


            }
        });



        sdf = new SimpleDateFormat("yyyy-MM-dd");
        myHelper = new DatabaseHelper(this);
        myHelper.open();
        populateCategoryList();

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddEntryActivity.this, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                selectedCategory = categoryList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent = getIntent();
        if (intent!=null) {
            int type = intent.getIntExtra("type", 1);
            typeButton.setChecked(type==1);
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());


        dateTextView.setText(dateString);

    }

    private void populateCategoryList() {
        categoryList = new ArrayList<>();
       /* categoryList.add(new Category(1, "zakupy"));
        categoryList.add(new Category(2, "lalala"));
        categoryList.add(new Category(3, "bar"));
        categoryList.add(new Category(4, "dom"));
        categoryList.add(new Category(5, "podróże")); */
        Cursor mCursor = myHelper.getCategories();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            categoryList.add(new Category(mCursor.getInt(0), mCursor.getString(1)));
        }


    }

    public void addButtonPressed(View view) throws ParseException {
        String name = nameEditText.getText().toString();
        String date = dateTextView.getText().toString();
        double value=0;
        if(!valueEditText.getText().toString().equals("")) {
             value = Double.parseDouble(valueEditText.getText().toString());
        }
        int type;
        if(typeButton.isChecked()){
            type=1;
        }
        else {
            type=2;
            value = value*-1;
        }
        int category = selectedCategory;

        if(!name.equals("") ) {
            myHelper.addEntry(name, value, date, type, category);
            Toast.makeText(this, R.string.Record_added, Toast.LENGTH_SHORT).show();
        }

        Date dateSelected = sdf.parse(date);
        Date currentDate = sdf.parse(currentDateString);
        if(dateSelected.after(currentDate)&&type==1)
        {
            Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
            intent.putExtra("Notification Key", 2);
            intent.putExtra("Name", name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            Toast.makeText(AddEntryActivity.this, getString(R.string.youWillNotify)+ " "+ dateSelected, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private BaseAdapter categoryAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return  categoryList.size();
        }

        @Override
        public Object getItem(int position) {
            return categoryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CategoryHolder holder;
            View categoryView = convertView;
            if(categoryView==null){
                categoryView = getLayoutInflater().inflate(R.layout.row_category_spinner, parent, false);

                holder = new CategoryHolder();
                holder.categoryId = categoryView.findViewById(R.id.tvCatId);
                holder.categoryName = categoryView.findViewById(R.id.tvCatName);
                categoryView.setTag(holder);
            }
            else{
                holder = (CategoryHolder) categoryView.getTag();
            }

            Category category = categoryList.get(position);
            holder.categoryId.setText(String.valueOf(category.getId()));
            holder.categoryName.setText(String.valueOf(category.getName()));
            return categoryView;
        }

    };

    public static class  CategoryHolder{
        public TextView categoryId;
        public TextView categoryName;
    }
}
