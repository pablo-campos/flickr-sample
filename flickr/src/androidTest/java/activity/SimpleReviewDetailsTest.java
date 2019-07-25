package activity;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.pablocampos.flickrsample.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SimpleReviewDetailsTest {

	@Rule
	public ActivityTestRule<FlickrActivity> mActivityTestRule = new ActivityTestRule<>(FlickrActivity.class);



	@Test
	public void simpleReviewDetailsTest () {
		ViewInteraction cardView = onView(
				allOf(
						withId(R.id.card_view),
						childAtPosition(
								allOf(
										withId(R.id.feedGrid),
										childAtPosition(
												withId(R.id.swiperefresh),
												0)),
								0),
						isDisplayed()));
		cardView.perform(click());

		// Added a sleep statement to match the app's execution delay.
		// The recommended way to handle such scenarios is to use Espresso idling resources:
		// https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		pressBack();
	}



	private static Matcher<View> childAtPosition (
			final Matcher<View> parentMatcher, final int position)
	{

		return new TypeSafeMatcher<View>() {
			@Override
			public void describeTo (Description description) {
				description.appendText("Child at position " + position + " in parent ");
				parentMatcher.describeTo(description);
			}



			@Override
			public boolean matchesSafely (View view) {
				ViewParent parent = view.getParent();
				return parent instanceof ViewGroup && parentMatcher.matches(parent)
						&& view.equals(((ViewGroup) parent).getChildAt(position));
			}
		};
	}
}
