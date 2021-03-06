package com.mycode.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

public class ChooseAvailableDay extends Activity implements OnClickListener {

    private static final int PICK_AVAILABLE_STYLE_REQUEST = 10;
    private CalendarView mCalView = null;
    private Button mBtnChooseDate = null;
    private int mAvailableStyle = 0;
    private long mPickDate = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_calendar_choose);
        
        mCalView = (CalendarView)this.findViewById(R.id.calendarChoose);
        mBtnChooseDate = (Button)this.findViewById(R.id.btnChooseDate);
        
        mBtnChooseDate.setOnClickListener(this);
    }

   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        if (PICK_AVAILABLE_STYLE_REQUEST == requestCode){
            if (RESULT_OK == resultCode){

                boolean isAllDayStyle = intent.getBooleanExtra(ChooseAvailableType.TAG_ALLDAY, false);
                boolean isMorningStyle = intent.getBooleanExtra(ChooseAvailableType.TAG_MORNING, false);
                boolean isAfternoonStyle = intent.getBooleanExtra(ChooseAvailableType.TAG_AFTERNOON, false);
                boolean isEveningStyle = intent.getBooleanExtra(ChooseAvailableType.TAG_EVENING, false);
                
                
                mAvailableStyle = ((isAllDayStyle?1:0) << 3)
                        | ((isMorningStyle?1:0) << 2)
                        | ((isAfternoonStyle?1:0) << 1)
                        | (isEveningStyle?1:0);
                
        		mPickDate = mCalView.getDate();
                Log.d("AvailableDay", "PickDate"+mPickDate);
                
                SubmitRecord();
                        
            }
        }
        
        super.onActivityResult(requestCode, resultCode, intent);
        
    }

	private void SubmitRecord() {
		// TODO Auto-generated method stub
		
		
        mAvailableStyle = 0;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

        Intent intentAvailableType = new Intent(Intent.ACTION_VIEW);
        intentAvailableType.setClass(this, ChooseAvailableType.class);

        this.startActivityForResult(intentAvailableType, PICK_AVAILABLE_STYLE_REQUEST);
	}
}
