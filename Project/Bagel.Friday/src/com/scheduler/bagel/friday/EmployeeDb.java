package com.scheduler.bagel.friday;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EmployeeDb {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_FIRSTNAME = "firstName";
	public static final String KEY_LASTNAME = "lastName";
	public static final String KEY_SCHEDULE = "scheduledDate";
	  
	 private static final String TAG = "EmployeesDbAdapter";
	 private DatabaseHelper mDbHelper;
	 private SQLiteDatabase mDb;
	 
	 private static final String DATABASE_NAME = "Employees_Directory";
	 private static final String SQLITE_TABLE = "EmployeesTable";
	 private static final int DATABASE_VERSION = 1;
	 
	 private final Context mCtx;
	 
	 private static final String DATABASE_CREATE =
	  "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
	  KEY_ROWID + " integer PRIMARY KEY autoincrement," +
	  KEY_FIRSTNAME + "," +
	  KEY_LASTNAME + "," +
	  KEY_SCHEDULE + ");";
	 
	 private static class DatabaseHelper extends SQLiteOpenHelper {
	 
	  DatabaseHelper(Context context) {
	   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }
	 
	 
	  @Override
	  public void onCreate(SQLiteDatabase db) {
	   Log.w(TAG, DATABASE_CREATE);
	   db.execSQL(DATABASE_CREATE);
	   
	  }
	 
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	     + newVersion + ", which will destroy all old data");
	   db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
	   onCreate(db);
	  }
	 }
	 
	 public EmployeeDb(Context ctx) {
	  this.mCtx = ctx;
	 }
	 
	 public EmployeeDb open() throws SQLException {
	  mDbHelper = new DatabaseHelper(mCtx);
	  mDb = mDbHelper.getWritableDatabase();
	  return this;
	 }
	 
	 public void close() {
	  if (mDbHelper != null) {
	   mDbHelper.close();
	  }
	 }
	 
	 public long createEmployee(String firstName, String lastName, String scheduledDate) {
	 
	  ContentValues initialValues = new ContentValues();
	  initialValues.put(KEY_FIRSTNAME, firstName);
	  initialValues.put(KEY_LASTNAME, lastName);
	  initialValues.put(KEY_SCHEDULE, scheduledDate);
	   
	  return mDb.insert(SQLITE_TABLE, null, initialValues);
	 }
	 
	 public boolean deleteAllEmployees() {
	 
	  int doneDelete = 0;
	  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
	  Log.w(TAG, Integer.toString(doneDelete));
	  return doneDelete > 0;
	 
	 }
	 
	 public boolean createTable() {
		 
		 Log.w(TAG, DATABASE_CREATE);
		 mDb.execSQL(DATABASE_CREATE);
		 return true;
		 
		 }
	 
		 
	 public Cursor fetchAllEmployees() {
	 
	  Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
	    KEY_FIRSTNAME, KEY_LASTNAME, KEY_SCHEDULE}, 
	    null, null, null, null, null);
	 
	  if (mCursor != null) {
	   mCursor.moveToFirst();
	  }
	  return mCursor;
	 }
	 
	 public void insertSomeEmployees() {
	 
	  createEmployee("Balaji","Sundaresan", "07/25/2014");
	  createEmployee("Inna","Akhiezer", "08/01/2014");
	  createEmployee("Ann","Whitt", "08/08/2014");
	  createEmployee("Kirsten","Piccini", "08/17/2014");
	  createEmployee("Jeff","Breuninger", "08/22/2014");
	  createEmployee("Pete","Carro", "08/29/2014");
	  createEmployee("Judy","Gazolli", "09/05/2014");
	  createEmployee("Neville","Soares", "09/12/2014");
	  createEmployee("Jay","Grassi", "09/19/2014");
	  createEmployee("Pete","Klimchuk", "09/26/2014");
	  createEmployee("Jay","Price", "10/03/2014");
	  createEmployee("Paul","Schachman", "10/10/2014");
	  createEmployee("Lindsey","Long", "10/17/2014");
	  
	 }	

}
