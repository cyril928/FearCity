package tw.edu.ntu.csie.srlab;

import tw.edu.ntu.csie.srlab.R;
import tw.edu.ntu.csie.srlab.R.drawable;
import tw.edu.ntu.csie.srlab.activity.ShowMemberMaps;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationProvider {
	
	private Context mContext;
	private NotificationManager mNotificationManager;
	private String ns = Context.NOTIFICATION_SERVICE;
	private Notification notification;
	private static final int HELLO_ID = 1;
	
	public NotificationProvider(Context context) {		
		mContext = context;
		mNotificationManager = (NotificationManager) mContext.getSystemService(ns);
		setup();
	}
	
	
	/*set icon, tickerText for the notification*/
	private void setup() {
		int icon = R.drawable.notification_icon;
		CharSequence tickerText = "Hello";
		long when = System.currentTimeMillis();
		notification = new Notification();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
	}
	
	/*set title, message for the notification in the extended window
	 an create intent for it*/
	public void setContent(String title, String text) {
		CharSequence contentTitle = title;
		CharSequence contentText = text;
		
		Intent notificationIntent = new Intent(mContext, ShowMemberMaps.class);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, null, 0);

		notification.setLatestEventInfo(mContext, contentTitle, contentText, contentIntent);		
	}
	
	/* send to  NotificationManager which can notify it */
	public void startNotify(int id) {
		mNotificationManager.notify(id, notification);
	}
}
