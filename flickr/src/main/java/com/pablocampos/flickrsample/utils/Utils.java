package com.pablocampos.flickrsample.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

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

}
