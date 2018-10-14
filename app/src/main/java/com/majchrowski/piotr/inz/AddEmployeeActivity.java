package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddEmployeeActivity extends AppCompatActivity {

    private EditText nameEditText, typeEditText, dateEditText, valueEditText;

    private Spinner categorySpinner;
    private List<Category> categoryList;
    private DatabaseHelper myHelper;
    int selectedCategory;

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        nameEditText = (EditText) findViewById(R.id.name_edittext);
        valueEditText = (EditText) findViewById(R.id.value_edittext);
        typeEditText = (EditText) findViewById(R.id.typ_edittext);
        dateEditText = (EditText) findViewById(R.id.date_edittext);



        myHelper = new DatabaseHelper(this);
        myHelper.open();
        populateCategoryList();

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddEmployeeActivity.this, categoryList.get(position).getName(), Toast.LENGTH_SHORT).show();
                selectedCategory = categoryList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    public void addButtonPressed(View view) {
        String name = nameEditText.getText().toString();
        String date = dateEditText.getText().toString();
        double value = Double.parseDouble(valueEditText.getText().toString());
        int type = Integer.parseInt(typeEditText.getText().toString());
        int category = selectedCategory;

        myHelper.addEntry(name, value, date, type, category);
        Toast.makeText(this, "Record added!", Toast.LENGTH_SHORT).show();

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
