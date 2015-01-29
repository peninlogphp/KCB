package lipuka.android.model;

import kcb.android.Main;
import kcb.android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Notifications {

	NotificationManager notificationManager;
	Context context;
	Class<?> klass;
	public Notifications (NotificationManager notificationManager, Context context,
			Class<?> klass){
		this.notificationManager = notificationManager;
		this.context = context;
		this.klass = klass;
	}
	
    public void showResponseNotification(String msg, int id)  {
     
    	int icon = R.drawable.lipuka_icon;
    	CharSequence tickerText = "KCB Message";
    	long when = System.currentTimeMillis();
    	Notification notification = new Notification(icon, tickerText, when);
    	
    	CharSequence contentTitle = "KCB Message";
    	Intent notificationIntent = new Intent(context, klass);
    	notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    	PendingIntent contentIntent = 
    		PendingIntent.getActivity(context, 0, notificationIntent, 0);

    	notification.setLatestEventInfo(context, contentTitle, 
    			msg, contentIntent);
    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
    	notification.defaults |= Notification.DEFAULT_SOUND;
    	notificationManager.notify(id, notification);
    	
        }
}
