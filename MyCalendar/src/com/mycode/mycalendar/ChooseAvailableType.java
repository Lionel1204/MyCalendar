package com.mycode.mycalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;


public class ChooseAvailableType extends Activity implements OnClickListener{

    private CheckBox mCheckAllDay = null;
    private CheckBox mCheckMorning = null;
    private CheckBox mCheckAfternoon = null;
    private CheckBox mCheckEvening = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_available_type_choose);
        
        mCheckAllDay = (CheckBox)this.findViewById(R.id.checkAllDay);
        mCheckMorning = (CheckBox)this.findViewById(R.id.checkMorning);
        mCheckAfternoon = (CheckBox)this.findViewById(R.id.checkAfternoon);
        mCheckEvening = (CheckBox)this.findViewById(R.id.checkEvening);
        
        mCheckAllDay.setOnClickListener(this);
        mCheckMorning.setOnClickListener(this);
        mCheckAfternoon.setOnClickListener(this);
        mCheckEvening.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        allDayEnable();
    }

    
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        allDayEnable();
    }

    private void allDayEnable() {
        if(mCheckAllDay.isChecked()){
            mCheckMorning.setEnabled(false);
            mCheckAfternoon.setEnabled(false);
            mCheckEvening.setEnabled(false);
        }else{
            mCheckMorning.setEnabled(true);
            mCheckAfternoon.setEnabled(true);
            mCheckEvening.setEnabled(true);
        }
    }
}
