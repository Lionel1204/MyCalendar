package com.mycode.mycalendar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class SchedularActivity extends Activity {

    private Calendar mCalendar = Calendar.getInstance();
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
                Toast.makeText(this, "check Item is pressed", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                Toast.makeText(this, "home Item is pressed", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
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

        @Override
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
