package utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.palette.graphics.Palette
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.pablocampos.flickrsample.R
import java.io.ByteArrayOutputStream
import kotlin.math.min
import kotlin.math.roundToInt

object Utils {


	/**
	 * Generate palette synchronously and return it.
	 */
	fun createPaletteSync(bitmap: Bitmap): Palette {
		return Palette.from(bitmap).generate()
	}


	/**
	 * Manipulate a color's brightness.
	 * @param color the color to manipulate.
	 * @param factor between 0.0f and 1.0f.
	 * @return manipulated color.
	 */
	fun manipulateColor(color: Int, factor: Float): Int {
		val a = Color.alpha(color)
		val r = (Color.red(color) * factor).roundToInt()
		val g = (Color.green(color) * factor).roundToInt()
		val b = (Color.blue(color) * factor).roundToInt()
		return Color.argb(a, min(r, 255), min(g, 255), min(b, 255))
	}


	fun checkInternet(context: Context): Boolean {

		// Test internet connection
		val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
		val netInfo = cm.activeNetworkInfo
		return if (netInfo != null && netInfo.isConnected) {
			true    // We have internet
		} else {

			// No internet, display error dialog
			val builder = AlertDialog.Builder(context)
					.setTitle(context.resources.getString(R.string.no_internet_error_title))
					.setMessage(context.resources.getString(R.string.no_internet_error_message))
					.setPositiveButton(context.resources.getString(android.R.string.ok), null)
					.setIconAttribute(android.R.attr.alertDialogIcon)
			builder.show()
			false
		}
    }

}
