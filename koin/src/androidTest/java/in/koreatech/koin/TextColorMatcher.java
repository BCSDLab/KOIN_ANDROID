package in.koreatech.koin;

import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.is;

public class TextColorMatcher extends BoundedMatcher<View, TextView> {
    private Matcher<Integer> integerMatcher;

    public TextColorMatcher(final Matcher<Integer> integerMatcher) {
        super(TextView.class);
        this.integerMatcher = integerMatcher;
    }

    public static Matcher<View> withTextColor(@ColorRes int textColor, Resources resources) {
        int color = resources.getColor(textColor);
        return new TextColorMatcher(is(color));
    }

    @Override
    public boolean matchesSafely(TextView textView) {
        return integerMatcher.matches(textView.getCurrentTextColor());
    }

    @Override
    public void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText("TextView with text color: " +
                ((TextView) item).getCurrentTextColor() + ", expected: ");
        integerMatcher.describeMismatch(item, mismatchDescription);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with text color ");
        integerMatcher.describeTo(description);
    }
}
