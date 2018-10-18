package com.majchrowski.piotr.inz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class ModifyEmployeeActivity extends AppCompatActivity {

    private EditText nameEditText, dateEditText, valueEditText;
    private Spinner categorySpinner;
    ArrayList<Category> categoryList;
    int selectedCategory;
    ToggleButton typeButton;

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
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ModifyEmployeeActivity.this, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                selectedCategory = categoryList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        valueEditText = (EditText) findViewById(R.id.value_edittext);


        dateEditText = (EditText) findViewById(R.id.date_edittext);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");
        int type = intent.getIntExtra("type", 1);
        int category = intent.getIntExtra("category", 1);
        double value = intent.getDoubleExtra("value", 0);

        _id = Long.parseLong(id);

        nameEditText.setText(name);
        dateEditText.setText(date);
        valueEditText.setText(""+value);
        categorySpinner.setSelection(category-1);
        typeButton.setChecked(type==1);
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

    private void returnToMainActivity()
    {
       finish();
    }

    public void updateButtonPressed(View view) {
        String name = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        double value = Double.parseDouble(valueEditText.getText().toString());
        int type;
        if(typeButton.isChecked()){
            type=1;
        }
        else type=2;
        int category = selectedCategory;

        myHelper.update(_id, name, value, date, type, category);
        Toast.makeText(this, "Record updated!", Toast.LENGTH_SHORT).show();
        returnToMainActivity();
    }

    public void deleteButtonPressed(View view) {
        myHelper.delete(_id);
        Toast.makeText(this, "Record deleted!", Toast.LENGTH_SHORT).show();
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
            AddEmployeeActivity.CategoryHolder holder;
            View categoryView = convertView;
            if(categoryView==null){
                categoryView = getLayoutInflater().inflate(R.layout.row_category_spinner, parent, false);

                holder = new AddEmployeeActivity.CategoryHolder();
                holder.categoryId = categoryView.findViewById(R.id.tvCatId);
                holder.categoryName = categoryView.findViewById(R.id.tvCatName);
                categoryView.setTag(holder);
            }
            else{
                holder = (AddEmployeeActivity.CategoryHolder) categoryView.getTag();
            }

            Category category = categoryList.get(position);
            holder.categoryId.setText(String.valueOf(category.getId()));
            holder.categoryName.setText(String.valueOf(category.getName()));
            return categoryView;
        }

    };
}
