package com.majchrowski.piotr.inz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>, EntriesAdapter.OnItemClickListener {

    private DatabaseHelper myHelper;


    private EntriesAdapter mAdapter;
    private TextView empty;
    private RecyclerView recyclerView;

    private SimpleCursorAdapter adapter;

    private static final int LOADER_ID = 1976;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ent_list);

        myHelper = new DatabaseHelper(this);
        myHelper.open();
        empty = (TextView) findViewById(R.id.empty);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EntriesAdapter(this, null);
        recyclerView.setAdapter(mAdapter);
        //myHelper.populateWithTestData();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                myHelper.delete((long)viewHolder.itemView.getTag());
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
            }
        }).attachToRecyclerView(recyclerView);



        mAdapter.setOnItemClickListener(MainActivity.this);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);





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
            Intent add_mem = new Intent(this, AddEntryActivity.class);
            startActivity(add_mem);
        }

        if (id == R.id.cal) {
            Intent add_mem = new Intent(this, CalendarActivity.class);
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
        mAdapter.swapCursor(data);
        if(recyclerView.getAdapter().getItemCount()==0)
        {
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int position, View v) {
        TextView idTextView = (TextView) v.findViewById(R.id.tvItemId);
        TextView nameTextView = (TextView) v.findViewById(R.id.tvItemName);
        TextView dateTextView = (TextView) v.findViewById(R.id.tvItemDate);
        TextView categoryTextView = (TextView) v.findViewById(R.id.tvItemCategory);
        TextView typeTextView = (TextView) v.findViewById(R.id.tvItemType);
        TextView valueTextView = v.findViewById(R.id.tvItemValue);

        String id = idTextView.getText().toString();
        String name = nameTextView.getText().toString();
        double value = Double.parseDouble(valueTextView.getText().toString());
        String typeName = typeTextView.getText().toString();
        Cursor cursor =myHelper.getTypeId(typeName);
        cursor.moveToFirst();
        int type = cursor.getInt(0);
        String categoryName = categoryTextView.getText().toString();
        cursor=myHelper.getCategoryId(categoryName);
        cursor.moveToFirst();
        int category = cursor.getInt(0);
        String date = dateTextView.getText().toString();


        Intent modify_intent = new Intent(getApplicationContext(), ModifyEntryActivity.class);
        modify_intent.putExtra("name", name);
        modify_intent.putExtra("category", category);
        modify_intent.putExtra("date", date);
        modify_intent.putExtra("id", id);
        modify_intent.putExtra("type", type);
        modify_intent.putExtra("value", value);

        startActivity(modify_intent);
    }
}
