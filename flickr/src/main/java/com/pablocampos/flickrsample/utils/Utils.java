package com.pablocampos.flickrsample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;

import com.pablocampos.flickrsample.R;

public class Utils {


	/**
	 * Generate palette synchronously and return it.
	 */
	public static Palette createPaletteSync(Bitmap bitmap) {
		Palette p = Palette.from(bitmap).generate();
		return p;
	}



	/**
	 *Manipulate a color's brightness.
	 * @param color the color to manipulate.
	 * @param factor between 0.0f and 1.0f.
	 * @return manipulated color.
	 */
	public static int manipulateColor(int color, float factor) {
		int a = Color.alpha(color);
		int r = Math.round(Color.red(color) * factor);
		int g = Math.round(Color.green(color) * factor);
		int b = Math.round(Color.blue(color) * factor);
		return Color.argb(a,
						  Math.min(r,255),
						  Math.min(g,255),
						  Math.min(b,255));
	}



	public static boolean checkInternet(final Context context){

		// Test internet connection
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;	// We have internet
		} else {

			// No internet, display error dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(context)
					.setTitle(context.getResources().getString(R.string.no_internet_error_title))
					.setMessage(context.getResources().getString(R.string.no_internet_error_message))
					.setPositiveButton(context.getResources().getString(android.R.string.ok), null)
					.setIconAttribute(android.R.attr.alertDialogIcon);
			builder.show();

			return false;
		}
	}

}
