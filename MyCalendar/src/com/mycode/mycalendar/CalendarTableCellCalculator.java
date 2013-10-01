package com.mycode.mycalendar;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarTableCellCalculator {
    
    private static final int mTableColumnCount = 8;
    private static final int mMonthAYear = 12;
    private long mFirstDayMillis = 0;
    private int mSolarTermFirst = 0;
    private int mSolarTermSecond = 0;
    private DateFormatter mDateFormatter;

    public CalendarTableCellCalculator(Resources resources, int monthIndex){
        int year = LunarCalendar.getMinYear() + (monthIndex / mMonthAYear);
        int month = monthIndex % mMonthAYear;
        Calendar date = new GregorianCalendar(year, month, 1);
        int offset = 1 - date.get(Calendar.DAY_OF_WEEK);
        date.add(Calendar.DAY_OF_MONTH, offset);
        mFirstDayMillis = date.getTimeInMillis();
        //first solar term of a month
        mSolarTermFirst = LunarCalendar.getSolarTerm(year, month * 2 + 1);
        //second solar term
        mSolarTermSecond = LunarCalendar.getSolarTerm(year, month * 2 + 2);
        
        mDateFormatter = new DateFormatter(resources);
    }

    public View getView(Context context, int position, LayoutInflater inflater, ViewGroup container) {
        // TODO Auto-generated method stub
        ViewGroup rootView = null;
        LunarCalendar date = new LunarCalendar(mFirstDayMillis + (position - (position / 8) - 1) * LunarCalendar.DAY_MILLIS);
        
        //calculate the index of weeks in a year(first column)
        if (position % mTableColumnCount == 0){
            rootView = (ViewGroup) inflater.inflate(R.layout.view_calendar_week_index, container, false);
            TextView txtWeekIndex = (TextView)rootView.findViewById(R.id.txtWeekIndex);
            txtWeekIndex.setText(String.valueOf(date.getGregorianDate(Calendar.WEEK_OF_YEAR)));
            return rootView;
        }
        
        //calculate the date.
        boolean isFestival = false;
        boolean isSolarTerm = false;
        
        rootView = (ViewGroup)inflater.inflate(R.layout.view_calendar_day_cell, container, false);
        TextView txtCellGregorian = (TextView)rootView.findViewById(R.id.txtCellGregorian);
        TextView txtCellLunar = (TextView)rootView.findViewById(R.id.txtCellLunar);
        //TextView txtTitleAddition = (TextView)rootView.findViewById(R.id.txtTitleAddition);
        int gregorianDay = date.getGregorianDate(Calendar.DAY_OF_MONTH);
        
        //is the day out of this month?
        //lunar festival > GregorianFestival festival > month > solarTerm > Lunar Day
        boolean isOutOfRange = (position % mTableColumnCount != 0)
                            && (position < mTableColumnCount && gregorianDay > 7)
                            || (position > mTableColumnCount && gregorianDay < position - 7 -6);
        
        txtCellGregorian.setText(String.valueOf(gregorianDay));
        
        //display the festival information
        int index = date.getLunarFestival();
        if (index >= 0){
            //the festival of lunar
            txtCellLunar.setText(mDateFormatter.getLunarFestivalName(index));
            isFestival = true;
        }else{
            index = date.getGregorianFestival();
            if(index >=0){
                //the festival of Gregorian
                txtCellLunar.setText(mDateFormatter.getGregorianFestivalName(index));
                isFestival = true;
            }else if(date.getLunar(LunarCalendar.LUNAR_DAY) == 1){
                //the lunar first day in a month, should display the month
                txtCellLunar.setText(mDateFormatter.getMonthName(date));
            }else if(!isOutOfRange && gregorianDay == mSolarTermFirst){
                //the first solar term in a month
                txtCellLunar.setText(mDateFormatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2));
                isSolarTerm = true;
            }else if(!isOutOfRange && gregorianDay == mSolarTermSecond){
                //the second solar term in a month
                txtCellLunar.setText(mDateFormatter.getSolarTermName(date.getGregorianDate(Calendar.MONTH) * 2 + 1));
                isSolarTerm = true;
            }else{
                txtCellLunar.setText(mDateFormatter.getDayName(date));
            }
        }
        
        //set style
        Resources resources = container.getResources();
        if(isOutOfRange){
            rootView.setBackgroundResource(R.drawable.selector_calendar_outrange);
            txtCellGregorian.setTextColor(resources.getColor(R.color.color_calendar_outrange));
            txtCellLunar.setTextColor(resources.getColor(R.color.color_calendar_outrange));
        }else if (isFestival){
            txtCellLunar.setTextColor(resources.getColor(R.color.color_calendar_festival));
        }else if (isSolarTerm){
            txtCellLunar.setTextColor(resources.getColor(R.color.color_calendar_solarterm));
        }
        
        //is it weedend?
        if (position % mTableColumnCount == 1 || position % mTableColumnCount == 7){
            rootView.setBackgroundResource(R.drawable.selector_calendar_weekend);
        }
        
        ImageView imgCellHint = (ImageView) rootView.findViewById(R.id.imgCellHint);
        if (date.isToday()){
            imgCellHint.setBackgroundResource(R.drawable.img_hint_today);
            rootView.setBackgroundResource(R.drawable.shape_calendar_cell_today);
        }
        
        //is it busy?
        int totalUser = 3;
        int checkedUser = 0;
        
        Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
		
		String selection = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE
  		      + "=? ";
        String[] selectionArgs = {String.valueOf(date.getTimeInMillis())};

        
		Cursor cursor = context.getContentResolver().query(uri,
                null,
                selection,
                selectionArgs,
                null);
		
		int iAvalibleStyle = cursor
				.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE);
		
		if (cursor.moveToFirst()) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
				if(cursor.getInt(iAvalibleStyle) > 0){
					checkedUser++;
				}
			}
		}
		cursor.close();
		
        if ((totalUser - checkedUser) <= 1) {
        	imgCellHint.setImageDrawable(rootView.getResources().getDrawable(
					R.drawable.img_cell_green));
		} else if ((checkedUser * 2) >= totalUser) {

			imgCellHint.setImageDrawable(rootView.getResources().getDrawable(
					R.drawable.img_cell_yellow));
		} else if ((checkedUser * 2) < totalUser) {
			imgCellHint.setImageDrawable(rootView.getResources().getDrawable(
					R.drawable.img_cell_red));
		}
        
        //store date into tag
        rootView.setTag(date);
        return rootView;
    }

}
