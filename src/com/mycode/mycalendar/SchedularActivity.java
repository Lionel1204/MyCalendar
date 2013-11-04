package com.mycode.mycalendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Calendar;

public class SchedularActivity extends Activity {

    private Calendar mCalendar = null;
    private long mCellDate = 0;
    private boolean mHasRecord = false;
    private ContentResolver mResolver = null;
    
    private TextView mTxtFromDateView = null;
    private TextView mTxtToDateView = null;
    private TextView mTxtFromTimeView = null;
    private TextView mTxtToTimeView = null;
    private TextView mTxtSubjectView = null;
    private TextView mTxtDescriptionView = null;
    private CheckBox mCheckAllDay = null;
    private CheckBox mCheckRecurrence = null;
    private Spinner mSpinnerRecurrenceStyle = null;
    
    private static final String YEAR = "Year";
    private static final String MONTH = "Month";
    private static final String DAY = "Day";
    private static final String HOUR = "Hour";
    private static final String MINUTE = "Minius";
    private static final int intentRequestCode = 0x00103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar_schedule_info);
        
        //ActionBar actionBar = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mCalendar = Calendar.getInstance();
        mResolver = this.getContentResolver();
        mCellDate = this.getIntent().getLongExtra("CellDay", 0);
        initControls();
        
    }

    private void initControls() {
        // TODO Auto-generated method stub
        mTxtFromDateView = (TextView)this.findViewById(R.id.editFromDate);
        mTxtToDateView = (TextView)this.findViewById(R.id.editToDate);
        mTxtFromTimeView = (TextView)this.findViewById(R.id.editFromTime);
        mTxtToTimeView = (TextView)this.findViewById(R.id.editToTime);
        mTxtSubjectView = (TextView)this.findViewById(R.id.editSubject);
        mTxtDescriptionView = (TextView)this.findViewById(R.id.editDescription);
        mCheckAllDay = (CheckBox)this.findViewById(R.id.checkAllDay);
        mCheckRecurrence = (CheckBox)this.findViewById(R.id.checkRecurrence);
        mSpinnerRecurrenceStyle = (Spinner)this.findViewById(R.id.spinnerRecurrenceStyle);
        
        ArrayAdapter<CharSequence> adapterRecurrenceStyle = ArrayAdapter.createFromResource( this, 
        		                                                                             R.array.recurrence_style,
        		                                                                             android.R.layout.simple_spinner_item);
        adapterRecurrenceStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        mSpinnerRecurrenceStyle.setAdapter(adapterRecurrenceStyle);
        
        if (mCellDate != 0){
            DateFormat dateFormat = DateFormat.getDateInstance();
            mCalendar.setTimeInMillis(mCellDate);
            mTxtFromDateView.setText(dateFormat.format(mCalendar.getTime()));
            Bundle bundle = new Bundle();
            bundle.putInt(YEAR, mCalendar.get(Calendar.YEAR));
            bundle.putInt(MONTH, mCalendar.get(Calendar.MONTH));
            bundle.putInt(DAY, mCalendar.get(Calendar.DAY_OF_MONTH));
            
            mTxtFromDateView.setTag(bundle);
            mTxtFromDateView.setEnabled(false);
        }
        
        //close ime
        mTxtFromDateView.setInputType(EditorInfo.TYPE_NULL);
        mTxtToDateView.setInputType(EditorInfo.TYPE_NULL);
        mTxtFromTimeView.setInputType(EditorInfo.TYPE_NULL);
        mTxtToTimeView.setInputType(EditorInfo.TYPE_NULL);
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.menu_action_check:
            	//TODO add start service with content provider
                SaveSchedular();
                this.finish();
                break;
            case R.id.menu_action_delete:
            	DeleteSchedular();
            	this.finish();
            	break;
            case android.R.id.home:
            	Intent intentBackhome = new Intent(this, MainActivity.class);  
            	intentBackhome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
                startActivity(intentBackhome);  
            	//Toast.makeText(this, "home Item is pressed", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteSchedular() {
        // TODO Auto-generated method stub
        if (!mHasRecord){
            return;
        }
            
        Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
        DateFormat dateFormat = DateFormat.getDateInstance();
        String where = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT + "=?";
        String[] whereClause = {dateFormat.format(mCalendar.getTime())};
        
        mResolver.delete(uri, where, whereClause);
    }

    private void SaveSchedular() {
        // TODO Auto-generated method stub
        Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
        ContentValues values = new ContentValues();
        
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_SUBJECT, mTxtSubjectView.getText().toString());
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT, mTxtFromDateView.getText().toString());
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE, (long)ConvertControlsDate2Millis(mTxtFromDateView, mTxtFromTimeView));
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_TO_DATE, (long)ConvertControlsDate2Millis(mTxtToDateView, mTxtToTimeView));
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_ALL_DAY, mCheckAllDay.isChecked());
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DESCRIPTION, (String)mTxtDescriptionView.getText().toString());
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_RECURRENCE, mCheckRecurrence.isChecked());
        values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_RECURRENCE_STYLE, (int)mSpinnerRecurrenceStyle.getSelectedItemPosition());
        if (mHasRecord){
            DateFormat dateFormat = DateFormat.getDateInstance();
            String where = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT + "=?";
            String[] whereClause = {dateFormat.format(mCalendar.getTime())};
            
            mResolver.update(uri, values, where, whereClause);
        }else{
            mResolver.insert(uri, values);
        }
        
        setAlarm(values);
        
    }

    private void setAlarm(ContentValues values) {
        // TODO Auto-generated method stub
        AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        
        Intent alarmIntent = new Intent(this, SchedularReceiver.class);
        alarmIntent.putExtra("AlarmMessage", values.getAsString(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_SUBJECT));
        PendingIntent pIntent = PendingIntent.getBroadcast(this, intentRequestCode, alarmIntent, 0);
        
        long triggerAtMillis = values.getAsLong(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE);
        am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pIntent);
    }

    private long ConvertControlsDate2Millis(TextView dateView, TextView timeView) {
        // TODO Auto-generated method stub
        int year   = 0;
        int month  = 0;
        int day    = 0;
        int hour   = 0;
        int minute = 0;
        int second = 0;
        
        Bundle dateBundle = (Bundle)dateView.getTag();
        if(null != dateBundle){
            year = dateBundle.getInt(YEAR, 0);
            month = dateBundle.getInt(MONTH, 0);
            day = dateBundle.getInt(DAY, 0);
        }
        
        Bundle timeBundle = (Bundle)timeView.getTag();
        if(null != timeBundle){
            hour = timeBundle.getInt(HOUR, 0);
            minute = timeBundle.getInt(MINUTE, 0);
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
    	updateControls();
        super.onStart();
    }

    private void updateControls() {
        // TODO Auto-generated method stub
        // get data from Provider
        Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
        
        String[] projection = new String[] {
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_SUBJECT,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_TO_DATE,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_ALL_DAY,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DESCRIPTION,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_RECURRENCE,
                SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_RECURRENCE_STYLE
        };

        mCalendar.setTimeInMillis(mCellDate);
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();
        String selection = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT + "=?";
        String[] selectionArgs = {dateFormat.format(mCalendar.getTime())};

    	Cursor cursor = mResolver.query(uri,
    	                                null,
    	                                selection,
    	                                selectionArgs,
    	                                null);

    	if (cursor.moveToFirst()){
    	    mHasRecord = true;
            //get the index of column in cursor, it is different from the database
    	    int iSubject = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_SUBJECT);
    	    //int iFromDateText = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE_TEXT);
    	    int iFromDate = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE);
    	    int iToDate = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_TO_DATE);
    	    int iIsRecurrence = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_RECURRENCE);
    	    int iRecurrenceStyle = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_RECURRENCE_STYLE);
    	    int iDescription = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DESCRIPTION);
    	    int iIsAllDay = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_ALL_DAY);
    	    
    	    //update the UI
    	    mTxtSubjectView.setText(cursor.getString(iSubject));
    	    mTxtDescriptionView.setText(cursor.getString(iDescription));
    	    mCheckAllDay.setChecked( 0 != cursor.getInt(iIsAllDay));
    	    if (mCheckAllDay.isChecked()){
    	        mTxtFromTimeView.setEnabled(false);
    	        mTxtToTimeView.setEnabled(false);
    	    }
    	    mCheckRecurrence.setChecked( 0 != cursor.getInt(iRecurrenceStyle));
    	    mSpinnerRecurrenceStyle.setSelection(cursor.getInt(iRecurrenceStyle));

    	    if(!mCheckRecurrence.isChecked()){
    	        mSpinnerRecurrenceStyle.setEnabled(false);
    	    }
    	    
    	    
    	    long milliFromDate = cursor.getLong(iFromDate);
    	    long milliToDate = cursor.getLong(iToDate);
    	    Calendar calendar = Calendar.getInstance();
    	    
    	    calendar.setTimeInMillis(milliFromDate);
    	    mTxtFromTimeView.setText(timeFormat.format(calendar.getTime()));
    	    
    	    calendar.setTimeInMillis(milliToDate);
    	    mTxtToDateView.setText(dateFormat.format(calendar.getTime()));
            mTxtToTimeView.setText(timeFormat.format(calendar.getTime()));
            }else{
                mHasRecord = false;
            }
    	cursor.close();

	}

	@Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        
        super.onStop();
    }

    public void onDateEditClick(View v){
        DatePickerDialog dpd = new DatePickerDialog( this, 
                                                     new DateListener(v),
                                                     mCalendar.get(Calendar.YEAR),
                                                     mCalendar.get(Calendar.MONTH),
                                                     mCalendar.get(Calendar.DAY_OF_MONTH));
        dpd.show();
         
    }
    
    public void onTimeEditClick(View v){
        TimePickerDialog tpd = new TimePickerDialog( this,
                                                     new TimeListener(v),
                                                     mCalendar.get(Calendar.HOUR_OF_DAY),
                                                     mCalendar.get(Calendar.MINUTE),
                                                     false);
        tpd.show();
    }
    
    public void onAllDayClick(View v) {
        mTxtToTimeView.setEnabled(!mCheckAllDay.isChecked());
        mTxtFromTimeView.setEnabled(!mCheckAllDay.isChecked());
    }
    
    public void onRecurrenceClick(View v){
        mSpinnerRecurrenceStyle.setEnabled(mCheckRecurrence.isChecked());
    }
    
    private class DateListener implements OnDateSetListener {
        private View mView;

        public DateListener(View view) {
            mView = view;
        }

        public void onDateSet(DatePicker view, int year, int month, int monthDay) {
            mCalendar.set(Calendar.YEAR, year); 
            mCalendar.set(Calendar.MONTH, month); 
            mCalendar.set(Calendar.DAY_OF_MONTH, monthDay); 
            
            updateView(year, month, monthDay);
        }

        private void updateView(int year, int month, int monthDay) {
            // TODO Auto-generated method stub
            DateFormat dateFormat = DateFormat.getDateInstance();
            
            if(mView == mTxtFromDateView){
                mTxtFromDateView.setText(dateFormat.format(mCalendar.getTime()));
                
                Bundle bundle = new Bundle();
                bundle.putInt(YEAR, year);
                bundle.putInt(MONTH, month);
                bundle.putInt(DAY, monthDay);
                
                mTxtFromDateView.setTag(bundle);
                
            }else if(mView == mTxtToDateView){
                mTxtToDateView.setText(dateFormat.format(mCalendar.getTime()));
                
                Bundle bundle = new Bundle();
                bundle.putInt(YEAR, year);
                bundle.putInt(MONTH, month);
                bundle.putInt(DAY, monthDay);
                
                mTxtToDateView.setTag(bundle);
            }
        }
    }
    
    private class TimeListener implements OnTimeSetListener {
        private View mView;

        public TimeListener(View view) {
            mView = view;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay); 
            mCalendar.set(Calendar.MINUTE, minute); 
            
            updateView(hourOfDay, minute);
        }

        private void updateView(int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            DateFormat dateFormat = DateFormat.getTimeInstance();
            if(mView == mTxtFromTimeView){
                mTxtFromTimeView.setText(dateFormat.format(mCalendar.getTime()));
                Bundle bundle = new Bundle();
                bundle.putInt(HOUR, hourOfDay);
                bundle.putInt(MINUTE, minute);
                
                mTxtFromTimeView.setTag(bundle);
            }else if(mView == mTxtToTimeView){
                mTxtToTimeView.setText(dateFormat.format(mCalendar.getTime()));
                Bundle bundle = new Bundle();
                bundle.putInt(HOUR, hourOfDay);
                bundle.putInt(MINUTE, minute);
                
                mTxtToTimeView.setTag(bundle);
            }
        }
    }
}
