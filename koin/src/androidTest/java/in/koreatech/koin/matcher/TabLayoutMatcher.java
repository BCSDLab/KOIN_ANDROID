package in.koreatech.koin.matcher;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.matcher.BoundedMatcher;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

public class TabLayoutMatcher {
    private static BoundedMatcher<View, TabLayout> checkTabSelectedAndText(@IdRes int tabLayoutId, int position, String tabText, boolean isSelected) {
        return new BoundedMatcher<View, TabLayout>(TabLayout.class) {
            @Override
            protected boolean matchesSafely(TabLayout item) {
                TabLayout.Tab tab = item.getTabAt(position);
                if(tab == null) throw new PerformException.Builder()
                        .withCause(new Throwable("No tab at index" + position))
                        .build();
                else return tab.isSelected() == isSelected && tab.getText().equals(tabText);
            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with selected tab check");
                is(onView(withId(tabLayoutId))).describeTo(description);
            }
        };
    }
}
