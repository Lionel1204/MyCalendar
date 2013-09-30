package com.mycode.mycalendar;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends ListActivity implements OnCheckedChangeListener{

    private ArrayList<HashMap<String, Object>> mListItems;
    private String[] mUserNames = new String[]{"Bob", "Jimmy", "Tim"};
    private String[] mItemControlsName = new String[]{"nameCheck", "txtUserName"};
    private int[] mListItemControls = new int[]{R.id.nameCheck, R.id.txtUserName};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_user_list);
        
        mListItems = new ArrayList<HashMap<String,Object>>(); 
        MySimpleAdapter adapter = new MySimpleAdapter( 
                this, 
                mListItems,
                R.layout.view_name_list,
                mItemControlsName,
                mListItemControls); 
        
        this.setListAdapter(adapter);
        
        for(int i=0;i<mUserNames.length;i++) {
            HashMap<String, Object>map = new HashMap<String, Object>();
            map.put(mItemControlsName[0], true);
            map.put(mItemControlsName[1], mUserNames[i]);
            mListItems.add(map);
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Intent intentChooseAvaliableDay = new Intent(Intent.ACTION_VIEW);
        intentChooseAvaliableDay.setClass(this, ChooseAvailableDay.class);

        this.startActivity(intentChooseAvaliableDay);
        //Toast.makeText(this, "position"+position+"id"+id, Toast.LENGTH_LONG).show();
    }
    
    private class MySimpleAdapter extends SimpleAdapter {

        public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource,
                String[] from, int[] to) {
            super(context, data, resource, from, to);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view =  super.getView(position, convertView, parent);
            if(view!=null) {
            CheckBox cb = (CheckBox) view.findViewById(R.id.nameCheck);
            cb.setTag(position);
            cb.setOnCheckedChangeListener(UserListActivity.this);
            }
            return view;
        }
    
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        int row = (Integer) buttonView.getTag();
        //Toast.makeText(this, "第"+row+"行的checkBox被点击", Toast.LENGTH_LONG).show();
    }


    
}

