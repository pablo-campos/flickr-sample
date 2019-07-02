package com.pablocampos.flickrsample;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class App extends Application {

	// Called when the application is starting, before any other application objects have been created.
	// Overriding this method is totally optional!
	@Override
	public void onCreate () {
		super.onCreate();

		// Initialize Firebase
		FirebaseApp.initializeApp(this);
	}
}
