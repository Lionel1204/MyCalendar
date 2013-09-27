package com.mycode.mycalendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    public CalendarPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return CalendarPagerFragment.create(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        final int monthAYear = 12;
        int years = LunarCalendar.getMaxYear() - LunarCalendar.getMinYear();
        return years * monthAYear;
    }

}
