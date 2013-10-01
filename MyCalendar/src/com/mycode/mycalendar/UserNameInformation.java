package com.mycode.mycalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class UserNameInformation extends Activity {

	public static final String TAG_PERSON_NAME = "PersonName";
	private TextView mTxtPersonName = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_information);
		
		mTxtPersonName = (TextView)this.findViewById(R.id.txtPersonName);
	}

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // TODO Auto-generated method stub
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.name_information_menu, menu);
	        return super.onCreateOptionsMenu(menu);
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // TODO Auto-generated method stub
	        switch(item.getItemId()){
	            case R.id.menu_action_submit:
                    Intent intent = new Intent();  
	                intent.putExtra(TAG_PERSON_NAME, mTxtPersonName.getText().toString());
	                setResult(RESULT_OK, intent);  
	                this.finish();
	                break;
	            default:
	                break;
	        }
	        return super.onOptionsItemSelected(item);
	    }

}
