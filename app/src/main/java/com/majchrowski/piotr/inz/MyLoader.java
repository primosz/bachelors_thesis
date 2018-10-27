package com.majchrowski.piotr.inz;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;



public class MyLoader extends CursorLoader {
    DatabaseHelper myDatabaseHelper;

    public MyLoader(Context context, DatabaseHelper db) {
        super(context);
        myDatabaseHelper = db;
    }

    @Override
    public Cursor loadInBackground() {
        return myDatabaseHelper.getAllEntriesJoin();
    }
}
