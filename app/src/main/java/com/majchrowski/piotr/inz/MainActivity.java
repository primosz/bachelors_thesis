package com.majchrowski.piotr.inz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DatabaseHelper myHelper;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    private static final int LOADER_ID = 1976;

    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.NAME, DatabaseHelper.DATE, DatabaseHelper.CATEGORY_ID , DatabaseHelper.TYPE_ID, DatabaseHelper.VALUE};

    final int[] to = new int[] { R.id.tvId, R.id.tvName, R.id.tvDate, R.id.tvCategory , R.id.tvType, R.id.tvValue};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ent_list);

        myHelper = new DatabaseHelper(this);
        //myHelper.drop();
        myHelper.open();


        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, null, from, to, 0);

        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView idTextView = (TextView) view.findViewById(R.id.tvId);
                TextView nameTextView = (TextView) view.findViewById(R.id.tvName);
                TextView dateTextView = (TextView) view.findViewById(R.id.tvDate);
                TextView categoryTextView = (TextView) view.findViewById(R.id.tvCategory);
                TextView typeTextView = (TextView) view.findViewById(R.id.tvType);
                TextView valueTextView = view.findViewById(R.id.tvValue);

                String id = idTextView.getText().toString();
                String name = nameTextView.getText().toString();
                double value = Double.parseDouble(valueTextView.getText().toString());
                int type = Integer.parseInt(typeTextView.getText().toString());
                int category = Integer.parseInt(categoryTextView.getText().toString());
                String date = dateTextView.getText().toString();


                Intent modify_intent = new Intent(getApplicationContext(), ModifyEmployeeActivity.class);
                modify_intent.putExtra("name", name);
                modify_intent.putExtra("category", category);
                modify_intent.putExtra("date", date);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("type", type);
                modify_intent.putExtra("value", value);

                startActivity(modify_intent);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddEmployeeActivity.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new MyLoader(this, myHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        adapter.swapCursor(null);
    }
}