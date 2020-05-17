package in.koreatech.koin.matcher;

import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;


public class TextColorMatcher {
    public static Matcher<View> textViewTextColorMatcher(final int matcherColor) {
        return new BoundedMatcher<View, TextView>(TextView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("with text color: " + matcherColor);
            }
            @Override
            protected boolean matchesSafely(TextView textView) {
                return matcherColor == textView.getCurrentTextColor();
            }
        };
    }
}
//ex)
//onView(withId(R.id.asd)).check(matches(textViewTextColorMatcher(context.getResources().getColor(R.color.red1))));
