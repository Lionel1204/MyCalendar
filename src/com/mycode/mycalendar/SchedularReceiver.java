package com.mycode.mycalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;


public class SchedularReceiver extends BroadcastReceiver {

    private static final String tag = "SchedularReceiver";
    private static final int mId = 0x01733;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String message = intent.getStringExtra("AlarmMessage");
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_notify_calendar)
                .setContentTitle(context.getText(R.string.notification_title))
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));  
                //.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        
        
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.cancel(mId);
            mNotificationManager.notify(mId, builder.build());
    }

}
