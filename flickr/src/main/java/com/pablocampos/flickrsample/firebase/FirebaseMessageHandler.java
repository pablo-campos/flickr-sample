package com.pablocampos.flickrsample.firebase;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import activity.FlickrActivity;

public class FirebaseMessageHandler extends FirebaseMessagingService {

	private static final String NOTIFICATION_TITLE_SAMI_KEY = "title";
	private static final String NOTIFICATION_BODY_SAMI_KEY = "body";



	/**
	 * remoteMessage with notification triggers onMessageReceived() only when the app is in foreground.
	 * remoteMessage with data triggers onMessageReceived() when the app is in foreground/background/killed.
	 * <p>
	 * https://stackoverflow.com/questions/37711082/how-to-handle-notification-when-app-in-background-in-firebase
	 */
	@Override
	public void onMessageReceived (RemoteMessage remoteMessage) {

		if (remoteMessage.getData() != null) {
			// Title and body of the notification received
			showNotification(remoteMessage.getData().get(NOTIFICATION_TITLE_SAMI_KEY), remoteMessage.getData().get(NOTIFICATION_BODY_SAMI_KEY));
		}
	}



	public void showNotification (final String title, final String message) {
		final NotificationBuilder notificationBuilder = new NotificationBuilder(this);
		Intent notificationIntent = new Intent(this, FlickrActivity.class);
		//notificationIntent.setAction(FlickrActivity.ACTION_NOTIFICATION_LAUNCH);
		//notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationBuilder.buildNotification(title, message, notificationIntent);
	}

}
