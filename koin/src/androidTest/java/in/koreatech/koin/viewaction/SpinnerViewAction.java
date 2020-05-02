package in.koreatech.koin.viewaction;

import android.view.View;
import android.widget.Spinner;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

public class SpinnerViewAction {
    private static ViewAction selectSpinnerPosition(int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.allOf(isDisplayed(), isAssignableFrom(Spinner.class));
            }
            @Override
            public String getDescription() {
                return "with spinner at index" + position;
            }
            @Override
            public void perform(UiController uiController, View view) {
                Spinner spinner = (Spinner) view;
                spinner.setSelection(position);
            }
        };
    }
}
