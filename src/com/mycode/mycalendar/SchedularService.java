package com.mycode.mycalendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SchedularService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
