package com.example.pegasys.rapmedixuser.activity.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pegasys.rapmedixuser.activity.pojo.User;

import java.util.ArrayList;
import java.util.List;

public class DataBase_Helper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "Rapmedix";
	private static final String TABLE_CONTENTS = "rapmedix";

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_PHONE = "phone";
	private static final String KEY_CITY = "city";
	private static final String KEY_User_ID = "user_id";

	public DataBase_Helper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_Service = "CREATE TABLE User (id TEXT PRIMARY KEY,name TEXT, email TEXT, phone TEXT, city TEXT, user_id TEXT)";
		db.execSQL(CREATE_Service);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS" + "User");
		onCreate(db);
	}

	public void insertData(User user) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		
		values.put(KEY_NAME, user.getName());
		values.put(KEY_EMAIL, user.getEmail());
		values.put(KEY_PHONE, user.getMobile());
		values.put(KEY_CITY, user.getCity());
		values.put(KEY_User_ID, user.getUid());
		db.insert("User", null, values);
		db.close();

	}
	
	public void insertUserId(User u) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		
		values.put(KEY_User_ID, u.getUid());
		values.put(KEY_PHONE, u.getMobile());
		values.put(KEY_ID, u.getId());
		values.put(KEY_NAME, u.getName());
		values.put(KEY_EMAIL, u.getEmail());
		values.put(KEY_CITY, u.getCity());
		db.insert("User", null, values);
		db.close();

	}
	
	public void updatetUserId(User u) {

		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();

		
		values.put(KEY_User_ID, u.getUid());
		values.put(KEY_PHONE, u.getMobile());
		values.put(KEY_ID, u.getId());
		String x="where id="+u.getId();
		db.update("User", values,x, null);
		db.close();

	}


	public List<User> getUserData() {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT * FROM User";
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		do {
			User u=new User();
			u.setName(cursor.getString(cursor
					.getColumnIndexOrThrow("name")));
			u.setCity(cursor.getString(cursor
					.getColumnIndexOrThrow("city")));
			u.setMobile( cursor.getString(cursor
					.getColumnIndexOrThrow("phone")));
			u.setEmail(cursor.getString(cursor
					.getColumnIndexOrThrow("email")));
			
			ul.add(u);


		} while (cursor.moveToNext());
		cursor.close();db.close();
		return ul;
	}
	
	public String getUserId(String id) {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT user_id FROM User where id="+id;
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		String s=cursor.getString(0);
		cursor.close();db.close();
		Log.e("User id", s);
		return s;
	}
	
	public String getUserMobileNumber(String id) {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT phone FROM User where id="+id;
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		String s=cursor.getString(0);
		cursor.close();db.close();
		return s;
	}
	
	public String getUserName(String id) {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT name FROM User where id="+id;
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		String s=cursor.getString(0);
		cursor.close();db.close();
		return s;
	}
	
	public String getUserEmail(String id) {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT email FROM User where id="+id;
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		String s=cursor.getString(0);
		cursor.close();db.close();
		return s;
	}
	
	public String getUserCity(String id) {
		List<User> ul=new ArrayList<User>();

		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT city FROM User where id="+id;
		Cursor cursor = db.rawQuery(query,null);
		Log.e("getting", DatabaseUtils.dumpCursorToString(cursor));
		cursor.moveToFirst();

		String s=cursor.getString(0);
		cursor.close();db.close();
		return s;
	}
	
	
	public int getUserCount()
	{
		SQLiteDatabase db = getReadableDatabase();
		String query = "SELECT * FROM User";
		Cursor cursor = db.rawQuery(query,null);
		int x=cursor.getCount();
		cursor.close();
		db.close();
		return x;
	}


}


/*
 * public List<Contact> getAllContacts() {
 * 
 * public void addContact (Contact contact) { SQLiteDatabase db =
 * getWritableDatabase(); ContentValues values = new ContentValues();
 * values.put(KEY_NAME, contact.name); values.put(KEY_EMAIL, contact.email);
 * values.put(KEY_PHONE, contact.phone); values.put(KEY_CITY, contact.city);
 * 
 * db.insert(TABLE_CONTENTS, null, values); db.close(); }
 */


