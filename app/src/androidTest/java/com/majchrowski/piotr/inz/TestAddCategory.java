package com.majchrowski.piotr.inz;

import android.database.Cursor;
import android.test.AndroidTestCase;

import org.junit.Test;

public class TestAddCategory extends AndroidTestCase {
    DatabaseHelper myHelper;
    String testCategory;
    String value;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myHelper = new DatabaseHelper(mContext);
        myHelper.open();
        myHelper.clearDatabase();
        testCategory="testCategory";
    }

    @Override
    public void tearDown() throws Exception {
        myHelper.close();
        super.tearDown();
    }
    @Test
    public void testAddCategory() {

        myHelper.addCategory(testCategory);
        Cursor c = myHelper.getCategories();
        if (c.moveToLast()) value = c.getString(1);
        assertEquals(testCategory, value);
    }

}
