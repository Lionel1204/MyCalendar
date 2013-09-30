
package com.mycode.mycalendar;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends FragmentActivity implements
    OnDateSetListener, OnMenuItemClickListener, OnFocusChangeListener {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private View mImgPreviousMonth;
    private View mImgNextMonth;
    private DateFormatter mFormatter;
    
    private TextView mTxtTitleGregorian;
    private TextView mTxtTitleAddition;
    private TextView mTxtTitleLunar;
    
    final static int mMonthAYear = 12;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mFormatter = new DateFormatter(this.getResources());
        mImgPreviousMonth = findViewById(R.id.imgPreviousMonth);
        mImgNextMonth = findViewById(R.id.imgNextMonth);
        mTxtTitleGregorian = (TextView)findViewById(R.id.txtTitleGreorian);
        mTxtTitleAddition = (TextView)findViewById(R.id.txtTitleAddition);
        mTxtTitleLunar = (TextView)findViewById(R.id.txtTitleLunar);
        
        mPager = (ViewPager)findViewById(R.id.pager);
        
        mPagerAdapter = new CalendarPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new simplePageChangeListener());
        mPager.setCurrentItem(getTodayMonthIndex());
    }

    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * onFocus when cell has/lose focus
     */
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        if(!hasFocus){
            return;
        }
        LunarCalendar lc = (LunarCalendar)v.getTag();
        CharSequence[] info = mFormatter.getFullDateInfo(lc);
        mTxtTitleLunar.setText(info[1]);
        mTxtTitleAddition.setText(info[0]);
        
    }

    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.menuGoto:
                int year = mPager.getCurrentItem() / mMonthAYear + LunarCalendar.getMinYear();
                int month = mPager.getCurrentItem() % mMonthAYear;
                DatePickerDialog dpd = new DatePickerDialog(
                        this,
                        android.R.style.Theme_DeviceDefault_DialogWhenLarge_NoActionBar, 
                        this,
                        year,
                        month,
                        1);
                dpd.getDatePicker().setCalendarViewShown(false);
                dpd.show();
                return true;
                //break;
            default:
                    break;
        }
        
        
        return false;
    }
    
    public void onMenuImageClick(View v){
        switch (v.getId()){
            case R.id.imgPreviousMonth:
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                break;
            case R.id.imgNextMonth:
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                break;
            case R.id.imgToday:
                mPager.setCurrentItem(getTodayMonthIndex());
                break;
            case R.id.imgPopupMenu:
                PopupMenu popup = new PopupMenu(this, v);
                popup.inflate(R.menu.main);
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            default:
                break;
        }
        
    }
    
    public void onCellClick(View v) {
        Toast.makeText(this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
        Intent intentUserList = new Intent(Intent.ACTION_VIEW);
        intentUserList.setClass(this, UserListActivity.class);
        LunarCalendar date = (LunarCalendar)v.getTag();
        long cellMillis = date.getTimeInMillis();
        //intentUserList.putExtra("CellDay", cellMillis);
        this.startActivity(intentUserList);
    }
    

    /**
     * has set the Date in Cell
     */
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // TODO Auto-generated method stub
        int offset = (year - LunarCalendar.getMinYear()) * mMonthAYear + monthOfYear;
        mPager.setCurrentItem(offset);
    }
    
    private int getTodayMonthIndex(){
        Calendar today = Calendar.getInstance();
        final int mMonthAYear = 12;
        int offset = (today.get(Calendar.YEAR) - LunarCalendar.getMinYear()) * mMonthAYear
                + today.get(Calendar.MONTH);
        return offset;
    }
    
    private class simplePageChangeListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            super.onPageSelected(position);
            
            StringBuilder title = new StringBuilder();
            title.append(LunarCalendar.getMinYear() + position / mMonthAYear);
            
            title.append('-');
            int month = position % mMonthAYear + 1;
            if (month < 10){
                title.append('0');
            }
            
            title.append(month);
            mTxtTitleGregorian.setText(title);
            mTxtTitleLunar.setText("");
            mTxtTitleAddition.setText("");
            
            if ( position < (mPagerAdapter.getCount() -1)
              && !mImgNextMonth.isEnabled()) {
                mImgNextMonth.setEnabled(true);
            }
            
            if (position > 0 && !mImgPreviousMonth.isEnabled()){
                mImgPreviousMonth.setEnabled(true);
            }
        }
        
    }

}
