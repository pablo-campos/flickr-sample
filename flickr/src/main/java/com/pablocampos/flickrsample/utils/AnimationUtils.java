package com.pablocampos.flickrsample.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.pablocampos.flickrsample.R;

/**
 * Utility class to provide several types of animations for view transitions.
 */
public class AnimationUtils {

	// Constants
	public enum Direction {
		UP,
		DOWN,
		LEFT,
		RIGHT
	}

	public enum Sense {
		IN,
		OUT
	}

	// Globals.
	private Context mContext;
    private int mShortAnimationTime;
    private int mLongAnimationTime;

	// Starting / End Points of Origin.
	public enum RevealOrigin {
		TOP_LEFT_CORNER,
		TOP_RIGHT_CORNER,
		CENTER,
		BOTTOM_LEFT_CORNER,
		BOTTOM_RIGHT_CORNER
	}



    /**
     * Context required so we can get system default values for animation duration.
     * @param context
     */
    public AnimationUtils (final Context context) {
        mContext = context;
        mShortAnimationTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        mLongAnimationTime = context.getResources().getInteger(android.R.integer.config_longAnimTime);
    }


	/**
	 * ************************************************* Animation Settings **************************************************
	 */

	public Context getContext() {
		return mContext;
	}

	public void setContext(final Context context) {
		this.mContext = mContext;
	}

	public int getShortAnimationTime() {
		return mShortAnimationTime;
	}

	public void setShortAnimationTime(final int shortAnimationTime) {
		this.mShortAnimationTime = shortAnimationTime;
	}

	public int getLongAnimationTime() {
		return mLongAnimationTime;
	}

	public void setLongAnimationTime(final int longAnimationTime) {
		this.mLongAnimationTime = longAnimationTime;
	}


	/**
	 * ************************************************* Mixed Methods **************************************************
	 */

    /**
     * Put the views above one another in a vertical LinearLayout, with one hidden. Call
     * this if you want the old view to shrinkVertically to the bottom and the new view to growVertically from the top
     * down.
     * WARNING: the entering view needs to be set as Invisible originally. If it is set as Gone,
     * some measurements will not be correct and odd behavior can result on the first call.
     * @param exitingView the view to be hidden (shrinkVertically)
     * @param enteringView the view to be revealed (growVertically)
     */
    public void shrinkDownGrowDownViewSwap(final View exitingView, final View enteringView) {
        if (enteringView.getVisibility() != View.VISIBLE) {
            //Don't run the animation if the new view is already in position
            shrinkDown(exitingView);
            growDown(enteringView);
        }
    }

    /**
     * Put the views above one another in a vertical LinearLayout, with one hidden. Call
     * this if you want the old view to shrinkVertically to the top and the new view to growVertically from the bottom
     * up.
     * WARNING: the entering view needs to be set as Invisible originally. If it is set as Gone,
     * some measurements will not be correct and odd behavior can result on the first call.
     * @param exitingView the view to be hidden (shrinkVertically)
     * @param enteringView the view to be revealed (growVertically)
     */
    public void shrinkUpGrowUpViewSwap(final View exitingView, final View enteringView) {
        if (enteringView.getVisibility() != View.VISIBLE) {
            //Don't run the animation if the new view is already in position
            shrinkUp(exitingView);
            growUp(enteringView);
        }
    }


	/**
	 * ************************************************* 1-Dimensional Animations **************************************************
	 */

    public void shrinkUp(final View v) {
        v.setPivotY(0);
        shrinkVertically(v);
    }

    public void shrinkDown(final View v) {
        v.setPivotY(v.getHeight());
        shrinkVertically(v);
    }

	public void shrinkLeft(final View v) {
		v.setPivotY(0);
		shrinkHorizontally(v);
	}

	public void shrinkRight(final View v) {
		v.setPivotY(v.getWidth());
		shrinkHorizontally(v);
	}


    public void growUp(final View v) {
	    v.setVisibility(View.VISIBLE);
        v.setPivotY(v.getHeight());
        growVertically(v);
    }

	public void growUp(final View v, final int pivotY) {
		v.setVisibility(View.VISIBLE);
		v.setPivotY(pivotY);
		growVertically(v);
	}

    public void growDown(final View v) {
        v.setPivotY(0);
        growVertically(v);
    }

	public void growLeft(final View v) {
		v.setVisibility(View.VISIBLE);
		v.setPivotX(v.getWidth());
		growHorizontally(v);
	}

