package com.majchrowski.piotr.inz;



import android.database.Cursor;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestSumOfSingleDay extends AndroidTestCase {

    DatabaseHelper myHelper;
    Double testValue1, testValue2, testValue3, testValue4, testValue5, testValue6;
    String testDate;
    Double value;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myHelper = new DatabaseHelper(mContext);
        myHelper.open();
        myHelper.clearDatabase();
        testValue1=40.0;
        testValue2=30.0;
        testValue3=1.1;
        testValue4=-50.0;
        testValue5=-3.0;
        testValue6=-1.1;
        testDate ="2018-11-22";
    }

    @Override
    public void tearDown() throws Exception {
        myHelper.close();
        super.tearDown();
    }
    @Test
    public void testSumIncomeOfSingleDay() {

        myHelper.addEntry("test1", testValue1, testDate, 1, 2 );
        myHelper.addEntry("test2", testValue2, testDate, 1, 2 );
        myHelper.addEntry("test3", testValue3, testDate, 1, 2 );
        Cursor c = myHelper.getSumIncomeOfSingleDay(testDate);
        if (c.moveToFirst()) value = c.getDouble(0);

        assertEquals( testValue1+testValue2+testValue3, value);
    }

    @Test
    public void testSumOutcomeOfSingleDay() {

        myHelper.addEntry("test1", testValue4, testDate, 2, 1 );
        myHelper.addEntry("test2", testValue5, testDate, 2, 2 );
        myHelper.addEntry("test3", testValue6, testDate, 2, 3 );
        Cursor c = myHelper.getSumOutcomeOfSingleDay(testDate);
        if (c.moveToFirst()) value = c.getDouble(0);

        assertEquals(testValue4+testValue5+testValue6, value);
    }
}