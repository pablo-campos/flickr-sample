package com.pablocampos.flickrsample.firebase;

import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class FirebaseTokenGenerator extends FirebaseMessagingService {

	public static final String FCMTOKEN = "FCMRegistrationToken";



	@Override
	public void onCreate() {
		generateToken();
	}



	private void generateToken() {
		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(task -> {
					if (!task.isSuccessful()) {
						Log.w(FCMTOKEN, "getInstanceId failed", task.getException());
						return;
					}

					// Get and save new Instance ID token
					saveGeneratedToken(task.getResult().getToken());
				});
	}



	private void saveGeneratedToken(String registrationToken) {
		PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
				.edit()
				.putString(FCMTOKEN, registrationToken)
				.apply();
	}



	/**
	 * Called if InstanceID token is updated. This may occur if the security of
	 * the previous token had been compromised. Note that this is called when the InstanceID token
	 * is initially generated so this is where you would retrieve the token.
	 */
	@Override
	public void onNewToken(String token) {
		saveGeneratedToken(token);

		Log.e(FCMTOKEN, "New token:: " + token);
	}


}
