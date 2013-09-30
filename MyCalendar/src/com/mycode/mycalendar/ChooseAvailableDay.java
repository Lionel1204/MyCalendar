package com.mycode.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class ChooseAvailableDay extends Activity implements OnDateChangeListener {

    private CalendarView mCalView = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar_choose);
        
        mCalView = (CalendarView)this.findViewById(R.id.calendarChoose);
        
        mCalView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        // TODO Auto-generated method stub
        Intent intentAvailableType = new Intent(Intent.ACTION_VIEW);
        intentAvailableType.setClass(this, ChooseAvailableType.class);

        this.startActivity(intentAvailableType);
    }
    

}
