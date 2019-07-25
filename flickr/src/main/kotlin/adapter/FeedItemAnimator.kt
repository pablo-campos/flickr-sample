package adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class FeedItemAnimator : DefaultItemAnimator() {


	override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
		return true
	}


	override fun animateAdd(viewHolder: RecyclerView.ViewHolder): Boolean {
		runEnterAnimation(viewHolder)
		return false
	}


	private fun runEnterAnimation(holder: RecyclerView.ViewHolder) {
		val screenHeight = getScreenHeight(holder.itemView.context)
		holder.itemView.translationY = holder.itemView.y + screenHeight
		holder.itemView.animate()
				.translationY(0f)
				.setInterpolator(DecelerateInterpolator(2f))
				.setDuration(1500)
				.setListener(object : AnimatorListenerAdapter() {
					override fun onAnimationEnd(animation: Animator) {
						dispatchAddFinished(holder)
					}
				})
				.start()
	}


	private fun getScreenHeight(c: Context): Int {
		val screenHeight: Int
		val wm = c.getSystemService(Context.WINDOW_SERVICE) as WindowManager
		val display = wm.defaultDisplay
		val size = Point()
		display.getSize(size)
		screenHeight = size.y
		return screenHeight
	}
}
