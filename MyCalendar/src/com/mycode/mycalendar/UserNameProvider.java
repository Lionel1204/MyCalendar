package com.mycode.mycalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.mycode.mycalendar.SchedularProviderMetaData.UserNameTableMetaData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class UserNameProvider extends ContentProvider {

	//Setup projection Map
		private static HashMap<String, String> sSchedularProjectionMap;
		
	    private static final UriMatcher sUriMatcher;
	    private static final int USER_NAME_COLLECTION_URI_INDICATOR = 1;
	    private static final int USER_NAME_SIGLE_URI_INDICATOR = 2;
	    
	    private SchedularDbHelper mDbHelper;
	    
		static {
			sSchedularProjectionMap = new HashMap<String, String>();
			sSchedularProjectionMap.put(UserNameTableMetaData._ID, 
					UserNameTableMetaData._ID);
			
			sSchedularProjectionMap.put(UserNameTableMetaData.COLUMN_USER_NAME, 
					UserNameTableMetaData.COLUMN_USER_NAME);
						
			sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
	        
			//add match code
			sUriMatcher.addURI(SchedularProviderMetaData.AUTHORITY_NAME, 
					          "UserNameTable", 
					          USER_NAME_COLLECTION_URI_INDICATOR);
			sUriMatcher.addURI(SchedularProviderMetaData.AUTHORITY_NAME, 
					          "UserNameTable/#", 
					          USER_NAME_SIGLE_URI_INDICATOR);
		}
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = 0;
		switch (sUriMatcher.match(uri)){
		case USER_NAME_COLLECTION_URI_INDICATOR:
			count = db.delete(UserNameTableMetaData.TABLE_NAME_USER_NAME, whereClause, whereArgs);
			break;
		case USER_NAME_SIGLE_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);//get id
			count = db.delete(UserNameTableMetaData.TABLE_NAME_USER_NAME, 
					UserNameTableMetaData._ID 
					              + "=" 
					              + rowId 
					              + (!TextUtils.isEmpty(whereClause) ? " AND (" + whereClause + ')' : ""), 
					          whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(sUriMatcher.match(uri)){
		case USER_NAME_COLLECTION_URI_INDICATOR:
			return UserNameTableMetaData.CONTENT_TYPE;
		case USER_NAME_SIGLE_URI_INDICATOR:
			return UserNameTableMetaData.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);	
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != USER_NAME_COLLECTION_URI_INDICATOR){
			throw new IllegalArgumentException("Unknown URI " + uri);	
		}
		
		ContentValues values = null;
		
		if(null != initialValues){
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		
		
		Long now = Long.valueOf(System.currentTimeMillis());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		DateFormat dateFormat = DateFormat.getTimeInstance();
		String today = dateFormat.format(calendar.getTime());
		
		//Make sure to set the fields
		
		if (!values.containsKey(UserNameTableMetaData.COLUMN_USER_NAME)){
		    values.put(UserNameTableMetaData.COLUMN_USER_NAME, "DefaultName");
		}
		
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		//insert data
		long rowId = db.insert(UserNameTableMetaData.TABLE_NAME_USER_NAME, 
				UserNameTableMetaData.COLUMN_USER_NAME, values);
		//insert sucessfully and notify change
		if (rowId > 0){
			Uri insertedSchedularUri = ContentUris.withAppendedId(UserNameTableMetaData.CONTENT_URI_NAME, rowId);
			getContext().getContentResolver().notifyChange(insertedSchedularUri, null);//notify who regists uri
			return insertedSchedularUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDbHelper = new SchedularDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables(UserNameTableMetaData.TABLE_NAME_USER_NAME);
		qb.setProjectionMap(sSchedularProjectionMap);
		
		switch (sUriMatcher.match(uri)){
		case USER_NAME_COLLECTION_URI_INDICATOR:
			break;
		case USER_NAME_SIGLE_URI_INDICATOR:
			
			qb.appendWhere(UserNameTableMetaData._ID
					     + "="
					     + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy = TextUtils.isEmpty(sortOrder) ? 
				UserNameTableMetaData.DEFAULT_SORT_ORDER : sortOrder;
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor cur = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		
		// int curCount = cur.getCount(); //try to get the count of cursor
		
		cur.setNotificationUri(getContext().getContentResolver(), uri);
		return cur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = 0;
		switch (sUriMatcher.match(uri)){
		case USER_NAME_COLLECTION_URI_INDICATOR:
			count = db.update(UserNameTableMetaData.TABLE_NAME_USER_NAME, values, whereClause, whereArgs);
			break;
		case USER_NAME_SIGLE_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);//get id
			count = db.update(UserNameTableMetaData.TABLE_NAME_USER_NAME, 
					          values, 
					          UserNameTableMetaData._ID 
					              + "=" 
					              + rowId 
					              + (!TextUtils.isEmpty(whereClause) ? " AND (" + whereClause + ')' : ""), 
					          whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
