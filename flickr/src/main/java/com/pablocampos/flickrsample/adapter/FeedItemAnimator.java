package com.pablocampos.flickrsample.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class FeedItemAnimator extends DefaultItemAnimator {


	@Override
	public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
		return true;
	}



	@Override
	public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
		runEnterAnimation(viewHolder);
		return false;
	}



	private void runEnterAnimation(final RecyclerView.ViewHolder holder) {
		final int screenHeight = getScreenHeight(holder.itemView.getContext());
		holder.itemView.setTranslationY(holder.itemView.getY() + screenHeight);
		holder.itemView.animate()
				.translationY(0)
				.setInterpolator(new DecelerateInterpolator(2.f))
				.setDuration(1500)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						dispatchAddFinished(holder);
					}
				})
				.start();
	}



	private static int getScreenHeight(Context c) {
		int screenHeight;
		WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenHeight = size.y;
		return screenHeight;
	}
}
