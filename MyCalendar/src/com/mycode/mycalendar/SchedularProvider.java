package com.mycode.mycalendar;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.mycode.mycalendar.SchedularProviderMetaData.SchedularTableMetaData;

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

public class SchedularProvider extends ContentProvider {

	//Setup projection Map
	private static HashMap<String, String> sSchedularProjectionMap;
	
    private static final UriMatcher sUriMatcher;
    private static final int CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR = 1;
    private static final int CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR = 2;
    
    private SchedularDbHelper mDbHelper;
    
	static {
		sSchedularProjectionMap = new HashMap<String, String>();
		sSchedularProjectionMap.put(SchedularTableMetaData._ID, 
				                    SchedularTableMetaData._ID);
		
		sSchedularProjectionMap.put(SchedularTableMetaData.SCHEDULAR_USER_ID, 
                                    SchedularTableMetaData.SCHEDULAR_USER_ID);
		sSchedularProjectionMap.put(SchedularTableMetaData.SCHEDULAR_USER_NAME, 
                                    SchedularTableMetaData.SCHEDULAR_USER_NAME);
		sSchedularProjectionMap.put(SchedularTableMetaData.SCHEDULAR_DATE, 
                                    SchedularTableMetaData.SCHEDULAR_DATE);
		sSchedularProjectionMap.put(SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE, 
                                    SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE);
		
		sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        
		//add match code
		sUriMatcher.addURI(SchedularProviderMetaData.AUTHORITY, 
				          "CalendarSchedular", 
				          CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(SchedularProviderMetaData.AUTHORITY, 
				          "CalendarSchedular/#", 
				          CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR);
		
	}
	
	@Override
	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count = 0;
		switch (sUriMatcher.match(uri)){
		case CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR:
			count = db.delete(SchedularTableMetaData.TABLE_NAME, whereClause, whereArgs);
			break;
		case CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);//get id
			count = db.delete(SchedularTableMetaData.TABLE_NAME, 
					          SchedularTableMetaData._ID 
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
		case CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR:
			return SchedularTableMetaData.CONTENT_TYPE;
		case CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR:
			return SchedularTableMetaData.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);	
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR){
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
		
		if (!values.containsKey(SchedularTableMetaData.SCHEDULAR_USER_ID)){
			values.put(SchedularTableMetaData.SCHEDULAR_USER_ID, "1");
		}
		
		if (!values.containsKey(SchedularTableMetaData.SCHEDULAR_USER_NAME)){
		    values.put(SchedularTableMetaData.SCHEDULAR_USER_NAME, "DefaultName");
		}
		
		if (!values.containsKey(SchedularTableMetaData.SCHEDULAR_DATE)){
			values.put(SchedularTableMetaData.SCHEDULAR_DATE, now);
		}
		
		if (!values.containsKey(SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE)){
			values.put(SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE, 8);
		}
				
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		//insert data
		long rowId = db.insert(SchedularTableMetaData.TABLE_NAME, 
				               SchedularTableMetaData.SCHEDULAR_USER_ID, values);
		//insert sucessfully and notify change
		if (rowId > 0){
			Uri insertedSchedularUri = ContentUris.withAppendedId(SchedularTableMetaData.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(insertedSchedularUri, null);//notify who regists uri
			return insertedSchedularUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDbHelper = new SchedularDbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		qb.setTables(SchedularTableMetaData.TABLE_NAME);
		qb.setProjectionMap(sSchedularProjectionMap);
		
		switch (sUriMatcher.match(uri)){
		case CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR:
			break;
		case CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR:
			
			qb.appendWhere(SchedularTableMetaData._ID
					     + "="
					     + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		
		String orderBy = TextUtils.isEmpty(sortOrder) ? 
				         SchedularTableMetaData.DEFAULT_SORT_ORDER : sortOrder;
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
		case CALENDAR_SCHEDULAR_COLLECTION_URI_INDICATOR:
			count = db.update(SchedularTableMetaData.TABLE_NAME, values, whereClause, whereArgs);
			break;
		case CALENDAR_SCHEDULAR_SIGLE_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);//get id
			count = db.update(SchedularTableMetaData.TABLE_NAME, 
					          values, 
					          SchedularTableMetaData._ID 
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
