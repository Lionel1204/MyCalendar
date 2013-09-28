package com.mycode.mycalendar;

import com.mycode.mycalendar.SchedularProviderMetaData.SchedularTableMetaData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SchedularDbHelper extends SQLiteOpenHelper {

	public SchedularDbHelper(Context context) {
		super(context, 
			  SchedularProviderMetaData.DATABASE_NAME, 
			  null, 
			  SchedularProviderMetaData.DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS "
        		 + SchedularTableMetaData.TABLE_NAME
        		 + " ("
        		 + SchedularTableMetaData._ID
        		 + " INTEGER PRIMARY KEY,"
        		 + SchedularTableMetaData.SCHEDULAR_SUBJECT
        		 + " TEXT,"
        		 + SchedularTableMetaData.SCHEDULAR_FROM_DATE
        		 + " INTEGER,"
        		 + SchedularTableMetaData.SCHEDULAR_TO_DATE
        		 + " INTEGER,"
        		 + SchedularTableMetaData.SCHEDULAR_IS_ALL_DAY
        		 + " INTEGER,"
        		 + SchedularTableMetaData.SCHEDULAR_DESCRIPTION
        		 + " TEXT,"
        		 + SchedularTableMetaData.SCHEDULAR_IS_RECURRENCE
        		 + " INTEGER,"
        		 + SchedularTableMetaData.SCHEDULAR_RECURRENCE_STYLE
        		 + " INTEGER"
        		 + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + SchedularTableMetaData.TABLE_NAME);
		onCreate(db);
	}

}
