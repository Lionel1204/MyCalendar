package com.mycode.mycalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

public class CalendarPagerFragment extends Fragment {

   
    private static final String ARG_PAGE = "page";
    private int mMonthIndex;
    
    public static Fragment create(int position) {
        // TODO Auto-generated method stub
        CalendarPagerFragment fragment = new CalendarPagerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mMonthIndex = getArguments().getInt(ARG_PAGE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateView(inflater, container, savedInstanceState);
        TableRow tableRow;
        View cellView;
        TableLayout tableView = (TableLayout) inflater.inflate(R.layout.view_calendar_table, container, false);
        CalendarTableCellCalculator cellCalculator = new CalendarTableCellCalculator(getResources(), mMonthIndex);
        
        final int tableMaxRow = 6;
        final int tableMaxColumn = 8;
        for (int row=0; row<tableMaxRow; row++){
            tableRow = new TableRow(tableView.getContext());
            for (int column=0; column<tableMaxColumn; column++){
                cellView = cellCalculator.getView(row * tableMaxColumn + column, inflater, tableRow);
                cellView.setOnFocusChangeListener((OnFocusChangeListener) container.getContext());
                tableRow.addView(cellView);
            }
            tableView.addView(tableRow);
        }

        return tableView;
    }

    
}
