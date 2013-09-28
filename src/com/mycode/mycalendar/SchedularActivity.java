package com.mycode.mycalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class SchedularActivity extends Activity {

    private Calendar mCalendar = null;
    
    private ContentResolver mResolver = null;
    
    private TextView mTxtFromDateView = null;
    private TextView mTxtToDateView = null;
    private TextView mTxtFromTimeView = null;
    private TextView mTxtToTimeView = null;
    private TextView mTxtToSubjectView = null;
    private TextView mTxtToDescriptionView = null;
    private CheckBox mCheckAllDay = null;
    private CheckBox mCheckRecurrence = null;
    private Spinner mSpinnerRecurrenceStyle = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar_schedule_info);
        
        //ActionBar actionBar = getActionBar();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        mCalendar = Calendar.getInstance();
        mResolver = this.getContentResolver();
        initControls();
        
    }

    private void initControls() {
        // TODO Auto-generated method stub
        mTxtFromDateView = (TextView)this.findViewById(R.id.editFromDate);
        mTxtToDateView = (TextView)this.findViewById(R.id.editToDate);
        mTxtFromTimeView = (TextView)this.findViewById(R.id.editFromTime);
        mTxtToTimeView = (TextView)this.findViewById(R.id.editToTime);
        mTxtToSubjectView = (TextView)this.findViewById(R.id.editSubject);
        mTxtToDescriptionView = (TextView)this.findViewById(R.id.editDescription);
        mCheckAllDay = (CheckBox)this.findViewById(R.id.checkAllDay);
        mCheckRecurrence = (CheckBox)this.findViewById(R.id.checkRecurrence);
        mSpinnerRecurrenceStyle = (Spinner)this.findViewById(R.id.spinnerRecurrenceStyle);
        
        ArrayAdapter<CharSequence> adapterRecurrenceStyle = ArrayAdapter.createFromResource( this, 
        		                                                                             R.array.recurrence_style,
        		                                                                             android.R.layout.simple_spinner_item);
        adapterRecurrenceStyle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        mSpinnerRecurrenceStyle.setAdapter(adapterRecurrenceStyle);
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
                //Toast.makeText(this, "check Item is pressed", Toast.LENGTH_SHORT).show();
            	Intent intentServer = new Intent(this, SchedularService.class); 
            	startService(intentServer);
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

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
    	updateControls();
        super.onStart();
    }

    private void updateControls() {
		// TODO Auto-generated method stub
		//get data from Provider
    	Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
    	String[] projection = new String []{
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_SUBJECT,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_FROM_DATE,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_TO_DATE,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_ALL_DAY,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DESCRIPTION,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_IS_RECURRENCE,
    			SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_RECURRENCE_STYLE
    	};
/*    	
    	ContentValues values=new ContentValues();
        //添加学生信息
        values.put(Student.NMAE, "Jack");
        values.put(Student.GENDER, "男");
        values.put(Student.AGE, 20);

        mResolver.insert(uri, values);
  */  	
    	Cursor cursor = mResolver.query(uri, projection, null, null, null);
    	
    	//int curCount = cursor.getCount();
    	if(cursor.moveToFirst()){
    		while (cursor.moveToNext()){
    			Log.d("SchedualrAcitivity", "id"+cursor.getInt(0)+"subject"+cursor.getString(1));
    		}
    	}
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
                                                     mCalendar.get(Calendar.HOUR),
                                                     mCalendar.get(Calendar.MINUTE),
                                                     true);
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
            
            updateView();
        }

        private void updateView() {
            // TODO Auto-generated method stub
            DateFormat dateFormat = DateFormat.getDateInstance();
            if(mView == mTxtFromDateView){
                mTxtFromDateView.setText(dateFormat.format(mCalendar.getTime()));
            }else if(mView == mTxtToDateView){
                mTxtToDateView.setText(dateFormat.format(mCalendar.getTime()));
            }
        }
    }
    
    private class TimeListener implements OnTimeSetListener {
        private View mView;

        public TimeListener(View view) {
            mView = view;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mCalendar.set(Calendar.HOUR, hourOfDay); 
            mCalendar.set(Calendar.MINUTE, minute); 
            
            updateView();
        }

        private void updateView() {
            // TODO Auto-generated method stub
            DateFormat dateFormat = DateFormat.getTimeInstance();
            if(mView == mTxtFromTimeView){
                mTxtFromTimeView.setText(dateFormat.format(mCalendar.getTime()));
            }else if(mView == mTxtToTimeView){
                mTxtToTimeView.setText(dateFormat.format(mCalendar.getTime()));
            }
        }
    }
}
