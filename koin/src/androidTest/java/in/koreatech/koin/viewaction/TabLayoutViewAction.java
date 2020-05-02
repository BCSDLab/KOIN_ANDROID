package in.koreatech.koin.viewaction;

import android.view.View;

import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class TabLayoutViewAction {
    private static ViewAction selectTabAtPosition(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }
            @Override
            public String getDescription() {
                return "with tab at index" + position;
            }
            @Override
            public void perform(UiController uiController, View view) {
                TabLayout tabLayout = (TabLayout) view;
                TabLayout.Tab tab = tabLayout.getTabAt(position);
                if(tab == null) throw new PerformException.Builder()
                        .withCause(new Throwable("No tab at index $tabIndex"))
                        .build();
                else tab.select();
            }
        };
    }
}
