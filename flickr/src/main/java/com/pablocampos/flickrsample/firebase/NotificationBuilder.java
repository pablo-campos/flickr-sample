package com.pablocampos.flickrsample.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import com.pablocampos.flickrsample.R;

import java.util.Date;

import androidx.core.app.NotificationCompat;

public class NotificationBuilder {


	private final int NOTIFICATION_ID = 0;

	private static final String NOTIFICATION_CHANNEL_ID = "fcm_default_channel";
	private static final String GROUP_KEY_BANKING_ALERT_SUFFIX = ".BANKING_ALERT";

	public static final String EXTRA_IS_PUSH_ALERT_OFF = "com.malauzai.extra.SET_PUSH_ALERT_OFF";
	public static final String EXTRA_IS_PUSH_SOUND_ENABLED = "com.malauzai.extra.IS_PUSH_SOUND_ENABLED";
	public static final String EXTRA_IS_PUSH_VIBRATION_ENABLED = "com.malauzai.extra.IS_PUSH_VIBRATION_ENABLED";




	private Context context;
	private String versionName;
	private NotificationManager notificationManager;




	public NotificationBuilder(final Context context){
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		try {
			versionName = context.getPackageName();
		} catch (Exception e) {
			versionName = "com.malauzai.orgID";
		}
	}


	public void buildNotification(final String title, final String message, final Intent intent){

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		final boolean isPushTurnedOff = sharedPreferences.getBoolean(EXTRA_IS_PUSH_ALERT_OFF, false);

		if (!isPushTurnedOff) {
			final boolean isPushSoundEnabled = sharedPreferences.getBoolean(EXTRA_IS_PUSH_SOUND_ENABLED, false);
			final boolean isPushVibrationEnabled = sharedPreferences.getBoolean(EXTRA_IS_PUSH_VIBRATION_ENABLED, false);

			long[] vibratePattern = { 0, 0, 0, 0, 0 };
			Uri notificationSound = null;
			int ledColor = 0;
			int icon = R.drawable.ic_launcher_foreground;

			//For OS levels 21 and greater, use transparent notification icon.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				//icon = com.malauzai.R.drawable.app_alert;
			}

			if (isPushSoundEnabled) {
				notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			}
			if (isPushVibrationEnabled) {
				vibratePattern = new long[] { 0, 500, 500 };
			}

			PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, 0);
			final NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

			Notification notification = builder.setContentIntent(pendingIntent).
					setContentTitle(title).
					setContentText(message).
					setSmallIcon(icon).
					setGroup(versionName + GROUP_KEY_BANKING_ALERT_SUFFIX).
					setPriority(NotificationCompat.PRIORITY_HIGH).
					setAutoCancel(true).
					setLights(ledColor, 1000, 1000).
					setVibrate(vibratePattern).
					setSound(notificationSound).
					setStyle(new NotificationCompat.BigTextStyle()).
					build();

			notificationManager.notify(createID(), notification);
		}
	}


	private int createID(){
		Date now = new Date();
		return (int) now.getTime();
	}
}
