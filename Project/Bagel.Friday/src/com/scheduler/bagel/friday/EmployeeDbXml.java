package com.scheduler.bagel.friday;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EmployeeDbXml {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_FIRSTNAME = "firstName";
	public static final String KEY_LASTNAME = "lastName";
	public static final String KEY_SCHEDULE = "scheduledDate";
	  
	 private static final String TAG = "EmployeesDbXmlAdapter";
	 private DatabaseHelper mDbHelper;
	 private SQLiteDatabase mDb;
	 
	 private static final String DATABASE_NAME = "Employees_Directory";
	 private static final String SQLITE_TABLE = "EmployeesTable";
	 private static final int DATABASE_VERSION = 1;
	 protected static Context mCtx;
	 
	 	 
	 private static class DatabaseHelper extends SQLiteOpenHelper {
	 
	  DatabaseHelper(Context context) {
	   super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  
	 
	  @Override
	  public void onCreate(SQLiteDatabase db) {
	   String s;
       try {
              InputStream in = mCtx.getResources().openRawResource(R.raw.sql);
              DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
              Document doc = builder.parse(in, null);
              NodeList statements = doc.getElementsByTagName("statement");
              for (int i=0; i<statements.getLength(); i++) {
                       s = statements.item(i).getChildNodes().item(0).getNodeValue();
                       db.execSQL(s);
              Log.d("DBXML", "DBXML Created");
               }
       } catch (Throwable t) {
    	   Log.d("No DBXML", "No DBXML Created");
       }

	   
	  }
	 
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	     + newVersion + ", which will destroy all old data");
	   onCreate(db);
	  }
	 }
	 
	 @SuppressWarnings("static-access")
	public EmployeeDbXml(Context ctx) {
	  this.mCtx = ctx;
	 }
	 
	 public EmployeeDbXml open() throws SQLException {
	  mDbHelper = new DatabaseHelper(mCtx);
	  mDb = mDbHelper.getWritableDatabase();
	  return this;
	 }
	 
	 public void close() {
	  if (mDbHelper != null) {
	   mDbHelper.close();
	  }
	 }
	 
	 public boolean deleteAllEmployees() {
	 
	  int doneDelete = 0;
	  doneDelete = mDb.delete(SQLITE_TABLE, null , null);
	  Log.w(TAG, Integer.toString(doneDelete));
	  return doneDelete > 0;
	 
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
 
	 
}
