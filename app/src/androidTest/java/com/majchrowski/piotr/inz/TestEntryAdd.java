package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.test.AndroidTestCase;

import org.junit.Test;

public class TestEntryAdd extends AndroidTestCase {
    DatabaseHelper myHelper;
    String testDate, testName;
    Double value, testValue;



    @Override
    public void setUp() throws Exception {
        super.setUp();
        myHelper = new DatabaseHelper(mContext);
        myHelper.open();
        myHelper.clearDatabase();
        testDate="2018-11-22";
        testName="testEntry";
        testValue= 123.1;

    }

    @Override
    public void tearDown() throws Exception {
        myHelper.close();
        super.tearDown();
    }
    @Test
    public void testAddEntry() {

        myHelper.addEntry(testName, 123.1, testDate, 1, 1);

        Cursor c = myHelper.getEntryByName(testName);
        if (c.moveToFirst()) value = c.getDouble(2);
        assertEquals(testValue, value );
    }
}
