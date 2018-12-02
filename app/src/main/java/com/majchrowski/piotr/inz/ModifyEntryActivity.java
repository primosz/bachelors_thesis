package com.majchrowski.piotr.inz;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModifyEntryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText nameEditText,valueEditText;
    private TextView dateTextView;
    private Spinner categorySpinner;
    ArrayList<Category> categoryList;
    int selectedCategory;
    ToggleButton typeButton;
    String currentDateString;

    private long _id;

    private DatabaseHelper myHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_entry);

        myHelper = new DatabaseHelper(this);
        myHelper.open();
        populateCategoryList();
        typeButton=(ToggleButton)findViewById(R.id.typeButton);
        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        dateTextView = (TextView) findViewById(R.id.date_textview);
        Date cal = (Date) Calendar.getInstance().getTime();
        currentDateString = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
        dateTextView.setText(currentDateString);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categoryList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        valueEditText = (EditText) findViewById(R.id.value_edittext);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        int type = intent.getIntExtra("type", 1);
        int category = intent.getIntExtra("category", 1);
        double value = intent.getDoubleExtra("value", 0);

        _id = Long.parseLong(id);

        nameEditText.setText(name);
        dateTextView.setText(date);
        valueEditText.setText(""+value);
        categorySpinner.setSelection(category-1);
        typeButton.setChecked(type==1);
    }

    private void populateCategoryList() {
        categoryList = new ArrayList<>();

        Cursor mCursor = myHelper.getCategories();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            // The Cursor is now set to the right position
            categoryList.add(new Category(mCursor.getInt(0), mCursor.getString(1)));
        }
    }

    private void returnToMainActivity()
    {
       finish();
    }

    public void updateButtonPressed(View view) {
        String name = nameEditText.getText().toString();
        String date = dateTextView.getText().toString();
        double value = Double.parseDouble(valueEditText.getText().toString());
        int type;
        if(typeButton.isChecked()){
            type=1;
        }
        else {
            type=2;
            if(value>0) {value = value*-1;};
        }
        int category = selectedCategory;

        myHelper.update(_id, name, value, date, type, category);
        Toast.makeText(this, R.string.record_updated, Toast.LENGTH_SHORT).show();
        returnToMainActivity();
    }

    public void deleteButtonPressed(View view) {
        myHelper.delete(_id);
        Toast.makeText(this, R.string.record_deleted, Toast.LENGTH_SHORT).show();
        returnToMainActivity();

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
            AddEntryActivity.CategoryHolder holder;
            View categoryView = convertView;
            if(categoryView==null){
                categoryView = getLayoutInflater().inflate(R.layout.row_category_spinner, parent, false);

                holder = new AddEntryActivity.CategoryHolder();
                holder.categoryId = categoryView.findViewById(R.id.tvCatId);
                holder.categoryName = categoryView.findViewById(R.id.tvCatName);
                categoryView.setTag(holder);
            }
            else{
                holder = (AddEntryActivity.CategoryHolder) categoryView.getTag();
            }

            Category category = categoryList.get(position);
            holder.categoryId.setText(String.valueOf(category.getId()));
            holder.categoryName.setText(String.valueOf(category.getName()));
            return categoryView;
        }

    };
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        currentDateString = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());


        dateTextView.setText(currentDateString);

    }
}
