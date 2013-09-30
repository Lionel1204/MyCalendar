package com.mycode.mycalendar;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListActivity extends ListActivity implements OnCheckedChangeListener{

	private static final int PICK_AVAILABLE_STYLE_REQUEST = 10;
	
    private ArrayList<HashMap<String, Object>> mListItems;
    private ListView mListView = null;
    private String[] mUserNames = new String[]{"Bob", "Jimmy", "Tim"};
    private String[] mItemControlsName = new String[]{"nameCheck", "txtUserName"};
    private int[] mListItemControls = new int[]{R.id.nameCheck, R.id.txtUserName};
    private int mAvailableStyle = 0;
    private long mPickDate = 0;
    private long mItemId = 0;
    private boolean mHasRecord = false;
    private ContentResolver mResolver = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.view_user_list);
         
        mListItems = new ArrayList<HashMap<String,Object>>(); 
        

        /*
        for(int i=0;i<mUserNames.length;i++) {
            HashMap<String, Object>map = new HashMap<String, Object>();
            map.put(mItemControlsName[0], false);
            map.put(mItemControlsName[1], mUserNames[i]);
            mListItems.add(map);
        }
        */
        
        Intent intent = this.getIntent();
        mPickDate = intent.getLongExtra(MainActivity.CHOOSED_DAY, System.currentTimeMillis());
        mResolver = this.getContentResolver();
        mListView = getListView();
        
        //insertARecord();
        UpdateControl();
    }
    
    private void insertARecord() {
		// TODO Auto-generated method stub
    	 Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
	     ContentValues values = new ContentValues();
	     values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_ID, 1);
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_NAME, "Jim");
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE, mPickDate);
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE, 4);
		 mResolver.insert(uri, values);
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
    	
		super.onStart();
	}

	private void UpdateControl() {
		// TODO Auto-generated method stub
		Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
		
		String selection = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE
  		      + "=? ";
        String[] selectionArgs = {String.valueOf(mPickDate)};

        
		Cursor cursor = mResolver.query(uri,
                null,
                selection,
                selectionArgs,
                null);
		
		int iUserId = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_ID);
		int iUserName = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_NAME);
		int iDate = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE);
		int iAvalibleStyle = cursor.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE);
		
		int[] views = new int[]{R.id.txtUserName, R.id.nameCheck};
		String[] columns = new String[]{SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_NAME,
				SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE};
		
		
		
        MySimpleAdapter adapter = new MySimpleAdapter( 
                this, 
                mListItems,
                R.layout.view_name_list,
                columns,
                views); 
       
        this.setListAdapter(adapter);
		
        
		if (cursor.moveToFirst()) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				long index = cursor.getLong(iUserId);
				int avalibalStyle = cursor.getInt(iAvalibleStyle);
				String string = cursor.getString(iUserName);
				map.put(columns[0], cursor.getString(iUserName));
				map.put(columns[1],
						(boolean) (0 != cursor.getInt(iAvalibleStyle)));
				mListItems.add(map);

			}
		}
		cursor.close();
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
                
                
                SubmitRecord();
            }
        }
        
        super.onActivityResult(requestCode, resultCode, intent);
        
    }

	private void SubmitRecord() {
		// TODO Auto-generated method stub
		 Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
	     ContentValues values = new ContentValues();
	     values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_ID, mItemId);
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_NAME, mUserNames[(int) mItemId]);
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE, mPickDate);
		 values.put(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE, mAvailableStyle);
		 
         mAvailableStyle = 0;
         
         if (mHasRecord){
             
             String where = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_ID 
            		      + "=? AND"
            		      + SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_DATE
            		      + "=? ";
             String[] whereClause = {String.valueOf(mItemId), String.valueOf(mPickDate)};
             
             mResolver.update(uri, values, where, whereClause);
         }else{
             mResolver.insert(uri, values);
         }
	}
	

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        mItemId = id;
        Intent intentChooseAvailableType = new Intent(Intent.ACTION_VIEW);
        intentChooseAvailableType.setClass(this, ChooseAvailableType.class);

        //this part should be in the work thread
        Uri uri = SchedularProviderMetaData.SchedularTableMetaData.CONTENT_URI;
		
		String selection = SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_USER_ID
  		      + "=? ";
        String[] selectionArgs = {String.valueOf(id)};

        
		Cursor cursor = mResolver.query(uri,
                null,
                selection,
                selectionArgs,
                null);
		
		int iAvalibleStyle = cursor
				.getColumnIndex(SchedularProviderMetaData.SchedularTableMetaData.SCHEDULAR_AVAILABLE_STYLE);
		int avalibleStyle = 0;
		if (cursor.moveToFirst()) {
			mHasRecord = true;
			avalibleStyle = cursor.getInt(iAvalibleStyle);
		}
		cursor.close();
		
		boolean isAllDayStyle = (boolean)((avalibleStyle & 0x01000) != 0);
        boolean isMorningStyle = (boolean)((avalibleStyle & 0x0100) != 0);
        boolean isAfternoonStyle = (boolean)((avalibleStyle & 0x010) != 0);
        boolean isEveningStyle = (boolean)((avalibleStyle & 0x01) != 0);
        
		intentChooseAvailableType.putExtra(ChooseAvailableType.TAG_ALLDAY, isAllDayStyle);
		intentChooseAvailableType.putExtra(ChooseAvailableType.TAG_MORNING, isMorningStyle);
		intentChooseAvailableType.putExtra(ChooseAvailableType.TAG_AFTERNOON, isAfternoonStyle);
		intentChooseAvailableType.putExtra(ChooseAvailableType.TAG_EVENING, isEveningStyle);
		
        this.startActivityForResult(intentChooseAvailableType, PICK_AVAILABLE_STYLE_REQUEST);
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

