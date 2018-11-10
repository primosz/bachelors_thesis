package com.majchrowski.piotr.inz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String MAIN_TABLE_NAME = "ENTRY";
    public static final String TYPE_TABLE_NAME = "TYPE_TABLE";
    public static final String CATEGORY_TABLE_NAME = "CATEGORY_TABLE";

    // Table columns
    public static final String _ID = "_id";
    public static final String CATEGORY = "category";
    public static final String TYPE = "type";
    public static final String CATEGORY_ID = "category_id";
    public static final String TYPE_ID = "type_id";
    public static final String DATE = "date";
    public static final String VALUE = "value";
    public static final String NAME = "name";
    // Database Information
    static public String DB_NAME = "baza.DB";

    // database version
    static final int DB_VERSION = 4;

    private SQLiteDatabase database;

    public static ContentValues DEFAULT_TYPES = new ContentValues();
    public static ContentValues DEFAULT_CATEGORIES = new ContentValues();


    // Creating table query
    private static final String CREATE_TYPE_TABLE = "CREATE TABLE " + TYPE_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TYPE + " TEXT NOT NULL);";

    private static final String CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORY_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY + " TEXT NOT NULL);";

    private static final String CREATE_MAIN_TABLE = "CREATE TABLE " + MAIN_TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " +  VALUE + " REAL, " + DATE + " TEXT," +
            TYPE_ID + " INTEGER REFERENCES " + TYPE_TABLE_NAME + "("+_ID+") NOT NULL, " +
            CATEGORY_ID + " INTEGER REFERENCES " + CATEGORY_TABLE_NAME+ "("+_ID+") NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE_NAME);
        db.execSQL(CREATE_TYPE_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_MAIN_TABLE);
        populateDBwithTestData(db);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TYPE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE_NAME);
        onCreate(db);

    }




    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public void clearDatabase() {
        String clearDBQuery = "DELETE FROM "+MAIN_TABLE_NAME;
        database.execSQL(clearDBQuery);
    }
    public void addCategory(String category)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY, category);

        database.insert(CATEGORY_TABLE_NAME, null, contentValues);
    }

    public void addType(String type)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TYPE, type);

        database.insert(TYPE_TABLE_NAME, null, contentValues);
    }

    public void addEntry(String name, double value, String date, int type, int category)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(VALUE, value);
        contentValues.put(DATE, date);
        contentValues.put(TYPE_ID, type);
        contentValues.put(CATEGORY_ID, category);

        database.insert(MAIN_TABLE_NAME, null, contentValues);
    }

    public void populateWithTestData(){
        addEntry("test1", 3, "2018-11-01", 1, 2 );
        addEntry("test2", -5, "2018-11-01", 2, 1 );
        addEntry("test3", 34, "2018-11-02", 1, 3 );
        addEntry("test4", 1, "2018-12-03", 1, 2 );
        addEntry("test5", -12.5, "2018-12-03", 2, 2 );
        addEntry("test6", 3, "2018-10-30", 1, 2 );
        addEntry("test7", -300.5, "2018-11-05", 2, 1 );
        addEntry("test8", 130.5, "2018-11-07", 1, 3 );
        addEntry("test9", 30.5, "2018-11-06", 1, 2 );
        addEntry("test10", 320.5, "2018-11-09", 1, 3 );
        addEntry("test11", 30.54, "2018-11-09", 1, 2 );
        addEntry("test12", 2.5, "2018-11-05", 1, 1 );
        addEntry("test13", -10.5, "2018-11-01", 2, 2 );
        addEntry("test14", 3.5, "2018-10-20", 1, 2 );


    }

    public Cursor getAllEntries()
    {
        String[] projection = {
                _ID, NAME, VALUE, DATE, TYPE_ID,CATEGORY_ID
        };

        Cursor cursor = database.query(MAIN_TABLE_NAME, projection, null, null, null, null, null);

        return cursor;
    }

    public Cursor getDailySums(){
        String rawQuery = "SELECT strftime('%j', "+DATE+") as valDay, date, \n" +
                "SUM("+VALUE+") as valTotalDay \n" +
                "FROM "+MAIN_TABLE_NAME+
                " GROUP BY valDay ORDER BY "+DATE;
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getSumOfCategories(int minusCurrent){
        String rawQuery = "SELECT CATEGORY_TABLE.category, SUM(ENTRY.value), strftime('%m',date('now', 'start of month', '"+minusCurrent+" months'))" +
                "FROM ENTRY join TYPE_TABLE on ENTRY.type_id=TYPE_TABLE._id\n" +
                "join CATEGORY_TABLE on ENTRY.category_id=CATEGORY_TABLE._id\n" +
                "WHERE type_id =2 and strftime('%m', ENTRY.date)= strftime('%m',date('now', 'start of month', '"+minusCurrent+" months')) and" +
                " strftime('%Y', date)= strftime('%Y',date('now', 'start of month', '"+minusCurrent+" months'))" +
                "GROUP BY category_id";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getSumIncomeOfMonth(int minusCurrent){
        String rawQuery = "SELECT IFNULL(SUM(value),0), strftime('%m',date('now', 'start of month', '"+minusCurrent+" months')), strftime('%Y',date('now', 'start of month', '"+minusCurrent+ " months'))" +
                "FROM ENTRY " +
                "WHERE type_id=1 and " +
                "strftime('%m', date)= strftime('%m',date('now', 'start of month', '"+minusCurrent+" months')) and" +
                " strftime('%Y', date)= strftime('%Y',date('now', 'start of month', '"+minusCurrent+" months'))";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }


    public Cursor getSumOutcomeOfMonth(int minusCurrent){
        String rawQuery = "SELECT IFNULL(SUM(value),0), strftime('%m',date('now', 'start of month', '"+minusCurrent+" months')), strftime('%Y',date('now', 'start of month', '"+minusCurrent+ " months'))" +
                "FROM ENTRY " +
                "WHERE type_id=2 and " +
                "strftime('%m', date)= strftime('%m',date('now', 'start of month', '"+minusCurrent+" months')) and" +
                " strftime('%Y', date)= strftime('%Y',date('now', 'start of month', '"+minusCurrent+" months'))";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getSumIncomeOfSingleDay(String date){
        String rawQuery = "SELECT          IFNULL(SUM(value), 0)\n" +
                "                FROM ENTRY\n" +
                "                WHERE type_id=1 and  date=\""+date+"\"";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getSumOutcomeOfSingleDay(String date){
        String rawQuery = "SELECT          IFNULL(SUM(value), 0)\n" +
                "                FROM ENTRY\n" +
                "                WHERE type_id=2 and  date=\""+date+"\"";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }


    public Cursor getSumIncomesOfDay(){
        String rawQuery = "SELECT strftime('%j', "+DATE+") as valDay, date, \n" +
                "SUM("+VALUE+") as valTotalDay \n" +
                "FROM "+MAIN_TABLE_NAME+
                " WHERE " + TYPE_ID+" =1"+
                " GROUP BY valDay ORDER BY "+DATE + " DESC";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getSumOutcomesOfDay(){
        String rawQuery = "SELECT strftime('%j', "+DATE+") as valDay, date, \n" +
                "SUM("+VALUE+") as valTotalDay \n" +
                "FROM "+MAIN_TABLE_NAME+
                " WHERE " + TYPE_ID+" =1"+
                " GROUP BY valDay ORDER BY "+DATE + " DESC";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getEntriesFromDay(String date)
    {   String rawQuery ="SELECT  ENTRY._ID, NAME, VALUE, DATE, TYPE ,CATEGORY\n" +
            "            FROM ENTRY join TYPE_TABLE on ENTRY.type_id=TYPE_TABLE._id\n" +
            "            join CATEGORY_TABLE on ENTRY.category_id=CATEGORY_TABLE._id\n" +
            "            WHERE date =\""+date+"\"";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getAllEntriesJoin()
    {   String rawQuery ="SELECT  ENTRY._ID, NAME, VALUE, DATE, TYPE ,CATEGORY\n" +
            "FROM ENTRY join TYPE_TABLE on ENTRY.type_id=TYPE_TABLE._id\n" +
            "join CATEGORY_TABLE on ENTRY.category_id=CATEGORY_TABLE._id";
        Cursor cursor = database.rawQuery(rawQuery, null);
        return cursor;
    }

    public Cursor getTypeId(String typeName){
        Cursor cursor = database.rawQuery("SELECT _id FROM TYPE_TABLE WHERE TYPE_TABLE.type=\""+typeName+"\"", null);
        return cursor;
    }

    public Cursor getCategoryId(String categoryName){
        Cursor cursor = database.rawQuery("SELECT _id FROM CATEGORY_TABLE WHERE CATEGORY_TABLE.category=\""+categoryName+"\"", null);
        return cursor;
    }

    public Cursor getCategories()
    {
        String[] projection = {_ID, CATEGORY};
        Cursor cursor = database.query(CATEGORY_TABLE_NAME, projection, null, null, null, null, null);
        return cursor;
    }

    public int update(long _id, String name, double value,  String date, int type, int category) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name);
        contentValues.put(VALUE, value);
        contentValues.put(DATE, date);
        contentValues.put(TYPE_ID, type);
        contentValues.put(CATEGORY_ID, category);


        int count = database.update(MAIN_TABLE_NAME, contentValues, this._ID + " = " + _id, null);
        return count;
    }

    public void delete(long _id)
    {
        database.delete(MAIN_TABLE_NAME, _ID + "=" + _id, null);
    }

    public Cursor getNames(String namePassedIn)
    {
        // Define a projection that specifies which columns from the database you will actually use after this query.
        String[] projection = {
                _ID, NAME, VALUE, DATE, TYPE_ID,CATEGORY_ID
        };

        // Filter results WHERE "name" = passed in parameter
        String selection = NAME + " = ?";
        String[] selectionArgs = { namePassedIn};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = NAME + " DESC";

        Cursor cursor = database.query(
                MAIN_TABLE_NAME,                     // The table to query
                projection,                     // The columns to return
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                sortOrder                      // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    private void populateDBwithTestData(SQLiteDatabase db) {
        DEFAULT_TYPES.put(TYPE, "Income");
        db.insert(TYPE_TABLE_NAME, null, DEFAULT_TYPES );
        DEFAULT_TYPES.put(TYPE, "Outcome");
        db.insert(TYPE_TABLE_NAME, null, DEFAULT_TYPES );
        DEFAULT_CATEGORIES.put(CATEGORY,"Jedzenie");
        db.insert(CATEGORY_TABLE_NAME, null, DEFAULT_CATEGORIES);
        DEFAULT_CATEGORIES.put(CATEGORY,"Dom");
        db.insert(CATEGORY_TABLE_NAME, null, DEFAULT_CATEGORIES);
        DEFAULT_CATEGORIES.put(CATEGORY,"Podróże");
        db.insert(CATEGORY_TABLE_NAME, null, DEFAULT_CATEGORIES);
    }
}
