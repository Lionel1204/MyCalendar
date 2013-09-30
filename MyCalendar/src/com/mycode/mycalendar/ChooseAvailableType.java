package com.mycode.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;


public class ChooseAvailableType extends Activity implements OnClickListener{

    public static final String TAG_ALLDAY = "AvailableType_AllDay";
    public static final String TAG_MORNING = "AvailableType_Morning";
    public static final String TAG_AFTERNOON = "AvailableType_Afternoon";
    public static final String TAG_EVENING = "AvailableType_Evening";
    
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
        
        Intent intent = this.getIntent();
        mCheckAllDay.setChecked(intent.getBooleanExtra(TAG_ALLDAY, true));
        mCheckMorning.setChecked(intent.getBooleanExtra(TAG_MORNING, true));
        mCheckAfternoon.setChecked(intent.getBooleanExtra(TAG_AFTERNOON, true));
        mCheckEvening.setChecked(intent.getBooleanExtra(TAG_EVENING, true)); 
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        CheckAllDayEnable();
    }
    
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    	CheckAllDayEnable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.choose_style_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.menu_action_ok:
                
                
                Intent intent = new Intent();  
                intent.putExtra(TAG_ALLDAY, mCheckAllDay.isChecked());
                intent.putExtra(TAG_MORNING, mCheckMorning.isChecked());
                intent.putExtra(TAG_AFTERNOON, mCheckAfternoon.isChecked());
                intent.putExtra(TAG_EVENING, mCheckEvening.isChecked());
                setResult(RESULT_OK, intent);  
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void CheckAllDayEnable() {
        
        if (mCheckMorning.isChecked()
                && mCheckAfternoon.isChecked()
                && mCheckEvening.isChecked()) {
            mCheckAllDay.setChecked(true);
            mCheckMorning.setChecked(false);
            mCheckAfternoon.setChecked(false);
            mCheckEvening.setChecked(false);
            mCheckMorning.setEnabled(false);
            mCheckAfternoon.setEnabled(false);
            mCheckEvening.setEnabled(false);
            
        } else {

            if (mCheckAllDay.isChecked()) {
                mCheckMorning.setChecked(false);
                mCheckAfternoon.setChecked(false);
                mCheckEvening.setChecked(false);
                mCheckMorning.setEnabled(false);
                mCheckAfternoon.setEnabled(false);
                mCheckEvening.setEnabled(false);
            } else {
                mCheckMorning.setEnabled(true);
                mCheckAfternoon.setEnabled(true);
                mCheckEvening.setEnabled(true);
            }
        }

    }
}
