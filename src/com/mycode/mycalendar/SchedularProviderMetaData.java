package com.mycode.mycalendar;

import android.net.Uri;
import android.provider.BaseColumns;

public class SchedularProviderMetaData {
	public static final String AUTHORITY = "com.mycode.mycalendar.SchedularProvider";
	
	public static final String DATABASE_NAME = "MyCalendarSchedular.db";
	public static final int DATABASE_VERSION = 1;
	public static final String SCHEDULAR_TABLE_NAME = "CalendarSchedular";
	
	private SchedularProviderMetaData() {}
	
	//inner class describing SchedularTable
	public static final class SchedularTableMetaData implements BaseColumns {
		private SchedularTableMetaData() {}
		
		public static final String TABLE_NAME = SCHEDULAR_TABLE_NAME;
		
		//uri and MIME type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/CalendarSchedular");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.mycode.mycalendar";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.mycode.mycalendar";
		
		public static final String DEFAULT_SORT_ORDER = "_id DESC";
		
		//additional Columns start here
		public static final String SCHEDULAR_SUBJECT = "Subject";
		public static final String SCHEDULAR_FROM_DATE = "FromDate";
		//public static final String SCHEDULAR_FROM_TIME = "FromTime";//may not use time field
		public static final String SCHEDULAR_TO_DATE = "ToDate";
		//public static final String SCHEDULAR_TO_TIME = "ToTime";//may not use time field
		public static final String SCHEDULAR_IS_ALL_DAY = "isAllDay";
		public static final String SCHEDULAR_DESCRIPTION = "Description";
		public static final String SCHEDULAR_IS_RECURRENCE = "isRecurrence";
		public static final String SCHEDULAR_RECURRENCE_STYLE = "RecurrenceStyle";
		
		//Database version information
		public static final String CREATED_DATE = "created";
		public static final String MODIFIED_DATE = "modified";
		
	}

}
