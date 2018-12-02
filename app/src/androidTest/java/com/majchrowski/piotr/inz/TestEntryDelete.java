package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.test.AndroidTestCase;

import org.junit.Test;

public class TestEntryDelete extends AndroidTestCase {
    DatabaseHelper myHelper;
    String testDate, testName;



    @Override
    public void setUp() throws Exception {
        super.setUp();
        myHelper = new DatabaseHelper(mContext);
        myHelper.open();
        myHelper.clearDatabase();
        testDate="2018-11-22";
        testName="testEntry";

    }

    @Override
    public void tearDown() throws Exception {
        myHelper.close();
        super.tearDown();
    }
    @Test
    public void testDeleteEntry() {

        myHelper.addEntry(testName, 123.1, testDate, 1, 1);
        myHelper.deleteByName(testName);
        Cursor c = myHelper.getEntriesFromDay(testDate);
        assertTrue(c.getCount()==0);
    }
}
