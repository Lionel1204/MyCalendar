package com.mycode.mycalendar;

import android.net.Uri;
import android.provider.BaseColumns;

public class SchedularProviderMetaData {
	public static final String AUTHORITY = "com.mycode.mycalendar.SchedularProvider";
	public static final String AUTHORITY_NAME = "com.mycode.mycalendar.UserNameProvider";
	
	public static final String DATABASE_NAME = "MyCalendarSchedular.db";
	public static final int DATABASE_VERSION = 1;
	public static final String SCHEDULAR_TABLE_NAME = "CalendarSchedular";
	
	private SchedularProviderMetaData() {}
	
	//inner class describing SchedularTable
	public static final class SchedularTableMetaData implements BaseColumns {
		private SchedularTableMetaData() {}
		
		public static final String TABLE_NAME_SCHEDULAR = SCHEDULAR_TABLE_NAME;
	
		
		//uri and MIME type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/CalendarSchedular");
		
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mycode.mycalendar";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mycode.mycalendar";
		
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		
		//additional Columns start here
		public static final String SCHEDULAR_USER_ID = "UserId";
		public static final String SCHEDULAR_USER_NAME = "UserName";
		public static final String SCHEDULAR_DATE = "Date";
		public static final String SCHEDULAR_AVAILABLE_STYLE = "AvailableStyle";
		
		//Database version information
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
	}
	
	public static final class UserNameTableMetaData implements BaseColumns {
		public static final String TABLE_NAME_USER_NAME = "UserNameTable";
		
		public static final Uri CONTENT_URI_NAME = Uri.parse("content://" + AUTHORITY_NAME + "/UserNameTable");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mycode.mycalendar";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mycode.mycalendar";
		
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		
		public static final String COLUMN_USER_NAME = "UserName";
		
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		
	}

}