	public void growRight(final View v) {
		v.setVisibility(View.VISIBLE);
		v.setPivotX(0);
		growHorizontally(v);
	}

	public ObjectAnimator fadeOut(final View v){
		ObjectAnimator fadeOut = ObjectAnimator.ofFloat(v, "alpha", 1.0f, 0.0f);
		fadeOut.setDuration(1000);
		fadeOut.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				v.setVisibility(View.INVISIBLE);
			}
		});
		fadeOut.start();
		return fadeOut;
	}

	public ObjectAnimator fadeIn(final View v){
		v.setVisibility(View.VISIBLE);
		ObjectAnimator fadeIn = ObjectAnimator.ofFloat(v, View.ALPHA, 0.0f, 1.0f);
		fadeIn.setDuration(1000);
		fadeIn.start();
		return fadeIn;
	}

	public void fadeInAndSlideDown(final View v, final int delay){

		v.setVisibility(View.VISIBLE);
		v.setAlpha(0.0f);
		v.setTranslationY(-140);

		v.animate()
				.alpha(1.0f)
				.translationY(0)
				.setStartDelay(delay)
				.setInterpolator(new DecelerateInterpolator())
				.setDuration(900)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						//animated changes don't always remain after the animation is complete.
						//reason unknown--but to be sure, manually make those changes when the
						//animation completes
						v.setAlpha(1.0f);
						v.setTranslationY(0);
					}
				});
	}

	public static void slide (final Sense sense, final Direction direction, final View view){

    	if (view.getAnimation() == null || view.getAnimation().hasEnded()){
			int visibility = View.VISIBLE;
			int translationValue = 0;
			String translationProperty;

			switch (direction){
				case UP:
					translationProperty = "translationY";
					switch (sense){
						case IN:
							view.setTranslationY(((View) view.getParent()).getHeight());
							view.setVisibility(View.VISIBLE);
							translationValue = 0;
							visibility = View.VISIBLE;
							break;
						case OUT:
							translationValue = -((View) view.getParent()).getHeight();
							visibility = View.GONE;
							break;
					}
					break;
				case DOWN:
					translationProperty = "translationY";
					switch (sense){
						case IN:
							view.setTranslationY(-((View) view.getParent()).getHeight());
							view.setVisibility(View.VISIBLE);
							translationValue = 0;
							visibility = View.VISIBLE;
							break;
						case OUT:
							translationValue = ((View) view.getParent()).getHeight();
							visibility = View.GONE;
							break;
					}
					break;
				case LEFT:
					translationProperty = "translationX";
					break;
				case RIGHT:
					translationProperty = "translationX";
					break;
				default:
					translationProperty = "";
			}

			final int endVisibility = visibility;
			ObjectAnimator animator = ObjectAnimator.ofFloat(view, translationProperty, translationValue);
			animator.setDuration(700);
			animator.setInterpolator(new AccelerateDecelerateInterpolator());
			animator.setTarget(view);
			animator.start();

			animator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart (final Animator animator) {

				}



				@Override
				public void onAnimationEnd (final Animator animator) {
					view.setVisibility(endVisibility);
				}



				@Override
				public void onAnimationCancel (final Animator animator) {

				}



				@Override
				public void onAnimationRepeat (final Animator animator) {

				}
			});
		}
	}


	/**
	 * ************************************************* Grow **************************************************
	 */

	public void growHorizontally(final View v) {
		v.setScaleX(0);
		v.setVisibility(View.VISIBLE);
		v.animate()
				.scaleX(1.0f)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setDuration(mShortAnimationTime)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						//animated changes don't always remain after the animation is complete.
						//reason unknown--but to be sure, manually make those changes when the
						//animation completes
						v.setScaleX(1.0f);
						v.setVisibility(View.VISIBLE);
					}
				});
	}


    public void growVertically(final View v) {
        v.setScaleY(0);
	    v.setVisibility(View.VISIBLE);
        v.animate()
                .scaleY(1.0f)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(mShortAnimationTime)
                .setListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(final Animator animation) {
		                //animated changes don't always remain after the animation is complete.
		                //reason unknown--but to be sure, manually make those changes when the
		                //animation completes
		                v.setScaleY(1.0f);
		                v.setVisibility(View.VISIBLE);
	                }
                });
    }


	public void growFromOrigin(final View v) {
		v.setScaleX(0);
		v.setScaleY(0);
		v.setPivotY(v.getHeight() / 2);
		v.setPivotX(v.getWidth() / 2);
		v.setVisibility(View.VISIBLE);
		v.animate()
				.scaleX(1.0f)
				.scaleY(1.0f)
				.setInterpolator(new OvershootInterpolator())
				.setDuration(500)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						//animated changes don't always remain after the animation is complete.
						//reason unknown--but to be sure, manually make those changes when the
						//animation completes
						v.setScaleY(1.0f);
						v.setVisibility(View.VISIBLE);
					}
				});
	}


	/**
	 * ************************************************* Shrink **************************************************
	 */

	public void shrinkHorizontally(final View v) {
		v.animate()
				.scaleX(0)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setDuration(mShortAnimationTime)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						v.setScaleX(0);
						v.setVisibility(View.GONE);
					}
				});
	}


	public void shrinkVertically(final View v) {
		v.animate()
				.scaleY(0)
				.setInterpolator(new AccelerateDecelerateInterpolator())
				.setDuration(mShortAnimationTime)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(final Animator animation) {
						v.setScaleY(0);
						v.setVisibility(View.GONE);
					}
				});
	}


	/**
	 * ************************************************* 2-Dimensional Animations **************************************************
	 */

	/**
	 * Configure a dialog with a reveal animation (ripple-like) with a
	 * predetermined starting point (look at RevealOrigin). Then show
	 * the dialog being configured.
	 * @param dialog
	 * @param rootView
	 * @param origin: the starting point of the ripple.
	 */
	public static void configureReveal(final Dialog dialog, final View rootView, final RevealOrigin origin){

		// Lollipop and up
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			dialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialogInterface) {
					if (isViewAttached(rootView)){
						revealShow(null, rootView, true, origin);
					}
				}
			});

			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		}
	}


	/**
	 * Configure a dialog with a hide animation (ripple-like) with a
	 * predetermined starting point (look at RevealOrigin). Then hide
	 * the dialog being configured.
	 * @param dialog
	 * @param rootView
	 * @param origin: the end point of the ripple.
	 */
	@Nullable
	public static Animator configureHide(final Dialog dialog, final View rootView, final RevealOrigin origin){

		// Lollipop and up
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			return revealShow(dialog, rootView, false, origin);
		} else {
			dialog.dismiss();
			return null;
		}
	}


	@TargetApi(19)
	public static boolean isViewAttached(View view){
		return view.isAttachedToWindow();
	}


	@TargetApi(21)
	@Nullable
	private static Animator revealShow(final Dialog dialog, final View view, boolean reveal, final RevealOrigin origin){

		// Lollipop and up
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			int w = view.getWidth();
			int h = view.getHeight();
			float maxRadius;

			if (reveal) {
				Animator revealAnimator;
				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, maxRadius);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w, 0, 0, maxRadius);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, 0, maxRadius);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, 0, h, 0, maxRadius);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w, h, 0, maxRadius);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, 0, maxRadius);
				}

				view.setVisibility(View.VISIBLE);
				revealAnimator.setDuration(800);
				revealAnimator.setInterpolator(new DecelerateInterpolator());
				revealAnimator.start();
				return revealAnimator;

			} else {

				Animator hideAnimator;
				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, 0, 0, maxRadius, 0);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w, 0, maxRadius, 0);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, maxRadius, 0);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, 0, h, maxRadius, 0);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w, h, maxRadius, 0);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, maxRadius, 0);
				}

				hideAnimator.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						dialog.dismiss();
						view.setVisibility(View.INVISIBLE);
					}
				});

				hideAnimator.setDuration(500);
				hideAnimator.setInterpolator(new AccelerateInterpolator());
				hideAnimator.start();
				return hideAnimator;
			}

		}
		return null;
	}



	@TargetApi(21)
	public static void circularReveal(final View view, boolean reveal, final RevealOrigin origin){

		// Lollipop and up
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			int w = view.getWidth();
			int h = view.getHeight();
			float maxRadius;

			if (reveal) {
				Animator revealAnimator;
				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, maxRadius);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w, 0, 0, maxRadius);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, 0, maxRadius);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, 0, h, 0, maxRadius);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w, h, 0, maxRadius);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						revealAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, 0, maxRadius);
				}

				view.setVisibility(View.VISIBLE);
				revealAnimator.setDuration(800);
				revealAnimator.setInterpolator(new DecelerateInterpolator());
				revealAnimator.start();

			} else {

				Animator hideAnimator;
				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, 0, 0, maxRadius, 0);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w, 0, maxRadius, 0);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, maxRadius, 0);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, 0, h, maxRadius, 0);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(w * w + h * h);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w, h, maxRadius, 0);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);
						hideAnimator = ViewAnimationUtils.createCircularReveal(view, w/2, h/2, maxRadius, 0);
				}

				hideAnimator.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						view.setVisibility(View.INVISIBLE);
					}
				});

				hideAnimator.setDuration(500);
				hideAnimator.setInterpolator(new AccelerateInterpolator());
				hideAnimator.start();
			}

		}
	}


	@TargetApi(21)
	public static Animator circularReveal(final View view, boolean reveal, final RevealOrigin origin, final int width, final int height){

		// Lollipop and up
		Animator animator = null;
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			float maxRadius;

			if (reveal) {
				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, 0, 0, 0, maxRadius);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, width, 0, 0, maxRadius);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(width * width / 4 + height * height / 4);
						animator = ViewAnimationUtils.createCircularReveal(view, width/2, height/2, 0, maxRadius);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, 0, height, 0, maxRadius);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, width, height, 0, maxRadius);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(width * width / 4 + height * height / 4);
						animator = ViewAnimationUtils.createCircularReveal(view, width/2, height/2, 0, maxRadius);
				}

				view.setVisibility(View.VISIBLE);
				animator.setDuration(800);
				animator.setInterpolator(new DecelerateInterpolator());

			} else {

				switch (origin){
					case TOP_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, 0, 0, maxRadius, 0);
						break;
					case TOP_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, width, 0, maxRadius, 0);
						break;
					case CENTER:
						maxRadius = (float) Math.sqrt(width * width / 4 + height * height / 4);
						animator = ViewAnimationUtils.createCircularReveal(view, width/2, height/2, maxRadius, 0);
						break;
					case BOTTOM_LEFT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, 0, height, maxRadius, 0);
						break;
					case BOTTOM_RIGHT_CORNER:
						maxRadius = (float) Math.sqrt(width * width + height * height);
						animator = ViewAnimationUtils.createCircularReveal(view, width, height, maxRadius, 0);
						break;
					default:
						// Default uses center
						maxRadius = (float) Math.sqrt(width * width / 4 + height * height / 4);
						animator = ViewAnimationUtils.createCircularReveal(view, width/2, height/2, maxRadius, 0);
				}

				animator.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						view.setVisibility(View.INVISIBLE);
					}
				});

				animator.setDuration(500);
				animator.setInterpolator(new AccelerateInterpolator());
				animator.start();
			}

			animator.start();
		}

		return animator;
	}


	/**
	 * ************************************************* 3-Dimensional Animations **************************************************
	 */

	public static AnimatorSet flipCardAnimation(final View layoutFront, final View layoutBack, final boolean reverseBack) {

		// Load animations:
		AnimatorSet mAnimationFlipLeftOut = (AnimatorSet) AnimatorInflater.loadAnimator(layoutFront.getContext(), R.animator.horizontal_flip_left_out);
		AnimatorSet mAnimationFlipLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(layoutBack.getContext(), R.animator.horizontal_flip_left_in);

		AnimatorSet mAnimationFlipRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(layoutFront.getContext(), R.animator.horizontal_flip_right_out);
		AnimatorSet mAnimationFlipRightIn = (AnimatorSet) AnimatorInflater.loadAnimator(layoutBack.getContext(), R.animator.horizontal_flip_right_in);

		// Change camera distance:
		int distance = 40000;
		float scale = layoutBack.getContext().getResources().getDisplayMetrics().density * distance;
		layoutFront.setCameraDistance(scale);
		layoutBack.setCameraDistance(scale);

		// Change visibility:
		layoutBack.setVisibility(View.VISIBLE);
		layoutBack.setAlpha(0);

		// Apply animations and start:
		if (reverseBack){
			mAnimationFlipLeftOut.setTarget(layoutFront);
			mAnimationFlipLeftIn.setTarget(layoutBack);
			mAnimationFlipLeftOut.start();
			mAnimationFlipLeftIn.start();

			// Set visibility gone by end:
			mAnimationFlipLeftOut.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					layoutFront.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			});

			return mAnimationFlipLeftIn;
		} else {
			mAnimationFlipRightIn.setTarget(layoutBack);
			mAnimationFlipRightOut.setTarget(layoutFront);
			mAnimationFlipRightIn.start();
			mAnimationFlipRightOut.start();

			// Set visibility gone by end:
			mAnimationFlipRightOut.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					layoutFront.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}
			});

			return mAnimationFlipRightIn;
		}
	}

}
